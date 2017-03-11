package edu.libsys.dao;

import edu.libsys.entity.Author;

/**
 * Created by spark on 3/11/17.
 */

public interface AuthorDaoInterface {
    public Author getAuthorById(int id);
    public void removeAuthorById(int id);
    public int likeAuthor();
    public int DisLikeAuthor();
    //public
}
