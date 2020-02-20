package com.example.demo.king.sort;

import java.util.Arrays;

/**
 * 冒泡排序
 *
 * @author k
 * @version 1.0
 * @date 2020/1/12 20:51
 */
public class SortTest01 {
    public static void main(String[] args) {
        int[] arr = new int[]{89, 32, 55, 543, 7, 8};
        sort(arr);
    }

    private static void sort(int[] arr) {
        for (int j = 0; j < arr.length; j++) {
            for (int i = 0; i < arr.length - j - 1; i++) {
                int before = arr[i];
                int after = arr[i + 1];
                if (before > after) {
                    arr[i] = after;
                    arr[i + 1] = before;
                }
            }
            System.out.println(Arrays.toString(arr));
        }
    }
}
