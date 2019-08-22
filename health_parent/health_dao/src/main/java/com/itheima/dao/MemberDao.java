package com.itheima.dao;

import com.itheima.pojo.Member;

/**
 * 会员数据库操作
 */
public interface MemberDao {

    /**
     * 根据手机号 查询会员信息
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 新增会员
     * @param member
     */
    void add(Member member);

    Integer findMemberCountBeforeDate(String date);

    Integer findMemberTotalCount();

    Integer findMemberCountAfterDate(String date);

    Integer findMemberCountByDate(String today);
}