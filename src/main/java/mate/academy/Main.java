package mate.academy;

import java.math.BigDecimal;
import mate.academy.dao.BookDao;
import mate.academy.lib.Injector;
import mate.academy.model.Book;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        Book book = new Book();
        book.setTitle("aboba");
        book.setPrice(BigDecimal.valueOf(123));
        book.setId(1L);
        BookDao bookDao = (BookDao) injector.getInstance(BookDao.class);
        bookDao.create(book);
        System.out.println(bookDao.findAll());
        book.setTitle("aboba2");
        bookDao.update(book);
        System.out.println(bookDao.findById(1L));
        bookDao.deleteById(1L);
    }
}
