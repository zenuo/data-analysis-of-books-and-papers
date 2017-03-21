package edu.libsys.data.dao;

import edu.libsys.data.mapper.PaperMapper;
import edu.libsys.entity.Paper;
import org.apache.ibatis.session.SqlSession;

import java.io.Serializable;
import java.util.List;

public class PaperDao implements Serializable {
    public Paper getPaperById(int id) {
        Paper paper = null;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            PaperMapper paperMapper = sqlSession.getMapper(PaperMapper.class);
            paper = paperMapper.getPaperById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paper;
    }

    public List<Paper> getPaperList(int page, int size) {
        List<Paper> paperList = null;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            PaperMapper paperMapper = sqlSession.getMapper(PaperMapper.class);
            paperList = paperMapper.getPaperList(page, size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paperList;
    }

    public List<Paper> getPaperListBySearchTitle(String keyWord) {
        List<Paper> paperList = null;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            PaperMapper paperMapper = sqlSession.getMapper(PaperMapper.class);
            paperList = paperMapper.getPaperListBySearchTitle(keyWord);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paperList;
    }

    public List<Paper> getPaperListBySearchIntro(String keyWord) {
        List<Paper> paperList = null;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            PaperMapper paperMapper = sqlSession.getMapper(PaperMapper.class);
            paperList = paperMapper.getPaperListBySearchIntro(keyWord);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paperList;
    }

    public List<Paper> getPaperListBySearchSearchWord(String keyWord) {
        List<Paper> paperList = null;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            PaperMapper paperMapper = sqlSession.getMapper(PaperMapper.class);
            paperList = paperMapper.getPaperListBySearchSearchWord(keyWord);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paperList;
    }

    public int addPaper(Paper paper) {
        int status = 0;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            PaperMapper paperMapper = sqlSession.getMapper(PaperMapper.class);
            paperMapper.addPaper(paper);
            status = 1;
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public int updatePaper(Paper paper) {
        int status = 0;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            PaperMapper paperMapper = sqlSession.getMapper(PaperMapper.class);
            paperMapper.updatePaper(paper);
            status = 1;
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public int deletePaper(int id) {
        int status = 0;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            PaperMapper paperMapper = sqlSession.getMapper(PaperMapper.class);
            paperMapper.deletePaper(id);
            status = 1;
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public int countPaper() {
        int count = 0;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            PaperMapper paperMapper = sqlSession.getMapper(PaperMapper.class);
            count = paperMapper.countPaper();
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
}
