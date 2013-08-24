package edu.umass.cs.pig.vm;

import static edu.umass.cs.pig.util.Assertions.check;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nl.flotsam.xeger.Xeger;

import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.newplan.Operator;
import org.apache.pig.newplan.logical.expression.AddExpression;
import org.apache.pig.newplan.logical.expression.AndExpression;
import org.apache.pig.newplan.logical.expression.BinaryExpression;
import org.apache.pig.newplan.logical.expression.CastExpression;
import org.apache.pig.newplan.logical.expression.ConstantExpression;
import org.apache.pig.newplan.logical.expression.DivideExpression;
import org.apache.pig.newplan.logical.expression.EqualExpression;
import org.apache.pig.newplan.logical.expression.GreaterThanEqualExpression;
import org.apache.pig.newplan.logical.expression.GreaterThanExpression;
import org.apache.pig.newplan.logical.expression.IsNullExpression;
import org.apache.pig.newplan.logical.expression.LessThanEqualExpression;
import org.apache.pig.newplan.logical.expression.LessThanExpression;
import org.apache.pig.newplan.logical.expression.LogicalExpression;
import org.apache.pig.newplan.logical.expression.LogicalExpressionPlan;
import org.apache.pig.newplan.logical.expression.ModExpression;
import org.apache.pig.newplan.logical.expression.MultiplyExpression;
import org.apache.pig.newplan.logical.expression.NotEqualExpression;
import org.apache.pig.newplan.logical.expression.NotExpression;
import org.apache.pig.newplan.logical.expression.OrExpression;
import org.apache.pig.newplan.logical.expression.ProjectExpression;
import org.apache.pig.newplan.logical.expression.RegexExpression;
import org.apache.pig.newplan.logical.expression.SubtractExpression;
import org.apache.pig.newplan.logical.expression.UserFuncExpression;
import org.apache.pig.newplan.logical.relational.LOGenerate;
import org.apache.pig.newplan.logical.relational.LogicalSchema;
import org.apache.pig.pen.InvertConstraintVisitor;
import org.apache.pig.pen.util.ExampleTuple;

import edu.umass.cs.pig.ast.ConstraintResult;
import edu.umass.cs.pig.cntr.EqualConstraint;
import edu.umass.cs.pig.cntr.OverflowUnderFlow;
import edu.umass.cs.pig.cntr.PltRelCntr;
import edu.umass.cs.pig.cntr.SyncNameDB;
import edu.umass.cs.pig.dataflow.ValueExtractor;
import edu.umass.cs.pig.dataflow.VarInfo;
import edu.umass.cs.pig.numeric.CAndCZ3;
import edu.umass.cs.pig.sort.BitVector32Sort;
import edu.umass.cs.pig.sort.BitVector64Sort;
import edu.umass.cs.pig.sort.Fp32Sort;
import edu.umass.cs.pig.sort.Fp64Sort;
import edu.umass.cs.pig.util.Allocator4Z3;
import edu.umass.cs.pig.z3.Z3Context;
import edu.umass.cs.userfunc.InputOutput;
import edu.umass.cs.userfunc.UninterpretedMaker;
import edu.umass.cs.userfunc.UserFuncValue;
import edu.umass.cs.z3.SWIGTYPE_p__Z3_ast;
import edu.umass.cs.z3.SWIGTYPE_p__Z3_symbol;

/**
 * TODO: implicit type conversion from int to double when
 * I'm doing multiplication and division of floats and ints 
 * and I forget the implicit conversion rules (and the words
 *  in the question seem too vague to google more quickly than asking here).
 * @author kaituo
 *
 */
public class ConstraintSolver {
	LogicalSchema schema;
//	List<LogicalFieldSchema> schema;
	ExampleTuple tOut;
	List<Integer> nullPos;
//	Collection<Object> userFuncVal;
	public static ArrayList<String> nameDeclared = new ArrayList<String>();
    Set<Object> seen = new HashSet<Object>();
//   public boolean success = false;
    Tuple defaultTuple;
    Allocator4Z3 alloc;
    ValueExtractor extr;
    OverflowUnderFlow flow;
    SyncNameDB sync;
    boolean udfcalled;
    CAndCZ3 ccz3;

	public ConstraintSolver(LogicalSchema schema) {
		super();
		this.schema = schema;
		defaultTuple = TupleFactory.getInstance()
				.newTuple(schema.getFields().size()); 
		//tOut = new ExampleTuple(defaultTuple);
//		userFuncVal = null;
		
		extr = new ValueExtractor();
		flow = new OverflowUnderFlow();
		nullPos = new ArrayList<Integer>();
		sync = new SyncNameDB();
		udfcalled = false;
		ccz3 = new CAndCZ3();
		alloc = new Allocator4Z3(ccz3);
	}
	
	public ConstraintSolver() {
//		userFuncVal = null;
		extr = new ValueExtractor();
		nullPos = new ArrayList<Integer>();
		sync = new SyncNameDB();
		udfcalled = false;
		ccz3 = new CAndCZ3();
		alloc = new Allocator4Z3(ccz3);
	}

	// generate a constraint tuple that conforms to the schema and passes the
	// predicate
	// (or null if unable to find such a tuple)

	// ExampleTuple GenerateMatchingTuple(LogicalSchema schema,
	// LogicalExpressionPlan plan,
	// boolean invert) throws FrontendException, ExecException {
	// return GenerateMatchingTuple(plan, invert);
	// }

	// generate a constraint tuple that conforms to the constraint and passes
	// the predicate
	// (or null if unable to find such a tuple)
	//
	// for now, constraint tuples are tuples whose fields are a blend of actual
	// data values and nulls,
	// where a null stands for "don't care"
	//
	// in the future, may want to replace "don't care" with a more rich
	// constraint language; this would
	// help, e.g. in the case of two filters in a row (you want the downstream
	// filter to tell the upstream filter
	// what predicate it wants satisfied in a given field)
	//

	public ExampleTuple gettOut() {
		return tOut;
	}
	
	public void resettOut() {
		Tuple newTuple = TupleFactory.getInstance()
				.newTuple(schema.getFields().size()); 
		this.tOut = new ExampleTuple(newTuple);
		this.tOut.synthetic = false;
	}
	
	public void resetNullPos() {
		nullPos.clear();
	}

