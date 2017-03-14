package edu.libsys.data.mapper;

import edu.libsys.entity.Paper;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PaperMapper {
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "searchWord", column = "searchWord"),
            @Result(property = "source", column = "source"),
            @Result(property = "url", column = "url"),
            @Result(property = "intro", column = "intro"),
            @Result(property = "likeCount", column = "likeCount"),
            @Result(property = "disLikeCount", column = "disLikeCount"),
            @Result(property = "site", column = "site")
    })

    @Select("SELECT id ,title, searchWord, source, url, intro, likeCount, disLikeCount, site FROM PAPER WHERE id=#{id}")
    Paper getPaperById(String id);

    @Select("SELECT id ,title, searchWord, source, url, intro, likeCount, disLikeCount, site FROM PAPER")
    List<Paper> getPaperList();

    @Select("SELECT id ,title, searchWord, source, url, intro, likeCount, disLikeCount, site FROM PAPER WHERE title LIKE CONCAT('%',#{keyWord},'%')")
    List<Paper> getPaperListBySearchTitle(String keyWord);

    @Select("SELECT id ,title, searchWord, source, url, intro, likeCount, disLikeCount, site FROM PAPER WHERE intro LIKE CONCAT('%',#{keyWord},'%')")
    List<Paper> getPaperListBySearchIntro(String keyWord);

    @Select("SELECT id ,title, searchWord, source, url, intro, likeCount, disLikeCount, site FROM PAPER WHERE searchWord LIKE CONCAT('%',#{keyWord},'%')")
    List<Paper> getPaperListBySearchSearchWord(String keyWord);

    @Select("SELECT COUNT(*) FROM PAPER")
    int countPaper();

    @Insert("INSERT INTO PAPER(id ,title, searchWord, source, url, intro, likeCount, disLikeCount, site) VALUES(#{id}, #{title}, #{searchWord}, #{source}, #{url}, #{intro}, #{likeCount}, #{disLikeCount}, #{site})")
    void addPaper(Paper paper);

    @Update("UPDATE PAPER SET title=#{title}, searchWord=#{searchWord}, source=#{source}, url=#{url}, intro=#{intro}, likeCount=#{likeCount}, disLikeCount=#{disLikeCount}, site=#{site} WHERE id=#{id}")
    void updatePaper(Paper paper);

    @Delete("DELETE FROM PAPER WHERE id={id}")
    void deletePaper(Paper paper);
}
