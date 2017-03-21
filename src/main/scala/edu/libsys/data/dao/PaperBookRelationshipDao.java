package edu.libsys.data.dao;

import edu.libsys.data.mapper.PaperBookRelationshipMapper;
import edu.libsys.entity.PaperBookRelationship;
import org.apache.ibatis.session.SqlSession;

import java.io.Serializable;
import java.util.List;

public class PaperBookRelationshipDao implements Serializable {
    public List<PaperBookRelationship> getPaperBookRelationshipListByPaperId(int paperId) {
        List<PaperBookRelationship> paperBookRelationshipList = null;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            PaperBookRelationshipMapper paperBookRelationshipMapper =
                    sqlSession.getMapper(PaperBookRelationshipMapper.class);
            paperBookRelationshipList =
                    paperBookRelationshipMapper.getPaperBookRelationshipListByPaperId(paperId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paperBookRelationshipList;
    }

    public List<PaperBookRelationship> getPaperBookRelationshipListByBookId(int bookId) {
        List<PaperBookRelationship> paperBookRelationshipList = null;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            PaperBookRelationshipMapper paperBookRelationshipMapper =
                    sqlSession.getMapper(PaperBookRelationshipMapper.class);
            paperBookRelationshipList =
                    paperBookRelationshipMapper.getPaperBookRelationshipListByBookId(bookId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paperBookRelationshipList;
    }

    public int addPaperBookRelationship(int paperId, int bookId) {
        int status = 0;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            PaperBookRelationshipMapper paperBookRelationshipMapper =
                    sqlSession.getMapper(PaperBookRelationshipMapper.class);
            paperBookRelationshipMapper.addPaperBookRelationship(
                    new PaperBookRelationship(paperId, bookId)
            );
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }
}
