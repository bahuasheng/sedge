package edu.umass.cs.pig.z3;

import java.io.Serializable;

import edu.umass.cs.pig.cntr.CountConstraint;
import edu.umass.cs.pig.z3.model.ModelConstants;


/**
 * Solution (model) produced by Z3
 * 
 * @author csallner@uta.edu (Christoph Csallner)
 */
public interface ConstraintSolution extends Serializable {

  /**
   * @return could parse solution
   */
  public abstract boolean isParsedOk();  
  
	/**
	 * @return concrete values
	 */
    public ModelConstants getVariablesAry1();

	/**
	 * Delete underlying Z3 model.
	 */
	public void deleteModel();
	
	public CountConstraint getCc();
	
	public boolean isDuplicate();
}