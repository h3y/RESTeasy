package com.restful.api.resteasy.model;
// Generated 05.05.2016 15:17:48 by Hibernate Tools 4.3.1



/**
 * Feature generated by hbm2java
 */
public class Feature  implements java.io.Serializable {


     private Integer id;
     private String renderingengine;
     private String browser;
     private String platforms;
     private String engineversion;
     private String cssgrade;

    public Feature() {
    }

    public Feature(String renderingengine, String browser, String platforms, String cssgrade) {
        this.renderingengine = renderingengine;
        this.browser = browser;
        this.platforms = platforms;
        this.cssgrade = cssgrade;
    }
    public Feature(String renderingengine, String browser, String platforms, String engineversion, String cssgrade) {
       this.renderingengine = renderingengine;
       this.browser = browser;
       this.platforms = platforms;
       this.engineversion = engineversion;
       this.cssgrade = cssgrade;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getRenderingengine() {
        return this.renderingengine;
    }
    
    public void setRenderingengine(String renderingengine) {
        this.renderingengine = renderingengine;
    }
    public String getBrowser() {
        return this.browser;
    }
    
    public void setBrowser(String browser) {
        this.browser = browser;
    }
    public String getPlatforms() {
        return this.platforms;
    }
    
    public void setPlatforms(String platforms) {
        this.platforms = platforms;
    }
    public String getEngineversion() {
        return this.engineversion;
    }
    
    public void setEngineversion(String engineversion) {
        this.engineversion = engineversion;
    }
    public String getCssgrade() {
        return this.cssgrade;
    }
    
    public void setCssgrade(String cssgrade) {
        this.cssgrade = cssgrade;
    }
}


