package com.demo.crm.workbench.dao;

import com.demo.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {

    int getCountByAids(String[] ids);

    int deleteByAids(String[] ids);

    List<ActivityRemark> selectByAids(String id);

    int deleteById(String id);

    int insertRemark(ActivityRemark remark);
}
