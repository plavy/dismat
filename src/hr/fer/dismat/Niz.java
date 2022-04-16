package hr.fer.dismat;

import java.util.ArrayList;
import java.util.Scanner;

public class Niz {
    static double k0, k1, k2;
    static ArrayList<Double> pocetni = new ArrayList<>();

	public static void main(String[] args) {
		
		double x0, x1, x2;
		int n;
		Scanner ulaz = new Scanner(System.in);
		
		System.out.print("Unesite prvo rjesenje x_0 karakteristicne jednadzbe: ");
		x0 = ulaz.nextDouble();
		
		System.out.print("Unesite drugo rjesenje x_1 karakteristicne jednadzbe: ");
		x1 = ulaz.nextDouble();
		
		System.out.print("Unesite trece rjesenje x_2 karakteristicne jednadzbe: ");
		x2 = ulaz.nextDouble();
		
		System.out.print("Unesite vrijednost nultog clana niza a_0: ");
		pocetni.add(ulaz.nextDouble());
		
		System.out.print("Unesite vrijednost prvog clana niza a_1: ");
		pocetni.add(ulaz.nextDouble());
		
		System.out.print("Unesite vrijednost drugog clana niza a_2: ");
		pocetni.add(ulaz.nextDouble());
		
		System.out.print("Unesite redni broj n trazenog clana niza: ");
		n = ulaz.nextInt();
		
		
		// opca formula
		
		double[][] adata = {
				{1, 1, 1},
        		{x0, x1, x2},
        		{x0 * x0, x1 * x1, x2 * x2}
        };
		Matrica a = new Matrica(adata);
		
		double[][] bdata = {
				{pocetni.get(0)},
				{pocetni.get(1)},
				{pocetni.get(2)}
		};
		Matrica b = new Matrica(bdata);
		
		// a * x = b
		// x = a^-1 * b
		Matrica x = Matrica.pomnozi(Matrica.inverz(a), b);
		
        double kon = x.data[0][0] * Math.pow(x0, n) + x.data[1][0] * Math.pow(x1, n) + x.data[2][0] * Math.pow(x2, n);
        
        System.out.println("Vrijednost n-tog clana niza pomocu formule: " + Math.round(kon * 10000) / 10000);


        // rekurzija
        
        k0 = x0 + x1 + x2;
        k1 = 0 - x0 * x1 - x1 * x2 - x0 * x2;
        k2 = x0 * x1 * x2;

    	System.out.println("Vrijednost n-tog clana niza iz rekurzije: " + Math.round(clan(n) * 10000) /10000);
	}
	
	static double clan(int n) {
		if(n <= 2)
			return pocetni.get(n);
		else
			return k0*clan(n-1) + k1*clan(n-2) + k2*clan(n-3);
    }
}

class Matrica{
	public double[][] data;
	public Matrica(double[][] data) {
		this.data = data;
	}
	public Matrica(int redovi, int stupci) {
        data = new double[redovi][stupci];
    }
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < data.length; i++) {
			for(int j = 0; j < data[0].length; j++) {
				sb.append(data[i][j] + " ");
			}
			sb.append('\n');
		}
		return sb.toString();
	}
		
	static Matrica pomnozi (Matrica a, Matrica b){
		if (a.data[0].length != b.data.length) {
			throw new IllegalArgumentException("Neskladne dimenzije matrica");
		}
		double[][] c = new double[a.data.length][b.data[0].length];
		int zajednicka_dimenzija = b.data.length;
		for(int i = 0; i < a.data.length; i++) {
			for(int j = 0; j < b.data[0].length; j++) {
				c[i][j] = 0;
				for(int k = 0; k < zajednicka_dimenzija; k++) {
					c[i][j] += a.data[i][k] * b.data[k][j];
				}
			}
		}
		return new Matrica(c);

	}
	
	public Matrica pomnoziKonstantom(double k) {
		double[][] pomnozena = new double[data.length][data[0].length];
		for(int i = 0; i < data.length; i++) {
			for(int j = 0; j < data[0].length; j++) {
				pomnozena[i][j] = data[i][j] * k;
			}
		}
		return new Matrica(pomnozena);
	}
	
	public static int predznaceno(int i) {
		return i%2==0 ? 1 : -1; 
	}
	
	// izvor svih dolje navedenih metoda: https://www.codeproject.com/Articles/405128/Matrix-Operations-in-Java
	// prevedeno na hrvatski
	public static Matrica transponiraj(Matrica matrica) {
	    double[][] transponirana = new double[matrica.data[0].length][matrica.data.length];
	    for (int i=0; i < matrica.data.length; i++) {
	        for (int j=0; j < matrica.data[0].length; j++) {
	            transponirana[j][i] = matrica.data[i][j];
	        }
	    }
	    return new Matrica(transponirana);
	}
	
	public static Matrica podmatrica(Matrica matrica, int isk_red, int isk_stupac) {
		Matrica mat = new Matrica(matrica.data.length - 1, matrica.data[0].length - 1);
	    int r = -1;
	    for (int i = 0; i < matrica.data.length; i++) {
	        if (i==isk_red)
	            continue;
	            r++;
	            int c = -1;
	        for (int j = 0; j < matrica.data[0].length; j++) {
	            if (j==isk_stupac)
	                continue;
	            mat.data[r][++c] = matrica.data[i][j];
	        }
	    }
	    return mat;
	}
	
	public static double determinanta(Matrica matrica) {
	    if (matrica.data.length != matrica.data[0].length)
	        throw new IllegalArgumentException("Matrica nije kvadratna");
	    if (matrica.data[0].length == 1) {
		return matrica.data[0][0];
	    }
	    if (matrica.data.length == 2) {
	        return (matrica.data[0][0] * matrica.data[1][1]) - ( matrica.data[0][1] * matrica.data[1][0]);
	    }
	    double det = 0.0;
	    for (int i = 0; i < matrica.data[0].length; i++) {
	        det += predznaceno(i) * matrica.data[0][i] * determinanta(podmatrica(matrica, 0, i));
	    }
	    return det;
	}
	
	public static Matrica kofaktor(Matrica matrica) {
		Matrica mat = new Matrica(matrica.data.length, matrica.data[0].length);
	    for (int i = 0; i < matrica.data.length; i++) {
	        for (int j = 0; j < matrica.data[0].length; j++) {
	            mat.data[i][j] = predznaceno(i) * predznaceno(j) * determinanta(podmatrica(matrica, i, j));
	        }
	    }
	    return mat;
	}
	
	public static Matrica inverz(Matrica matrica) {
	    return (transponiraj(kofaktor(matrica)).pomnoziKonstantom(1.0/determinanta(matrica)));
	}
	
}