	ConstraintResult GenerateLhsColumn(long uid, List<LogicalSchema> ls) {
		ConstraintResult ret = null;
		for(Iterator<LogicalSchema> ils = ls.iterator(); ils.hasNext();) {
			LogicalSchema element = ils.next();
			List<LogicalSchema.LogicalFieldSchema> fields = element.getFields();
			for(Iterator<LogicalSchema.LogicalFieldSchema> ilfs = fields.iterator(); ilfs.hasNext();) {
				LogicalSchema.LogicalFieldSchema fieldInfo = ilfs.next();
				if(uid == fieldInfo.uid) {
					ret = mallocInZ3(fieldInfo);
					return ret;
				}
			}
		}
		return null;
	}
	
	
	List<ConstraintResult> GenerateMatchingTuple(LOGenerate lgen)
			throws ExecException, FrontendException {
		List<LogicalExpressionPlan> gens = lgen.getOutputPlans();
		final Z3Context z3 = Z3Context.get();
		List<LogicalSchema> ls = lgen.getOutputPlanSchemas(); 
		List<ConstraintResult> retAst = new ArrayList<ConstraintResult>();
		
		for(Iterator<LogicalExpressionPlan> it = gens.iterator(); it
                    .hasNext();) {
			LogicalExpressionPlan genExp = it.next();
			Operator op = genExp.getSources().get(0);
			ConstraintResult assertAst;
			
			if (op instanceof AddExpression) {
				assertAst = GenerateMatchingTupleHelper((AddExpression) op);
				long fuid = ((AddExpression) op).getFieldSchema().uid;
				ConstraintResult lhs = GenerateLhsColumn(fuid, ls);
				if(lhs == null)
					return null;
				SWIGTYPE_p__Z3_ast cntr = z3.mk_eq(lhs.getZ3ast(), assertAst.getZ3ast());
				ConstraintResult r = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.TRUE);
				retAst.add(r);
			} else if (op instanceof SubtractExpression) {
				assertAst = GenerateMatchingTupleHelper((SubtractExpression) op);
				long fuid = ((SubtractExpression) op).getFieldSchema().uid;
				ConstraintResult lhs = GenerateLhsColumn(fuid, ls);
				if(lhs == null)
					return null;
				SWIGTYPE_p__Z3_ast cntr = z3.mk_eq(lhs.getZ3ast(), assertAst.getZ3ast());
				ConstraintResult r = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.TRUE);
				retAst.add(r);
			} else if (op instanceof MultiplyExpression) {
				assertAst = GenerateMatchingTupleHelper((MultiplyExpression) op);
				long fuid = ((MultiplyExpression) op).getFieldSchema().uid;
				ConstraintResult lhs = GenerateLhsColumn(fuid, ls);
				if(lhs == null)
					return null;
				SWIGTYPE_p__Z3_ast cntr = z3.mk_eq(lhs.getZ3ast(), assertAst.getZ3ast());
				ConstraintResult r = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.TRUE);
				retAst.add(r);
			} else if (op instanceof DivideExpression) {
				assertAst = GenerateMatchingTupleHelper((DivideExpression) op);
				long fuid = ((DivideExpression) op).getFieldSchema().uid;
				ConstraintResult lhs = GenerateLhsColumn(fuid, ls);
				if(lhs == null)
					return null;
				SWIGTYPE_p__Z3_ast cntr = z3.mk_eq(lhs.getZ3ast(), assertAst.getZ3ast());
				ConstraintResult r = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.TRUE);
				retAst.add(r);
			} else if (op instanceof ModExpression) {
				assertAst = GenerateMatchingTupleHelper((ModExpression) op);
				long fuid = ((ModExpression) op).getFieldSchema().uid;
				ConstraintResult lhs = GenerateLhsColumn(fuid, ls);
				if(lhs == null)
					return null;
				SWIGTYPE_p__Z3_ast cntr = z3.mk_eq(lhs.getZ3ast(), assertAst.getZ3ast());
				ConstraintResult r = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.TRUE);
				retAst.add(r);
			} else if (op instanceof UserFuncExpression) {
				// has been taken care of using data flow hierarchy
				break;
			} else
				return null;
		}
		
