package edu.libsys.data.dao;

import edu.libsys.data.mapper.LendRecordMapper;
import edu.libsys.entity.LendRecord;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class LendRecordDao {
    public List<LendRecord> getLendRecordListByMarcRecId(int marcRecId) {
        List<LendRecord> lendRecordList = null;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            LendRecordMapper lendRecordMapper = sqlSession.getMapper(LendRecordMapper.class);
            lendRecordList = lendRecordMapper.getLendRecordListByMarcRecId(marcRecId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return lendRecordList;
    }

    public List<LendRecord> getLendRecordListByCertId(int certId) {
        List<LendRecord> lendRecordList = null;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            LendRecordMapper lendRecordMapper = sqlSession.getMapper(LendRecordMapper.class);
            lendRecordList = lendRecordMapper.getLendRecordListByCertId(certId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return lendRecordList;
    }

    public List<LendRecord> getLendRecordList() {
        List<LendRecord> lendRecordList = null;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            LendRecordMapper lendRecordMapper = sqlSession.getMapper(LendRecordMapper.class);
            lendRecordList = lendRecordMapper.getLendRecordList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return lendRecordList;
    }

    public int countLendRecord() {
        int count = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            LendRecordMapper lendRecordMapper = sqlSession.getMapper(LendRecordMapper.class);
            count = lendRecordMapper.countLendRecord();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return count;
    }

    public int addLendRecord(LendRecord lendRecord) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            LendRecordMapper lendRecordMapper = sqlSession.getMapper(LendRecordMapper.class);
            lendRecordMapper.addLendRecord(lendRecord);
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }
}
