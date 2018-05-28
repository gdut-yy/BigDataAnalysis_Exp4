package com.devyy;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class MinHash {
	// 得到set  
    protected static Set<String> getSet(int k, String str) throws IOException{  
        String replacedStr = KShingle.deleteWord(str);  
        Set<String> set = KShingle.split(replacedStr, k);  
        return set;  
    }  
      
    // 构建特征集合矩阵  
    protected static String[][] characteristicMatrix(Set<String> set, Set<String> set1, Set<String> set2){  
        String[] a = new String[set.size()];  
        set.toArray(a);  
        String[] set1Array = new String[set1.size()];  
        set1.toArray(set1Array);  
        String[] set2Array = new String[set2.size()];  
        set2.toArray(set2Array);  
        String[][] matrix = new String[a.length][5];//此处构造为5是为了后面的最小哈希签名中的两个哈希函数的结果存放。  
        int i, j, temp;  
        for(i = 0; i < matrix.length; i++){  
            for(j = 0; j < matrix[0].length; j++){  
                matrix[i][j] = "0";  
            }  
        }  
        i = 0;  
        for(Iterator<String> iter = set.iterator(); iter.hasNext();){  
            matrix[i++][0] = iter.next();  
        }  
        i = 0;  
        temp = 0;  
        for(j = i; j < a.length && temp < set1Array.length; j++){  
            if(matrix[j][0].equals(set1Array[temp])){  
                matrix[j][1] = "1";  
                temp++;  
            }  
        }  
        temp = 0;  
        for(j = i; j < a.length && temp < set2Array.length; j++){  
            if(matrix[j][0].equals(set2Array[temp])){  
                matrix[j][2] = "1";  
                temp++;  
            }  
        }  
        return matrix;  
    }  
      
    //行打乱  
    protected static String[][] rowMess(String[][] matrix){  
        int rowNumber1, rowNumber2;  
        int i, j;  
        String temp;  
        Random r = new Random();  
        //随机进行行打乱十次  
        for(i = 0; i < 9; i++){  
            rowNumber1 = r.nextInt(matrix.length);  
            rowNumber2 = r.nextInt(matrix.length);  
            for(j = 0; j < matrix[0].length; j ++){  
                temp = matrix[rowNumber2][j];  
                matrix[rowNumber2][j] = matrix[rowNumber1][j];  
                matrix[rowNumber1][j] = temp;  
            }  
        }  
        return matrix;  
    }  
      
    //根据最小hash值求相似度  
    protected static double minHashJaccard(int k, Set<String> set) throws IOException{  
        Set<String> set1 = getSet(k, KShingle.str1);  
        Set<String> set2 = getSet(k, KShingle.str2);  
        String[][] matrix = characteristicMatrix(set, set1, set2);  
        matrix = rowMess(matrix);  
        double result;  
        System.out.println("已知定义：两个集合经随机排列转换之后得到的两个最小哈希值相等的概率等于这两个集合的jaccard相似度");  
        int equalHashValue = 0;  
        for(int i = 0; i < matrix.length; i++){  
            if(matrix[i][1].equals(matrix[i][2]) && matrix[i][1].equals("1")){  
                equalHashValue++;  
            }  
        }  
        System.out.println("全集共有项的数目：" + set.size());  
        System.out.println("都为1(该子串在两段文本中均出现)的数目：" + equalHashValue);  
        result = equalHashValue * 1.0 / set.size();  
        DecimalFormat df = new DecimalFormat("0.00");  
        System.out.println("第一项与第二项得到最小哈希值相等的概率计算为P = " + equalHashValue + " / "  + set.size() + " = " + df.format(result));  
        System.out.println("即MinHash算得的两段文本之间的jaccard相似度结果为：" + df.format(result));  
        return equalHashValue;  
    }  
}
