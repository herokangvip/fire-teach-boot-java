package com.example.demo.king.sort;

import java.util.Arrays;

/**
 * 选择排序
 *
 * @author k
 * @version 1.0
 * @date 2020/1/12 20:51
 */
public class SortTest02 {
    public static void main(String[] args) {
        int[] arr = new int[]{89, 32, 55, 543, 7, 8};
        sort(arr);
    }

    private static void sort(int[] arr) {
        for (int j = 0; j < arr.length; j++) {
            int min = j;
            for (int i = j + 1; i < arr.length; i++) {
                int num = arr[i];
                if (num < arr[min]) {
                    min = i;
                }
            }
            swap(arr, min, j);
            System.out.println(Arrays.toString(arr));
        }
    }

    private static void swap(int[] arr, int min, int j) {
        if (min != j) {
            int temp = arr[min];
            arr[min] = arr[j];
            arr[j] = temp;
        }
    }


}
