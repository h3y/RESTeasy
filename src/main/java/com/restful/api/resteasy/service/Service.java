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
    @Path("/get_features/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public FeaturesDb getFeatureById(@PathParam("id")int id){
        return featureDao.getFeatureById(id);
    }
    @GET
    @Path("/get_features")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FeaturesDb> getFeature(){
        return featureDao.getFeature();
    }
    @DELETE
    @Path("/delete_features/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteFeature(@PathParam("id")int id){
        if(!featureDao.deleteFeature(id))
            return "{\"response\": \"success\"}";
         return "{\"error\": \"fail\"}";
        
    }
    
    @POST
    @Path("/add_features/{Renderingengine}/{Browser}/{Platforms}/{Engineversion}/{Cssgrade}")
    @Produces(MediaType.APPLICATION_JSON)
    public String addFeature(@PathParam("Renderingengine")String Renderingengine,@PathParam("Browser")String Browser,@PathParam("Platforms")String Platforms,@PathParam("Engineversion")Double Engineversion,@PathParam("Cssgrade")String Cssgrade){
        FeaturesDb f = new FeaturesDb();
        f.setRenderingengine(Renderingengine);
        f.setBrowser(Browser);
        f.setPlatforms(Platforms);
        f.setEngineversion(Engineversion);
        f.setCssgrade(Cssgrade);
        if(!featureDao.addFeature(f))
            return "{\"response\": \"success\"}";
         return "{\"error\": \"fail\"}";
    }
    @PUT
    @Path("/update_features/{Id}/{Renderingengine}/{Browser}/{Platforms}/{Engineversion}/{Cssgrade}")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateFeature(@PathParam("Id")int Id,@PathParam("Renderingengine")String Renderingengine,@PathParam("Browser")String Browser,@PathParam("Platforms")String Platforms,@PathParam("Engineversion")Double Engineversion,@PathParam("Cssgrade")String Cssgrade){
        FeaturesDb f = new FeaturesDb();
        f.setId(Id);
        f.setRenderingengine(Renderingengine);
        f.setBrowser(Browser);
        f.setPlatforms(Platforms);
        f.setEngineversion(Engineversion);
        f.setCssgrade(Cssgrade);
        if(!featureDao.addFeature(f))
            return "{\"response\": \"success\"}";
         return "{\"error\": \"fail\"}";
    }
}
