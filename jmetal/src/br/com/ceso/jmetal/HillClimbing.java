package br.com.ceso.jmetal;

import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.core.Variable;
import jmetal.util.JMException;

public class HillClimbing extends Algorithm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3793855276125752671L;

	public HillClimbing(Problem problem){
		super(problem);
	}

	@Override
	public SolutionSet execute() throws JMException, ClassNotFoundException {
		int maxEvaluations;
		double gamma;
		int evaluations = 0;
		
		//Read the params
		// maxEvaluations = ((Integer) this.getInputParameter("maxEvaluations")).intValue();
		// gamma = ((Double) this.getInputParameter("gamma")).doubleValue();
		maxEvaluations = (int) getInputParameter("maxEvaluations");
		gamma = (double) getInputParameter("gamma");
		
		//Initialize the variables
		Solution solution = new Solution(problem_);
		problem_.evaluate(solution);
		evaluations++;
		
		boolean stop = false; //acho que isso é uma gambiarrinha

		while(!stop && evaluations < maxEvaluations && solution.getObjective(0) != 0.0) {
			Variable[] var = solution.getDecisionVariables().clone();
			
			for(int i = 0; i < var.length; i++) {
				var[i].setValue(var[i].getValue() - gamma * 2.0 * (i + 1) * var[i].getValue());

				//coisa nova tbm
				if(var[i].getValue() > var[i].getUpperBound()) {
					var[i].setValue(var[i].getUpperBound());
				} else if (var[i].getValue() < var[i].getLowerBound()) {
					var[i].setValue(var[i].getLowerBound());
				}
			}
			
			Solution nSol = new Solution(problem_, var);
			problem_.evaluate(nSol);
			evaluations++;
			
			if(nSol.getObjective(0) < solution.getObjective(0)) {
				solution = nSol;
			} else { //coisa nova
				stop = true;
			}
		}
		
		//Return the required evaluation as a output parameter
		setOutputParameter("evaluations", evaluations);
		
		//Return the best individual
		SolutionSet result = new SolutionSet(1);
		result.add(solution);
		
		return result;
	}

}
