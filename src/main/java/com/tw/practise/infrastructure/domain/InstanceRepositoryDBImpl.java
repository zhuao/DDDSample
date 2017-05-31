package com.tw.practise.infrastructure.domain;

import com.tw.practise.domain.Instance;
import com.tw.practise.domain.InstanceRepository;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import java.util.UUID;

/**
 * Created by azhu on 30/05/2017.
 */
@Component("instanceRepository")
public class InstanceRepositoryDBImpl implements InstanceRepository, Repository<Instance, String> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Instance save(Instance instance) {
        if (StringUtils.isEmpty(instance.getInstanceId())) {
            instance.setInstanceId(UUID.randomUUID().toString());
        }
        entityManager.persist(instance);
        entityManager.flush();
        return instance;
    }
}
