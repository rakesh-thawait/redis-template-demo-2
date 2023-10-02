package com.rt.redistemplatedemo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rt.redistemplatedemo.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v2")
public class BookController {

    private final static String idPrefix = Book.class.getName();

    ObjectMapper objectMapper;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisKeyValueTemplate template;

    private SetOperations<String, Object> redisSets() {
        return this.redisTemplate.opsForSet();
    }

    private HashOperations<String, String, Object> redisHash() {
        return redisTemplate.opsForHash();
    }
    @PostMapping("/book")
    public ResponseEntity<Book> saveBook(@RequestBody Book book) {
        if (book.getId() == null) {
            book.setId(UUID.randomUUID().toString());
        }
        // Save Primary key / Index
        redisSets().add(idPrefix, book.getId());
        // Save Book
        redisTemplate.opsForHash().put(book.getId(), book.getId(), book);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        Book book = getBookDetail(id);
        return ResponseEntity.ok(book);
    }

    /*
    @PutMapping("/book")
    public ResponseEntity<Book> updateBook(@RequestBody Book book) {
        PartialUpdate<Book> update = new PartialUpdate<>( book.getId(), Book.class)
                .set("name", book.getName())
                .set("isbn", book.getIsbn());

        template.update(update);
        return ResponseEntity.ok(book);
    }
     */

    private Book getBookDetail(String id) {
        Book book = (Book)this.redisTemplate.opsForHash().get(id, id);
        if (book == null) {
            throw new RuntimeException("Book Details Not Found for Id :: " + id);
        }
        return book;
    }
}
