package com.example.managing_books.dtos;

import java.sql.Date;

public interface BookProjection {
    String getAuthor();
    String getTitle();
    Date getPublishedDate();
}
