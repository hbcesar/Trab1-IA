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
		maxEvaluations = ((Integer) this.getInputParameter("maxEvaluations")).intValue();
		gamma = ((Double) this.getInputParameter("gamma")).doubleValue();
		
		//Initialize the variables
		Solution solution = new Solution(problem_);
		problem_.evaluate(solution);
		evaluations++;
		
		while(evaluations < maxEvaluations && solution.getObjective(0) != 0.0) {
			Variable[] r = solution.getDecisionVariables().clone();
			
			for(int i = 0; i < r.length; i++) {
				r[i].setValue(r[i].getValue() - gamma * 2.0 * (i + 1) * r[i].getValue());
			}
			
			Solution nSol = new Solution(problem_, r);
			problem_.evaluate(nSol);
			evaluations++;
			
			if(nSol.getObjective(0) < solution.getObjective(0)) {
				solution = nSol;
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
