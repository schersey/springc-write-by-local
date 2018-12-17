package com.smart.vo;

/**
 * Created by yanchangxian on 2018/12/17.
 */
public class Person {

    private int id;
    private String name;
    private int age;
    private String message;

    public Person() {
    }

    public Person(int id, String name, int age, String message) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", message='" + message + '\'' +
                '}';
    }
}
