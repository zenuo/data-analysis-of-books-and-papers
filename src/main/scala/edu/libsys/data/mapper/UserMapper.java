package edu.libsys.data.mapper;

import edu.libsys.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "type", column = "type"),
            @Result(property = "email", column = "email"),
            @Result(property = "passwd", column = "passwd")
    })

    @Select("SELECT id, name, type, email, passwd FROM USER WHERE id=#{id}")
    User getUserById(int id);

    @Select("SELECT id, name, type, email, passwd FROM USER")
    List<User> getUserList();

    @Select("SELECT id, name, type, email, passwd FROM USER WHERE name LIKE CONCAT('%',#{keyWord},'%')")
    List<User> getUserListBySearchName(String keyWord);

    @Select("SELECT id, name, type, email, passwd FROM USER WHERE type=#{type}")
    List<User> getUserListByType(int type);

    @Select("SELECT COUNT(*) FROM USER")
    int countUser();

    @Select("SELECT passwd FROM USER WHERE name=#{name}")
    String getPasswdByUser(String name);

    @Insert("INSERT INTO USER(name, type, email, passwd) VALUES(#{name}, #{type}, #{email}, #{passwd})")
    void addUser(User user);

    @Update("UPDATE USER SET name=#{name}, type=#{type}, email=#{email}, passwd=#{passwd} WHERE id={id}")
    void updateUser(User user);

    @Delete("DELETE FROM USER WHERE id=#{id}")
    void deleteUser(User user);
}
