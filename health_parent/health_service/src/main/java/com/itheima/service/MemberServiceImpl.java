package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    /**
     * 新增会员
     *
     * @param member
     */
    @Override
    public void add(Member member) {
        if (member.getPassword() != null) {
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }

        memberDao.add(member);
    }

    /**
     * 根据月份 查询 会员数量
     *
     * @param months
     * @return
     */
    @Override
    public List<Integer> findMemberCountByMonths(List<String> months) {
        //2019.1 =>  2019.1.31 查找2019.1.31 之前的 所有 注册会员数量
        //2019.2  => 2019.2.31

        List<Integer> result = new ArrayList<>();

        for (String month : months) {
            String date = month + ".31";

            Integer count = memberDao.findMemberCountBeforeDate(date);

            result.add(count);
        }

        return result;
    }

    @Override
    public Integer findMemberCountByDate(String today) {
        return memberDao.findMemberCountByDate(today);
    }

    @Override
    public Integer findMemberCountAfterDate(String date) {
        return memberDao.findMemberCountAfterDate(date);
    }

    @Override
    public Integer findMemberTotalCount() {
        return memberDao.findMemberTotalCount();
    }
}