package com.fh.project.controller;

import com.fh.common.utils.ResultVoUtil;
import com.fh.common.vo.ResultVo;
import com.fh.project.dao.LoginAndRegisterDao;
import com.fh.project.vo.BossInfo;
import com.fh.project.vo.EmployeeInfo;
import com.fh.project.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class LoginAndRegisterController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LoginAndRegisterDao loginAndRegisterDao;

    @RequestMapping("/register")
    @ResponseBody
    public ResultVo registerUser(@RequestBody User user, HttpServletResponse response){

        // 判断是否存在
        User dbUser = loginAndRegisterDao.hasUser(user.getUsername());

        if(null==dbUser){
            SimpleDateFormat simpleFormatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            String time = simpleFormatter.format(new Date());
            user.setUpdateTime(time);
            loginAndRegisterDao.insertUser(user);
            return ResultVoUtil.success(user);
        }else{
            return ResultVoUtil.error(500, "用户名已存在!");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/userLogin",method = RequestMethod.POST)
    public ResultVo userLogin(@RequestBody User user, HttpServletResponse response){
        User dbUser = loginAndRegisterDao.hasUser(user.getUsername());
        if(null==dbUser){
            return ResultVoUtil.error(500, "用户不存在请先注册!");
        }else if(!dbUser.getPassword().equals(user.getPassword())){
            return ResultVoUtil.error(500, "密码错误!");
        }else {
            // 根据用户名去查询不同数据库
            if("1".equals(dbUser.getType())){
                EmployeeInfo employeeInfo = loginAndRegisterDao.getEmpInfo(dbUser.getId());
                dbUser.setInfo(employeeInfo);
            }else{
                BossInfo bossInfo = loginAndRegisterDao.getBossInfo(dbUser.getId());
                dbUser.setInfo(bossInfo);
            }
            return ResultVoUtil.success(dbUser);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/updateBoss")
    public ResultVo bossUpdate(@RequestBody BossInfo bossInfo){
        loginAndRegisterDao.insertBoss(bossInfo);
        return ResultVoUtil.success();
    }

    @ResponseBody
    @RequestMapping(value = "/updateEmp")
    public ResultVo empUpdate(@RequestBody EmployeeInfo employeeInfo){
        loginAndRegisterDao.insertEmp(employeeInfo);
        return ResultVoUtil.success();
    }

    @ResponseBody
    @RequestMapping("/getUserById")
    public ResultVo getUserById(String id){
        User user = loginAndRegisterDao.getUserById(id);
        if("1".equals(user.getType())){
            EmployeeInfo employeeInfo = loginAndRegisterDao.getEmpInfo(user.getId());
            user.setInfo(employeeInfo);
        }else{
            BossInfo bossInfo = loginAndRegisterDao.getBossInfo(user.getId());
            user.setInfo(bossInfo);
        }
        return ResultVoUtil.success(user);
    }

    @ResponseBody
    @RequestMapping("/getAllBoss")
    public ResultVo getAllBoss(){
        List<User> users = new ArrayList<>();
        List<BossInfo> allBoss = loginAndRegisterDao.getAllBoss();
        for (BossInfo bossInfo: allBoss
        ) {
            User user = loginAndRegisterDao.getUserById(bossInfo.getId());
            user.setInfo(bossInfo);
            users.add(user);
        }

        return ResultVoUtil.success(users);
    }

    @ResponseBody
    @RequestMapping("/getAllEmp")
    public ResultVo getAllEmp(){
        List<User> users = new ArrayList<>();
        List<EmployeeInfo> allEmp = loginAndRegisterDao.getAllEmp();
        for (EmployeeInfo employeeInfo: allEmp
             ) {
            User user = loginAndRegisterDao.getUserById(employeeInfo.getId());
            user.setInfo(employeeInfo);
            users.add(user);
        }

        return ResultVoUtil.success(users);
    }

    // 获取聊天记录
    @ResponseBody
    @RequestMapping("/getMsgList")
    public ResultVo getMsgList(String from,String to){
        String[] ids = new String[]{from,to};
        Arrays.sort(ids);
        // 修改为已读
        loginAndRegisterDao.updateStatus(ids[0]+"_"+ids[1],to);
        return ResultVoUtil.success(loginAndRegisterDao.getMsgList(ids[0]+"_"+ids[1]));
    }

    @ResponseBody
    @RequestMapping("/getAllChatUser")
    public ResultVo getAllChatUser(String id){
        List<User> users = new ArrayList<>();
        List<String> ids = loginAndRegisterDao.getAllChatUserId(id);
        for (String tmp:ids) {
            User user = loginAndRegisterDao.getUserById(tmp);
            if("1".equals(user.getType())){
                EmployeeInfo employeeInfo = loginAndRegisterDao.getEmpInfo(user.getId());
                user.setInfo(employeeInfo);
            }else{
                BossInfo bossInfo = loginAndRegisterDao.getBossInfo(user.getId());
                user.setInfo(bossInfo);
            }

            // 获取未读
            String[] userIds = new String[]{id,user.getId()};
            Arrays.sort(userIds);
            user.setCount(loginAndRegisterDao.getUnRead(userIds[0]+"_"+userIds[1], user.getId()));

            users.add(user);
        }
        return ResultVoUtil.success(users);
    }




    @RequestMapping("/addCookie")
    public void addCookie(HttpServletResponse response,String name,String value){
        Cookie cookie = new Cookie(name.trim(), value.trim());
        cookie.setMaxAge(30 * 60);// 设置为30min
        cookie.setPath("/");
        System.out.println("已添加===============");
        response.addCookie(cookie);
    }

    @RequestMapping("/showCookies")
    public void showCookies(HttpServletRequest request,HttpServletResponse response ){

        Cookie[] cookies = request.getCookies();//这样便可以获取一个cookie数组
        if (null==cookies) {
            System.out.println("没有cookie=========");
        } else {
            for(Cookie cookie : cookies){
                System.out.println("name:"+cookie.getName()+",value:"+ cookie.getValue());
            }
        }
    }
}
