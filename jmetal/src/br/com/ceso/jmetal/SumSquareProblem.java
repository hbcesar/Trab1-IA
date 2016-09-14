package br.com.ceso.jmetal;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;

public class SumSquareProblem extends Problem {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2987824473637836202L;
	
	public SumSquareProblem(String solutionType, Integer numberOfVariables){
		numberOfVariables_ = numberOfVariables;
		numberOfObjectives_ = 1;
		numberOfConstraints_ = 0;
		problemName_ = "SumSquare";
		
		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];
		
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
		
		double sum = 0.0;
		for(int var = 0; var < numberOfVariables_; var++){
			sum += (var+1) * decisionVariables[var].getValue() * decisionVariables[var].getValue();
		}

		solution.setObjective(0, sum);
	}

}
