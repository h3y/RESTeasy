/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restful.api.resteasy.service;

import com.restful.api.resteasy.dao.FeaturesDao;
import com.restful.api.resteasy.model.FeaturesDb;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author slavik
 */
@Path("api")
public class Service {
    @Context
    private UriInfo context;
    private FeaturesDao featureDao = new FeaturesDao();
    
    /**
     * Creates a new instance of Service
     */
    public Service() {
    }

    /**
     * Retrieves representation of an instance of com.restful.api.resteasy.service.Service
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/feature/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public FeaturesDb getFeatureById(@PathParam("id")int id){
        return featureDao.getFeatureById(id);
    }
    @GET
    @Path("/features")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FeaturesDb> getFeature(){
        return featureDao.getFeature();
    }
    @DELETE
    @Path("/feature/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteFeature(@PathParam("id")int id){
        if(!featureDao.deleteFeature(id))
            return "{\"response\": \"success\"}";
         return "{\"error\": \"fail\"}";
        
    }
    @POST
    @PUT
    @Path("/feature")
    @Produces(MediaType.APPLICATION_JSON)
    public String addFeature(FeaturesDb f){
        if(!featureDao.addFeature(f))
            return "{\"response\": \"success\"}";
         return "{\"error\": \"fail\"}";
    }
    @PUT
    @Path("/feature/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String editFeature(@PathParam("id")int id,FeaturesDb f){
        if(!featureDao.editFeature(id,f))
            return "{\"response\": \"success\"}";
         return "{\"error\": \"fail\"}";
    }
}
