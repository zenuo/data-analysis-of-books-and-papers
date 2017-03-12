package edu.libsys.data.dao;

import edu.libsys.data.mapper.BehaviorMapper;
import edu.libsys.entity.Behavior;
import org.apache.ibatis.session.SqlSession;

public class BehaviorDao {
    public Behavior get(int id) {
        Behavior behavior = null;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            BehaviorMapper behaviorMapper = sqlSession.getMapper(BehaviorMapper.class);
            behavior = behaviorMapper.select(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return behavior;
    }

    public int add(Behavior behavior) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            BehaviorMapper behaviorMapper = sqlSession.getMapper(BehaviorMapper.class);
            behaviorMapper.insert(behavior);
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }
}
