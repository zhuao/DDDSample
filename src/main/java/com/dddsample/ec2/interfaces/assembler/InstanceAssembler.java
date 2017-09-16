package com.dddsample.ec2.interfaces.assembler;

import com.dddsample.ec2.domain.instance.Instance;
import io.swagger.api.InstancesApiController;
import io.swagger.model.*;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import java.util.List;

/**
 * Created by azhu on 27/05/2017.
 */
public class InstanceAssembler {



    public InstanceResponse toDTO(Instance instance) {

        Link link = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(InstancesApiController.class).findInstance(instance.getInstanceId())).withSelfRel();
        Links links = new Links();
        links.add(new LinksInner().rel(link.getRel()).href(link.getHref()));

        return new InstanceResponse()
                .flavorId(instance.getFlavorId())
                .instanceId(instance.getInstanceId())
                .az(instance.getAZ())
                .status(instance.status().toString())
                .links(links);
    }

    public InstanceListResponse toDTO(List<Instance> instanceList) {
        InstanceListResponse instancesResponses = new InstanceListResponse();
        for (Instance instance : instanceList) {
            instancesResponses.add(toDTO(instance));
        }
        return instancesResponses;
    }

    public Instance fromDTO(InstanceCreateRequest creationRequest) {
        Instance instance = new Instance(creationRequest.getFlavorId());
        instance.setAZ(creationRequest.getAz());
        return instance;
    }
}
