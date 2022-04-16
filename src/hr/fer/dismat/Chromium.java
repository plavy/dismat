package hr.fer.dismat;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Chromium {
	static int matrica[][];
	static ArrayList<Integer> vrhovi = new ArrayList<>();
	static ArrayList<ArrayList<Integer>> permutacije = new ArrayList<>();
	static ArrayList<Boolean> boje = new ArrayList<>();
	static ArrayList<Integer> boje_vrhova = new ArrayList<>();
	static long safety_counter = 0;

	public static void main(String[] args) throws Exception {
		System.out.print("Unesite ime datoteke: ");
		Scanner input = new Scanner(System.in);
		String name = input.next();
		input.close();

		Scanner file = new Scanner(Paths.get(name));

		int n;
		n = Integer.valueOf(file.nextLine());

		file.nextLine(); // preskakanje praznog reda

		String line;
		matrica = new int[n][n];
		// generiranje matrice susjedstva
		for (int i = 0; i < n; i++) {
			line = file.nextLine();
			for (int j = 0; j < 2 * n; j += 2) {
				matrica[i][j / 2] = line.charAt(j) - '0';
			}
		}

		file.close();

		// generiranje jednostavne liste vrhova
		for (int i = 0; i < n; i++)
			vrhovi.add(i);

		// generiranje svih permutacija vrhova
		heapPermutation(vrhovi, n, n);

		int min_brojac = n;
		for (int i = 0; i < permutacije.size(); i++) {

			boje.clear();
			// generiranje liste za iskoristavanje boje
			for (int j = 0; j < n; j++)
				boje.add(false); // nekoristena boja

			boje_vrhova.clear();
			// generiranje liste boja vrhova
			for (int j = 0; j < n; j++)
				boje_vrhova.add(-1); // neobojeno

			// boji vrh po vrh trenutne permutacije
			for (int j = 0; j < n; j++) {
				oboji(permutacije.get(i).get(j));
			}

			// prebroji iskoristene boje
			int brojac = 0;
			for (int j = 0; j < n; j++) {
				if (boje.get(j) == true)
					brojac++;
			}

			// azuriraj najmanji brojac
			if (brojac < min_brojac)
				min_brojac = brojac;
		}

		System.out.println(min_brojac); // kromatski broj

	}

	static void oboji(int index) {
		ArrayList<Integer> susjedi = new ArrayList<>();
		for (int i = 0; i < vrhovi.size(); i++) {
			if (matrica[i][index] == 1)
				susjedi.add(boje_vrhova.get(i)); // boja susjeda
		}

		int b;
		for (b = 0; b < boje.size(); b++) {
			if (boje.get(b) == true) {
				boolean dostupna_boja = true;
				for (int boja_susjeda : susjedi) {
					if (boja_susjeda == b) {
						dostupna_boja = false;
						break;
					}
				}
				if (dostupna_boja == true)
					break;
			} else
				break;
		}
		boje_vrhova.set(index, b);
		boje.set(b, true);
	}

	// Algoritam za trazenje permutacija (Heap)
	// dijelom preuzeto s:
	// https://www.geeksforgeeks.org/heaps-algorithm-for-generating-permutations
	private static void heapPermutation(ArrayList<Integer> a, int size, int n) {
		if (safety_counter < 10000000) {
			if (size == 1) {
				ArrayList<Integer> dodaj = new ArrayList<>(a);
				permutacije.add(dodaj);
				safety_counter++;
			}

			for (int i = 0; i < size; i++) {
				heapPermutation(a, size - 1, n);

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

}
