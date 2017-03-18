package edu.libsys.data.mapper;

import edu.libsys.entity.LendRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface LendRecordMapper {
    @Results({
            @Result(property = "certId", column = "certId"),
            @Result(property = "time", column = "time"),
            @Result(property = "marcRecId", column = "marcRecId")
    })

    @Select("SELECT certId, time, marcRecId FROM LENDRECORD WHERE marcRecId=#{marcRecId}")
    List<LendRecord> getLendRecordListByMarcRecId(int marcRecId);

    @Select("SELECT certId, time, marcRecId FROM LENDRECORD WHERE certId=#{certId}")
    List<LendRecord> getLendRecordListByCertId(String certId);

    @Select("SELECT certId, time, marcRecId FROM LENDRECORD")
    List<LendRecord> getLendRecordList();

    @Select("SELECT COUNT(*) FROM LENDRECORD")
    int countLendRecord();

    @Insert("INSERT INTO LENDRECORD(certId, time, marcRecId) VALUES(#{certId}, #{time}, #{marcRecId})")
    void addLendRecord(LendRecord lendRecord);
}
