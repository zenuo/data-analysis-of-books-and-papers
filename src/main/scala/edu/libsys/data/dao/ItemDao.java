package edu.libsys.data.dao;

import edu.libsys.data.mapper.ItemMapper;
import edu.libsys.entity.Item;
import org.apache.ibatis.session.SqlSession;

import java.io.Serializable;
import java.util.List;

public class ItemDao implements Serializable {
    public Item getItemBymarcRecId(int marcRecId) {
        Item item = null;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            ItemMapper itemMapper = sqlSession.getMapper(ItemMapper.class);
            item = itemMapper.getItemBymarcRecId(marcRecId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    public int addItem(Item item) {
        int status = 0;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            ItemMapper itemMapper = sqlSession.getMapper(ItemMapper.class);
            itemMapper.addItem(item);
            sqlSession.close();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public int lendCountPlusOne(Item item) {
        int status = 0;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            ItemMapper itemMapper = sqlSession.getMapper(ItemMapper.class);
            itemMapper.lendCountPlusOne(item);
            sqlSession.commit();
            status = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public int countItem() {
        int count = 0;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            ItemMapper itemMapper = sqlSession.getMapper(ItemMapper.class);
            count = itemMapper.countItem();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<Item> getItemList() {
        List<Item> itemList = null;
        try (SqlSession sqlSession = SessionFactory.getSqlSession()) {
            ItemMapper itemMapper = sqlSession.getMapper(ItemMapper.class);
            itemList = itemMapper.getItemList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemList;
    }
}
