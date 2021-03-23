package com.demo.crm.workbench.service.impl;

import com.demo.crm.settings.domain.User;
import com.demo.crm.vo.PaginationVO;
import com.demo.crm.workbench.dao.ActivityDao;
import com.demo.crm.workbench.dao.ActivityRemarkDao;
import com.demo.crm.workbench.domain.Activity;
import com.demo.crm.workbench.domain.ActivityRemark;
import com.demo.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Service;

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

    /**
     * 保存新增的市场活动信息
     * @param activity 传入市场活动
     * @param request 请求
     * @return 返回受影响的记录条数
     */
    @Override
    public int saveActivity(Activity activity,HttpServletRequest request) {

        //封装请求以外的数据
        activity.setId(getUUID());
        activity.setCreateTime(getSysTime());
        activity.setCreateBy(((User) request.getSession().getAttribute("user")).getName());

        int count = activityDao.insertActivity(activity);
        return count;
    }

    /**
     * 查询所有市场活动信息集合封装到map中，再以vo类的形式返回给前端
     * 根据传入的显示页数和每页显示记录数进行查询
     * @param request 请求信息
     * @return 返回自定义vo类
     */
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

        //将查询结果封装到自定义Vo类中，并返回该vo对象
        PaginationVO<Activity> vo = new PaginationVO<>();
        List<Activity> dataList = activityDao.selectActivity(map);
        int total = activityDao.getTotal(map);
        vo.setDataList(dataList);
        vo.setTotal(total);
        vo.setSuccessState("1");
        return vo;
    }


    /**
     * 删除单条市场活动信息
     * @param request 请求信息
     * @return 返回为真表示删除成功
     */
    @Override
    public boolean deleteActivity(HttpServletRequest request) {

        boolean flag = true;
        String ids[] = request.getParameterValues("id");
        //查询出需要删除备注的数量
        int count1 = activityRemarkDao.getCountByAids(ids);
        //删除备注，返回收到影响的记录条数
        int count2 = activityRemarkDao.deleteByAids(ids);

        //如果查询到的备注数量和删除的备注数量相等，则执行删除，commit
        //否则返回false删除失败，前台报错
        if (count1!=count2){
            flag = false;
        }
        activityDao.delete(ids);
        return flag;
    }

    /**
     * 以id值获取市场活动信息
     * @param request 请求信息
     * @return 返回一个Activity市场活动的完整信息
     */
    @Override
    public Activity getActivity(HttpServletRequest request) {
        String id = request.getParameter("id");
        //以id值查询市场活动信息
        Activity activity = activityDao.selectActivityById(id);
        return activity;
    }


    /**
     * 更新市场活动的字段信息
     * @param activity 传入新更改的市场活动对象
     * @param request 请求信息
     * @return 返回值为真，表示更改市场活动成功
     */
    @Override
    public boolean updateActivity(Activity activity,HttpServletRequest request) {

        //调用自定义工具类的getSysTime方法获取当前系统时间，为编辑时间赋值
        activity.setEditTime(getSysTime());
        activity.setEditBy(((User) request.getSession().getAttribute("user")).getName());
        //通过传入新更改的市场活动对象更新市场活动的字段信息
        int count =  activityDao.updateActivity(activity);
        if (1==count) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 获取单个市场活动的全部详细信息
     * @param request 请求信息
     * @return 返回一个市场活动对象
     */
    @Override
    public Activity getActivityDetail(HttpServletRequest request) {

        String id = request.getParameter("id");
        //执行查询详细市场活动信息的方法
        return activityDao.detail(id);
    }


    /**
     * 通过市场活动ID获取所有备注信息
     * @param request 请求信息，提取id
     * @return 返回一个List<ActivityRemark>集合
     */
    @Override
    public List<ActivityRemark> getRemarkById(HttpServletRequest request) {

        String id = request.getParameter("id");
        //通过市场活动ID获取所有备注信息
        return activityRemarkDao.selectByAids(id);
    }


    /**
     * 删除单条备注信息
     * @param request 请求信息
     * @return 返回int
     */
    @Override
    public int deleteRemark(HttpServletRequest request) {
        String id = request.getParameter("id");
        //执行删除备注方法
        return activityRemarkDao.deleteById(id);
    }

    /**
     * 更新市场活动备注的信息
     * @param remark 传入的更改备注信息
     * @param request 请求信息
     * @return 返回更改后的备注与数据库受影响的条数
     */
    @Override
    public Map updateRemark(ActivityRemark remark,HttpServletRequest request) {

        Map map = new HashMap();
        //获取系统当前时间作为编辑时间
        remark.setEditTime(getSysTime());
        //获取当前请求session域中的登录用户的姓名作为修改人
        remark.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        //设置修改状态为1，表示被修改
        remark.setEditFlag("1");
        //执行更新方法
        int count = activityRemarkDao.updateRemark(remark);
        String id = request.getParameter("id");
        //根据id查询当前的备注
        ActivityRemark remark1 = activityRemarkDao.selectById(id);
        map.put("remark",remark);
        map.put("count",count);
        return map;
    }

    /**
     * 保存市场活动备注信息
     * @param remark 传入备注对象
     * @param request 请求
     * @return 返回新增的备注和影响数据库记录的条数
     */
    @Override
    public Map saveRemark(ActivityRemark remark, HttpServletRequest request) {
        Map map = new HashMap();
        //通过自定义的util类中的个体UUID方法获取uuid作为id
        remark.setId(getUUID());
        //获取当前系统时间作为创建时间 格式固定
        remark.setCreateTime(getSysTime());
        //获取当前请求session域中的登录用户的姓名
        remark.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        //因为是新增所以设置编辑状态为0
        remark.setEditFlag("0");
        //执行备注插入方法
        int count = activityRemarkDao.insertRemark(remark);
        map.put("remark",remark);
        map.put("count",count);
        return map;
    }


}
