package br.com.ceso.jmetal.problem;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;

public class Bukin extends Problem {
	
	public Bukin (String solutionType, Integer numberOfVariables){
		numberOfVariables_ = numberOfVariables;
		numberOfObjectives_ = 1;
		numberOfConstraints_ = 0;
		problemName_ = "Bukin";
		
		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];
		
		//?????????
		for(int var = 0; var < numberOfVariables_; var++){
			lowerLimit_[var] = -10.0;
			upperLimit_[var] = 10.0;
		}
		
		if(solutionType.compareTo("Real") == 0) {
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
		
		fx = 100 * Math.sqrt(Math.abs(x2 - 0.01 * x1)) + 0.01 * Math.abs(x1 + 10);

		solution.setObjective(0, fx);
	}

}
