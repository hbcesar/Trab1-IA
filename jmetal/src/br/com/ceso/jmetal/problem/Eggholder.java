package br.com.ceso.jmetal.problem;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;

public class Eggholder extends Problem {
	
	public Eggholder (String solutionType, Integer numberOfVariables){
		numberOfVariables_ = numberOfVariables;
		numberOfObjectives_ = 1;
		numberOfConstraints_ = 0;
		problemName_ = "Eggholder";
		
		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];
		
//		Seta o limite mínimo da função
		for(int var = 0; var < numberOfVariables_; var++){
			lowerLimit_[var] = -959.6407;
//			upperLimit_[var] = 10.0;
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
		
		fx = (decisionVariables[1].getValue() + 47) * Math.sin(Math.sqrt(Math.abs(decisionVariables[1].getValue() + (decisionVariables[0].getValue()/2) + 47))) - decisionVariables[0].getValue() * Math.sin(Math.sqrt(Math.abs(decisionVariables[0].getValue() - (decisionVariables[1].getValue() + 47))));

		solution.setObjective(0, fx);
	}

}
