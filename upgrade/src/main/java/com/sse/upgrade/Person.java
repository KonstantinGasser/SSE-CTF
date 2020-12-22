package com.sse.upgrade;

public class Person {
    private String name;
    private int alter;

    public Person(String name, int alter) {
        this.name = name;
        this.alter = alter;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getAlter() {
        return alter;
    }

    public void setAlter(final int alter) {
        this.alter = alter;
    }

}
