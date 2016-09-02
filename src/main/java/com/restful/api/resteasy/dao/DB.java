/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restful.api.resteasy.dao;

/**
 *
 * @author slavik
 */
public class DB {

  private static FeatureDAO featureDAO = null;
  private static DB instance = null;

  public static synchronized DB getDB() {
    if (instance == null) {
      instance = new DB();
    }
    return instance;
  }

  public FeatureDAO getFeatureDAO() {
    if (featureDAO == null) {
      featureDAO = new FeatureDAO();
    }
    return featureDAO;
  }

}
