package edu.libsys.util;

import edu.libsys.data.dao.AuthorDao;
import edu.libsys.entity.Author;
import org.apache.log4j.PropertyConfigurator;

import java.util.List;

public class Test {
    //test
    public static void main(String[] args) {
        PropertyConfigurator.configure("src/main/scala/edu/libsys/conf/log4j.properties");
        org.apache.ibatis.logging.LogFactory.useLog4JLogging();
        AuthorDao authorDao = new AuthorDao();
        List<Author> authorList = authorDao.search("name");
        authorList.forEach(System.out::println);
    }
}