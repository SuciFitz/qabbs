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
import com.qabbs.util.WendaUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String update(Model model, String msg){
        User user = hostHolder.getUser();
        model.addAttribute("user", user);
        model.addAttribute("msg", msg);
        return "changePass";
    }

    @RequestMapping(path = {"/update/pass"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String updatePass(@Param("oldPass") String oldPass, @Param("pass") String pass, Model model,
    RedirectAttributes attr){
        if(hostHolder.getUser().getPassword().equals(WendaUtil.MD5(oldPass))) {
            try {
                userService.updatePass(hostHolder.getUser().getId(), WendaUtil.MD5(pass));
                if (hostHolder.getUser().getAuth() == 0) {
                    userService.updateAuth(hostHolder.getUser().getId(), 1);
                    return "redirect:/";
                }else {
                    return "redirect:/";
                }
            } catch (Exception e) {
                attr.addAttribute("msg", "激活失败");
                return "redirect:/update";
            }
        }else{
                attr.addAttribute("msg", "密码错误");
            return "redirect:/update";
        }
    }
}