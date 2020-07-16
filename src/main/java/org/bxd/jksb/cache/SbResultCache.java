package org.bxd.jksb.cache;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SbResultCache {

    private Map<String, List<String>> result = new ConcurrentHashMap<>();

    public void setResult(String user, String info) {
        List<String> temp = this.result.get(user);
        if (temp == null) {
            temp = new LimitedQueue<>(5);
            result.put(user, temp);
        }
        temp.add(info);
    }

    public List<String> get(String usr) {
        return result.get(usr);
    }
}

class LimitedQueue<E> extends LinkedList<E> {
    private static final long serialVersionUID = 1L;
    private int limit;

    public LimitedQueue(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(E o) {
        super.add(o);
        while (size() > limit) { super.remove(); }
        return true;
    }
}
