package com.fh.project.dao;

import com.fh.project.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoginAndRegisterDao {
    User hasUser(String username);

    int insertUser(User user);

    int insertBoss(BossInfo bossInfo);

    int insertEmp(EmployeeInfo bossInfo);

    BossInfo getBossInfo(String id);

    EmployeeInfo getEmpInfo(String id);

    User getUserById(String id);

    List<BossInfo> getAllBoss();

    List<EmployeeInfo> getAllEmp();

    int addChat(ChatVo chatVo);

    List<MessageVo> getMsgList(String id);

    List<String> getAllChatUserId(String id);

    Integer getUnRead(@Param("id") String id, @Param("from") String from);

    int updateStatus(@Param("id") String id,@Param("from") String from);
}
