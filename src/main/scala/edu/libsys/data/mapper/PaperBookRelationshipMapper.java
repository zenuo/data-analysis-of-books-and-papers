package edu.libsys.data.mapper;

import edu.libsys.entity.PaperBookRelationship;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PaperBookRelationshipMapper {
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "paperId", column = "paperId"),
            @Result(property = "bookId", column = "bookId")
    })

    @Select("SELECT id, paperId, bookId FROM PAPER_BOOK WHERE paperId=#{paperId} LIMIT 20")
    List<PaperBookRelationship> getPaperBookRelationshipListByPaperId(int paperId);

    @Select("SELECT id, paperId, bookId FROM PAPER_BOOK WHERE paperId=#{bookId} LIMIT 20")
    List<PaperBookRelationship> getPaperBookRelationshipListByBookId(int bookId);

    @Insert("INSERT INTO PAPER_BOOK(paperId, bookId) VALUES(#{paperId}, #{bookId})")
    void addPaperBookRelationship(PaperBookRelationship paperBookRelationship);
}
