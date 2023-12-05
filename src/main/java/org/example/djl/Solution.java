package org.example.djl;

import java.util.HashMap;

public class Solution {




//    public int firstCompleteIndex(int[] arr, int[][] mat) {
//        int[] w = new int[mat[0].length];
//        int[] h = new int[mat.length];
//
//        HashMap<Integer, Integer> xMap = new HashMap<>();
//        HashMap<Integer, Integer> yMap = new HashMap<>();
//
//        for (int i = 0; i < arr.length; i++) {
//            for (int j = 0; j < mat.length; j++) {
//                for (int k = 0; k < mat[j].length; k++) {
//                    if (arr[i] == mat[j][k]) {
//                        xMap.put(j,xMap.getOrDefault(j,0));
//                        yMap.put(k,yMap.getOrDefault(k,0));
//
//                        System.err.println(j);
//                        System.err.println(k);
//                        /**
//                         * 4 3 5
//                         * 1 2 6
//                         */
//                        if(xMap.get(j)==mat[0].length||yMap.get(k)==mat.length){
//                            return i;
//                        }
//                    }
//                }
//            }
//        }
//        return 0;
//    }

    public int firstCompleteIndex(int[] arr, int[][] mat) {

        HashMap<Integer, int[]> hashMap = new HashMap<>();
        for (int j = 0; j < mat.length; j++) {
            for (int k = 0; k < mat[j].length; k++) {
                hashMap.put(mat[j][k],new int[]{j,k});
            }
        }
        int[] width = new int[mat[0].length];
        int[] height = new int[mat.length];
        for (int i = 0; i < arr.length; i++) {
            int[] ints = hashMap.get(arr[i]);
            height[ints[0]]++;
            width[ints[1]]++;
            if(width[ints[1]]==mat.length||height[ints[0]]==mat[0].length){
                return i;
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        int[] arr = {1,4,5,2,6,3};
        int[][] mat = {{4,3,5},{1,2,6}};
        Solution solution = new Solution();
        System.out.println("--");
        System.out.println(solution.firstCompleteIndex(arr, mat));
    }
}
