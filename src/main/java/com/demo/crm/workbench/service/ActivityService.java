package com.demo.crm.workbench.service;

import com.demo.crm.vo.PaginationVO;
import com.demo.crm.workbench.domain.Activity;
import com.demo.crm.workbench.domain.ActivityRemark;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Service
public interface ActivityService {
    int saveActivity(Activity activity,HttpServletRequest request);

    PaginationVO queryActivity(HttpServletRequest request);

    boolean deleteActivity(HttpServletRequest request);

    Activity getActivity(HttpServletRequest request);

    boolean updateActivity(Activity activity,HttpServletRequest request);

    Activity getActivityDetail(HttpServletRequest request);

    List<ActivityRemark> getRemarkById(HttpServletRequest request);
}
