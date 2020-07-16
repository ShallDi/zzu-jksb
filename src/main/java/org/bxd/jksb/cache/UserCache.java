package org.bxd.jksb.cache;

import org.bxd.jksb.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserCache {

    private static final HashMap<String, User> userMap = new HashMap<>();

    public void setUser(String name, User user) {
        userMap.put(name, user);
    }

    public User getUserMap(String user) {
        return userMap.get(user);
    }

    public Map<String, User> getAll() {
        return userMap;
    }
}
