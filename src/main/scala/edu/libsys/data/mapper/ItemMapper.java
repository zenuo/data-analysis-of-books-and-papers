package edu.libsys.data.mapper;

import edu.libsys.entity.Item;
import org.apache.ibatis.annotations.*;

public interface ItemMapper {
    @Results({
            @Result(property = "marcRecId", column = "marcRecId"),
            @Result(property = "propId", column = "propId"),
            @Result(property = "lendCount", column = "lendCount"),
            @Result(property = "likeCount", column = "likeCount"),
            @Result(property = "disLikeCount", column = "disLikeCount")
    })

    @Select("SELECT * FROM ITEM WHERE marcRecId=#{marcRecId}")
    Item getItemBymarcRecId(int marcRecId);

    @Insert("INSERT INTO ITEM(marcRecId, propId, lendCount, likeCount, disLikeCount) VALUES(#{marcRecId}, #{propId}, #{lendCount}, #{likeCount}, #{disLikeCount})")
    void addItem(Item item);

    @Update("UPDATE ITEM SET lendCount=lendCount+1 WHERE marcRecId=#{marcRecId}")
    void lendCountPlusOne(Item item);

    @Update("UPDATE ITEM SET likeCount=likeCount+1 WHERE marcRecId=#{marcRecId}")
    void likeCountPlusOne(Item item);

    @Update("UPDATE ITEM SET likeCount=likeCount+1 WHERE marcRecId=#{marcRecId}")
    void disLikeCountPlusOne(Item item);

    @Select("SELECT COUNT(*) FROM ITEM")
    int countItem();
}