//		tOut.synthetic = true;
		return retAst;
	}

	ConstraintResult GenerateMatchingTuple(LogicalExpressionPlan predicate, boolean invert)
			throws ExecException, FrontendException {
		// Tuple t = TupleFactory.getInstance().newTuple(constraint.size());
		//
		// for (int i = 0; i < t.size(); i++)
		// tOut.set(i, constraint.get(i));
		ConstraintResult ret = GenerateMatchingTupleHelper(predicate.getSources().get(0), invert);
//		tOut.synthetic = true;
		return ret;
	}

	ConstraintResult GenerateMatchingTupleHelper(Operator pred, boolean invert) throws FrontendException,
			ExecException {
		ConstraintResult ret = null;
		if (pred instanceof BinaryExpression)
			ret = GenerateMatchingTupleHelper((BinaryExpression) pred, invert);
		else if (pred instanceof NotExpression) {
			ret = GenerateMatchingTupleHelper((NotExpression) pred, invert);
		}
		else if (pred instanceof IsNullExpression) {
			GenerateMatchingTupleHelper((IsNullExpression) pred, invert);
		}
		else if (pred instanceof UserFuncExpression) {
			ret = GenerateMatchingTupleHelper((UserFuncExpression)pred, invert);
		}
		else {
			throw new FrontendException("Unknown operator in filter predicate");
		}
		if(ret == null)
			ret = new ConstraintResult();
		return ret;
	}

	ConstraintResult GenerateMatchingTupleHelper(BinaryExpression pred,
			boolean invert) throws FrontendException, ExecException {
		final Z3Context z3 = Z3Context.get();
		ConstraintResult lhsAst = null, rhsAst = null, retAst = null;
		SWIGTYPE_p__Z3_ast cntr = null;
		LogicalExpression leftHs = pred.getLhs(), rightHs = pred.getRhs();

		if (pred instanceof AndExpression) {
			retAst = GenerateMatchingTupleHelper((AndExpression) pred, invert);
			return retAst;
		} else if (pred instanceof OrExpression) {
			retAst = GenerateMatchingTupleHelper((OrExpression) pred, invert);
			return retAst;
		} else if (pred instanceof RegexExpression) {
			retAst = GenerateMatchingTupleHelper((RegexExpression)pred, invert);
			return retAst;
		}

		// now we are sure that the expression operators are the roots of the
		// plan

		// boolean leftIsConst = false, rightIsConst = false;
		// Object leftConst = null, rightConst = null;
		// byte leftDataType = 0, rightDataType = 0;

		// int leftCol = -1, rightCol = -1;

		// if (pred instanceof AddExpression || pred instanceof
		// SubtractExpression
		// || pred instanceof MultiplyExpression || pred instanceof
		// DivideExpression
		// || pred instanceof ModExpression || pred instanceof RegexExpression)
		// return; // We don't try to work around these operators right now

		lhsAst = GenerateMatchingColumnExpression(leftHs);
		if (lhsAst == null) {
			if (leftHs instanceof AddExpression) {
				lhsAst = GenerateMatchingTupleHelper((AddExpression) leftHs);
			} else if (leftHs instanceof SubtractExpression) {
				lhsAst = GenerateMatchingTupleHelper((SubtractExpression) leftHs);
			} else if (leftHs instanceof MultiplyExpression) {
				lhsAst = GenerateMatchingTupleHelper((MultiplyExpression) leftHs);
			} else if (leftHs instanceof DivideExpression) {
				lhsAst = GenerateMatchingTupleHelper((DivideExpression) leftHs);
			} else if (leftHs instanceof ModExpression) {
				lhsAst = GenerateMatchingTupleHelper((ModExpression) leftHs);
			} else if (leftHs instanceof UserFuncExpression) {
				lhsAst = GenerateMatchingTupleHelper((UserFuncExpression)leftHs, invert);
			} else
				return null;
		}

		rhsAst = GenerateMatchingColumnExpression(rightHs);
		if (rhsAst == null) {
			if (rightHs instanceof AddExpression) {
				rhsAst = GenerateMatchingTupleHelper((AddExpression) rightHs);

			} else if (rightHs instanceof SubtractExpression) {
				rhsAst = GenerateMatchingTupleHelper((SubtractExpression) rightHs);

			} else if (rightHs instanceof MultiplyExpression) {
				rhsAst = GenerateMatchingTupleHelper((MultiplyExpression) rightHs);

			} else if (rightHs instanceof DivideExpression) {
				rhsAst = GenerateMatchingTupleHelper((DivideExpression) rightHs);

			} else if (rightHs instanceof ModExpression) {
				rhsAst = GenerateMatchingTupleHelper((ModExpression) rightHs);

			} else
				return null;
		}

		// now we try to change some nulls to constants

		// convert some nulls to constants
		if (!invert) {
			if (pred instanceof EqualExpression) {
				if (lhsAst.getType() == rhsAst.getType()) {
					if (lhsAst.getType() == DataType.INTEGER
							|| lhsAst.getType() == DataType.LONG
							|| lhsAst.getType() == DataType.FLOAT 
							|| lhsAst.getType() == DataType.DOUBLE) {
						cntr = z3.mk_eq(lhsAst.getZ3ast(), rhsAst.getZ3ast());
						
						retAst = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.TRUE);
						return retAst;
					} else if (lhsAst.getType() == DataType.CHARARRAY || lhsAst.getType() == DataType.BOOLEAN) {
						if (lhsAst.isConst()) {
		                    tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(), lhsAst.getConstVal()
		                            .toString()));
		                } else if (rhsAst.isConst()) {
		                    tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), rhsAst.getConstVal()
		                            .toString()));
		                } else {
		                    tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), "0"));
		                    tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(), "0"));
		                }
					}
				} else
					return null;
			} else if (pred instanceof NotEqualExpression) {
				if (lhsAst.getType() == rhsAst.getType()) {
					if (lhsAst.getType() == DataType.INTEGER
							|| lhsAst.getType() == DataType.LONG
							|| lhsAst.getType() == DataType.FLOAT 
							|| lhsAst.getType() == DataType.DOUBLE) {
						cntr = z3.mk_not(Z3Context.get().mk_eq(lhsAst.getZ3ast(),
								rhsAst.getZ3ast()));
						
						retAst = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.TRUE);
						return retAst;
					} else if (lhsAst.getType() == DataType.CHARARRAY || lhsAst.getType() == DataType.BOOLEAN) {
						if (lhsAst.isConst()) {
		                    tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(),
		                            GetUnequalValue(lhsAst.getConstVal()).toString()));
		                } else if (rhsAst.isConst()) {
		                	tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), GetUnequalValue(
		                			rhsAst.getConstVal()).toString()));
		                } else {
		                	tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), "0"));
		                	tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(), "1"));
		                }
					}
				} else
					return null;
			} else if (pred instanceof GreaterThanExpression) {
				if (lhsAst.getType() == rhsAst.getType()) {
					if (lhsAst.getType() == DataType.INTEGER
							|| lhsAst.getType() == DataType.LONG 
							) {
						cntr = z3
								.mk_bvsgt(lhsAst.getZ3ast(), rhsAst.getZ3ast());
						
						retAst = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.TRUE);
						return retAst;
					} else if (lhsAst.getType() == DataType.FLOAT 
							|| lhsAst.getType() == DataType.DOUBLE) {
						cntr = z3
								.mk_gt(lhsAst.getZ3ast(), rhsAst.getZ3ast());
						
						retAst = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.TRUE);
						return retAst;
					}
					else if (lhsAst.getType() == DataType.CHARARRAY || lhsAst.getType() == DataType.BOOLEAN) {
						if (lhsAst.isConst()) {
		                    tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(),
		                            GetSmallerValue(lhsAst.getConstVal()).toString()));
		                } else if (rhsAst.isConst()) {
		                	tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), GetLargerValue(
		                			rhsAst.getConstVal()).toString()));
		                } else {
		                	tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), "1"));
		                	tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(), "0"));
		                }
					} else {
						check(false);
						return null;
					}
				} else
					return null;
			} else if (pred instanceof GreaterThanEqualExpression) {
				if (lhsAst.getType() == rhsAst.getType()) {
					if (lhsAst.getType() == DataType.INTEGER
							|| lhsAst.getType() == DataType.LONG
							) {
						cntr = z3
								.mk_bvsge(lhsAst.getZ3ast(), rhsAst.getZ3ast());
						
						retAst = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.TRUE);
						return retAst;
					} else if (lhsAst.getType() == DataType.FLOAT 
							|| lhsAst.getType() == DataType.DOUBLE) {
						cntr = z3
								.mk_ge(lhsAst.getZ3ast(), rhsAst.getZ3ast());
						
						retAst = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.TRUE);
						return retAst;
					}
					else if (lhsAst.getType() == DataType.CHARARRAY || lhsAst.getType() == DataType.BOOLEAN) {
						if (lhsAst.isConst()) {
		                    tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(),
		                            GetSmallerValue(lhsAst.getConstVal()).toString()));
		                } else if (rhsAst.isConst()) {
		                	tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), GetLargerValue(
		                			rhsAst.getConstVal()).toString()));
		                } else {
		                	tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), "1"));
		                	tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(), "0"));
		                }
					} else {
						check(false);
						return null;
					}
				} else
					return null;
			} else if (pred instanceof LessThanExpression) {
				if (lhsAst.getType() == rhsAst.getType()) {
					if (lhsAst.getType() == DataType.INTEGER
							|| lhsAst.getType() == DataType.LONG
							) {
						cntr = z3
								.mk_bvslt(lhsAst.getZ3ast(), rhsAst.getZ3ast());
						
						retAst = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.TRUE);
						return retAst;
					} else if(lhsAst.getType() == DataType.FLOAT 
							|| lhsAst.getType() == DataType.DOUBLE) {
						cntr = z3
								.mk_lt(lhsAst.getZ3ast(), rhsAst.getZ3ast());
						
						retAst = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.TRUE);
						return retAst;
					}
					else if (lhsAst.getType() == DataType.CHARARRAY || lhsAst.getType() == DataType.BOOLEAN) {
						if (lhsAst.isConst()) {
		                    tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(), GetLargerValue(
		                    		lhsAst.getConstVal()).toString()));
		                } else if (rhsAst.isConst()) {
		                    tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), GetSmallerValue(
		                    		rhsAst.getConstVal()).toString()));
		                } else {
		                    tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), "0"));
		                    tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(), "1"));
		                }
					} else {
						check(false);
						return null;
					}
				} else
					return null;
			} else if (pred instanceof LessThanEqualExpression) {
				if (lhsAst.getType() == rhsAst.getType()) {
					if (lhsAst.getType() == DataType.INTEGER
							|| lhsAst.getType() == DataType.LONG) {
						cntr = z3
								.mk_bvsle(lhsAst.getZ3ast(), rhsAst.getZ3ast());
						
						retAst = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.TRUE);
						return retAst;
					} else if(lhsAst.getType() == DataType.FLOAT 
							|| lhsAst.getType() == DataType.DOUBLE) {
						cntr = z3
								.mk_le(lhsAst.getZ3ast(), rhsAst.getZ3ast());
						
						retAst = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.TRUE);
						return retAst;
					} else if (lhsAst.getType() == DataType.CHARARRAY || lhsAst.getType() == DataType.BOOLEAN) {
						if (lhsAst.isConst()) {
		                    tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(), GetLargerValue(
		                    		lhsAst.getConstVal()).toString()));
		                } else if (rhsAst.isConst()) {
		                    tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), GetSmallerValue(
		                    		rhsAst.getConstVal()).toString()));
		                } else {
		                    tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), "0"));
		                    tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(), "1"));
		                }
					} else {
						check(false);
						return null;
					}
				} else
					return null;
			} 
		} else {
			if (pred instanceof EqualExpression) {
				if (lhsAst.getType() == rhsAst.getType()) {
					if (lhsAst.getType() == DataType.INTEGER
							|| lhsAst.getType() == DataType.LONG
							|| lhsAst.getType() == DataType.FLOAT 
							|| lhsAst.getType() == DataType.DOUBLE) {
						cntr = z3.mk_not(Z3Context.get().mk_eq(lhsAst.getZ3ast(),
								rhsAst.getZ3ast()));
						
						retAst = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.FALSE);
						return retAst;
					} else if (lhsAst.getType() == DataType.CHARARRAY || lhsAst.getType() == DataType.BOOLEAN) {
						if (lhsAst.isConst()) {
		                    tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(),
		                            GetUnequalValue(lhsAst.getConstVal()).toString()));
		                } else if (rhsAst.isConst()) {
		                    tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), GetUnequalValue(
		                    		rhsAst.getConstVal()).toString()));
		                } else {
		                    tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), "0"));
		                    tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(), "1"));
		                }
					}
				} else
					return null;
			} else if (pred instanceof NotEqualExpression) {
				if (lhsAst.getType() == rhsAst.getType()) {
					if (lhsAst.getType() == DataType.INTEGER
							|| lhsAst.getType() == DataType.LONG
							|| lhsAst.getType() == DataType.FLOAT 
							|| lhsAst.getType() == DataType.DOUBLE) {
						cntr = z3.mk_eq(lhsAst.getZ3ast(), rhsAst.getZ3ast());
						
						retAst = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.FALSE);
						return retAst;
					} else if (lhsAst.getType() == DataType.CHARARRAY || lhsAst.getType() == DataType.BOOLEAN) {
						if (lhsAst.isConst()) {
		                    tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(), lhsAst.getConstVal()
		                            .toString()));
		                } else if (rhsAst.isConst()) {
		                    tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), rhsAst.getConstVal()
		                            .toString()));
		                } else {
		                    tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), "0"));
		                    tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(), "0"));
		                }
					}
				} else
					return null;
			} else if (pred instanceof GreaterThanExpression) {
				if (lhsAst.getType() == rhsAst.getType()) {
					if (lhsAst.getType() == DataType.INTEGER
							|| lhsAst.getType() == DataType.LONG
							) {
						cntr = z3
								.mk_bvsle(lhsAst.getZ3ast(), rhsAst.getZ3ast());
						
						retAst = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.FALSE);
						return retAst;
					} else if(lhsAst.getType() == DataType.FLOAT 
							|| lhsAst.getType() == DataType.DOUBLE) {
						cntr = z3
								.mk_le(lhsAst.getZ3ast(), rhsAst.getZ3ast());
						retAst = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.FALSE);
						return retAst;
					}
					else if (lhsAst.getType() == DataType.CHARARRAY || lhsAst.getType() == DataType.BOOLEAN) {
						if (lhsAst.isConst()) {
		                    tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(), GetLargerValue(
		                    		lhsAst.getConstVal()).toString()));
		                } else if (rhsAst.isConst()) {
		                    tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), GetSmallerValue(
		                    		rhsAst.getConstVal()).toString()));
		                } else {
		                    tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), "0"));
		                    tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(), "1"));
		                }
					} else {
						check(false);
						return null;
					}
				} else
					return null;
			} else if (pred instanceof GreaterThanEqualExpression) {
				if (lhsAst.getType() == rhsAst.getType()) {
					if (lhsAst.getType() == DataType.INTEGER
							|| lhsAst.getType() == DataType.LONG 
							) {
						cntr = z3
								.mk_bvslt(lhsAst.getZ3ast(), rhsAst.getZ3ast());
						
						retAst = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.FALSE);
						return retAst;
					} else if(lhsAst.getType() == DataType.FLOAT 
							|| lhsAst.getType() == DataType.DOUBLE) {
						cntr = z3
								.mk_lt(lhsAst.getZ3ast(), rhsAst.getZ3ast());
						
						retAst = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.FALSE);
						return retAst;
					}
					else if (lhsAst.getType() == DataType.CHARARRAY || lhsAst.getType() == DataType.BOOLEAN) {
						if (lhsAst.isConst()) {
		                    tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(), GetLargerValue(
		                    		lhsAst.getConstVal()).toString()));
		                } else if (rhsAst.isConst()) {
		                    tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), GetSmallerValue(
		                    		rhsAst.getConstVal()).toString()));
		                } else {
		                    tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), "0"));
		                    tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(), "1"));
		                }
					} else {
						check(false);
						return null;
					}
				} else
					return null;
			} else if (pred instanceof LessThanExpression) {
				if (lhsAst.getType() == rhsAst.getType()) {
					if (lhsAst.getType() == DataType.INTEGER
							|| lhsAst.getType() == DataType.LONG
							) {
						cntr = z3
								.mk_bvsge(lhsAst.getZ3ast(), rhsAst.getZ3ast());
						
						retAst = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.FALSE);
						return retAst;
					} else if(lhsAst.getType() == DataType.FLOAT 
							|| lhsAst.getType() == DataType.DOUBLE) {
						cntr = z3
								.mk_ge(lhsAst.getZ3ast(), rhsAst.getZ3ast());
						
						retAst = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.FALSE);
						return retAst;
					}
					else if (lhsAst.getType() == DataType.CHARARRAY || lhsAst.getType() == DataType.BOOLEAN) {
						if (lhsAst.isConst()) {
		                    tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(),
		                            GetSmallerValue(lhsAst.getConstVal()).toString()));
		                } else if (rhsAst.isConst()) {
		                    tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), GetLargerValue(
		                    		rhsAst.getConstVal()).toString()));
		                } else {
		                    tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), "1"));
		                    tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(), "0"));
		                }
					} else {
						check(false);
						return null;
					}
				} else
					return null;
			} else if (pred instanceof LessThanEqualExpression) {
				if (lhsAst.getType() == rhsAst.getType()) {
					if (lhsAst.getType() == DataType.INTEGER
							|| lhsAst.getType() == DataType.LONG
							) {
						cntr = z3
								.mk_bvsgt(lhsAst.getZ3ast(), rhsAst.getZ3ast());
						
						retAst = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.FALSE);
						return retAst;
					} else if(lhsAst.getType() == DataType.FLOAT 
							|| lhsAst.getType() == DataType.DOUBLE) {
						cntr = z3
								.mk_gt(lhsAst.getZ3ast(), rhsAst.getZ3ast());
						
						retAst = new ConstraintResult(cntr, DataType.BOOLEAN, CntrTruth.FALSE);
						return retAst;
					}
					else if (lhsAst.getType() == DataType.CHARARRAY || lhsAst.getType() == DataType.BOOLEAN) {
						if (lhsAst.isConst()) {
		                    tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(),
		                            GetSmallerValue(lhsAst.getConstVal()).toString()));
		                } else if (rhsAst.isConst()) {
		                    tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), GetLargerValue(
		                    		rhsAst.getConstVal()).toString()));
		                } else {
		                    tOut.set(lhsAst.getCol(), generateData(lhsAst.getType(), "1"));
		                    tOut.set(rhsAst.getCol(), generateData(rhsAst.getType(), "0"));
		                }
					} else {
						check(false);
						return null;
					}
				} else
					return null;
			}
		}
		return retAst;

	}

	
	
	protected ConstraintResult mallocInZ3(LogicalSchema.LogicalFieldSchema pe) {
		final Z3Context z3 = Z3Context.get();
//		SWIGTYPE_p__Z3_symbol s;
		ConstraintResult ret = null;
		SWIGTYPE_p__Z3_ast z = null;
		byte type;
		
//		s = z3.mk_string_symbol(pe.alias);
		type = pe.type;
		switch (type) {
		case DataType.INTEGER:
//			z = z3.mk_const(s, BitVector32Sort.getInstance().getZ3Sort());
			z = ccz3.checkCreateInt(pe.alias);
			break;
		case DataType.LONG:
//			z = z3.mk_const(s, BitVector64Sort.getInstance().getZ3Sort());
			z = ccz3.checkCreateLong(pe.alias);
			break;
		case DataType.FLOAT:
//			z = z3.mk_const(s, Fp32Sort.getInstance().getZ3Sort());
			z = ccz3.checkCreateFloat(pe.alias);
			break;
		case DataType.DOUBLE:
//			z = z3.mk_const(s, Fp64Sort.getInstance().getZ3Sort());
			z = ccz3.checkCreateDouble(pe.alias);
			break;
		default:
			return null;
		}
		
		ret = new ConstraintResult(z, type, CntrTruth.UNKNOWN);
		return ret;
	}
	
