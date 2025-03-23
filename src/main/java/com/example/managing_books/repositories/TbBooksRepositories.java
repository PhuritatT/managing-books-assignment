package com.example.managing_books.repositories;

import com.example.managing_books.entity.tbBooks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TbBooksRepositories extends JpaRepository<tbBooks, Integer> {
}
