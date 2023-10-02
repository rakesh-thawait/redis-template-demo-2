package com.rt.redistemplatedemo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;


@Data
@RedisHash
public class Book implements Serializable {
    String id;
    String name;
    String isbn;
    Author author;
}
