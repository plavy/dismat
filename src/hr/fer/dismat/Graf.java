package hr.fer.dismat;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Graf {
	static ArrayList<ArrayList<Integer>> kombinacije = new ArrayList<>();
	static ArrayList<ArrayList<Integer>> ciklusi = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		System.out.print("Unesite ime datoteke: ");
		Scanner input = new Scanner(System.in);
		String name = input.next();
		input.close();

		Scanner file = new Scanner(Paths.get(name));

		int n;
		n = Integer.valueOf(file.nextLine());

		file.nextLine(); // preskakanje praznog reda

		String line;
		int matrica[][] = new int[n][n];

		// generiranje matrice susjedstva
		for (int i = 0; i < n; i++) {
			line = file.nextLine();
			for (int j = 0; j < 2 * n; j += 2) {
				matrica[i][j / 2] = line.charAt(j) - '0';
			}
		}

		file.close();

		// generiranje jednostavne liste vrhova
		ArrayList<Integer> vrhovi = new ArrayList<>();
		for (int i = 0; i < n; i++)
			vrhovi.add(i);

		// stvaranje kombinacija za svaku duljinu od n do 1
		ArrayList<Integer> result = new ArrayList<>(vrhovi);
		for (int i = n - 1; i > 0; i--) {
			result.remove(0);

			for (int v = 0; v < n; v++) {
				ArrayList<Integer> pomocna = new ArrayList<>(vrhovi);
				pomocna.remove(v); // micanje pocetnog/zavrsnog vrha

				combinations(v, pomocna, i, 0, result);
			}
		}

		// stvaranje permutacija za svaku kombinaciju te dodavanje pocetnog i zavrsnog
		// vrha
		for (int i = 0, v; i < kombinacije.size(); i++) {
			ArrayList<Integer> kombinacija = kombinacije.get(i);
			v = kombinacija.get(0);
			kombinacija.remove(0);
			heapPermutation(v, kombinacija, kombinacija.size(), kombinacija.size());
		}

		// testiranje svakog ciklusa dok se ispravan ne pronade
		int duljina = 0;
		for (int i = 0; i < ciklusi.size(); i++) {

			// regeneriranje matrice susjedstva
			int tragovi[][] = new int[n][n];
			for (int x = 0; x < n; x++) {
				for (int y = 0; y < n; y++)
					tragovi[x][y] = matrica[x][y];
			}

			// dohvat ciklusa
			ArrayList<Integer> ciklus = ciklusi.get(i);
			for (int j = 0; j < ciklus.size() - 1; j++) {
				int trenutni = ciklus.get(j);
				int sljedeci = ciklus.get(j + 1);

				// ostavljanje traga na iskoristenom bridu
				if (tragovi[trenutni][sljedeci] == 1) {
					tragovi[trenutni][sljedeci] = 0;
					tragovi[sljedeci][trenutni] = 0;
				} else
					break;

				// provjera kraja ciklusa i postavljanje duljine
				if (j == ciklus.size() - 2) {
					duljina = ciklus.size() - 1;
					break;
				}
			}

			if (duljina != 0) {
				System.out.println(ciklus);
				break;
			}
		}

		System.out.println(duljina);

	}

	// Algoritam za trazenje kombinacija
	// dijelom preuzeto s:
	// https://stackoverflow.com/questions/127704/algorithm-to-return-all-combinations-of-k-elements-from-n
	static void combinations(int v, ArrayList<Integer> a, int len, int startPosition, ArrayList<Integer> result) {
		if (len == 0) {
			ArrayList<Integer> dodaj = new ArrayList<>(result);
			dodaj.add(0, v);
			kombinacije.add(dodaj);
			return;
		}
		for (int i = startPosition; i <= a.size() - len; i++) {
			result.set(result.size() - len, a.get(i));
			combinations(v, a, len - 1, i + 1, result);
		}
	}

	// Algoritam za trazenje permutacija (Heap)
	// dijelom preuzeto s:
	// https://www.geeksforgeeks.org/heaps-algorithm-for-generating-permutations
	private static void heapPermutation(int v, ArrayList<Integer> a, int size, int n) {
		if (size == 1) {
			ArrayList<Integer> dodaj = new ArrayList<>(a);
			if (dodaj.size() == 1)
				dodaj.remove(0); // setnja V -> A -> V nije ciklus jednostavnog grafa, a V -> V jest
			dodaj.add(0, v);
			dodaj.add(v);
			ciklusi.add(dodaj);
		}

		for (int i = 0; i < size; i++) {
			heapPermutation(v, a, size - 1, n);

			if (size % 2 == 1) {
				int temp = a.get(0);
				a.set(0, a.get(size - 1));
				a.set(size - 1, temp);
			}

			else {
				int temp = a.get(i);
				a.set(i, a.get(size - 1));
				a.set(size - 1, temp);
			}
		}
	}
}
