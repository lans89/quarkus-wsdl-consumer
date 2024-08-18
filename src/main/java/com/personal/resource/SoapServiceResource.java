package com.personal.resource;

import com.personal.dto.RequestDTO;
import com.personal.entity.SoapRequestEntity;
import com.personal.entity.SoapResponseEntity;
import com.personal.entity.SoapServiceEntity;
import com.personal.service.SoapExecutorService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/config")
public class SoapServiceResource {

    @Inject
    SoapExecutorService executorService;

    @Path("/services/")
    @GET
    public Uni<List<SoapServiceEntity>> getAllSoapService(){
        return SoapServiceEntity.findAll().list();
    }

    @Path("/services/{id}")
    @GET
    public Uni<SoapServiceEntity> getOneSoapService(@PathParam("id") String id){
        return SoapServiceEntity.findByIdentify(id);
    }

    @Path("/services/request/{id}")
    @GET
    public Uni<SoapRequestEntity> getOneSoapRequest(@PathParam("id") String id){
        return SoapRequestEntity.findFromId(id);
    }

    @Path("/services/response/{id}")
    @GET
    public Uni<List<SoapResponseEntity>> getAllSoapResponse(@PathParam("id") String id){
        return SoapResponseEntity.listAllChainRead(id);
    }

    @Path("/call")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.TEXT_PLAIN})
    public Uni<String> executeSoapWebService(RequestDTO request){
        return executorService.callMethodService(request);
    }

}
