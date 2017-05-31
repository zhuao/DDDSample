package com.tw.practise.domain;


import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by azhu on 25/05/2017.
 */
public class InstanceTest {

    //TODO:replace with right constraints
    private static final String DEFAULT_REGION = "SZ";

    @Test
    public void should_assign_to_SZ_region() {
        Instance instanceWithoutRegion = new Instance();
        assertThat(instanceWithoutRegion.region(), is(DEFAULT_REGION));
    }

    @Test
    public void should_assign_to_specified_region() {
        Instance instanceInBJ = new Instance("BJ");
        assertThat(instanceInBJ.region(), is("BJ"));
    }


    @Test
    public void should_assign_to_default_region_if_region_is_invalid() {
        Instance instanceInSH = new Instance("SH");
        assertThat(instanceInSH.region(), is(DEFAULT_REGION));
    }


}