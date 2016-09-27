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
import jmetal.util.wrapper.XReal;

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
	private double v = 0.0;
	private double inertiaComponent;
	private double cognitiveComponent;
	private double socialComponent;

	// Variaveis de controle de melhor local e global
	private Solution globalBest;
	private Solution[] localBest;

	// Variáveis para velocidade e localizacao da particula
	private double[][] velocidade;

	/**
	 * Comparator object
	 */
	Comparator comparator;
	Operator findBestSolution;

	public PSO(Problem problem) {
		super(problem);

		comparator = new ObjectiveComparator(0); // Single objective comparator
		HashMap parameters; // Operator parameters

		parameters = new HashMap();
		parameters.put("comparator", comparator);
		findBestSolution = new BestSolutionSelection(parameters);
	}

	@Override
	public SolutionSet execute() throws JMException, ClassNotFoundException {
		// Lê os parametros do PSO
		maxEvaluations = (int) getInputParameter("maxEvaluations");
		w = (double) getInputParameter("inertialCoefficient");
		c1 = (double) getInputParameter("c1");
		c2 = (double) getInputParameter("c2");
		nmrParticulas = (int) getInputParameter("numeroParticulas");

		// Inicializa variáveis
		Solution solution = new Solution(problem_);
		problem_.evaluate(solution);
		evaluations++;
		particulas = new SolutionSet(nmrParticulas);
		localBest = new Solution[nmrParticulas];
		velocidade = new double[nmrParticulas][problem_.getNumberOfVariables()];

		boolean stop = false; // stop será setada como verdadeira quando
								// processo atingir condição de erro mínima

		// Cria as particulas e já guarda/atualiza seus melhores globais
		for (int i = 0; i < nmrParticulas; i++) {
			Solution particula = new Solution(problem_);

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

		while (!stop && evaluations < maxEvaluations) {
			// Calcula a velocidade de cada particula
			for (int i = 0; i < nmrParticulas; i++) {
				// gera valores randômicos
				r1 = random();
				r2 = random();

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

			// Atualiza a memória de cada particula
			for (int i = 0; i < nmrParticulas; i++) {
				if ((particulas.get(i).getObjective(0) < localBest[i].getObjective(0))) {
					Solution particle = new Solution(particulas.get(i));
					localBest[i] = particle;
				}
				if ((particulas.get(i).getObjective(0) < globalBest.getObjective(0))) {
					Solution particle = new Solution(particulas.get(i));
					globalBest = particle;
				} 
			}
			
			// iteration_++;
		}
		
		//Seta outputs para relatorio
		setOutputParameter("avaliacoes", evaluations);
		

		//Retorna a particula com o melhor resultado
	    SolutionSet resultPopulation = new SolutionSet(1) ;
	    resultPopulation.add(particulas.get((Integer)findBestSolution.execute(particulas)));
	    
	    return resultPopulation ;
	}

	private float random() {
		float min = 0;
		float max = 1;
		Random gerador = new Random();

		float numero = (gerador.nextFloat() * max) + min;

		return numero;
	}
}
