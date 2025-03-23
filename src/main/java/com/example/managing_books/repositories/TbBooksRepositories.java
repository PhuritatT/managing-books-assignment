package com.example.managing_books.repositories;

import com.example.managing_books.dtos.BookProjection;
import com.example.managing_books.entity.tbBooks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TbBooksRepositories extends JpaRepository<tbBooks, Integer> {
    List<BookProjection> findByAuthor(String author);
    List<BookProjection> findAllBy();
}
