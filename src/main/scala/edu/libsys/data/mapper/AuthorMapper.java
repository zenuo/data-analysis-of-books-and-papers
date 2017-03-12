package edu.libsys.data.mapper;

import edu.libsys.entity.Author;
import org.apache.ibatis.annotations.Select;

/**
 * Created by spark on 3/12/17.
 */
public interface AuthorMapper {
    @Select("SELECT * FROM AUTHOR WHERE id = #{id}")
    Author selectAuthor(int id);
}
