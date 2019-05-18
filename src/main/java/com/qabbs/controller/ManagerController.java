/**
 * FileName: ManagerController
 * Author:   73952
 * Date:     2019/5/16 16:37
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.qabbs.controller;

import com.qabbs.model.EntityType;
import com.qabbs.model.Question;
import com.qabbs.model.User;
import com.qabbs.model.ViewObject;
import com.qabbs.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉
 *
 *
 * @author 73952
 * @create 2019/5/16
 * @since 1.0.0
 */
@Controller
public class ManagerController {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    FollowService followService;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;


    @RequestMapping(path = {"/manager"})
    public String index(){
        return "management";
    }

    @RequestMapping(path = {"/userlist"})
    public String user(){
        return "userlist";
    }
    /**
    * 功能描述:返回所有问题
     *
     * @since: 1.0.0
     * @Author:73952
     * @Date: 2019/5/16
     */
    @RequestMapping(path = {"/management"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String questionAll(Model model) {
        List<Question> questionList = questionService.getAll();
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : questionList) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
            vo.set("user", userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        model.addAttribute("vos", vos);
        return "management";
    }

    @RequestMapping(path = {"/management/searchque"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String search(Model model,@RequestParam("title") String title) {
        List<Question> questionList = questionService.getByName(title);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : questionList) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
            vo.set("user", userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        model.addAttribute("vos", vos);
        return "management";
    }

    @RequestMapping(path = {"/management/searchuser"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userAll(Model model) {
        List<User> userList = userService.getAll();
        List<ViewObject> vos = new ArrayList<>();
        for (User user : userList) {
            ViewObject vo = new ViewObject();
            // vo.set("user", user);
            // vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, user.getId()));
            // vo.set("user", userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        model.addAttribute("vos", vos);
        return "userlist";
    }

    @RequestMapping(path = {"/management/user"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String followers(Model model) {
        List<ViewObject> vos = new ArrayList<>();
        List<User> userList = userService.getAll();
        for (User user : userList) {
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            System.out.println(user.getName());
            vo.set("commentCount", commentService.getUserCommentCount(user.getId()));
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, user.getId()));
            vo.set("followeeCount", followService.getFolloweeCount(user.getId(), EntityType.ENTITY_USER));
            vos.add(vo);
        }
        System.out.println(vos);
        model.addAttribute("vos", vos);
        return "userlist";
    }
}