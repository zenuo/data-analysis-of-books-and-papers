package edu.libsys.data.dao;

import edu.libsys.data.mapper.BehaviorMapper;
import edu.libsys.entity.Behavior;
import org.apache.ibatis.session.SqlSession;

import java.io.Serializable;
import java.util.List;

public class BehaviorDao implements Serializable {
    public Behavior getBehaviorById(int id) {
        Behavior behavior = null;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            BehaviorMapper behaviorMapper = sqlSession.getMapper(BehaviorMapper.class);
            behavior = behaviorMapper.getBehaviorById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return behavior;
    }

    public int addBehavior(Behavior behavior) {
        int status = 0;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            BehaviorMapper behaviorMapper = sqlSession.getMapper(BehaviorMapper.class);
            behaviorMapper.addBehavior(behavior);
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public List<Behavior> getBehaviorList() {
        List<Behavior> behaviorList = null;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            BehaviorMapper behaviorMapper = sqlSession.getMapper(BehaviorMapper.class);
            behaviorList = behaviorMapper.getBehaviorList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return behaviorList;
    }

    public List<Behavior> getBehaviorListByUserId(int userId) {
        List<Behavior> behaviorList = null;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            BehaviorMapper behaviorMapper = sqlSession.getMapper(BehaviorMapper.class);
            behaviorList = behaviorMapper.getBehaviorListByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return behaviorList;
    }

    List<Behavior> getBehaviorListByItemId(int itemId) {
        List<Behavior> behaviorList = null;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            BehaviorMapper behaviorMapper = sqlSession.getMapper(BehaviorMapper.class);
            behaviorList = behaviorMapper.getBehaviorListByItemId(itemId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return behaviorList;
    }
}
