package edu.libsys.util;

import edu.libsys.data.dao.AuthorDao;
import org.apache.log4j.PropertyConfigurator;

public class Test {
    //test
    public static void main(String[] args) {
        PropertyConfigurator.configure("src/main/scala/edu/libsys/conf/log4j.properties");
        org.apache.ibatis.logging.LogFactory.useLog4JLogging();
        AuthorDao authorDao = new AuthorDao();
        //List<Author> authorList = authorDao.getAuthorListBySearchName("a");
        //authorList.forEach(System.out::println);
    }
}