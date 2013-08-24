package edu.umass.cs.pig.cntr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.pig.impl.logicalLayer.FrontendException;

import edu.umass.cs.pig.dataflow.ValueExtractor;
import edu.umass.cs.pig.dataflow.VarInfo;
import edu.umass.cs.pig.util.Allocator4Z3;
import edu.umass.cs.pig.z3.Z3Context;
import edu.umass.cs.z3.SWIGTYPE_p__Z3_ast;

public abstract class PltRelCntr implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	Allocator4Z3 alloc;
	protected byte memberType, resultType;
	//schema is the schema of the child path list
	//LogicalSchema uidOnlySchema;
	ValueExtractor extr;
	ArrayList<String> nameDB;
//	LogicalSchema schema;
	
	
	
//	public String getChildVarUid() {
//		return childVar;
//	}
//	public void setChildVar(String childVar) {
//		this.childVar = childVar;
//	}
	
	// LogicalSchema schema, 
	// byte type
	public PltRelCntr(byte mt, byte rt) {
		
		//this.childVar = childVar;
//		alloc = new Allocator4Z3();
		//this.uidOnlySchema = uidOnlySchema;
		extr = new ValueExtractor();
//		this.schema = schema;
		this.memberType = mt;
		this.resultType = rt;
		nameDB = null;
	}
	
	public abstract SWIGTYPE_p__Z3_ast asZ3ast(Z3Context z3, Allocator4Z3 alloc) throws FrontendException;
	
	public abstract void syncNameDB(ArrayList<String> ndb);

	
//	public abstract void accept(CntrVisitor v) throws FrontendException;
	
//	public String asString() {
//		return (String)value;
//	}
//	
//	public boolean asBoolean() {
//		return (Boolean)value;
//	}
	
//	public String findChildVarName(Long childVarUid) {
//		for(int i=0; i< schema.size(); i++) {
//            LogicalFieldSchema f = schema.getField(i);
//            // if this field has the same uid, then return this field
//            if (f.uid == childVarUid) {
//                return f.alias;
//            } 
//		}
//		return null;
//	}
	
//	public byte findChildVarType(Long childVarUid) {
//		for(int i=0; i< uidOnlySchema.size(); i++) {
//            LogicalFieldSchema f = uidOnlySchema.getField(i);
//            // if this field has the same uid, then return this field
//            if (f.uid == childVarUid) {
//                return f.type;
//            } 
//		}
//		return -1;
//	}
//	public byte findChildVarType() {
//		return findChildVarType(childVarUid);
//	}
	
//	public LogicalSchema getSchema() {
//		return schema;
//	}
//	
//	public void setSchema(LogicalSchema schema) {
//		this.schema = schema;
//	}
	
	public VarInfo getFirstElem(List<VarInfo> params) {
		VarInfo param = null;
		if (params != null && !params.isEmpty()) {
			param = params.get(0);
		}
		return param;
	}

	public byte getMemberType() {
		return memberType;
	}

	public void setMemberType(byte memberType) {
		this.memberType = memberType;
	}

	public byte getResultType() {
		return resultType;
	}

	public void setResultType(byte resultType) {
		this.resultType = resultType;
	}
	
	protected String analyzeName(String n) {
		SyncNameDB sc = new SyncNameDB();
		return sc.analyzeName(n, nameDB);
	}
	
}
