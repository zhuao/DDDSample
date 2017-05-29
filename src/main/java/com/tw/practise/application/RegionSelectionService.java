package com.tw.practise.application;

import com.tw.practise.domain.Flavor;
import org.springframework.stereotype.Component;

/**
 * Created by azhu on 26/05/2017.
 */
@Component
public class RegionSelectionService {
    public String selectAZ(String az, Flavor flavor) {
        //TODO: Implement choose biz requirement; should pass flaovr object, not id
        // 1. if not specific az,  Choose the AZ which utilization is blow 50%,
        // 2. if specific az, but the az utilization is above 80%, chose another one.
        return az;

    }
}
