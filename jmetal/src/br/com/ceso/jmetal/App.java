package br.com.ceso.jmetal;

import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.core.Variable;
import jmetal.util.JMException;

public class App {

	public static void main(String[] args) throws ClassNotFoundException, JMException {
		Problem problem; //The problem to solve
		Algorithm algorithm; //The algorithm to use
		
		problem = new SumSquareProblem("Real", 1);
		algorithm = new HillClimbing(problem);
		
		//Algorithm parameters
		algorithm.setInputParameter("gamma", 0.001);
		algorithm.setInputParameter("maxEvaluations", 25000);
		
		//Execute the algorithm
		long initTime = System.currentTimeMillis();
		SolutionSet result = algorithm.execute();
		long estimatedTime = System.currentTimeMillis() - initTime;
		System.out.println("Total execution time: " + estimatedTime);
		
		//Log messages
		System.out.println("Total de avaliações realizadas: " + algorithm.getOutputParameter("evaluations"));
		Variable v[] = result.get(0).getDecisionVariables();
		System.out.print("Posição e resultado encontrado: \n[" + v[0].getValue());
		for(int i = 1; i < v.length; i++) {
			System.out.print("," + v[i].getValue());
		}
		System.out.println("] = " + result.get(0).getObjective(0));
		
		System.out.println("Objectives values have been writen to file FUN");
		result.printVariablesToFile("FUN");
		System.out.println("Variables values have been writen to file VAR");
		result.printVariablesToFile("VAR");
	}

}
