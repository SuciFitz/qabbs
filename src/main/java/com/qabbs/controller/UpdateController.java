/**
 * FileName: UpdateController
 * Author:   73952
 * Date:     2019/5/18 21:27
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.qabbs.controller;

import com.qabbs.model.HostHolder;
import com.qabbs.model.User;
import com.qabbs.service.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author 73952
 * @create 2019/5/18
 * @since 1.0.0
 */
@Controller
public class UpdateController {

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    FollowService followService;

    @Autowired
    LikeService likeService;

    @RequestMapping(path = {"/update"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String update(Model model){
        User user = hostHolder.getUser();
        model.addAttribute("user", user);
        return "changePass";
    }

    @RequestMapping(path = {"/update/pass"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String updatePass(@Param("oldPass") String oldPass, @Param("pass") String pass, Model model){
        if(hostHolder.getUser().getPassword().equals(oldPass)) {
            try {
                userService.updatePass(hostHolder.getUser().getId(), pass);
                if (hostHolder.getUser().getAuth() == 0) {
                    userService.updateAuth(hostHolder.getUser().getId(), 1);
                    model.addAttribute("msg", "激活失败");
                }
                return "update";
            } catch (Exception e) {
                model.addAttribute("msg", "修改密码失败");
                return "update";
            }
        }else{
            model.addAttribute("msg", "密码错误");
        }
        return "redirect:/";
    }
}