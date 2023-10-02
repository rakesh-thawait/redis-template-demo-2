package com.rt.redistemplatedemo.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Author implements Serializable {
    String name;
    int age;
}
