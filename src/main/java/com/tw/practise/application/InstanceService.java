package com.tw.practise.application;

import com.tw.practise.domain.FlavorRepository;
import com.tw.practise.domain.Instance;
import com.tw.practise.domain.InstanceRepository;
import org.springframework.stereotype.Component;

/**
 * Created by azhu on 26/05/2017.
 */
@Component
public class InstanceService {

    private RegionSelectionService regionSelectionService;
    private InstanceRepository instanceRepository;
    private FlavorRepository flavorRepository;

    public Instance createInstance(Instance instance) {
        instance.setAZ(regionSelectionService.selectAZ(instance.getAZ(), flavorRepository.findFlavor(instance.getFlavorId())));
        return instanceRepository.save(instance);
    }

    public InstanceService(RegionSelectionService regionSelectionService, InstanceRepository instanceRepository, FlavorRepository flavorRepository) {
        this.regionSelectionService = regionSelectionService;
        this.instanceRepository = instanceRepository;
        this.flavorRepository = flavorRepository;
    }
}
