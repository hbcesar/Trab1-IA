package br.com.ceso.jmetal;

import br.com.ceso.erro.RealNumberException;
import br.com.ceso.estatisticas.Estatisticas;
import br.com.ceso.jmetal.algorithm.PSOAlgorithm;
import br.com.ceso.jmetal.problem.*;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.core.Variable;
import jmetal.util.JMException;

public class PSO {
	public static void main(String[] args) throws ClassNotFoundException, JMException {
		// Variaveis inerentes ao algoritmo
		Problem problem; // The problem to solve
		Algorithm algorithm; // The algorithm to use
		long initTime;
		long estimatedTime;

		// Variaveis usadas para gerar o relatorio
		Estatisticas tempos = new Estatisticas();
		Estatisticas valores = new Estatisticas();
		Estatisticas avaliacoes = new Estatisticas();
		Estatisticas x = new Estatisticas();
		Estatisticas y = new Estatisticas();

		/*****************************************
		 * Roda o PSO para a Equação de Bukin
		 *****************************************/
		try {
			problem = new Bukin("Real", 2);

			algorithm = new PSOAlgorithm(problem);

			// Algorithm parameters
			algorithm.setInputParameter("maxEvaluations", 25000);
			algorithm.setInputParameter("inertialCoefficient", 1.2);
			algorithm.setInputParameter("c1", 2.0);
			algorithm.setInputParameter("c2", 1.7);
			algorithm.setInputParameter("numeroParticulas", 20);
			algorithm.setInputParameter("erro", 0.000001);
			algorithm.setInputParameter("generations", 20);

			// Execute the algorithm
			for (int i = 0; i < 10; i++) {
				initTime = System.currentTimeMillis();
				SolutionSet result = algorithm.execute();
				estimatedTime = System.currentTimeMillis() - initTime;

				Variable v[] = result.get(0).getDecisionVariables();
				x.add(v[0].getValue());
				y.add(v[1].getValue());

				tempos.add(estimatedTime);
				valores.add(result.get(0).getObjective(0));
				avaliacoes.add((double) Integer.parseInt(algorithm.getOutputParameter("avaliacoes").toString()));
			}

			// Relatorios
			System.out.println("------------------- EQUAÇÃO DE BUKIN -------------------");
			System.out.println("TEMPO (em ms)");
			System.out.println(tempos.toString());
			System.out.println("NÚMERO DE AVALIAÇÕES");
			System.out.println(avaliacoes.toString());
			System.out.println("OTIMOS");
			System.out.println(valores.toString(x, y));
			System.out.println("\n");

		} catch (RealNumberException e) {
			System.out.println("Nao foi possivel rodar algoritmo de Bukin: ");
			e.printStackTrace();
		}
		/*********************************************
		 * Roda o PSO para a Equação de Eggholder
		 *********************************************/
		try {
			problem = new Eggholder("Real", 2);

			algorithm = new PSOAlgorithm(problem);

			tempos = new Estatisticas();
			valores = new Estatisticas();
			avaliacoes = new Estatisticas();
			x = new Estatisticas();
			y = new Estatisticas();

			// Algorithm parameters
			algorithm.setInputParameter("maxEvaluations", 25000);
			algorithm.setInputParameter("inertialCoefficient", 1.2);
			algorithm.setInputParameter("c1", 2.0);
			algorithm.setInputParameter("c2", 1.7);
			algorithm.setInputParameter("numeroParticulas", 20);
			algorithm.setInputParameter("erro", 0.000001);
			algorithm.setInputParameter("generations", 20);

			// Execute the algorithm
			for (int i = 0; i < 10; i++) {
				initTime = System.currentTimeMillis();
				SolutionSet result = algorithm.execute();
				estimatedTime = System.currentTimeMillis() - initTime;

				Variable v[] = result.get(0).getDecisionVariables();
				x.add(v[0].getValue());
				y.add(v[1].getValue());

				tempos.add(estimatedTime);
				valores.add(result.get(0).getObjective(0));
				avaliacoes.add((double) Integer.parseInt(algorithm.getOutputParameter("avaliacoes").toString()));
			}

			// Relatorios
			System.out.println("------------------- EQUAÇÃO DE EGGHOLDER -------------------");
			System.out.println("TEMPO (em ms)");
			System.out.println(tempos.toString());
			System.out.println("NÚMERO DE AVALIAÇÕES");
			System.out.println(avaliacoes.toString());
			System.out.println("OTIMOS");
			System.out.println(valores.toString(x, y));
			System.out.println("\n");

		} catch (RealNumberException e) {
			System.out.println("Nao foi possivel rodar algoritmo de Eggholder: ");
			e.printStackTrace();
		}
		
		/**********************************************
		 * Roda o PSO para a Equação de Griewank
		 **********************************************/
		try {
			problem = new Griewank("Real", 3);

			algorithm = new PSOAlgorithm(problem);

			tempos = new Estatisticas();
			valores = new Estatisticas();
			avaliacoes = new Estatisticas();
			x = new Estatisticas();
			y = new Estatisticas();
			Estatisticas z = new Estatisticas();

			// Algorithm parameters
			algorithm.setInputParameter("maxEvaluations", 25000);
			algorithm.setInputParameter("inertialCoefficient", 1.2);
			algorithm.setInputParameter("c1", 2.0);
			algorithm.setInputParameter("c2", 1.7);
			algorithm.setInputParameter("numeroParticulas", 20);
			algorithm.setInputParameter("erro", 0.000001);
			algorithm.setInputParameter("generations", 20);

			// Execute the algorithm
			for (int i = 0; i < 10; i++) {
				initTime = System.currentTimeMillis();
				SolutionSet result = algorithm.execute();
				estimatedTime = System.currentTimeMillis() - initTime;

				Variable v[] = result.get(0).getDecisionVariables();
				x.add(v[0].getValue());
				y.add(v[1].getValue());
				z.add(v[2].getValue());
				tempos.add(estimatedTime);
				valores.add(result.get(0).getObjective(0));
				avaliacoes.add((double) Integer.parseInt(algorithm.getOutputParameter("avaliacoes").toString()));
			}

			// Relatorios
			System.out.println("------------------- EQUAÇÃO DE GRIEWANK -------------------");
			System.out.println("TEMPO (em ms)");
			System.out.println(tempos.toString());
			System.out.println("NÚMERO DE AVALIAÇÕES");
			System.out.println(avaliacoes.toString());
			System.out.println("OTIMOS");
			System.out.println(valores.toString(x, y, z));
			System.out.println("\n");

		} catch (RealNumberException e) {
			System.out.println("Nao foi possivel rodar algoritmo de Griewank: ");
			e.printStackTrace();
		}
	}

}
