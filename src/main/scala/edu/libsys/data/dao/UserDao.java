package edu.libsys.data.dao;

import edu.libsys.data.mapper.UserMapper;
import edu.libsys.entity.User;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class UserDao {
    public User getUserById(int id) {
        User user = null;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            user = userMapper.getUserById(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return user;
    }

    public List<User> getUserList() {
        List<User> userList = null;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userList = userMapper.getUserList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return userList;
    }

    public List<User> getUserListBySearchName(String keyWord) {
        List<User> userList = null;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userList = userMapper.getUserListBySearchName(keyWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return userList;
    }

    public List<User> getUserListByType(int type) {
        List<User> userList = null;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userList = userMapper.getUserListByType(type);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return userList;
    }

    public int countUser() {
        int count = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            count = userMapper.countUser();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return count;
    }

    public String getPasswdByUser(String name) {
        String passwd = null;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            passwd = userMapper.getPasswdByUser(name);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return passwd;
    }

    public int addUser(User user) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userMapper.addUser(user);
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }

    public int updateUser(User user) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userMapper.updateUser(user);
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }

    public int deleteUser(User user) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userMapper.deleteUser(user);
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }
}

