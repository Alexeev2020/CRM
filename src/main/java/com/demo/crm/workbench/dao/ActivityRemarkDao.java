package com.demo.crm.workbench.dao;

import com.demo.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {

    //查询出需要删除备注的数量
    int getCountByAids(String[] ids);

    //删除备注，返回收到影响的记录条数
    int deleteByAids(String[] ids);

    //通过市场活动ID获取所有备注信息
    List<ActivityRemark> selectByAids(String id);

    //通过备注id查询市场活动备注
    ActivityRemark selectById(String id);

    //通过备注id删除备注
    int deleteById(String id);

    //插入新备注
    int insertRemark(ActivityRemark remark);

    //通过传入新更改的市场活动对象更新市场活动的字段信息
    int updateRemark(ActivityRemark remark);
}
