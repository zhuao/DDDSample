package com.tw.practise.interfaces.controller;

import com.google.gson.GsonBuilder;
import com.tw.practise.application.InstanceService;
import com.tw.practise.domain.Instance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class InstancesController {


    private InstanceService instanceService;

    @RequestMapping(path = "/instances", method = RequestMethod.POST)
    @ResponseBody
    public String createInstance(@RequestBody RequestParamWrapper requestParamWrapper) {
        Instance instance = new InstanceAssembler(requestParamWrapper.get()).assembleInstance();
        instanceService.createInstance(instance);
        return new GsonBuilder().create().toJson(instance);
    }

    public InstancesController(InstanceService instanceService) {
        this.instanceService = instanceService;
    }
}
