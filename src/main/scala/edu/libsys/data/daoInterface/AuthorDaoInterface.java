package edu.libsys.data.daoInterface;

import edu.libsys.entity.Author;

import java.util.stream.Stream;

public interface AuthorDaoInterface {
    //get an instance of Author from the database.
    public Author get(int id);
    //add an instance of Author into the database.
    public int add(Author author);
    //update an instance of Author from the database.
    public int update(Author author);
    //delete an instance of Author from the database.
    public int delete(int id);
    //update the likeCount of an instance of Author from the database.
    public int like(int id);
    //update the disLikeCount of an instance of Author from the database.
    public int disLike(int id);
    //select a set of instances of Author from the database.
    public Stream<Author> gets(int pageNo);
    //count the instances of Author from the database.
    public int count();
}
