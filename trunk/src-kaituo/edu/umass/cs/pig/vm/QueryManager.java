package edu.umass.cs.pig.vm;

import static edu.umass.cs.pig.util.Assertions.notNull;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.newplan.logical.relational.LogicalSchema;
import org.apache.pig.newplan.logical.relational.LogicalSchema.LogicalFieldSchema;

import edu.umass.cs.pig.MainConfig;
import edu.umass.cs.pig.ast.ConstraintResult;
import edu.umass.cs.pig.cntr.CountConstraint;
import edu.umass.cs.pig.cntr.FakeLimit;
import edu.umass.cs.pig.util.Allocator4Z3;
import edu.umass.cs.pig.z3.ConstraintSolution;
import edu.umass.cs.pig.z3.CoralSolutionImpl;
import edu.umass.cs.pig.z3.Z3Context;
import edu.umass.cs.pig.z3.Z3SolutionImpl;
import edu.umass.cs.z3.SWIGTYPE_p__Z3_ast;
import edu.umass.cs.z3.SWIGTYPE_p__Z3_model;
import gov.nasa.jpf.symbc.numeric.solvers.ConstraintResultCoral;
import gov.nasa.jpf.symbc.numeric.solvers.ConstraintSolverCoral;
import gov.nasa.jpf.symbc.numeric.solvers.ProblemCoral;


/**
 * Manages the Z3 context stack and issues all calls to Z3.
 *
 * <p>Each method exploration has its own query manager.
 */
public final class QueryManager {

  private Z3Context z3 = Z3Context.get();
  private ProblemCoral pb = ProblemCoral.get();
  
  private ConstraintSolver c;
  private ConstraintSolverCoral ccoral;
  
  
  /**
   * Constructor
   *
   * <p>Register all variables: jvm variables (method parameters) and
   * Z3 arrays to encode type properties, arrays, fields, etc.
   */
  public QueryManager(LogicalSchema schema) {
    c = new ConstraintSolver(schema);
    ccoral = new ConstraintSolverCoral(schema);
  }



  public ConstraintSolver getC() {
	return c;
}



/**
   * Assert constraints from path condition.
   * @param leaf node
   */
//  private void assertPathCondition(PathTreeNode goalLeaf) 
//  {   
//  	final Deque<PathTreeNode> goalPath = new LinkedList<PathTreeNode>();
//    PathTreeNode node = notNull(goalLeaf);
//    while (node != null) {
//    	goalPath.push(node);    // build: (methRoot, firstBranchOutcome, ..., goalLeaf)
//    	node = node.getParent();
//    }
//    
//    z3.push();
//    
//    for (PathTreeNode newNode: goalPath) {
//    	PathBranch branchTaken = newNode.getBranchTaken();
//    	ConstraintResult r = GetMatchingConstraint(branchTaken); // force malloc of C pointers
//    	z3.assert_cnstr(r.getZ3ast());
//     }
//  }
  
