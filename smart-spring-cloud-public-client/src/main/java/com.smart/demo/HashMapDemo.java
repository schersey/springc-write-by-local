package com.smart.demo;

import java.util.ConcurrentModificationException;
import java.util.HashMap;

/**
 * Created by yanchangxian on 2018/12/17.
 */
public class HashMapDemo {

    //JDK8中HsahMap有哪些改动？


    //JDK8中为什么要用到红黑树


    //HashMap扩容机制是什么，jdk7与jdk8有什么不同


    //为什么重写equals方法时，为什么要重写hashcode方法？跟hashmap有关系么？为什么


    //Hashmap是线程安全么？遇到过ConcurrentModificationException异常？什么会出现？如何解决？


    //在使用hashmap时候的过程中我们应该注意什么问题？


    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();
        map.put("AAAAA", "11111");
        map.put("BBBBB", "22222");
        map.put("CCCCC", "33333");
        map.put("DDDDD", "44444");
        map.put("EEEEE", "55555");
        map.put("FFFFF", "66666");
        map.put("GGGGG", "77777");
        map.put("HHHHH", "88888");
        map.put("HHHHH", "12345");
        System.out.println(map.get("AAAAA"));
        for (String m : map.keySet()) {
            System.out.println(String.format("%s hashcode:%s, index=%s",m , m.hashCode(), m.hashCode() % 8));
        }
    }
}
