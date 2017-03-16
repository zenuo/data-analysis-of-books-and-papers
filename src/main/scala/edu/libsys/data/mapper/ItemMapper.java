package edu.libsys.data.mapper;

import edu.libsys.entity.Item;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ItemMapper {
    @Results({
            @Result(property = "marcRecId", column = "marcRecId"),
            @Result(property = "propId", column = "propId"),
            @Result(property = "lendCount", column = "lendCount")
    })

    @Select("SELECT marcRecId, propId, lendCount, likeCount, disLikeCount FROM ITEM WHERE marcRecId=#{marcRecId}")
    Item getItemBymarcRecId(int marcRecId);

    @Select("SELECT marcRecId, propId, lendCount, likeCount, disLikeCount FORM ITEM")
    List<Item> getItemList();

    @Select("SELECT COUNT(*) FROM ITEM")
    int countItem();

    @Insert("INSERT INTO ITEM(marcRecId, propId, lendCount, likeCount, disLikeCount) VALUES(#{marcRecId}, #{propId}, #{lendCount}, #{likeCount}, #{disLikeCount})")
    void addItem(Item item);

    @Update("UPDATE ITEM SET lendCount=lendCount+1 WHERE marcRecId=#{marcRecId}")
    void lendCountPlusOne(Item item);
}
