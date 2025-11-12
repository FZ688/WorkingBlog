package com.iblog.controller.admin;

import com.iblog.bean.RespBean;
import com.iblog.bean.Role;
import com.iblog.bean.User;
import com.iblog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author iblog
 */
@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserManaController {
    private final UserService userService;

    @GetMapping(value = "/user")
    public List<User> getUserByNickname(String nickname) {
        return userService.getUserByNickname(nickname);
    }

    @GetMapping(value = "/user/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping(value = "/roles")
    public List<Role> getAllRole() {
        return userService.getAllRole();
    }

    @PutMapping(value = "/user/enabled")
    public RespBean updateUserEnabled(Boolean enabled, Long uid) {
        if (userService.updateUserEnabled(enabled, uid) == 1) {
            return new RespBean("success", "更新成功!");
        } else {
            return new RespBean("error", "更新失败!");
        }
    }

    @DeleteMapping(value = "/user/{uid}")
    public RespBean deleteUserById(@PathVariable Long uid) {
        if (userService.deleteUserById(uid) == 1) {
            return new RespBean("success", "删除成功!");
        } else {
            return new RespBean("error", "删除失败!");
        }
    }

    @RequestMapping(value = "/user/role", method = RequestMethod.PUT)
    public RespBean updateUserRoles(@RequestBody List<Long> rids, Long id) {
        if (userService.updateUserRoles(rids, id) == rids.size()) {
            return new RespBean("success", "更新成功!");
        } else {
            return new RespBean("error", "更新失败!");
        }
    }

    @PostMapping(value = "/user")
    public RespBean addUser(User user, @RequestParam(value = "rids", required = false) String rids) {
        // rids 以逗号分隔的字符串，例如 "1,2"，这是前端使用 x-www-form-urlencoded 发送数组的简易方式
        List<Long> ridList = null;
        if (rids != null && !rids.trim().isEmpty()) {
            String[] parts = rids.split(",");
            ridList = new java.util.ArrayList<>();
            for (String p : parts) {
                try {
                    ridList.add(Long.parseLong(p.trim()));
                } catch (NumberFormatException e) {
                    // ignore invalid
                    log.error(e.getMessage());
                }
            }
        }
        int res = userService.adminAddUser(user, ridList);
        if (res == 0) {
            return new RespBean("success", "添加成功!");
        } else if (res == 1) {
            return new RespBean("error", "用户名重复，添加失败!");
        } else {
            return new RespBean("error", "添加失败!");
        }
    }
}
