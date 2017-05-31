package com.tw.practise.interfaces.controller;

import com.google.gson.GsonBuilder;
import com.tw.practise.application.InstanceService;
import com.tw.practise.domain.Instance;
import com.tw.practise.domain.InstanceRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class InstancesController {


    private InstanceService instanceService;
    private InstanceRepository instanceRepository;

    @RequestMapping(path = "/instances", method = RequestMethod.POST)
    @ResponseBody
    public String createInstance(@RequestBody RequestParamWrapper requestParamWrapper) {
        Instance instance = new InstanceAssembler(requestParamWrapper.get()).assembleInstance();
        instanceService.createInstance(instance);
        return new GsonBuilder().create().toJson(instance);
    }

    @RequestMapping(path = "/instances", method = RequestMethod.GET)
    @ResponseBody
    public String findInstances() {
        return new GsonBuilder().create().toJson(instanceRepository.findAll());
    }

    public InstancesController(InstanceService instanceService, InstanceRepository instanceRepository) {
        this.instanceService = instanceService;
        this.instanceRepository = instanceRepository;
    }
}
