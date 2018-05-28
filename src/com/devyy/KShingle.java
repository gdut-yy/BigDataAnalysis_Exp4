package com.devyy;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Set;
import java.util.TreeSet;

public class KShingle {

	// 文本一
	protected static final String str1 = "The TOEFL test is an English language assessment that is often required for admission by "
			+ "English-speaking universities and programs around the world. In addition to being accepted at "
			+ "more than 10,000 institutions in over 130 countries, including Australia, Canada, and the US, "
			+ "TOEFL scores help you get noticed by admissions officers who consider the TOEFL test a more "
			+ "accurate measure of your ability to succeed in a university setting.";

	// 文本二
	protected static final String str2 = "The TOEFL test is the most widely respected English-language test in the world, recognized by "
			+ "more than 10,000 colleges, universities and agencies in more than 130 countries, including "
			+ "Australia, Canada, the U.K. and the United States. Wherever you want to study, the TOEFL "
			+ "test can help you get there.";	

	// 删除停用词及空格符(仅针对本样例文本)
	protected static String deleteWord(String str) {
		String replaceStr = str.replaceAll("and ", "").replaceAll("by ", "").replaceAll("the ", "")
				.replaceAll("of ", "").replace("with ", "").replaceAll("\\)", "").replaceAll("\\(", "")
				.replaceAll(",", "").replaceAll("\\D\\.", "").replaceAll("\\s", "");
		return replaceStr;
	}

	// 使用k-shingle算法分隔
	protected static Set<String> split(String str, int k) {
		Set<String> shingSet = new TreeSet<String>();// 使用TreeSet而不使用HashSet有利于在MinHash算法中降低算法复杂度
		for (int i = 0; i <= str.length() - k; i++) {
			shingSet.add(str.substring(i, i + k));
		}
		return shingSet;
	}

	// 获得两段文本之间的相似度
	protected static Set<String> jaccard(int k) throws IOException {
		String replacedStr1 = deleteWord(str1);
		String replacedStr2 = deleteWord(str2);
		Set<String> set1 = split(replacedStr1, k);
		Set<String> set2 = split(replacedStr2, k);
		Set<String> allElementSet = new TreeSet<String>();
		allElementSet.addAll(set1);
		allElementSet.addAll(set2);
		double jaccardValue = (set1.size() + set2.size() - allElementSet.size()) * 1.0 / allElementSet.size();
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("使用" + k + "-shingle的两段文本之间的相似度结果为：" + df.format(jaccardValue));
		return allElementSet;
	}
}
