package edu.libsys.data.dao;

import edu.libsys.data.mapper.ItemMapper;
import edu.libsys.entity.Item;
import org.apache.ibatis.session.SqlSession;

/**
 * Created by spark on 3/13/17.
 */
public class ItemDao {
    public Item getItemBymarcRecId(int marcRecId) {
        Item item = null;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            ItemMapper itemMapper = sqlSession.getMapper(ItemMapper.class);
            item = itemMapper.getItemBymarcRecId(marcRecId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return item;
    }

    public int addItem(Item item) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            ItemMapper itemMapper = sqlSession.getMapper(ItemMapper.class);
            itemMapper.addItem(item);
            sqlSession.close();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }

    public int lendCountPlusOne(Item item) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            ItemMapper itemMapper = sqlSession.getMapper(ItemMapper.class);
            itemMapper.lendCountPlusOne(item);
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }

    public int likeCountPlusOne(Item item) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            ItemMapper itemMapper = sqlSession.getMapper(ItemMapper.class);
            itemMapper.likeCountPlusOne(item);
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }

    public int disLikeCountPlusOne(Item item) {
        int status = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            ItemMapper itemMapper = sqlSession.getMapper(ItemMapper.class);
            itemMapper.disLikeCountPlusOne(item);
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return status;
    }

    public int countItem() {
        int count = 0;
        SqlSession sqlSession = SessionFactory.getSqlSession();
        try {
            ItemMapper itemMapper = sqlSession.getMapper(ItemMapper.class);
            count = itemMapper.countItem();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return count;
    }
}
