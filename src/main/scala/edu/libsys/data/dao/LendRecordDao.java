package edu.libsys.data.dao;

import edu.libsys.data.mapper.LendRecordMapper;
import edu.libsys.entity.LendRecord;
import org.apache.ibatis.session.SqlSession;

import java.io.Serializable;
import java.util.List;

public class LendRecordDao implements Serializable {
    public List<LendRecord> getLendRecordListByMarcRecId(int marcRecId) {
        List<LendRecord> lendRecordList = null;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            LendRecordMapper lendRecordMapper = sqlSession.getMapper(LendRecordMapper.class);
            lendRecordList = lendRecordMapper.getLendRecordListByMarcRecId(marcRecId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lendRecordList;
    }

    public List<LendRecord> getLendRecordListByCertId(String certId) {
        List<LendRecord> lendRecordList = null;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            LendRecordMapper lendRecordMapper = sqlSession.getMapper(LendRecordMapper.class);
            lendRecordList = lendRecordMapper.getLendRecordListByCertId(certId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lendRecordList;
    }

    public List<LendRecord> getLendRecordList() {
        List<LendRecord> lendRecordList = null;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            LendRecordMapper lendRecordMapper = sqlSession.getMapper(LendRecordMapper.class);
            lendRecordList = lendRecordMapper.getLendRecordList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lendRecordList;
    }

    public int countLendRecord() {
        int count = 0;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            LendRecordMapper lendRecordMapper = sqlSession.getMapper(LendRecordMapper.class);
            count = lendRecordMapper.countLendRecord();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int addLendRecord(LendRecord lendRecord) {
        int status = 0;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            LendRecordMapper lendRecordMapper = sqlSession.getMapper(LendRecordMapper.class);
            lendRecordMapper.addLendRecord(lendRecord);
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }
}
