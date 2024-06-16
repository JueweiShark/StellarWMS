package com.example.wmsspringbootproject.Utils;

import com.example.wmsspringbootproject.Service.UserService;
import com.example.wmsspringbootproject.constants.Constants;
import com.example.wmsspringbootproject.model.entity.Users;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommonQuery {
    @Autowired
    private UserService userService;

    public Users getUser(Integer userId) {
        Users user = (Users) WmsCache.get(Constants.USER_CACHE + userId.toString());
        if (user != null) {
            return user;
        }
        Users u = userService.getById(userId);
        if (u != null) {
            WmsCache.put(Constants.USER_CACHE + userId.toString(), u, Constants.EXPIRE);
            return u;
        }
        return null;
    }
}
