package br.com.ceso.jmetal.problem;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;

public class Eggholder extends Problem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2934969167137497775L;

	public Eggholder(String solutionType, Integer numberOfVariables) {
		numberOfVariables_ = numberOfVariables;
		numberOfObjectives_ = 1;
		numberOfConstraints_ = 0;
		problemName_ = "Eggholder";

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		// Seta os limites mínimo e máximo da função
		for (int i = 0; i < numberOfVariables_; i++) {
			lowerLimit_[i] = -512.0;
			upperLimit_[i] = 512.0;
		}

		if (solutionType.compareTo("Real") == 0) {
			solutionType_ = new RealSolutionType(this);
		} else {
			System.out.println("Erro: solution type");
			System.exit(-1);
		}
	}

	@Override
	public void evaluate(Solution solution) throws JMException {
		Variable[] decisionVariables = solution.getDecisionVariables();

		double fx;
		
		double term1;
		double term2;
		
		term1 = - (decisionVariables[1].getValue() + 47.0) * Math.sin(Math.sqrt(Math.abs(decisionVariables[1].getValue() + decisionVariables[0].getValue()/2.0 + 47.0)));
		term2 = -decisionVariables[0].getValue() * Math.sin(Math.sqrt(Math.abs(decisionVariables[0].getValue()-(decisionVariables[1].getValue() + 47.0))));

		fx = term1 + term2;
		
		solution.setObjective(0, fx);
	}

}