	private AssertionResult assertPathCondition(ArrayList<PathBranch> goalPath) {
		boolean z3declared = false;
		boolean duplicate = false;
		CountConstraint cc = null; // only one CountConstraint is considered right now
		for (PathBranch branchTaken : goalPath) {
			try {
				if (branchTaken.getL() != null) {
					
					ConstraintResult r = c.GenerateMatchingTuple(
							branchTaken.getL(), branchTaken.isInvert());
					if (r != null && r.getZ3ast() != null) {
						z3.assert_cnstr(r.getZ3ast());
//						Iterator<SWIGTYPE_p__Z3_ast> iter = c.getFl().getBounds().iterator();
//						while(iter.hasNext()) {
//							SWIGTYPE_p__Z3_ast fltoAssert = iter.next();
//							z3.assert_cnstr(fltoAssert);
//						}
//						c.getFl().clearBounds();
//						FakeLimit fl = new FakeLimit();
//						Allocator4Z3 alloc = new Allocator4Z3();
//						ConstraintResult sumAst = alloc.mallocInZ3("xx", (byte)15, -1);
//						boolean assertFl =  fl.buildFakeLimitLong(sumAst.getZ3ast());
//						if(!assertFl)
//							throw new FrontendException(
//									"Failed to assert fake integer limit.");
//						
//						ConstraintResult sumAst2 = alloc.mallocInZ3("yy", (byte)15, -1);
//						boolean assertFl2 =  fl.buildFakeLimitLong(sumAst2.getZ3ast());
//						if(!assertFl2)
//							throw new FrontendException(
//									"Failed to assert fake integer limit.");
						z3declared = true;
					}
					if (branchTaken.isDuplicate) {
						duplicate = true;
					}
				} else if (branchTaken.getLog() != null) {
					List<ConstraintResult> r = c
							.GenerateMatchingTuple(branchTaken.getLog());
					if (r != null && r.get(0).getZ3ast() != null) {
						for (Iterator<ConstraintResult> irlt = r.iterator(); irlt
								.hasNext();) {
							ConstraintResult ur = irlt.next();
							z3.assert_cnstr(ur.getZ3ast());
							z3declared = true;
						}

					}
				} else if (branchTaken.isDuplicate) {
					duplicate = true;
				} else if (branchTaken.getCtr() != null) {
					ConstraintResult r = c.GenerateRlTuple(branchTaken.getCtr());
					if (r != null && r.getZ3ast() != null) {
						z3.assert_cnstr(r.getZ3ast());
						z3declared = true;
					}
				} else if (branchTaken.getCc() != null) {
					cc = branchTaken.getCc();
				}
			} catch (ExecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FrontendException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new AssertionResult(duplicate, z3declared, cc);
	}
	
	private boolean isCoralSatisfiable(ArrayList<PathBranch> goalPath) {
		

		for (PathBranch branchTaken : goalPath) {
			try {
				if (branchTaken.getL() != null) {
					ccoral.GenerateMatchingTuple(
							branchTaken.getL(), branchTaken.isInvert());	
				}
			} catch (ExecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FrontendException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		
		return false;
	}


  /**
   * TODO: Z3 faster with or without backtracking points?
   *
   * @return our high-level representation of the Z3 model,
   * null iff we could not find a model
   */
//  public ConstraintSolution solve( 
//  		PathTreeNode pathCondLeafGoal)
//  {
//  	queryId += 1;															// query id across all explorations
//
//  	z3.push();
//  	
//    assertPathCondition(pathCondLeafGoal);  	// assert pc conjuncts
//    
//    //try different concrete values of udf
//    
//    SWIGTYPE_p__Z3_model z3model =
//      z3.check_and_get_model_simple();  // Expensive call: 100+ ms
//    
//    if (z3model==null)
//      return null;     // found no model
//
//    z3.pop(1);
//    
//    return new Z3SolutionImpl(
//    		        z3model,
//    		        queryId);
//  }
  
	public ConstraintSolution solve(ArrayList<PathBranch> pathCondLeafGoal) {
		z3.push();
		
		if(pathCondLeafGoal.size() == 0)
			return null;
		
		AssertionResult ar = assertPathCondition(pathCondLeafGoal); // assert pc
		// conjuncts

		boolean z3declared = ar.isZ3declared();
		boolean z3duplicate = ar.isDuplicate();
		CountConstraint cc = ar.getCc();

		// try different concrete values of udf

		if (z3declared) {
			if(MainConfig.debug) {
				System.out.println("Z3 Context: ");
				System.out.println(z3.context_to_string());
			}
			
			SWIGTYPE_p__Z3_model z3model = z3.check_and_get_model_simple(); // Expensive
			// call:
			// 100+
			// ms
			// found no model by z3
			if (z3model == null) {
				if(c.udfcalled) {
					if(isCoralSatisfiable(pathCondLeafGoal)) {
						Boolean result = pb.solve();
	
						if (result == null) {
							System.out
									.println("## Coral Warning: timed out/ don't know (returned PC not-satisfiable) ");
						}
						if (result == Boolean.TRUE) {
							return new CoralSolutionImpl(ccoral.getCccoral(), false, null);
						} else {
							System.out
							.println("## Coral Warning: unsatisfiable");
						}
					}
				}

				return null; 
			}
				
			if(MainConfig.debug) {
				(new edu.umass.cs.pig.z3.test.SimpleTest())
				.display_function_interpretations(z3model);
			}
			

			

			return new Z3SolutionImpl(z3model, z3duplicate, cc);
		} else if (z3duplicate) {
			return new Z3SolutionImpl(null, z3duplicate, cc);
		} else if (cc != null) {
			return new Z3SolutionImpl(null, false, cc);
		} else
			return null;

	}
  
//	ConstraintResult GetMatchingConstraint(PathBranch p) {
//		try {
//			if(p.getL() != null)
//				return c.GenerateMatchingTuple(p.getL(), p.isInvert());
//			else if(p.getLog() != null)
//				return c.GenerateMatchingTuple(p.getLog());
//		} catch (ExecException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (FrontendException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
}
