package edu.libsys.util;

import edu.libsys.data.dao.BookDao;
import edu.libsys.entity.Book;
import org.apache.log4j.PropertyConfigurator;

import java.util.List;

public class Test {
    //test
    public static void main(String[] args) {
        PropertyConfigurator.configure("src/main/scala/edu/libsys/conf/log4j.properties");
        org.apache.ibatis.logging.LogFactory.useLog4JLogging();
        BookDao bookDao = new BookDao();
        List<Book> bookList = bookDao.getBookListBySearchTitle("大学");
        bookList.forEach(System.out::println);
    }
}