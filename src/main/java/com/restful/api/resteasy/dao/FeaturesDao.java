/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restful.api.resteasy.dao;

import com.restful.api.resteasy.model.FeaturesDb;
import com.restful.api.resteasy.model.requestParam;
import com.restful.api.resteasy.util.HibernateUtil;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.simple.JSONObject;

/**
 *
 * @author slavik
 */
public class FeaturesDao {
     SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public FeaturesDb getFeatureById(int id) {

        FeaturesDb feature = null;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            feature = (FeaturesDb) session.createQuery("from FeaturesDb where id= :Id").setParameter("Id", id).uniqueResult();
            session.getTransaction().commit();
        } 
        catch (Exception ex) {
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return feature;
    }
    
    public Response getFeature(UriInfo context){
        
        requestParam r = new requestParam(Integer.parseInt(context.getQueryParameters().getFirst("length")),
                                    Integer.parseInt(context.getQueryParameters().getFirst("start")),
                                    context.getQueryParameters().getFirst("column"),
                                    context.getQueryParameters().getFirst("dir"),
                                    context.getQueryParameters().getFirst("regex"),
                                    context.getQueryParameters().getFirst("value"));
         
        List feature = null;
        Session session = null;
        Object recordsFiltered = null ;
        Object recordsTotal = null ;  
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            String query = "from FeaturesDb";       
            if (r.getValue().length() != 0){
                query += " where renderingengine like '%"+r.getValue()+"%'";
                query += "or browser like '%"+r.getValue()+"%'";
                query += "or platforms like '%"+r.getValue()+"%'";
                query += "or engineversion like '%"+r.getValue()+"%'";
                query += "or cssgrade like '%"+r.getValue()+"%'";
            }

            if(r.getDir().length() != 0)
                query += " order by "+r.getColumn()+" "+r.getDir();

            feature = session.createQuery(query).setMaxResults(r.getLength()).setFirstResult(r.getStart()).list();
            recordsTotal  = session.createQuery("SELECT COUNT(*) FROM FeaturesDb").list().get(0);
            recordsFiltered = session.createQuery("SELECT COUNT(*)"+query).list().get(0);
            session.getTransaction().commit();
        } 
        catch (Exception ex) {
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }   
        return Response.status(200).header("recordsFiltered", recordsFiltered).header("recordsTotal", recordsTotal).entity(feature).build();
    }
    
      public List getFeatureGroupBy() {

        List feature = null;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            feature =  session.createQuery("select renderingengine, count(renderingengine) from FeaturesDb group by renderingengine").list();
            session.getTransaction().commit();
        } 
        catch (Exception ex) {
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return feature;
    }
    
     public boolean addFeature(FeaturesDb feature) {
        boolean Error = false;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.saveOrUpdate(feature);
            session.getTransaction().commit();
        } 
        catch (Exception ex) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            Error = true;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return Error;
    }
     public boolean editFeature(int id,FeaturesDb feature) {
        boolean Error = false;
        Session session = null;
        feature.setId(id);
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.saveOrUpdate(feature);
            session.getTransaction().commit();
        } 
        catch (Exception ex) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            Error = true;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return Error;
    }
     
    public boolean deleteFeature(int id) {
        boolean Error = false;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.createQuery("delete FeaturesDb  where id = :Id").setParameter("Id", id).executeUpdate();
            session.getTransaction().commit();
        } 
        catch (Exception ex) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            Error = true;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return Error;
    }
}
