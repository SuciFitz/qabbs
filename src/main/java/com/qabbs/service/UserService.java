package com.qabbs.service;

import com.qabbs.dao.LoginTicketDAO;
import com.qabbs.dao.UserDAO;
import com.qabbs.model.LoginTicket;
import com.qabbs.model.User;
import com.qabbs.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User selectByName(String name) {
        return userDAO.selectByName(name);
    }

    public List<User> getAll() {
        return userDAO.selectAll();
    }

    /**
    * 功能描述:用户注册
     *
     * @since: 1.0.0
     * @Author:73952
     * @Date: 2019/5/16
     */
    public Map<String, Object> register(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);

        if (user != null) {
            map.put("msg", "用户名已经被注册");
            return map;
        }

        // 密码强度
        user = new User();
        user.setName(username);
        String head = String.format("http://localhost:8080/images/head/%d.png", new Random().nextInt(66));
        user.setHeadUrl(head);
        user.setPassword(WendaUtil.MD5(password));
        userDAO.addUser(user);

        // 登陆
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }


    /**
    * 功能描述:用户登录
     *
     * @since: 1.0.0
     * @Author:73952
     * @Date: 2019/5/16
     */
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);

        if (user == null) {
            map.put("msg", "用户名不存在");
            return map;
        }

        if (!WendaUtil.MD5(password).equals(user.getPassword())) {
            map.put("msg", "密码不正确");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        map.put("userId", user.getId());
        return map;
    }

    /**
    * 功能描述:生成一个7天的ticket，若选择记住用户则添加到cookies
     *
     * @param
     * @return:
     * @since: 1.0.0
     * @Author:73952
     * @Date: 2019/5/16
     */
    private String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    /**
    * 功能描述:用户登出，更新ticket状态为失效
     *
     * @since: 1.0.0
     * @Author:73952
     * @Date: 2019/5/16
     */
    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }
}
