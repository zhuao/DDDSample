package com.tw.dddsample.domain;


import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by azhu on 25/05/2017.
 */
public class InstanceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    //TODO:replace with right constraints
    private static final String DEFAULT_REGION = "SZ";

    @Test
    @Ignore
    public void should_assign_to_SZ_region() {
        Instance instanceWithoutRegion = new Instance();
        assertThat(instanceWithoutRegion.region(), is(DEFAULT_REGION));
    }

    @Test
    @Ignore
    public void should_assign_to_specified_region() {

        Instance instanceInBJ = new Instance("BJ");
        assertThat(instanceInBJ.region(), is("BJ"));
    }


    @Test
    @Ignore
    public void should_assign_to_default_region_if_region_is_invalid() {
        Instance instanceInSH = new Instance("SH");
        assertThat(instanceInSH.region(), is(DEFAULT_REGION));
    }

    @Test
    public void should_start_instance_after_it_is_created() {
        Instance instance = new Instance();

        assertTrue(instance.start());
    }

    @Test
    public void should_not_retire_instance_when_is_running() {
        Instance instance = new Instance();
        instance.start();

        assertFalse("Should stop instance before retire", instance.retire());
    }

    @Test
    public void should_retire_instance_when_is_not_running() {
        Instance instance = new Instance();

        assertTrue("Should retire instance for unused instance", instance.retire());
    }

    @Test
    public void should_not_start_instance_after_retire() {
        Instance instance = new Instance();
        instance.retire();

        assertFalse("Instance could not be started after retired", instance.start());
    }


}