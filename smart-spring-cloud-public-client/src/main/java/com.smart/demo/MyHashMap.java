package com.smart.demo;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by yanchangxian on 2018/12/17.
 */
public class MyHashMap<K, V>{

    static final int CHANGSHI = 0;
    int size = 0;
    private Entry<K, V>[] table;

    public MyHashMap() {
        this.table = new Entry[CHANGSHI];
    }

    public int size(){
        return size;
    }
    public Object get(Object key) {
        Integer integer = key.hashCode();
        Integer index = integer % CHANGSHI;
        for(Entry<K, V> entry = table[index]; entry!=null; entry= entry.next){
            if(entry.key.equals(key)){
                return entry.value;
            }
        }
        return null;
    }


    public Object put(Object key, Object value) {
        Integer integer = key.hashCode();
        Integer index = integer % CHANGSHI;
        for(Entry<K, V> entry = table[index]; entry!=null; entry= entry.next){
            if(entry.key.equals(key)){
                V oldValue = entry.value;
                entry.value = (V) value;
                return oldValue;
            }
        }
        table[index] = new Entry(key, value, table[index]); //一个新的节点插到了链表的头部

        return null;
    }


    class Entry<K, V>{
        private K key;
        private V value;
        private Entry<K, V> next;

        public Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public static void main(String[] args) {
        MyHashMap<String, String> hashMap = new MyHashMap<>();
        for(int i=0;i<10;i++){
//            MyHashMap.put("1"+i,"2"+i);
        }

        System.out.println(hashMap.get("1"));
    }

}
