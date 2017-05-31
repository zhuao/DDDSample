package com.tw.practise.application;

import com.tw.practise.domain.FlavorRepository;
import com.tw.practise.domain.Instance;
import com.tw.practise.domain.InstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by azhu on 26/05/2017.
 */
@Component
public class InstanceService {

    @Autowired
    private RegionSelectionService regionSelectionService;
    @Autowired
    private InstanceRepository instanceRepository;
    @Autowired
    private FlavorRepository flavorRepository;

    public Instance createInstance(Instance instance) {
        instance.setAZ(regionSelectionService.selectAZ(instance.getAZ(), flavorRepository.findFlavor(instance.getFlavorId())));
        return instanceRepository.save(instance);
    }
}
