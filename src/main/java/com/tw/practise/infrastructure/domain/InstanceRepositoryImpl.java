package com.tw.practise.infrastructure.domain;

import com.tw.practise.domain.Instance;
import com.tw.practise.domain.InstanceRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by azhu on 29/05/2017.
 */
@Component
public class InstanceRepositoryImpl implements InstanceRepository {

    private static final HashMap<String, Instance> MEM_DB = new HashMap<>();

    @Override
    public Instance save(Instance instance) {
        if (StringUtils.isEmpty(instance.getInstanceId())) {
            instance.setInstanceId(UUID.randomUUID().toString());
            MEM_DB.put(instance.getInstanceId(), instance);
        }

        return instance;
    }
}
