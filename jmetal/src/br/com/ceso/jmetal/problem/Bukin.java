package br.com.ceso.jmetal.problem;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;

public class Bukin extends Problem {

	public Bukin(String solutionType, Integer numberOfVariables) {
		numberOfVariables_ = numberOfVariables;
		numberOfObjectives_ = 2;
		numberOfConstraints_ = 0;
		problemName_ = "Bukin";

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		// Seta o limite mínimo da função
		lowerLimit_[0] = -15.0;
		upperLimit_[0] = -5.0;
		lowerLimit_[1] = -3.0;
		upperLimit_[1] = 3.0;

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
		
//		System.out.println("Mocinhas da cidade: " + decisionVariables[0].getValue());

		double fx = 0;

		fx = 100 * Math
				.sqrt(Math.abs(decisionVariables[1].getValue() - 0.01 * Math.pow(decisionVariables[0].getValue(), 2)))
				+ 0.01 * Math.abs(decisionVariables[0].getValue() + 10);

		solution.setObjective(0, fx);
	}

}
