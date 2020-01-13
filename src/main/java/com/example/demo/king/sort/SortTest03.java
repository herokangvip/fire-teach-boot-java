package com.example.demo.king.sort;

import java.util.Arrays;

/**
 * 插入排序
 *
 * @author k
 * @version 1.0
 * @date 2020/1/12 20:51
 */
public class SortTest03 {
    public static void main(String[] args) {
        int[] arr = new int[]{89, 32, 55, 543, 7, 8};
        sort(arr);
    }
    private static void sort(int[] arr) {
        for (int j = 1; j < arr.length; j++) {
            int index = j;
            for (int i = j - 1; i >= 0; i--) {
                int after = arr[index];
                int before = arr[i];
                if (after < before) {
                    arr[i] = after;
                    arr[index] = before;
                    index = i;
                }
            }
            System.out.println(Arrays.toString(arr));
        }
    }


}
