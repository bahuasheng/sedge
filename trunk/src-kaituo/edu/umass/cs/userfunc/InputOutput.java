package edu.umass.cs.userfunc;


import org.apache.pig.backend.hadoop.executionengine.physicalLayer.Result;

public class InputOutput {
	Result input, output;
	
	public InputOutput() {
		
	}

	public Result getInput() {
		return input;
	}

	public void setInput(Result input) {
		this.input = input;
	}

	public Result getOutput() {
		return output;
	}

	public void setOutput(Result output) {
		this.output = output;
	}
	
	

}
