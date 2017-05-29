package com.tw.practise.infrastructure.domain;

import com.tw.practise.domain.Flavor;
import com.tw.practise.domain.FlavorRepository;
import org.springframework.stereotype.Component;

/**
 * Created by azhu on 29/05/2017.
 */
@Component
public class FlavorRepositoryImpl implements FlavorRepository {
    @Override
    public Flavor findFlavor(String flavorId) {
        return new Flavor(flavorId, "tiny", 1, 1024);
    }
}
