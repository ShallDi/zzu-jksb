package org.bxd.jksb.cache;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SbResultCache {

    private Map<String, List<String>> result = new HashMap<>();

    public void setResult(String user, String info) {
        List<String> temp = this.result.get(user);
        if (temp == null) {
            temp = new ArrayList<>();
            result.put(user, temp);
        }
        temp.add(info);
    }

    public List<String> get(String usr) {
        return result.get(usr);
    }
}
