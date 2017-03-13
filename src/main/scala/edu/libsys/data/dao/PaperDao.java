package edu.libsys.data.dao;

import edu.libsys.data.mapper.PaperMapper;
import edu.libsys.entity.Paper;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class PaperDao {
    public Paper getPaperById(String id) {
        Paper paper = null;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            PaperMapper paperMapper = sqlSession.getMapper(PaperMapper.class);
            paper = paperMapper.getPaperById(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return paper;
    }

    public List<Paper> getPaperList() {
        List<Paper> paperList = null;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            PaperMapper paperMapper = sqlSession.getMapper(PaperMapper.class);
            paperList = paperMapper.getPaperList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return paperList;
    }

    public List<Paper> getPaperListBySearchTitle(String keyWord) {
        List<Paper> paperList = null;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            PaperMapper paperMapper = sqlSession.getMapper(PaperMapper.class);
            paperList = paperMapper.getPaperListBySearchTitle(keyWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return paperList;
    }

    public List<Paper> getPaperListBySearchIntro(String keyWord) {
        List<Paper> paperList = null;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            PaperMapper paperMapper = sqlSession.getMapper(PaperMapper.class);
            paperList = paperMapper.getPaperListBySearchIntro(keyWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return paperList;
    }

    public List<Paper> getPaperListBySearchSearchWord(String keyWord) {
        List<Paper> paperList = null;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            PaperMapper paperMapper = sqlSession.getMapper(PaperMapper.class);
            paperList = paperMapper.getPaperListBySearchSearchWord(keyWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return paperList;
    }

    public int addPaper(Paper paper) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            PaperMapper paperMapper = sqlSession.getMapper(PaperMapper.class);
            paperMapper.addPaper(paper);
            status = 1;
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }

    public int updatePaper(Paper paper) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            PaperMapper paperMapper = sqlSession.getMapper(PaperMapper.class);
            paperMapper.updatePaper(paper);
            status = 1;
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }

    public int deletePaper(Paper paper) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            PaperMapper paperMapper = sqlSession.getMapper(PaperMapper.class);
            paperMapper.deletePaper(paper);
            status = 1;
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }
}
