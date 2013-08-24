package edu.umass.cs.userfunc;

import java.util.List;

import org.apache.pig.FuncSpec;
import org.apache.pig.impl.util.MultiMap;

import edu.umass.cs.z3.AstArray;
import edu.umass.cs.z3.AstArrayTheirs;
import edu.umass.cs.z3.SWIGTYPE_p__Z3_ast;
import edu.umass.cs.z3.SWIGTYPE_p__Z3_func_decl;
import edu.umass.cs.z3.SWIGTYPE_p__Z3_sort;

public class UserFuncValue {
	private static UserFuncValue singleton = new UserFuncValue();
	private MultiMap<FuncSpec, InputOutput> userToValMap = new MultiMap<FuncSpec, InputOutput>();

	/**
	 * Singleton
	 */
	public static UserFuncValue get() {
		return singleton;
	}

	/**
	 * If no config set yet, then set a new MainConfig.
	 */
	public static UserFuncValue setInstance() {
		if (singleton == null)
			singleton = new UserFuncValue();
		return get();
	}

	/**
	 * If no config set yet, then set conf. This method should only be called if
	 * conf is an instance of a sub-class of MainConfig.
	 */
	public static UserFuncValue setInstance(UserFuncValue conf) {
		if (singleton == null)
			singleton = conf;
		return get();
	}

	/**
	 * Constructor.
	 * 
	 * <p>
	 * <b>Should only be called from subclass constructor</b> (or
	 * {@link #setInstance()}).
	 */
	protected UserFuncValue() {
		// empty
	}

	public MultiMap<FuncSpec, InputOutput> getUserToValMap() {
		return userToValMap;
	}

	public void setUserToValMap(MultiMap<FuncSpec, InputOutput> userToValMap) {
		this.userToValMap = userToValMap;
	}
	
	public void put(FuncSpec key, InputOutput value) {
		 userToValMap.put(key, value);
	}
	
	public  List<InputOutput> get(FuncSpec key) {
		return userToValMap.get(key);
	}
}