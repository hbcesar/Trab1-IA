package br.com.ceso.jmetal.algorithm;

import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.core.Variable;
import jmetal.util.JMException;

public class PSOAlgorithm extends Algorithm {
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

	public PSOAlgorithm(Problem problem) {
		super(problem);
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
		globalBest = null;
		localBest = new Solution[nmrParticulas];
		velocidade = new double[nmrParticulas][problem_.getNumberOfVariables()];

		boolean stop = false; // stop será setada como verdadeira quando
								// processo atingir condição de erro mínima

		// Cria as particulas e já guarda/atualiza seus melhores globais
		for (int i = 0; i < nmrParticulas; i++) {
			Solution particula = new Solution(problem_);
			
			Variable v[] = particula.getDecisionVariables();
			
			for(int j = 0; j < v.length; j++) {
				v[j].setValue(random(problem_.getLowerLimit(j), problem_.getUpperLimit(j)));
			}
			
			particula.setDecisionVariables(v);
			
			problem_.evaluate(particula);
			evaluations++;
			particulas.add(particula);

			// Durante a criação da particulas, já cria/atualiza o global best.
			if ((globalBest == null) || (particula.getObjective(0) < globalBest.getObjective(0))) {
				globalBest = new Solution(particula);
			}
		}

		// Inicializa velocidade de cada particula
		for (int i = 0; i < nmrParticulas; i++) {
			for (int j = 0; j < problem_.getNumberOfVariables(); j++) {
				velocidade[i][j] = 0.0;
			}
		}

		// Inicializa memória de cada particula (primeiro localbest é a propria particula)
		for (int i = 0; i < nmrParticulas; i++) {
			Solution particle = new Solution(particulas.get(i));
			localBest[i] = particle;
		}

		double erro = (-1) * Double.MAX_VALUE - 1.0; //minimo possivel
		double gen = 0; //verifica quantas vezes o erro minimo foi atingido

		while (!stop && evaluations < (maxEvaluations - nmrParticulas)) {
			// Calcula a velocidade de cada particula
			for (int i = 0; i < nmrParticulas; i++) {
				// gera valores randômicos
				r1 = random(0, 1);
				r2 = random(0, 1);

				Variable[] bestGlobal = globalBest.getDecisionVariables();
				Variable[] p = particulas.get(i).getDecisionVariables(); //particula que sera analisada
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

					//Confere se nova posicao esta dentro dos limites do problema, se nao tiver, seta para o limite
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
				if ((particulas.get(i).getObjective(0) < localBest[i].getObjective(0))) {
					Solution particle = new Solution(particulas.get(i));
					localBest[i] = particle;
				}
				if ((particulas.get(i).getObjective(0) < globalBest.getObjective(0))) {
					Solution particle = new Solution(particulas.get(i));
					globalBest = particle;
				} 
			}
			
			//Calcula o erro
			if(g - globalBest.getObjective(0) > erro){
				erro = g - globalBest.getObjective(0);
			}
			
			//Se erro minimo for atingido 20 vezes consecutivas, para iteracao (while)
			//Baseado em: http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.142.3511&rep=rep1&type=pdf
			if(erro < erroMax){
				if(gen > generations) {
					stop = true;
				} else {
					gen++;
				}
			} else {
				gen = 0;
			}
		}
		
		//Seta outputs para relatorio
		setOutputParameter("avaliacoes", evaluations);
		

		//Retorna a particula com o melhor resultado
	    SolutionSet resultPopulation = new SolutionSet(1) ;
	    resultPopulation.add(globalBest);
	    
	    return resultPopulation ;
	}

	private double random(double min, double max) {
		double numero = Math.random() * (max - min) + min;

		return numero;
	}
}
