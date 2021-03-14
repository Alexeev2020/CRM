package com.demo.crm.workbench.web.controller;

import com.demo.crm.settings.domain.User;
import com.demo.crm.settings.service.UserService;

import com.demo.crm.vo.PaginationVO;
import com.demo.crm.workbench.domain.Activity;
import com.demo.crm.workbench.domain.ActivityRemark;
import com.demo.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller

@RequestMapping("/workbench/activity")
public class ActivityController {
    @Resource
    private UserService userService;
    @Resource
    private ActivityService activityService;
    //市场活动相关方法

    /**
     * 查询所有用户的全部信息
     * @return 返回的是User类型对象的List集合
     */
    @ResponseBody
    @RequestMapping("/getUserList.do")
    public List getUserList() {
        //查询用户信息
        return userService.findUsers();
    }

    /**
     *
     * @param activity 直接给处理方法传入Activity类，框架底层会将请求中的键值对自动赋值给与类同名属性
     * @param request 传request以供在Service中提取Session对象的数据补充赋值给Activity对象
     * @return 返回一个Map，方法上有@ResponseBody注解，框架会自动封装为json并返回
     */
    @ResponseBody
    @RequestMapping("/save.do")
    public Map saveActivity(Activity activity,HttpServletRequest request){
        Map<String, String> map = new HashMap<>();
        //保存市场活动信息
        int count = activityService.saveActivity(activity,request);
        map.put("nums",String.valueOf(count));
        return map;
    }

    /**
     *
     * @param request 传入request对象，方便在service层进行数据的处理与赋值
     * @return PaginationVO 返回一个自定义的VO类，类中声明了setter方法，
     *                      可以把查询得到的List对象和记录总条数封装到一起返回给前端
     */
    @ResponseBody
    @RequestMapping("/query.do")
    public PaginationVO queryActivity(HttpServletRequest request){
        System.out.println("进入查询Controller");
        //保存市场活动信息
        return activityService.queryActivity(request);
    }

    /**
     * 删除市场活动
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/delete.do")
    public boolean deleteActivity(HttpServletRequest request){
        System.out.println("进入到市场活动信息删除Controller");
        return activityService.deleteActivity(request);
    }

    /**
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getUAA.do")
    public Map getUAA(HttpServletRequest request){
        Map map = new HashMap();
        System.out.println("进入查询市场活动和用户controller");
        //查询用户信息
        List<User> users = userService.findUsers();
        Activity activity  = activityService.getActivity(request);
        map.put("ulist",users);
        map.put("activity",activity);
        return map;
    }

    /**
     *
     * @param activity
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateActivity.do")
    public boolean updateActivity(Activity activity,HttpServletRequest request){
        System.out.println("进入执行市场活动更新操作");
        return activityService.updateActivity(activity,request);
    }

    /**
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/detail.do")
    public ModelAndView detail(HttpServletRequest request){
        System.out.println("进入跳转到详细信息页的操作");
        ModelAndView mv = new ModelAndView();

        //查询id对应的市场活动信息
        Activity activity  = activityService.getActivityDetail(request);

        mv.addObject(activity);
        mv.setViewName("/workbench/activity/detail.jsp");
        return mv;
    }

    /**
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryRemarks.do")
    public List<ActivityRemark> queryRemarks(HttpServletRequest request){
        //查询id对应用户的备注信息
        return activityService.getRemarkById(request);
    }

    /**
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateRemark.do")
    public Map updateRemark(ActivityRemark remark,HttpServletRequest request){
        //保存备注信息
        return activityService.updateRemark(remark,request);
    }

    /**
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/deleteRemark.do")
    public int deleteRemark(HttpServletRequest request){
        //删除备注信息
        return activityService.deleteRemark(request);
    }

    /**
     *
     * @param remark
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveRemark.do")
    public Map saveRemark(ActivityRemark remark,HttpServletRequest request){
        //保存备注信息
        return activityService.saveRemark(remark,request);
    }
}
