package com.tw.practise.domain;

import org.springframework.data.repository.Repository;

/**
 * Created by azhu on 27/05/2017.
 */
public interface InstanceRepository {
    public Instance save(Instance instance);
}