//	private String analyzeName(String n) {
//		for(String name : nameDeclared) {
//			if(name.equals(n))
//				return n;
//			else if(name.indexOf("::" + n) > 0)
//				return name;
//		}
//		nameDeclared.add(n);
//		return n;
//	}

	protected ConstraintResult mallocInZ3(ProjectExpression pe, byte typeCast) {
		byte type;
		String alias, alias2;
		int lcol;
		try {
			alias = pe.getFieldSchema().alias;

			if(nameDeclared != null) {
				String aName = sync.analyzeName(alias, nameDeclared);
				if(alias.equals(aName)) {
					// add declared name to an array to make sure every one use the same name
					// for the same variable.
					nameDeclared.add(alias);
				} 
				alias2 = aName;
			} else {
				alias2 = alias;
			}
			
			if(typeCast == -1)
				type = pe.getType();
			else
				type = typeCast;
			lcol = extr.extractPosition(schema, alias);
			return alloc.mallocInZ3(alias2, type, lcol);//, fl
		} catch (FrontendException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	

	protected ConstraintResult mallocInZ3(ConstantExpression ce, byte typeCast) {
		Object value = ce.getValue();
		byte type;
		if(typeCast == -1)
			type = DataType.findType(value);
		else
			type = typeCast;
		try {
			return alloc.mallocInZ3(value, type);
		} catch (FrontendException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
//		
//		mallocInZ3(value, type);
	}

	

	
	protected byte getType(LogicalExpression le) {
		try {
			if (le instanceof ProjectExpression) {
				return ((ProjectExpression) le).getType();
			} else if (le instanceof ConstantExpression) {
				return DataType.findType(((ConstantExpression) le).getValue());
			}
		} catch (FrontendException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return DataType.ERROR;
	}

	ConstraintResult GenerateMatchingColumnExpression(LogicalExpression op)
			throws FrontendException, ExecException {
		ConstraintResult ret = null;
		byte typeCast = -1;
		if (op instanceof ConstantExpression) {
			ret = mallocInZ3((ConstantExpression) op, typeCast);
			
		} else {
			if (op instanceof CastExpression) {
				CastExpression castOp = (CastExpression) op;
				op = castOp.getExpression();
				typeCast = castOp.getFieldSchema().type;
			}
			// if (!(pred.getLhsOperand() instanceof ProjectExpression &&
			// ((ProjectExpression)
			// pred
			// .getLhsOperand()).getProjection().size() == 1))
			// return; // too hard
			if (op instanceof ProjectExpression) {
				ret = mallocInZ3((ProjectExpression) op, typeCast);
			}
			else if (op instanceof ConstantExpression) {
				ret = mallocInZ3((ConstantExpression) op, typeCast);
			} else if (op instanceof AddExpression) {
				ret = GenerateMatchingTupleHelper((AddExpression) op, typeCast);
			} else if (op instanceof SubtractExpression) {
				ret = GenerateMatchingTupleHelper((SubtractExpression) op, typeCast);
			} else if (op instanceof MultiplyExpression) {
				ret = GenerateMatchingTupleHelper((MultiplyExpression) op, typeCast);
			} else if (op instanceof DivideExpression) {
				ret = GenerateMatchingTupleHelper((DivideExpression) op, typeCast);
			} else if (op instanceof ModExpression) {
				ret = GenerateMatchingTupleHelper((ModExpression) op, typeCast);
			} else if (op instanceof UserFuncExpression) {
				ret = GenerateMatchingTupleHelper((UserFuncExpression) op, typeCast);
			} else
				return null;
//			if(ret == null && op.getType() == DataType.CHARARRAY) {
//				
//			}
		}
		return ret;
	}
	
	ConstraintResult GenerateMatchingColumnExpression(LogicalExpression op, byte typeCast)
			throws FrontendException, ExecException {
		ConstraintResult ret = null;
		
		if (op instanceof ConstantExpression) {
			ret = mallocInZ3((ConstantExpression) op, typeCast);
			
		} else {
			if (op instanceof CastExpression) {
				CastExpression castOp = (CastExpression) op;
				op = castOp.getExpression();
				typeCast = castOp.getFieldSchema().type;
			}
			
			if (op instanceof ProjectExpression) {
				ret = mallocInZ3((ProjectExpression) op, typeCast);
			}
			else if (op instanceof ConstantExpression) {
				ret = mallocInZ3((ConstantExpression) op, typeCast);
			} else if (op instanceof AddExpression) {
				ret = GenerateMatchingTupleHelper((AddExpression) op, typeCast);
			} else if (op instanceof SubtractExpression) {
				ret = GenerateMatchingTupleHelper((SubtractExpression) op, typeCast);
			} else if (op instanceof MultiplyExpression) {
				ret = GenerateMatchingTupleHelper((MultiplyExpression) op, typeCast);
			} else if (op instanceof DivideExpression) {
				ret = GenerateMatchingTupleHelper((DivideExpression) op, typeCast);
			} else if (op instanceof ModExpression) {
				ret = GenerateMatchingTupleHelper((ModExpression) op, typeCast);
			} else if (op instanceof UserFuncExpression) {
				ret = GenerateMatchingTupleHelper((UserFuncExpression) op, typeCast);
			} else
				return null;
//			if(ret == null && op.getType() == DataType.CHARARRAY) {
//				
//			}
		}
		return ret;
	}
	
	ConstraintResult ConnectLhsRhsAnd(ConstraintResult lr, ConstraintResult rr, byte b) {
		ConstraintResult ret;
		SWIGTYPE_p__Z3_ast retAst;
		if(lr.getType() == DataType.BOOLEAN && rr.getType() == DataType.BOOLEAN) {
			retAst = Z3Context.get().mk_and(lr.getZ3ast(), rr.getZ3ast());
		    ret = new ConstraintResult(retAst, DataType.BOOLEAN, CntrTruth.TRUE);
		}
		else if(lr.getType() == DataType.BOOLEAN && rr.getType() != DataType.BOOLEAN) {
			ret = new ConstraintResult(lr.getZ3ast(), DataType.BOOLEAN, b);
		}
		else if(lr.getType() != DataType.BOOLEAN && rr.getType() == DataType.BOOLEAN) {
			ret = new ConstraintResult(rr.getZ3ast(), DataType.BOOLEAN, b);
		}
		else {
			ret = new ConstraintResult();
		}
		return ret;
	}
	
	ConstraintResult ConnectLhsRhsOr(ConstraintResult lr, ConstraintResult rr, byte b) {
		ConstraintResult ret;
		SWIGTYPE_p__Z3_ast retAst;
		if(lr.getType() == DataType.BOOLEAN && rr.getType() == DataType.BOOLEAN) {
			retAst = Z3Context.get().mk_or(lr.getZ3ast(), rr.getZ3ast());
		    ret = new ConstraintResult(retAst, DataType.BOOLEAN, CntrTruth.TRUE);
		}
		else if(lr.getType() == DataType.BOOLEAN && rr.getType() != DataType.BOOLEAN) {
			ret = new ConstraintResult(lr.getZ3ast(), DataType.BOOLEAN, b);
		}
		else if(lr.getType() != DataType.BOOLEAN && rr.getType() == DataType.BOOLEAN) {
			ret = new ConstraintResult(rr.getZ3ast(), DataType.BOOLEAN, b);
		}
		else {
			ret = new ConstraintResult();
		}
		return ret;
	}

	/**
	 * Original implementation is bad. When they generate data for a column,
	 * they just inspect a unit constraint in a whole bunch of constriant. For
	 * example, x>5 && x<5.5 and x is a double, they would generate x=6 for the
	 * left-hand constraint , and set t.x =6 where t is the tuple in the
	 * parameter. Then for the right-hand constraint, they would generate x=4.5,
	 * and set t.x=4.5. But t.x=4.5 is in conflict with the first constraint.
	 */
	ConstraintResult GenerateMatchingTupleHelper(AndExpression op, boolean invert)
			throws FrontendException, ExecException {
		Operator left, right;
		ConstraintResult lr, rr;
		ConstraintResult ret;
		byte b = CntrTruth.toCntrTruth(invert);
		
		if(!invert) {
			left = op.getLhs();
			lr = GenerateMatchingTupleHelper(left, invert);
			right = op.getRhs();
			rr = GenerateMatchingTupleHelper(right, invert);
			ret = ConnectLhsRhsAnd(lr, rr, b);
			
			return ret;
		} else {
			left = op.getLhs();
			lr = GenerateMatchingTupleHelper(left, invert);
			right = op.getRhs();
			rr = GenerateMatchingTupleHelper(right, invert);
			ret = ConnectLhsRhsOr(lr, rr, b);
			return ret;
		}
	}

	ConstraintResult GenerateMatchingTupleHelper(OrExpression op, boolean invert)
			throws FrontendException, ExecException {
		Operator left, right;
		ConstraintResult lr, rr;
		
		ConstraintResult ret;
		byte b = CntrTruth.toCntrTruth(invert);
		
		if(invert) {
			left = op.getLhs();
			lr = GenerateMatchingTupleHelper(left, invert);
			right = op.getRhs();
			rr = GenerateMatchingTupleHelper(right, invert);
			ret = ConnectLhsRhsAnd(lr, rr, b);
			return ret;
		} else {
			left = op.getLhs();
			lr = GenerateMatchingTupleHelper(left, invert);
			right = op.getRhs();
			rr = GenerateMatchingTupleHelper(right, invert);
			ret = ConnectLhsRhsOr(lr, rr, b);
			return ret;
		}
		

	}

	ConstraintResult GenerateMatchingTupleHelper(AddExpression op, byte typecast)
			throws FrontendException, ExecException {
		ConstraintResult left = GenerateMatchingColumnExpression(op.getLhs(), typecast);
		ConstraintResult right = GenerateMatchingColumnExpression(op.getRhs(), typecast);
		if (left == null || right == null) {
			System.err
					.println("Cannot support complex arithmetic function now.");
			return null;
		} else {
			byte typeLhs = left.getType();//getType(op.getLhs());
			byte typeRhs = right.getType();//getType(op.getRhs());
			SWIGTYPE_p__Z3_ast c = null;
			if ((typeLhs == DataType.INTEGER && typeRhs == DataType.INTEGER)
					|| (typeLhs == DataType.LONG && typeRhs == DataType.LONG)) {
				c = Z3Context.get().mk_bvadd(
						left.getZ3ast(), right.getZ3ast());
				flow.bvaddouflow(left, right);
				
			} else if ((typeLhs == DataType.FLOAT && typeRhs == DataType.FLOAT)
					|| (typeLhs == DataType.DOUBLE && typeRhs == DataType.DOUBLE)) {
				SWIGTYPE_p__Z3_ast args[] = {left.getZ3ast(), right.getZ3ast()};
				
				c = Z3Context.get().mk_add(
						2, args);
				
			} else {
				check(false); // TODO
				return null;
			}
			ConstraintResult ret = new ConstraintResult(c, typeLhs, CntrTruth.UNKNOWN);
			return ret;
		}

	}

	ConstraintResult GenerateMatchingTupleHelper(SubtractExpression op, byte typecast)
			throws FrontendException, ExecException {
		ConstraintResult left = GenerateMatchingColumnExpression(op.getLhs(), typecast);
		ConstraintResult right = GenerateMatchingColumnExpression(op.getRhs(), typecast);
		if (left == null || right == null) {
			System.err
					.println("Cannot support complex arithmetic function now.");
			return null;
		} else {
			byte typeLhs = left.getType();//getType(op.getLhs());
			byte typeRhs = right.getType();//getType(op.getRhs());
			SWIGTYPE_p__Z3_ast c = null;
			if ((typeLhs == DataType.INTEGER && typeRhs == DataType.INTEGER)
					|| (typeLhs == DataType.LONG && typeRhs == DataType.LONG)) {
				c = Z3Context.get().mk_bvsub(
						left.getZ3ast(), right.getZ3ast());
				flow.bvsubouflow(left, right);
				
			} else if((typeLhs == DataType.FLOAT && typeRhs == DataType.FLOAT)
					|| (typeLhs == DataType.DOUBLE && typeRhs == DataType.DOUBLE)) {
				
				SWIGTYPE_p__Z3_ast args[] = {left.getZ3ast(), right.getZ3ast()};
				
				c = Z3Context.get().mk_sub(
						2, args);
			} else {
				check(false); // TODO
				return null;
			}
			ConstraintResult ret = new ConstraintResult(c, typeLhs, CntrTruth.UNKNOWN);
			return ret;
		}

	}

	ConstraintResult GenerateMatchingTupleHelper(MultiplyExpression op, byte typecast)
			throws FrontendException, ExecException {
		ConstraintResult left = GenerateMatchingColumnExpression(op.getLhs(), typecast);
		ConstraintResult right = GenerateMatchingColumnExpression(op.getRhs(), typecast);
		if (left == null || right == null) {
			System.err
					.println("Cannot support complex arithmetic function now.");
			return null;
		} else {
			SWIGTYPE_p__Z3_ast c = null;
			byte typeLhs = left.getType();//getType(op.getLhs());
			byte typeRhs = right.getType();//getType(op.getRhs());
			if ((typeLhs == DataType.INTEGER && typeRhs == DataType.INTEGER)
					|| (typeLhs == DataType.LONG && typeRhs == DataType.LONG)) {
				c = Z3Context.get().mk_bvmul(
						left.getZ3ast(), right.getZ3ast());
				flow.bvmulouflow(left, right);
			} else if((typeLhs == DataType.FLOAT && typeRhs == DataType.FLOAT)
					|| (typeLhs == DataType.DOUBLE && typeRhs == DataType.DOUBLE)) {
				
				SWIGTYPE_p__Z3_ast args[] = {left.getZ3ast(), right.getZ3ast()};
				
				c = Z3Context.get().mk_mul(
						2, args);
			}
			else {
				check(false); // TODO
				return null;
			}
			ConstraintResult ret = new ConstraintResult(c, typeLhs, CntrTruth.UNKNOWN);
			return ret;
		}

	}

	ConstraintResult GenerateMatchingTupleHelper(DivideExpression op, byte typeCast)
			throws FrontendException, ExecException {
		ConstraintResult left = GenerateMatchingColumnExpression(op.getLhs(), typeCast);
		ConstraintResult right = GenerateMatchingColumnExpression(op.getRhs(), typeCast);
		if (left == null || right == null) {
			System.err
					.println("Cannot support complex arithmetic function now.");
			return null;
		} else {
			byte typeLhs = left.getType();//getType(op.getLhs());
			byte typeRhs = right.getType();//getType(op.getRhs());
			SWIGTYPE_p__Z3_ast c = null;
			if ((typeLhs == DataType.INTEGER && typeRhs == DataType.INTEGER)
					|| (typeLhs == DataType.LONG && typeRhs == DataType.LONG)) {
				c = Z3Context.get().mk_bvsdiv(
						left.getZ3ast(), right.getZ3ast());
				flow.bvdivouflow(left, right);
				
			} else if((typeLhs == DataType.FLOAT && typeRhs == DataType.FLOAT)
					|| (typeLhs == DataType.DOUBLE && typeRhs == DataType.DOUBLE)) { 
				c = Z3Context.get().mk_div(
						left.getZ3ast(), right.getZ3ast());
			} else {
				check(false); // TODO
				return null;
			}
			ConstraintResult ret = new ConstraintResult(c, typeLhs, CntrTruth.UNKNOWN);
			return ret;
		}

	}

	ConstraintResult GenerateMatchingTupleHelper(ModExpression op, byte typeCast)
			throws FrontendException, ExecException {
		ConstraintResult left = GenerateMatchingColumnExpression(op.getLhs(), typeCast);
		ConstraintResult right = GenerateMatchingColumnExpression(op.getRhs(), typeCast);
		if (left == null || right == null) {
			System.err
					.println("Cannot support complex arithmetic function now.");
			return null;
		} else {
			byte typeLhs = left.getType();//getType(op.getLhs());
			byte typeRhs = right.getType();//getType(op.getRhs());
			SWIGTYPE_p__Z3_ast c = null;
			if ((typeLhs == DataType.INTEGER && typeRhs == DataType.INTEGER)
					|| (typeLhs == DataType.LONG && typeRhs == DataType.LONG)) {
				c = Z3Context.get().mk_bvsmod(
						left.getZ3ast(), right.getZ3ast());
				
				
			} else if((typeLhs == DataType.FLOAT && typeRhs == DataType.FLOAT)
					|| (typeLhs == DataType.DOUBLE && typeRhs == DataType.DOUBLE)) { 
				c = Z3Context.get().mk_mod(
						left.getZ3ast(), right.getZ3ast());
			} else {
				check(false); // TODO
				return null;
			}
			ConstraintResult ret = new ConstraintResult(c, typeLhs, CntrTruth.UNKNOWN);
			return ret;
		}

	}
	
	ConstraintResult GenerateMatchingTupleHelper(AddExpression op)
			throws FrontendException, ExecException {
		ConstraintResult left = GenerateMatchingColumnExpression(op.getLhs());
		ConstraintResult right = GenerateMatchingColumnExpression(op.getRhs());
		if (left == null || right == null) {
			System.err
					.println("Cannot support complex arithmetic function now.");
			return null;
		} else {
			byte typeLhs = getType(op.getLhs());
			byte typeRhs = getType(op.getRhs());
			SWIGTYPE_p__Z3_ast c = null;
			if ((typeLhs == DataType.INTEGER && typeRhs == DataType.INTEGER)
					|| (typeLhs == DataType.LONG && typeRhs == DataType.LONG)) {
				c = Z3Context.get().mk_bvadd(
						left.getZ3ast(), right.getZ3ast());
				flow.bvaddouflow(left, right);
				
			} else if ((typeLhs == DataType.FLOAT && typeRhs == DataType.FLOAT)
					|| (typeLhs == DataType.DOUBLE && typeRhs == DataType.DOUBLE)) {
				SWIGTYPE_p__Z3_ast args[] = {left.getZ3ast(), right.getZ3ast()};
				
				c = Z3Context.get().mk_add(
						2, args);
				
			} else {
				check(false); // TODO
				return null;
			}
			ConstraintResult ret = new ConstraintResult(c, typeLhs, CntrTruth.UNKNOWN);
			return ret;
		}

	}

	ConstraintResult GenerateMatchingTupleHelper(SubtractExpression op)
			throws FrontendException, ExecException {
		ConstraintResult left = GenerateMatchingColumnExpression(op.getLhs());
		ConstraintResult right = GenerateMatchingColumnExpression(op.getRhs());
		if (left == null || right == null) {
			System.err
					.println("Cannot support complex arithmetic function now.");
			return null;
		} else {
			byte typeLhs = getType(op.getLhs());
			byte typeRhs = getType(op.getRhs());
			SWIGTYPE_p__Z3_ast c = null;
			if ((typeLhs == DataType.INTEGER && typeRhs == DataType.INTEGER)
					|| (typeLhs == DataType.LONG && typeRhs == DataType.LONG)) {
				c = Z3Context.get().mk_bvsub(
						left.getZ3ast(), right.getZ3ast());
				flow.bvsubouflow(left, right);
				
			} else if((typeLhs == DataType.FLOAT && typeRhs == DataType.FLOAT)
					|| (typeLhs == DataType.DOUBLE && typeRhs == DataType.DOUBLE)) {
				
				SWIGTYPE_p__Z3_ast args[] = {left.getZ3ast(), right.getZ3ast()};
				
				c = Z3Context.get().mk_sub(
						2, args);
			} else {
				check(false); // TODO
				return null;
			}
			ConstraintResult ret = new ConstraintResult(c, typeLhs, CntrTruth.UNKNOWN);
			return ret;
		}

	}

	ConstraintResult GenerateMatchingTupleHelper(MultiplyExpression op)
			throws FrontendException, ExecException {
		ConstraintResult left = GenerateMatchingColumnExpression(op.getLhs());
		ConstraintResult right = GenerateMatchingColumnExpression(op.getRhs());
		if (left == null || right == null) {
			System.err
					.println("Cannot support complex arithmetic function now.");
			return null;
		} else {
			SWIGTYPE_p__Z3_ast c = null;
			byte typeLhs = getType(op.getLhs());
			byte typeRhs = getType(op.getRhs());
			if ((typeLhs == DataType.INTEGER && typeRhs == DataType.INTEGER)
					|| (typeLhs == DataType.LONG && typeRhs == DataType.LONG)) {
				c = Z3Context.get().mk_bvmul(
						left.getZ3ast(), right.getZ3ast());
				flow.bvmulouflow(left, right);
			} else if((typeLhs == DataType.FLOAT && typeRhs == DataType.FLOAT)
					|| (typeLhs == DataType.DOUBLE && typeRhs == DataType.DOUBLE)) {
				
				SWIGTYPE_p__Z3_ast args[] = {left.getZ3ast(), right.getZ3ast()};
				
				c = Z3Context.get().mk_mul(
						2, args);
			}
			else {
				check(false); // TODO
				return null;
			}
			ConstraintResult ret = new ConstraintResult(c, typeLhs, CntrTruth.UNKNOWN);
			return ret;
		}

	}

	ConstraintResult GenerateMatchingTupleHelper(DivideExpression op)
			throws FrontendException, ExecException {
		ConstraintResult left = GenerateMatchingColumnExpression(op.getLhs());
		ConstraintResult right = GenerateMatchingColumnExpression(op.getRhs());
		if (left == null || right == null) {
			System.err
					.println("Cannot support complex arithmetic function now.");
			return null;
		} else {
			byte typeLhs = getType(op.getLhs());
			byte typeRhs = getType(op.getRhs());
			SWIGTYPE_p__Z3_ast c = null;
			if ((typeLhs == DataType.INTEGER && typeRhs == DataType.INTEGER)
					|| (typeLhs == DataType.LONG && typeRhs == DataType.LONG)) {
				c = Z3Context.get().mk_bvsdiv(
						left.getZ3ast(), right.getZ3ast());
				flow.bvdivouflow(left, right);
				
			} else if((typeLhs == DataType.FLOAT && typeRhs == DataType.FLOAT)
					|| (typeLhs == DataType.DOUBLE && typeRhs == DataType.DOUBLE)) { 
				c = Z3Context.get().mk_div(
						left.getZ3ast(), right.getZ3ast());
			} else {
				check(false); // TODO
				return null;
			}
			ConstraintResult ret = new ConstraintResult(c, typeLhs, CntrTruth.UNKNOWN);
			return ret;
		}

	}

	ConstraintResult GenerateMatchingTupleHelper(ModExpression op)
			throws FrontendException, ExecException {
		ConstraintResult left = GenerateMatchingColumnExpression(op.getLhs());
		ConstraintResult right = GenerateMatchingColumnExpression(op.getRhs());
		if (left == null || right == null) {
			System.err
					.println("Cannot support complex arithmetic function now.");
			return null;
		} else {
			byte typeLhs = getType(op.getLhs());
			byte typeRhs = getType(op.getRhs());
			SWIGTYPE_p__Z3_ast c = null;
			if ((typeLhs == DataType.INTEGER && typeRhs == DataType.INTEGER)
					|| (typeLhs == DataType.LONG && typeRhs == DataType.LONG)) {
				c = Z3Context.get().mk_bvsmod(
						left.getZ3ast(), right.getZ3ast());
				
				
			} else if((typeLhs == DataType.FLOAT && typeRhs == DataType.FLOAT)
					|| (typeLhs == DataType.DOUBLE && typeRhs == DataType.DOUBLE)) { 
				c = Z3Context.get().mk_mod(
						left.getZ3ast(), right.getZ3ast());
			} else {
				check(false); // TODO
				return null;
			}
			ConstraintResult ret = new ConstraintResult(c, typeLhs, CntrTruth.UNKNOWN);
			return ret;
		}

	}

	ConstraintResult GenerateMatchingTupleHelper(RegexExpression pred,
			boolean invert) throws FrontendException, ExecException {
		// now we are sure that the expression operators are the roots of the
		// plan

		Object rightConst = null;
		int leftCol = -1;
		
		ConstraintResult ret = new ConstraintResult();

		LogicalExpression lhs = pred.getLhs();
		if (lhs instanceof CastExpression)
			lhs = ((CastExpression) lhs).getExpression();
		// if (!(pred.getLhsOperand() instanceof ProjectExpression &&
		// ((ProjectExpression)
		// pred
		// .getLhsOperand()).getProjection().size() == 1))
		// return; // too hard
		if (!(lhs instanceof ProjectExpression))
			return ret;
		//leftCol = ((ProjectExpression) lhs).getColNum();
//		LogicalFieldSchema lfs = schema.getFieldSubNameMatch(((ProjectExpression)lhs).getFieldSchema().alias);
//		if ( lfs != null) {
//			leftCol = schema.getFields().indexOf(lfs);
//  		} else
//  			return ret;
		
		leftCol = extr.extractPosition(schema, ((ProjectExpression)lhs).getFieldSchema().alias);
		if(leftCol == -1)
			return ret;
		
		if (!(pred.getRhs() instanceof ConstantExpression))
			return ret;

		rightConst = ((ConstantExpression) (pred.getRhs())).getValue();

		
		Xeger generator = new Xeger((String) rightConst);
		String text = generator.generate();
		// now we try to change some nulls to constants
		if (invert)
			tOut.set(leftCol, text + "edu.umass.cs.pig");
		else
			tOut.set(leftCol, text);
		ret = new ConstraintResult(CntrTruth.toCntrTruth(invert));
		return ret;
	}

	ConstraintResult GenerateMatchingTupleHelper(NotExpression op, boolean invert)
			throws FrontendException, ExecException {
		LogicalExpression input = op.getExpression();
		ConstraintResult ret = GenerateMatchingTupleHelper(input, !invert);
		return ret;
	}

	/*
	 * op.getExpression(): x:(Name: Project Type: chararray Uid: 5 Input: 0 Column: 0)
	 * op.getExpression().fieldSchema: x#5:chararray
	 */
	void GenerateMatchingTupleHelper(IsNullExpression op,
			boolean invert) throws FrontendException, ExecException {
		int lcol = extr.extractPosition(schema, op.getExpression().getFieldSchema().alias);
		if (!invert)
			nullPos.add(lcol);
//			tOut.set(lcol, null);
//		else {
//			byte type = op.getExpression().getType();
//			tOut.set(lcol, generateData(type, "0"));
//		}
	}
	
	Object generateData(byte type, String data) {
        switch (type) {
        case DataType.BOOLEAN:
            if (data.equalsIgnoreCase("true")) {
                return Boolean.TRUE;
            } else if (data.equalsIgnoreCase("false")) {
                return Boolean.FALSE;
            } else {
                return null;
            }
        case DataType.BYTEARRAY:
            return new DataByteArray(data.getBytes());
        case DataType.DOUBLE:
            return Double.valueOf(data);
        case DataType.FLOAT:
            return Float.valueOf(data);
        case DataType.INTEGER:
            return Integer.valueOf(data);
        case DataType.LONG:
            return Long.valueOf(data);
        case DataType.CHARARRAY:
            return data;
        default:
            return null;
        }
    }

	
	ConstraintResult GenerateMatchingTupleHelper(UserFuncExpression op, boolean invert)
			throws FrontendException, ExecException {
		udfcalled = true;
		UserFuncValue u = UserFuncValue.get();
		List<InputOutput> concreteVals = u.get(op.getFuncSpec());
		
		if(concreteVals == null)
			return null;
		
		byte retT = op.getFieldSchema().type;
		
		// We can only handle numeric udf right now.
		if(retT != DataType.INTEGER && retT != DataType.LONG && retT != DataType.FLOAT && retT != DataType.DOUBLE)
			return null;
		
		UninterpretedMaker umake = new UninterpretedMaker(concreteVals, op, retT);
		SWIGTYPE_p__Z3_ast unInterpret = umake.toUninterpFunc();
		if(unInterpret != null) {
			ConstraintResult ret = new ConstraintResult(unInterpret, retT, CntrTruth.UNKNOWN);
			return ret;
		} else
			return null;
//		return null;
	}
	
	ConstraintResult GenerateMatchingTupleHelper(UserFuncExpression op, byte typecast)
			throws FrontendException, ExecException {
		udfcalled = true;
		UserFuncValue u = UserFuncValue.get();
		List<InputOutput> concreteVals = u.get(op.getFuncSpec());
		
		if(concreteVals == null)
			return null;
		
		byte retT = op.getFieldSchema().type;
		
		// We can only handle numeric udf right now.
		if(retT != DataType.INTEGER && retT != DataType.LONG && retT != DataType.FLOAT && retT != DataType.DOUBLE)
			return null;
		
		UninterpretedMaker umake = new UninterpretedMaker(concreteVals, op, retT);
		SWIGTYPE_p__Z3_ast unInterpret = umake.toUninterpFunc();
		if(unInterpret != null) {
			ConstraintResult ret = new ConstraintResult(unInterpret, retT, CntrTruth.UNKNOWN);
			return ret;
		} else
			return null;
//		return null;
	}
	
	Object GetUnequalValue(Object v) {
        byte type = DataType.findType(v);

        if (type == DataType.BAG || type == DataType.TUPLE
                || type == DataType.MAP)
            return null;

        Object zero = generateData(type, "0");

        if (v.equals(zero))
            return generateData(type, "1");

        return zero;
    }

     Object GetSmallerValue(Object v) {
        byte type = DataType.findType(v);

        if (type == DataType.BAG || type == DataType.TUPLE
                || type == DataType.MAP)
            return null;

        switch (type) {
        case DataType.CHARARRAY:
            String str = (String) v;
            if (str.length() > 0)
                return str.substring(0, str.length() - 1);
            else
                return null;
        case DataType.BYTEARRAY:
            DataByteArray data = (DataByteArray) v;
            if (data.size() > 0)
                return new DataByteArray(data.get(), 0, data.size() - 1);
            else
                return null;
        case DataType.INTEGER:
            return Integer.valueOf((Integer) v - 1);
        case DataType.LONG:
            return Long.valueOf((Long) v - 1);
        case DataType.FLOAT:
            return Float.valueOf((Float) v - 1);
        case DataType.DOUBLE:
            return Double.valueOf((Double) v - 1);
        default:
            return null;
        }

    }

     Object GetLargerValue(Object v) {
        byte type = DataType.findType(v);

        if (type == DataType.BAG || type == DataType.TUPLE
                || type == DataType.MAP)
            return null;

        switch (type) {
        case DataType.CHARARRAY:
            return (String) v + "0";
        case DataType.BYTEARRAY:
            String str = ((DataByteArray) v).toString();
            str = str + "0";
            return new DataByteArray(str);
        case DataType.INTEGER:
            return Integer.valueOf((Integer) v + 1);
        case DataType.LONG:
            return Long.valueOf((Long) v + 1);
        case DataType.FLOAT:
            return Float.valueOf((Float) v + 1);
        case DataType.DOUBLE:
            return Double.valueOf((Double) v + 1);
        default:
            return null;
        }
    }
     
     /**
      * Generate concrete tuple or z3 constraints based on the parameter.
      * @param ctr
      *  can be any contraint type, not only EqualConstraint
      * @return
      * @throws FrontendException
      * @throws ExecException
      */
     ConstraintResult GenerateRlTuple(PltRelCntr ctr) throws FrontendException, ExecException {
    	 byte type = ctr.getResultType();
    	 ConstraintResult equalCntr = null;
    	 SWIGTYPE_p__Z3_ast z3ast = null;
    	 int position;
    	 switch(type) {
    	 case DataType.CHARARRAY:
    	 case DataType.BOOLEAN:
    		 if(ctr instanceof EqualConstraint) {
    			 EqualConstraint eqctr = (EqualConstraint)ctr;
    			 position = extr.extractPosition(schema, eqctr.getChildVar());
        	     if(position == -1) {
         			 throw new FrontendException("The column number of the input variable cannot be negative!");
        	     }
                 tOut.set(position, eqctr.getValue());
                 equalCntr = new ConstraintResult(CntrTruth.UNKNOWN);
                 
    		 }
             break;
         case DataType.INTEGER:
        	 ctr.syncNameDB(nameDeclared);
        	 z3ast = ctr.asZ3ast(Z3Context.get(), alloc);
        	 equalCntr = new ConstraintResult(z3ast, DataType.INTEGER, CntrTruth.UNKNOWN);
        	 break;
         case DataType.LONG:
        	 ctr.syncNameDB(nameDeclared);
        	 z3ast = ctr.asZ3ast(Z3Context.get(), alloc);
        	 equalCntr = new ConstraintResult(z3ast, DataType.LONG, CntrTruth.UNKNOWN);
        	 break;
         case DataType.FLOAT:
        	 ctr.syncNameDB(nameDeclared);
        	 z3ast = ctr.asZ3ast(Z3Context.get(), alloc);
        	 equalCntr = new ConstraintResult(z3ast, DataType.FLOAT, CntrTruth.UNKNOWN);
        	 break;
         case DataType.DOUBLE:
        	 ctr.syncNameDB(nameDeclared);
        	 z3ast = ctr.asZ3ast(Z3Context.get(), alloc);
        	 equalCntr = new ConstraintResult(z3ast, DataType.DOUBLE, CntrTruth.UNKNOWN);
        	 break;
         default: // handle EqualVarCntr here
        	 ctr.syncNameDB(nameDeclared);
        	 z3ast = ctr.asZ3ast(Z3Context.get(), alloc); 
        	 equalCntr = new ConstraintResult(z3ast, DataType.UNKNOWN, CntrTruth.UNKNOWN);
        	 break;
    	 }
    	 return equalCntr;
     }

	public List<Integer> getNullPos() {
		return nullPos;
	}

	public void setNullPos(List<Integer> nullPos) {
		this.nullPos = nullPos;
	}

	public boolean isUdfcalled() {
		return udfcalled;
	}
}
