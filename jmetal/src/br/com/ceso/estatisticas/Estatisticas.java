package br.com.ceso.estatisticas;

public class Estatisticas {
	private double[] valores = new double[10];
	private int n = 0;

	public void add(double valor) {
		valores[n++] = valor;
	}

	public double media() {
		double media = 0;

		for (int i = 0; i < n; i++) {
			media += valores[i];
		}

		return media / (double) n;
	}

	public double minimo() {
		double min = Double.MAX_VALUE;

		for (int i = 0; i < n; i++) {
			if (valores[i] < min) {
				min = valores[i];
			}
		}

		return min;
	}

	public double maximo() {
		double max = (-1) * Double.MAX_VALUE - 1.0; // ou zero?

		for (int i = 0; i < n; i++) {
			if (valores[i] > max) {
				max = valores[i];
			}
		}

		return max;
	}

	public double variancia() {
		double media = media();
		double variancia = 0;

		for (int i = 0; i < n; i++) {
			variancia += Math.pow(valores[i] - media, 2);

		}

		return variancia / (double) (n - 1.0);
	}

	public double desvioPadrao() {
		double variancia = variancia();

		return Math.sqrt(variancia);
	}

	public String toString() {
		String s = "";

		s += "Média: " + this.media() + "\n";
		s += "Mínimo: " + this.minimo() + "\n";
		s += "Máximo: " + this.maximo() + "\n";
		s += "Desvio Padrão: " + String.format("%.2f", this.desvioPadrao()) + "\n\n";

		return s;
	}
}
