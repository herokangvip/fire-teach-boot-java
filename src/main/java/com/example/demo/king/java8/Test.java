package com.example.demo.king.java8;

import com.example.demo.domain.User;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by kangqingqing on 2018/12/8.
 */
public class Test {

    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        users.add(new User(1, "name1", "passWord"));
        users.add(new User(3, "name3", "passWord"));
        users.add(new User(2, "name2", "passWord"));
        users.add(new User(4, "name4", "passWord"));

        //testSort(users);
        //选出id小于100，并按照id排序的用户名字的集合
        //testSelectBefore(users);
        //java8
        testSelect(users);


    }

    private static void testSelectBefore(List<User> users) {
        ArrayList<User> tempList = new ArrayList<>();
        for (User user:users) {
            if(user.getId()<100){
                tempList.add(user);
            }
        }
        Collections.sort(tempList, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        ArrayList<String> result = new ArrayList<>();
        for (User user:tempList) {
            result.add(user.getName());
        }
        System.out.println("==========");
    }

    private static void testSelect(List<User> users) {
        List<String> collect = users.parallelStream()
                .filter(a -> a.getId() < 100)
                .sorted(Comparator.comparing(User::getId)).distinct()
                .map(User::getName)
                .collect(Collectors.toList());
        System.out.println(collect);
    }

    private static void testSort(List<User> users) {
        //java8之前
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User a, User b) {
                return a.getId().compareTo(b.getId());
            }
        });

        //java8之后
        users.sort((User a, User b) -> a.getId().compareTo(b.getId()));

        users.sort((a, b) -> a.getId().compareTo(b.getId()));

        users.sort(Comparator.comparing(a -> a.getId()));
        //方法引用
        users.sort(Comparator.comparing(User::getId));
        //逆序
        users.sort(Comparator.comparing(User::getId).reversed());
        //按照名字排序如果名字相同按照id排序
        users.sort(Comparator
                .comparing(User::getName)
                .thenComparing(Comparator.comparing(User::getId).reversed()));


    }
}
