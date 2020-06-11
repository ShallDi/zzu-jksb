package org.bxd.jksb.controller;

import org.bxd.jksb.cache.SbResultCache;
import org.bxd.jksb.service.AutoJksbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/")
@RestController
public class Controller {

    @Autowired
    private SbResultCache sbResultCache;

    @Autowired
    private AutoJksbService autoJksbService;

    @RequestMapping("/sb_now")
    public List<String> sbNow() {
        return autoJksbService.autoSb();
    }

    @RequestMapping("/{user}")
    public String getResult(@PathVariable("user") String user) {
        List<String> temp = sbResultCache.get(user);
        if (temp == null) {
            return "No User";
        }
        int size = temp.size();
        if (size == 0) {
            return "No Data";
        }
        return temp.get(size - 1);
    }
}
