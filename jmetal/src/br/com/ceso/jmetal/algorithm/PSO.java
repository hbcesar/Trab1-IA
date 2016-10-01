package br.com.ceso.jmetal.algorithm;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.core.Variable;
import jmetal.operators.selection.BestSolutionSelection;
import jmetal.util.JMException;
import jmetal.util.comparators.ObjectiveComparator;

public class PSO extends Algorithm {
	/**
	 * 
	 */
	private static final long serialVersionUID = -823473837484710493L;

	// Controladores do numero de avaliações
	private int maxEvaluations;
	private int evaluations = 0;

	// Variaveis de controle de particulas
	private int nmrParticulas = 0; // numero de particulas
	private SolutionSet particulas;

	// Variaveis especificas do PSO
	private double w; // coeficiente de inércia
	private double c1, c2; // coeficientes de aceleração
	private double r1, r2; // valores randômicos gerados a cada atualização de
	// velocidade

	// Variaveis que armazenam etapas da equação
	private double inertiaComponent;
	private double cognitiveComponent;
	private double socialComponent;

	// Variaveis de controle de melhor local e global
	private Solution globalBest;
	private Solution[] localBest;

	// Variáveis para velocidade e localizacao da particula
	private double[][] velocidade;
	
	//Variaveis de erro
	private double erroMax = 0;
	private int generations = 0;

	/**
	 * Comparator object
	 */
	Comparator<?> comparator;
	Operator findBestSolution;

	public PSO(Problem problem) {
		super(problem);

		comparator = new ObjectiveComparator(0); // Single objective comparator
		HashMap<String, Object> parameters; // Operator parameters

		parameters = new HashMap<String, Object>();
		parameters.put("comparator", comparator);
		findBestSolution = new BestSolutionSelection(parameters);
	}

	@Override
	public SolutionSet execute() throws JMException, ClassNotFoundException {
		// Lê os parametros do PSO
		maxEvaluations = (int) getInputParameter("maxEvaluations");
		evaluations = 0;
		w = (double) getInputParameter("inertialCoefficient");
		c1 = (double) getInputParameter("c1");
		c2 = (double) getInputParameter("c2");
		nmrParticulas = (int) getInputParameter("numeroParticulas");
		erroMax = (double) getInputParameter("erro");
		generations = (int) getInputParameter("generations");
		
		// maxEvaluations = maxEvaluations - nmrParticulas;
		evaluations = 0;

		// Inicializa variáveis
		evaluations++;
		particulas = new SolutionSet(nmrParticulas);
		localBest = new Solution[nmrParticulas];
		velocidade = new double[nmrParticulas][problem_.getNumberOfVariables()];

		boolean stop = false; // stop será setada como verdadeira quando
								// processo atingir condição de erro mínima

		// Cria as particulas e já guarda/atualiza seus melhores globais
		for (int i = 0; i < nmrParticulas; i++) {
			Solution particula = new Solution(problem_);
			
			Variable v[] = particula.getDecisionVariables();
			
			for(int j = 0; j < v.length; j++) {
				double range = Math.abs(problem_.getUpperLimit(j) - problem_.getLowerLimit(j))/(nmrParticulas-1);
				v[j].setValue(problem_.getLowerLimit(j) + range * i);
			}
			
			particula.setDecisionVariables(v);
			
			problem_.evaluate(particula);
			evaluations++;
			particulas.add(particula);

			// Durante a criação da particulas, já cria/atualiza o global best.
			if ((globalBest == null) || (particula.getObjective(0) < globalBest.getObjective(0))) {
				globalBest = new Solution(particula); // Quando já passa uma
														// Solution como
														// parametro para o
														// construtor de outra
														// solution, ele cria
														// uma cópia da mesma
			}
		}

		// Inicializa velocidade de cada particula
		for (int i = 0; i < nmrParticulas; i++) {
			for (int j = 0; j < problem_.getNumberOfVariables(); j++) {
				velocidade[i][j] = 0.0;
			}
		}

		// Inicializa memória de cada particula
		for (int i = 0; i < nmrParticulas; i++) {
			Solution particle = new Solution(particulas.get(i));
			localBest[i] = particle;
		}

		double erro = (-1) * Double.MAX_VALUE - 1.0;
		double gen = 0;

		while (!stop && evaluations < (maxEvaluations - nmrParticulas)) {
			//Armazena erro maximo

			// Calcula a velocidade de cada particula
			for (int i = 0; i < nmrParticulas; i++) {
				// gera valores randômicos
				r1 = random(0, 1);
				r2 = random(0, 1);

				Variable[] bestGlobal = globalBest.getDecisionVariables();
				Variable[] p = particulas.get(i).getDecisionVariables();
				Variable[] bestParticle = localBest[i].getDecisionVariables();

				for (int j = 0; j < problem_.getNumberOfVariables(); j++) {

					// Calcula coeficiente de inércia
					inertiaComponent = w * velocidade[i][j];

					// Calcula coeficiente cognitivo
					cognitiveComponent = c1 * r1 * (bestParticle[j].getValue() - p[j].getValue());

					// Calcula coeficiente social
					socialComponent = c2 * r2 * (bestGlobal[j].getValue() - p[j].getValue());

					// Calcula velocidade da partícula
					velocidade[i][j] = inertiaComponent + cognitiveComponent + socialComponent;

					// Atualiza posicao dentro do upper e lower bound
					p[j].setValue(p[j].getValue() + velocidade[i][j]);


					if (p[j].getValue() < problem_.getLowerLimit(j)) {
						p[j].setValue(problem_.getLowerLimit(j));
					}
					if (p[j].getValue() > problem_.getUpperLimit(j)) {
						p[j].setValue(problem_.getUpperLimit(j));
					}
				}
			}
			
			// Avalia as particulas na nova posicao
			for (int i = 0; i < nmrParticulas; i++) {
				Solution particle = particulas.get(i);
				problem_.evaluate(particle);
				evaluations++;
			}
			
			//Guarda o valor do best global anterior para o calculo do erro
			double g = globalBest.getObjective(0);

			// Atualiza a memória de cada particula
			for (int i = 0; i < nmrParticulas; i++) {
				if ((particulas.get(i).getObjective(0) > localBest[i].getObjective(0))) {
					Solution particle = new Solution(particulas.get(i));
					localBest[i] = particle;
				}
				if ((particulas.get(i).getObjective(0) > globalBest.getObjective(0))) {
					Solution particle = new Solution(particulas.get(i));
					globalBest = particle;
				} 
			}
			
			if(g - globalBest.getObjective(0) > erro){
				erro = g - globalBest.getObjective(0);
			}
			
			if(erro < erroMax){
				if(gen > generations) {
					stop = true;
				} else {
					gen++;
				}
			}
		}
		
		//Seta outputs para relatorio
		setOutputParameter("avaliacoes", evaluations);
		

		//Retorna a particula com o melhor resultado
	    SolutionSet resultPopulation = new SolutionSet(1) ;
//	     resultPopulation.add(particulas.get((Integer)findBestSolution.execute(particulas)));
	    resultPopulation.add(globalBest);
	    
	    return resultPopulation ;
	}

	private double random(double min, double max) {
		Random gerador = new Random();

		double numero = (gerador.nextFloat() * max) + min;

		return numero;
	}

//	private double distanciaEuclidiana(double[] err, double[] err2) {
//		double erro = 0;
//
////		System.out.println("erro length: " + err.length);
//		for (int i = 0; i < err.length; i++){
//			erro += Math.pow(err[i] - err2[i], 2);
//		}
//		
////		System.out.println("Erro: " + erro);
//
//		return Math.sqrt(erro);
//	}
}
