/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restful.api.resteasy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restful.api.resteasy.dao.DB;
import com.restful.api.resteasy.model.Feature;
import com.restful.api.resteasy.model.page;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web FeaturesController
 *
 * @author slavik
 */
@Path("api")
public class FeaturesController {

  @GET
  @Path("/feature/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String getFeatureById(@PathParam("id") int id) throws JsonProcessingException {
    Feature feature = DB.getDB().getFeatureDAO().getFeatureById(id);
    return new ObjectMapper().writeValueAsString(feature);
  }

  @GET
  @Path("/features")
  @Produces(MediaType.APPLICATION_JSON)
  public String getFeature(@QueryParam("length") int length, @QueryParam("start") int start,
          @QueryParam("column") String column, @QueryParam("dir") String dir, @QueryParam("value") String value)
          throws JsonProcessingException {
    List<Feature> feature = DB.getDB().getFeatureDAO().getFeatures(length, start, column, dir, value);
    long count = DB.getDB().getFeatureDAO().getFeaturesCount();
    long filteredCount = DB.getDB().getFeatureDAO().getFeaturesFilteredCount(column, dir, value);
    
    return new ObjectMapper().writeValueAsString(new page(feature,count,filteredCount));
  }

  @GET
  @Path("/featuresgroup")
  @Produces(MediaType.APPLICATION_JSON)
  public String getFeatureGroupBy() throws JsonProcessingException {
    List feature = DB.getDB().getFeatureDAO().getFeatureGroupBy();
    return new ObjectMapper().writeValueAsString(feature);
  }

  @DELETE
  @Path("/feature/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public String deleteFeature(@PathParam("id") int id) throws JsonProcessingException {
    DB.getDB().getFeatureDAO().deleteFeature(id);
    return new ObjectMapper().writeValueAsString("{\"response\": \"success\"}");
  }

  @POST
  @Path("/feature")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public String addFeature(Feature feature) throws JsonProcessingException {
    DB.getDB().getFeatureDAO().addFeature(feature);
    return new ObjectMapper().writeValueAsString("{\"response\": \"success\"}");
  }

  @PUT
  @Path("/feature/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public String updateFeature(@PathParam("id") int id, Feature feature) throws JsonProcessingException {
    DB.getDB().getFeatureDAO().updateFeature(id, feature);
    return new ObjectMapper().writeValueAsString("{\"response\": \"success\"}");
  }
}
