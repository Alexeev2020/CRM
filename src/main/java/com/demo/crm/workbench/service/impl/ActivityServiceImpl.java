package com.demo.crm.workbench.service.impl;

import com.demo.crm.settings.domain.User;
import com.demo.crm.utils.DateTimeUtil;
import com.demo.crm.utils.UUIDUtil;
import com.demo.crm.vo.PaginationVO;
import com.demo.crm.workbench.dao.ActivityDao;
import com.demo.crm.workbench.dao.ActivityRemarkDao;
import com.demo.crm.workbench.domain.Activity;
import com.demo.crm.workbench.domain.ActivityRemark;
import com.demo.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Service;
import sun.applet.resources.MsgAppletViewer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.demo.crm.utils.DateTimeUtil.getSysTime;
import static com.demo.crm.utils.UUIDUtil.getUUID;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Resource
    private ActivityDao activityDao;
    @Resource
    private ActivityRemarkDao activityRemarkDao;

    @Override
    public int saveActivity(Activity activity,HttpServletRequest request) {


        //封装请求以外的数据
        activity.setId(getUUID());
        activity.setCreateTime(getSysTime());
        activity.setCreateBy(((User) request.getSession().getAttribute("user")).getName());

        int count = activityDao.insertActivity(activity);
        return count;
    }

    @Override
    public PaginationVO<Activity> queryActivity(HttpServletRequest request) {
        //获取请求参数
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");

        //每页可展现的记录数
        int pageSize = Integer.valueOf(pageSizeStr);

        //计算出略过的记录数,既传入select方法中的limit(skipCount,pageSize)
        int pageNo = Integer.valueOf(pageNoStr);
        int skipCount = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        PaginationVO<Activity> vo = new PaginationVO<>();
        List<Activity> dataList = activityDao.selectActivity(map);
        int total = activityDao.getTotal(map);
        vo.setDataList(dataList);
        vo.setTotal(total);
        vo.setSuccessState("1");
        return vo;
    }


    @Override
    public boolean deleteActivity(HttpServletRequest request) {

        boolean flag = true;
        String ids[] = request.getParameterValues("id");
        //查询出需要删除备注的数量
        int count1 = activityRemarkDao.getCountByAids(ids);
        //删除备注，返回收到影响的记录条数
        int count2 = activityRemarkDao.deleteByAids(ids);

        //如果查询到的
        if (count1!=count2){
            flag = false;
        }
        activityDao.delete(ids);
        return flag;
    }

    @Override
    public Activity getActivity(HttpServletRequest request) {
        String id = request.getParameter("id");
        Activity activity = activityDao.selectActivityById(id);
        return activity;
    }

    @Override
    public boolean updateActivity(Activity activity,HttpServletRequest request) {

        activity.setEditTime(getSysTime());
        activity.setEditBy(((User) request.getSession().getAttribute("user")).getName());
        int count =  activityDao.updateActivity(activity);
        if (1==count) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Activity getActivityDetail(HttpServletRequest request) {

        String id = request.getParameter("id");
        return activityDao.detail(id);
    }

    @Override
    public List<ActivityRemark> getRemarkById(HttpServletRequest request) {

        String id = request.getParameter("id");
        return activityRemarkDao.selectByAids(id);
    }

    @Override
    public int deleteRemark(HttpServletRequest request) {
        String id = request.getParameter("id");
        return activityRemarkDao.deleteById(id);
    }

    @Override
    public Map updateRemark(ActivityRemark remark,HttpServletRequest request) {

        Map map = new HashMap();
        remark.setId(getUUID());
        remark.setCreateTime(getSysTime());
        remark.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        remark.setEditFlag("0");
        int count = activityRemarkDao.insertRemark(remark);
        map.put("remark",remark);
        map.put("count",count);
        return map;
    }

    @Override
    public Map saveRemark(ActivityRemark remark, HttpServletRequest request) {
        Map map = new HashMap();
        remark.setId(getUUID());
        remark.setCreateTime(getSysTime());
        remark.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        remark.setEditFlag("0");
        int count = activityRemarkDao.insertRemark(remark);
        map.put("remark",remark);
        map.put("count",count);
        return map;
    }


}
