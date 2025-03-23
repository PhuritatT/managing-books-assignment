package com.example.managing_books.service.imp;

import ch.qos.logback.classic.Logger;
import com.example.managing_books.dtos.BookProjection;
import com.example.managing_books.dtos.addBook;
import com.example.managing_books.dtos.apiResponseAddBook;
import com.example.managing_books.dtos.apiResponseGet;
import com.example.managing_books.entity.tbBooks;
import com.example.managing_books.repositories.TbBooksRepositories;
import com.example.managing_books.service.BookManagementService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Service
public class bookManagmentServiceImp implements BookManagementService {

    @Autowired
    private TbBooksRepositories tbBooksRepositories;

    public ResponseEntity<apiResponseAddBook> addBook(addBook book) {
        Logger logger = (Logger) LoggerFactory.getLogger("ADD BOOK");
        if (book.getTitle() == null || book.getAuthor() == null) {
            apiResponseAddBook errorResponse = new apiResponseAddBook(false, "Title and Author are required");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        if (book.getPublishedDate() != null) {
            String[] possibleDateFormats = {
                    "yyyy-MM-dd",
                    "dd/MM/yyyy",
                    "MM/dd/yyyy",
                    "dd-MMM-yyyy",
                    "yyyy/MM/dd"
            };

            java.util.Date utilDate = null;
            boolean validDate = false;

            for (String format : possibleDateFormats) {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                sdf.setLenient(false);
                try {
                    utilDate = sdf.parse(book.getPublishedDate().toString());
                    validDate = true;
                    break;
                } catch (Exception e) {
                    logger.error("Error parsing date : ", e);
                }
            }

            if (!validDate) {
                apiResponseAddBook errorResponse = new apiResponseAddBook(false, "Invalid date format");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            // Create java.sql.Date from java.util.Date
            Date parsedDate = new Date(utilDate.getTime());

            Calendar cal = Calendar.getInstance();
            Calendar currentCal = Calendar.getInstance();
            cal.setTime(parsedDate);
            int year = cal.get(Calendar.YEAR);
            int currentYear = currentCal.get(Calendar.YEAR);

            if (year > currentYear && year > currentYear + 543) {
                apiResponseAddBook errorResponse = new apiResponseAddBook(false, "Book cannot be published in the future");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
            book.setPublishedDate(parsedDate);
        }
        tbBooks tbBooks = new tbBooks();
        tbBooks.setTitle(book.getTitle());
        tbBooks.setAuthor(book.getAuthor());
        tbBooks.setPublishedDate(book.getPublishedDate());
        tbBooks.setCreatedAt(new Date(System.currentTimeMillis()));
        try {
            tbBooksRepositories.save(tbBooks);
            apiResponseAddBook response = new apiResponseAddBook(true, "Book added successfully");
            return ResponseEntity.created(URI.create("/books")).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            apiResponseAddBook errorResponse = new apiResponseAddBook(false, "Internal server error");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<apiResponseGet> getBookByAuthor(String author) {
        List<BookProjection> bookData = this.tbBooksRepositories.findByAuthorContaining(author);
        if (bookData != null) {
            BookProjection[] bookArray = bookData.toArray(new BookProjection[0]);
            apiResponseGet response = new apiResponseGet(true, "Book Founded.", bookArray);
            return ResponseEntity.ok(response);
        } else {
            apiResponseGet response = new apiResponseGet(false,"Book Not Found.", null);
            return ResponseEntity.ok(response);
        }
    }

}
