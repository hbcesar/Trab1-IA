package br.com.ceso.jmetal;

import br.com.ceso.estatisticas.Estatisticas;
import br.com.ceso.jmetal.algorithm.PSO;
import br.com.ceso.jmetal.problem.*;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.core.Variable;
import jmetal.util.JMException;

public class App2 {
	public static void main(String[] args) throws ClassNotFoundException, JMException {
		//Variaveis inerentes ao algoritmo
		Problem problem; //The problem to solve
		Algorithm algorithm; //The algorithm to use
//		SolutionSet[] resultados;
		long initTime;
		long estimatedTime;
		
		/*********************** Roda o PSO para a Equação de Bukin *******************************/
		problem = new Bukin("Real", 2);
		algorithm = new PSO(problem);
		
		Estatisticas tempos = new Estatisticas();
		Estatisticas valores = new Estatisticas();
		Estatisticas avaliacoes = new Estatisticas();
		Estatisticas x = new Estatisticas();
		Estatisticas y = new Estatisticas();
		
		
		//Algorithm parameters
		algorithm.setInputParameter("maxEvaluations", 25000);
		algorithm.setInputParameter("inertialCoefficient", 0.8);
		algorithm.setInputParameter("c1", 1.2);
		algorithm.setInputParameter("c2", 1.8);
		algorithm.setInputParameter("numeroParticulas", 30);
		
		
		//Execute the algorithm
		for(int i = 0; i < 10; i++){
			initTime = System.currentTimeMillis();
			SolutionSet result = algorithm.execute();
			estimatedTime = System.currentTimeMillis() - initTime;
			
			Variable v[] = result.get(0).getDecisionVariables();
			x.add(v[0].getValue());
			y.add(v[1].getValue());
			
			tempos.add(estimatedTime);
			valores.add(result.get(0).getObjective(0));
			avaliacoes.add((double)Integer.parseInt(algorithm.getOutputParameter("avaliacoes").toString()));
		}
		
		//Relatorios
		System.out.println("/********** EQUAÇÃO DE BUKIN *********/");
		System.out.println("/********** TEMPO (em ms) *********/");
		System.out.println(tempos.toString());
		System.out.println("/********** NÚMERO DE AVALIAÇÕES *********/");
		System.out.println(avaliacoes.toString());
		System.out.println("/********** OTIMOS *********/");
		System.out.println(valores.toString());
		System.out.println("\n");
		
		
		/*********************** Roda o PSO para a Equação de Eggholder *******************************/
		problem = new Eggholder("Real", 2);
		algorithm = new PSO(problem);
		
		tempos = new Estatisticas();
		valores = new Estatisticas();
		avaliacoes = new Estatisticas();
		x = new Estatisticas();
		y = new Estatisticas();
		
		//Algorithm parameters
		algorithm.setInputParameter("maxEvaluations", 25000);
		algorithm.setInputParameter("inertialCoefficient", 0.8);
		algorithm.setInputParameter("c1", 1.2);
		algorithm.setInputParameter("c2", 1.8);
		algorithm.setInputParameter("numeroParticulas", 50);
		
		
		//Execute the algorithm
		for(int i = 0; i < 10; i++){
			initTime = System.currentTimeMillis();
			SolutionSet result = algorithm.execute();
			estimatedTime = System.currentTimeMillis() - initTime;
			
			Variable v[] = result.get(0).getDecisionVariables();
			x.add(v[0].getValue());
			y.add(v[1].getValue());
			
			tempos.add(estimatedTime);
			valores.add(result.get(0).getObjective(0));
			avaliacoes.add((double)Integer.parseInt(algorithm.getOutputParameter("avaliacoes").toString()));
		}
		
		//Relatorios
		System.out.println("/********** EQUAÇÃO DE EGGHOLDER *********/");
		System.out.println("/********** TEMPO (em ms) *********/");
		System.out.println(tempos.toString());
		System.out.println("/********** NÚMERO DE AVALIAÇÕES *********/");
		System.out.println(avaliacoes.toString());
		System.out.println("/********** OTIMOS *********/");
		System.out.println(valores.toString());
		System.out.println("\n");
		
		/*********************** Roda o PSO para a Equação de Griewank *******************************/
		problem = new Griewank("Real", 3);
		algorithm = new PSO(problem);
		
		tempos = new Estatisticas();
		valores = new Estatisticas();
		avaliacoes = new Estatisticas();
		x = new Estatisticas();
		y = new Estatisticas();
		Estatisticas z = new Estatisticas();
		
		
		//Algorithm parameters
		algorithm.setInputParameter("maxEvaluations", 25000);
		algorithm.setInputParameter("inertialCoefficient", 0.8);
		algorithm.setInputParameter("c1", 1.2);
		algorithm.setInputParameter("c2", 1.8);
		algorithm.setInputParameter("numeroParticulas", 50);
		
		
		//Execute the algorithm
		for(int i = 0; i < 10; i++){
			initTime = System.currentTimeMillis();
			SolutionSet result = algorithm.execute();
			estimatedTime = System.currentTimeMillis() - initTime;
			
			Variable v[] = result.get(0).getDecisionVariables();
			x.add(v[0].getValue());
			y.add(v[1].getValue());
			z.add(v[2].getValue());
			tempos.add(estimatedTime);
			valores.add(result.get(0).getObjective(0));
			avaliacoes.add((double)Integer.parseInt(algorithm.getOutputParameter("avaliacoes").toString()));
		}
		
		//Relatorios
		System.out.println("/********** EQUAÇÃO DE GRIEWANK *********/");
		System.out.println("/********** TEMPO (em ms) *********/");
		System.out.println(tempos.toString());
		System.out.println("/********** NÚMERO DE AVALIAÇÕES *********/");
		System.out.println(avaliacoes.toString());
		System.out.println("/********** OTIMOS *********/");
		System.out.println(valores.toString());
		
		
		//Log messages
//		System.out.println("Total de avaliações realizadas: " + algorithm.getOutputParameter("evaluations"));
//		Variable v[] = result.get(0).getDecisionVariables();
//		System.out.print("Posição e resultado encontrado: \n[" + v[0].getValue());
//		for(int i = 1; i < v.length; i++) {
//			System.out.print("," + v[i].getValue());
//		}
//		System.out.println("] = " + result.get(0).getObjective(0));
//		
//		System.out.println("Objectives values have been writen to file FUN");
//		result.printVariablesToFile("FUN");
//		System.out.println("Variables values have been writen to file VAR");
//		result.printVariablesToFile("VAR");
//		Variable v[] = result.get(0).getDecisionVariables();
//		x.add(v[0]);
//		y.add(v[1]);
	}

}


