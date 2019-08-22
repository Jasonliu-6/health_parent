package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.List;

/**
 * 会员服务接口
 */
public interface MemberService {

    /**
     * 根据手机号查询 会员信息
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 添加会员
     * @param member
     */
    void add(Member member);

    /**
     * 根据月份 查询 会员数量
     * @param months
     * @return
     */
    List<Integer> findMemberCountByMonths(List<String> months);

    Integer findMemberCountByDate(String today);

    Integer findMemberCountAfterDate(String thisWeekMonday);

    Integer findMemberTotalCount();
}