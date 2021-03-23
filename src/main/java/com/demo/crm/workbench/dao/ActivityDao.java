package com.demo.crm.workbench.dao;

import com.demo.crm.settings.domain.User;
import com.demo.crm.vo.PaginationVO;
import com.demo.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

    //插入市场活动
    int insertActivity(Activity activity);

    //查询满足查询条件的市场活动信息返回市场活动对象集合
    List<Activity> selectActivity(Map<String,Object> map);

    //获得满足查询条件de市场活动总条数
    int getTotal(Map<String,Object> map);

    //删除ids数组中id对应的市场活动信息
    void delete(String[] id);

    //根据id获取对应的市场活动信息
    Activity selectActivityById(String id);

    //更新Activity表
    int updateActivity(Activity activity);

    //根据id获取activity的详细信息
    Activity detail(String id);
}
