/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restful.api.resteasy.service;

import com.restful.api.resteasy.dao.FeaturesDao;
import com.restful.api.resteasy.model.Feature;
import com.restful.api.resteasy.model.requestParam;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;

/**
 * REST Web Service
 *
 * @author slavik
 */
@Path("api")
public class Service{
    private final  String HAS_ERROR = "{\"response\": \"error\"}";
    private final  String HAS_SUCCESS = "{\"response\": \"success\"}";
   
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
    public Response getFeatureById(@PathParam("id")int id){
        Feature f = featureDao.getFeatureById(id); 
        if(f == null)
            return Response.status(404).entity(HAS_ERROR).build();
        else
            return Response.status(200).entity(f).build();
    }
    
    @GET
    @Path("/features")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFeature(){       
        JSONObject obj = featureDao.getFeature(context);
        if(obj.get("data")==null)
            return Response.status(404).build();
        else
            return Response.status(200).entity(obj.get("data")).header("recordsFiltered", obj.get("recordsFiltered")).header("recordsTotal", obj.get("recordsTotal")).build();  
     }
    
    @GET
    @Path("/featuresgroup")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFeatureGroupBy(){
        List f = featureDao.getFeatureGroupBy();
        return Response.status(200).entity(f).build();
    }
    
    @DELETE
    @Path("/feature/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFeature(@PathParam("id")int id){
          return !featureDao.deleteFeature(id)? Response.status(200).entity(HAS_SUCCESS).build():
                 Response.status(404).entity(HAS_ERROR).build();
    }
    
    @POST
    @Path("/feature")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFeature(Feature f){
         return !featureDao.addFeature(f)? Response.status(200).entity(HAS_SUCCESS).build():
                 Response.status(404).entity(HAS_ERROR).build();
    }
    
    @PUT
    @Path("/feature/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editFeature(@PathParam("id")int id,Feature f){
        return !featureDao.editFeature(id,f)? Response.status(200).entity(HAS_SUCCESS).build():
                Response.status(404).entity(HAS_ERROR).build();
    }
}
