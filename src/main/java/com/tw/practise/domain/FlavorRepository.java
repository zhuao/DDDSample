package com.tw.practise.domain;

import com.tw.practise.domain.Flavor;

/**
 * Created by azhu on 27/05/2017.
 */
public interface FlavorRepository {

    public Flavor findFlavor(String flavorId);
}
