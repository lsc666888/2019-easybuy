package com.java.controller;

import com.java.service.LoginService;
import com.java.utils.MD5Too;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 登录
     * @param username
     * @param pwd
     */
    @RequestMapping("login.do")
    public String login(String username,String pwd,HttpSession session) throws Exception{
        System.out.println("username="+username+"----"+"pwd="+pwd);
        //没有做任何的校验
        if(username==null || pwd==null){
            return "logion";
        }
        boolean flag1 = username.matches(".{3,12}");
        boolean flag2 = pwd.matches(".{6,12}");
        if(!flag1 || !flag2){
            return "login";
        }
        //没有对密码进行加密
        pwd = MD5Too.MD5(pwd);
        boolean flag = loginService.login(username, pwd);
        if(flag){
            session.setAttribute("username",username);
        }
        return flag?"index":"login";
    }

    @RequestMapping("/getAuthorityByUsername.do")
    public @ResponseBody List<Map<String,Object>> getAuthorityByUsername(
            @RequestParam(name="id",defaultValue = "0")Long id, HttpSession session
            ){
        String username = (String) session.getAttribute("username");
        System.out.println(username);
        Map<String,Object> pmp=new HashMap<>();
        pmp.put("username",username);
        pmp.put("id",id);
        return loginService.findAuthorityByUsername(pmp);
    }
}
