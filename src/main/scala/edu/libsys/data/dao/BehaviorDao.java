package edu.libsys.data.dao;

import edu.libsys.data.mapper.BehaviorMapper;
import edu.libsys.entity.Behavior;
import org.apache.ibatis.session.SqlSession;

public class BehaviorDao {
    public Behavior getBehaviorById(int id) {
        Behavior behavior = null;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            BehaviorMapper behaviorMapper = sqlSession.getMapper(BehaviorMapper.class);
            behavior = behaviorMapper.getBehaviorById(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return behavior;
    }

    public int addBehavior(Behavior behavior) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            BehaviorMapper behaviorMapper = sqlSession.getMapper(BehaviorMapper.class);
            behaviorMapper.addBehavior(behavior);
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }
}
