package br.com.ceso.jmetal.algorithm;

import java.util.Random;

import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.core.Variable;
import jmetal.util.JMException;

public class PSO extends Algorithm {
	/**
	 * 
	 */
	private static final long serialVersionUID = -823473837484710493L;

	public PSO(Problem problem) {
		super(problem);
	}

	@Override
	public SolutionSet execute() throws JMException, ClassNotFoundException {
		//Controladores do numero de avaliações
		int maxEvaluations;
		int evaluations = 0;
		
		//Variaveis especificas do PSO
		double w; //coeficiente de inércia
		double c1, c2; //coeficientes de aceleração
		double r1, r2; //valores randômicos gerados a cada atualização de velocidade
		
		//Variaveis que armazenam etapas da equação
		double v = 0.0;
		double inertiaComponent;
		double cognitiveComponent;
		double socialComponent;
		
		//Melhor global
		double globalBest = 0.0;
		
		//Read/set the params
		maxEvaluations = (int) getInputParameter("maxEvaluations");
		w = (double) getInputParameter("inertialCoefficient");
		c1 = (double) getInputParameter("c1");
		c2 = (double) getInputParameter("c2");
		
		
		//Initialize the variables
		Solution solution = new Solution(problem_);
		problem_.evaluate(solution);
		evaluations++;
		
		boolean stop = false; //acho que isso é uma gambiarrinha

		while(!stop && evaluations < maxEvaluations) {
			Variable[] var = solution.getDecisionVariables().clone();
			
			double localBests[] = new double[var.length];
			
			for(int i = 0; i < var.length; i++){
				localBests[i] = 0;
			}
			
			for(int i = 0; i < var.length; i++) {
				//gera valores randômicos
				r1 = random(0, 5000);
				r2 = random(0, 5000);
				
				inertiaComponent = w * v;
				cognitiveComponent = c1 * r1 * (localBests[i] - var[i].getValue());
				socialComponent = c2 * r2 * (globalBest - var[i].getValue());
				
				v = inertiaComponent + cognitiveComponent + socialComponent;
				
				var[i].setValue(var[i].getValue() + v);
				
				//atualiza global best
				if(var[i].getValue() > globalBest) {
					globalBest = var[i].getValue();
				}
				
				//atualiza local best
				if(var[i].getValue() > localBests[i]){
					localBests[i] = var[i].getValue();
				}

				//Limita valores das variaveis ao limite minimo da função
//				if(var[i].getValue() > var[i].getUpperBound()) {
//					var[i].setValue(var[i].getUpperBound());
//				} else 
				if (var[i].getValue() < var[i].getLowerBound()) {
					var[i].setValue(var[i].getLowerBound());
				}
			}
			
			Solution nSol = new Solution(problem_, var);
			problem_.evaluate(nSol);
			evaluations++;
			
			//o que isso faz?
			if(nSol.getObjective(0) < solution.getObjective(0)) {
				solution = nSol;
			} else { //coisa nova
				stop = true;
			}
		}
		
		//Return the required evaluation as a output parameter
		setOutputParameter("evaluations", evaluations);
		
		//Return the best individual
		SolutionSet result = new SolutionSet(1);
		result.add(solution);
		
		return result;
	}
	
	private float random(float min, float max){
		Random gerador = new Random();
		 
        float numero = (gerador.nextFloat() * max) + min;
        
        return numero;
	}
}
