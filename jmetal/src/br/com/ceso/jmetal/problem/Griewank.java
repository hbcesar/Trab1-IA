package br.com.ceso.jmetal.problem;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;

public class Griewank extends Problem {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5017238166842643298L;

	public Griewank (String solutionType, Integer numberOfVariables){
		numberOfVariables_ = numberOfVariables;
		numberOfObjectives_ = 1;
		numberOfConstraints_ = 0;
		problemName_ = "Griewank";
		
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
		double somatorio = 0;
		double produtorio = 0;
		
		for(int i = 1; i < numberOfVariables_; i++){
			somatorio += Math.pow(decisionVariables[i].getValue(), 2) / 4000;
			produtorio *= Math.cos(decisionVariables[i].getValue() / Math.sqrt(i));
		}
		
		fx = somatorio - produtorio + 1;
		
		solution.setObjective(0, fx);
	}

}
