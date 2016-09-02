/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restful.api.resteasy.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author slavik
 */
@Entity
@Table(name = "features_db")
public class Feature implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id")
  private int id;
  @Column(name = "Renderingengine")
  private String rendering_engine;
  @Column(name = "Browser")
  private String browser;
  @Column(name = "Platforms")
  private String platform;
  @Column(name = "Engineversion")
  private String engine_version;
  @Column(name = "CSSgrade")
  private String css_grade;
  
  public Feature(){}

  public Feature(int id, String rendering_engine, String browser, String platform, String engine_version, String css_grade) {
    this.id = id;
    this.rendering_engine = rendering_engine;
    this.browser = browser;
    this.platform = platform;
    this.engine_version = engine_version;
    this.css_grade = css_grade;
  }

  public int getId() {
    return id;
  }

  public String getRendering_engine() {
    return rendering_engine;
  }

  public String getBrowser() {
    return browser;
  }

  public String getPlatform() {
    return platform;
  }

  public String getEngine_version() {
    return engine_version;
  }

  public String getCss_grade() {
    return css_grade;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setRendering_engine(String rendering_engine) {
    this.rendering_engine = rendering_engine;
  }

  public void setBrowser(String browser) {
    this.browser = browser;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public void setEngine_version(String engine_version) {
    this.engine_version = engine_version;
  }

  public void setCss_grade(String css_grade) {
    this.css_grade = css_grade;
  }

}
