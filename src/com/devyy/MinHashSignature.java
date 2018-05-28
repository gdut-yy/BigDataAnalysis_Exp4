package com.devyy;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Set;

public class MinHashSignature {
	protected static final int INF = 10000;

	// 构造出特征矩阵
	protected static String[][] createCharacteristicMatrix(Set<String> set, int k) throws IOException {
		Set<String> set1 = MinHash.getSet(k, KShingle.str1);
		Set<String> set2 = MinHash.getSet(k, KShingle.str2);
		String[][] matrix = MinHash.characteristicMatrix(set, set1, set2);
		return matrix;
	}

	// 将哈希函数h1(r)=(3r +1) mod 7，h2(r)=(5r +1) mod 7的结果加入
	protected static String[][] addHashFunction(String[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			matrix[i][3] = Integer.toString((3 * i + 1) % 7);
			matrix[i][4] = Integer.toString((5 * i + 1) % 7);
		}
		return matrix;
	}

	// 签名矩阵的计算算法
	protected static int[][] signatureCount(String[][] matrix) {
		int[][] signatureMatrix = new int[][] { { INF, INF }, { INF, INF } };
		int i, j;
		for (i = 0; i < matrix.length; i++) {
			if (matrix[i][1].equals("1") && Integer.valueOf(matrix[i][3]) <= signatureMatrix[0][0]) {
				signatureMatrix[0][0] = Integer.valueOf(matrix[i][3]);
			}
			if (matrix[i][1].equals("1") && Integer.valueOf(matrix[i][4]) <= signatureMatrix[1][0]) {
				signatureMatrix[1][0] = Integer.valueOf(matrix[i][4]);
			}
			if (matrix[i][2].equals("1") && Integer.valueOf(matrix[i][3]) <= signatureMatrix[0][1]) {
				signatureMatrix[0][1] = Integer.valueOf(matrix[i][3]);
			}
			if (matrix[i][2].equals("1") && Integer.valueOf(matrix[i][4]) <= signatureMatrix[1][1]) {
				signatureMatrix[1][1] = Integer.valueOf(matrix[i][3]);
			}
		}
		System.out.println("得到的签名矩阵为：");
		System.out.println("     S1  S2");
		for (i = 0; i < signatureMatrix.length; i++) {
			System.out.print("h" + i + "    ");
			for (j = 0; j < signatureMatrix[0].length; j++) {
				System.out.print(signatureMatrix[i][j] + "  ");
			}
			System.out.println();
		}
		return signatureMatrix;
	}

	// 求jaccard相似度
	protected static double signatureJaccard(Set<String> set, int k) throws IOException {
		int count = 0;
		double result = 0.0;
		String[][] matrix = createCharacteristicMatrix(set, k);
		matrix = addHashFunction(matrix);
		int[][] signatureMatrix = signatureCount(matrix);
		for (int i = 0; i < signatureMatrix.length; i++) {
			if (signatureMatrix[i][0] == signatureMatrix[i][1]) {
				count++;
			}
		}
		result = count * 1.0 / signatureMatrix.length;
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("所以可以推测SIM(S1, S2) = " + df.format(result));
		return result;
	}
}
