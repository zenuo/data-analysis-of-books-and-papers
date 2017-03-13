package edu.libsys.util;

import com.github.pagehelper.PageHelper;
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
        PageHelper.startPage(2, 5);
        List<Book> bookList = bookDao.getBookList();
        bookList.forEach(System.out::println);
    }
}