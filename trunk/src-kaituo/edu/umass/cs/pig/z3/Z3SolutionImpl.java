package edu.umass.cs.pig.z3;

import edu.umass.cs.pig.MainConfig;
import edu.umass.cs.pig.cntr.CountConstraint;
import edu.umass.cs.pig.z3.model.ModelConstants;
import edu.umass.cs.pig.z3.model.Z3ToJava;
import edu.umass.cs.z3.SWIGTYPE_p__Z3_model;

/**
 * Model (solution) produced by our Z3 constraint solver.
 */
public class Z3SolutionImpl implements ConstraintSolution {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7059022898286319573L;
	// private final SymbolicState vmState;
	private boolean duplicate;
	private CountConstraint cc = null;
	private final Z3ToJava z3ToJava = Z3ToJava.get();
	// private final Map<String, Variable> name2variable;

	protected final Z3Context z3 = Z3Context.get();
	protected final MainConfig conf = MainConfig.get();

	/**
	 * Model produced by Z3.
	 */
	private SWIGTYPE_p__Z3_model model;

	/**
	 * variable name --> integer
	 */
	// private final TObjectIntHashMap<String> variablesAry1;
	private final ModelConstants variablesAry1;

	// public TObjectIntHashMap<String> getVariablesAry1() {
	// return variablesAry1;
	// }

	
	public ModelConstants getVariablesAry1() {
		return variablesAry1;
	}

	/**
	 * Constructor
	 * 
	 * @param model
	 *            from Z3.
	 * @throws ModelParsingBugInDsc
	 *             if Dsc fails to parse the Z3 model.
	 */
	public Z3SolutionImpl(SWIGTYPE_p__Z3_model model, boolean d, CountConstraint c) {
		// loglnIf(conf.LOG_MODEL_Z3, conf.LOG_SECTION_PREFIX, "model by Z3");
		// loglnIf(conf.LOG_MODEL_Z3, z3.model_to_string(model)); // Model
		// string produced by Z3

		// loglnIf(conf.LOG_MODEL_DSC, conf.LOG_SECTION_PREFIX,
		// "model in Dsc notation");

		this.model = model;
		this.duplicate = d;
		this.cc = c;

		if (model != null) {
			if(MainConfig.debug) {
				System.out.println("Model Start : ");
				DisplayModel m = new DisplayModel();
				m.display_model(model);
				System.out.println("Model End : ");
			}
			
			variablesAry1 = // Phase 2 -- simple model values
			z3ToJava.parse1AryVariables(model);

		} else
			variablesAry1 = null;
		// success = computeSolution();// Phases 3 and 4

		// addDefaultSolution();
	}

	@Override
	public boolean isParsedOk() {
		return variablesAry1.isEmpty();
	}

//	public int getSolution(String name) {
//		return variablesAry1.get(name);
//	}
	
	public Object getSolution(String name) {
		return variablesAry1.getSolution(name);
	}

	@Override
	public void deleteModel() {
		z3.del_model(model);
		model = null;
	}

	public boolean isDuplicate() {
		return duplicate;
	}

	public void setDuplicate(boolean duplicate) {
		this.duplicate = duplicate;
	}

	public SWIGTYPE_p__Z3_model getModel() {
		return model;
	}

	public void setModel(SWIGTYPE_p__Z3_model model) {
		this.model = model;
	}
	
	public CountConstraint getCc() {
		return cc;
	}

	public void setCc(CountConstraint cc) {
		this.cc = cc;
	}

	public static void main(String[] args) {
		long i = 32L;
		System.out.println(i == 32);
	}
}