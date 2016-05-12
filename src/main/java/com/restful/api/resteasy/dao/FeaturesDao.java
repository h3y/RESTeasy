/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restful.api.resteasy.dao;

import com.restful.api.resteasy.model.FeaturesDb;
import com.restful.api.resteasy.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

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
            feature = (FeaturesDb) session.createQuery("from Features where id= :Id").setParameter("Id", id).uniqueResult();
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
    
    public  List <FeaturesDb> getFeature() {

        List <FeaturesDb> feature = null;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            feature = session.createQuery("from FeaturesDb").list();
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
            session.createQuery("delete Features  where id = :Id").setParameter("Id", id).executeUpdate();
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
