/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restful.api.resteasy.dao;

import com.restful.api.resteasy.model.Feature;
import com.restful.api.resteasy.config.HibernateUtil;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author slavik
 */
public class FeatureDAO {

  public Feature getFeatureById(int id) {
    Feature feature = null;
    Session session = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      Criteria crit = session.createCriteria(Feature.class);
      crit.add(Restrictions.eq("id", id));
      feature = (Feature) crit.uniqueResult();
    } catch (Exception ex) {
      Logger.getLogger(Feature.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    return feature;
  }

  public List<Feature> getFeatures(Integer length, Integer start, String column, String dir, String value) {

    List<Feature> features = null;
    Session session = null;

    try {
      session = HibernateUtil.getSessionFactory().openSession();

      Criteria crit = session.createCriteria(Feature.class);
      if (value != null) {
        Criterion v1 = Restrictions.like("rendering_engine", "%" + value + "%");
        Criterion v3 = Restrictions.like("browser", "%" + value + "%");
        Criterion v4 = Restrictions.like("platform", "%" + value + "%");
        Criterion v5 = Restrictions.like("engine_version", "%" + value + "%");
        Criterion v6 = Restrictions.like("css_grade", "%" + value + "%");
        crit.add(Restrictions.or(v1, v3, v4, v5, v6));
      }
      if (dir != null && column != null) {
        if ("desc".equals(dir.toLowerCase())) {
          crit.addOrder(Order.desc(column));//сортування
        } else {
          crit.addOrder(Order.asc(column));//сортування
        }
      }
      if (length > 0) {
        crit.setMaxResults(length);
      }
      if (start > 0) {
        crit.setFirstResult(start);
      }
      features = crit.list();
    } catch (Exception ex) {
      Logger.getLogger(Feature.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    return features;
  }

    public long getFeaturesFilteredCount(String column, String dir, String value) {

    long features = 0;
    Session session = null;

    try {
      session = HibernateUtil.getSessionFactory().openSession();

      Criteria crit = session.createCriteria(Feature.class);
      if (value != null) {
        Criterion v1 = Restrictions.like("rendering_engine", "%" + value + "%");
        Criterion v3 = Restrictions.like("browser", "%" + value + "%");
        Criterion v4 = Restrictions.like("platform", "%" + value + "%");
        Criterion v5 = Restrictions.like("engine_version", "%" + value + "%");
        Criterion v6 = Restrictions.like("css_grade", "%" + value + "%");
        crit.add(Restrictions.or(v1, v3, v4, v5, v6));
      }
      if (dir != null && column != null) {
        if ("desc".equals(dir.toLowerCase())) {
          crit.addOrder(Order.desc(column));//сортування
        } else {
          crit.addOrder(Order.asc(column));//сортування
        }
      }

       crit.setProjection(Projections.countDistinct("id"));
      
      features = (long) crit.uniqueResult();
    } catch (Exception ex) {
      Logger.getLogger(Feature.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    return features;
  }
    
   public long getFeaturesCount() {

    long features = 0;
    Session session = null;

    try {
      session = HibernateUtil.getSessionFactory().openSession();

      Criteria crit = session.createCriteria(Feature.class);
      crit.setProjection(Projections.countDistinct("id"));
      
      features = (long) crit.uniqueResult();
    } catch (Exception ex) {
      Logger.getLogger(Feature.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    return features;
  }
  
  public List getFeatureGroupBy() {
    List features = null;
    Session session = null;

    try {
      session = HibernateUtil.getSessionFactory().openSession();
      features = session.createQuery("select rendering_engine, count(rendering_engine) from Feature group by renderingengine").list();
    } catch (Exception ex) {
      Logger.getLogger(Feature.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    return features;
  }

  public void addFeature(Feature feature) {
    Session session = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      session.beginTransaction();
      session.save(feature);
      session.getTransaction().commit();
    } catch (Exception ex) {
      Logger.getLogger(Feature.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }

  public void updateFeature(int id, Feature feature) {
    Session session = null;
    try {
      feature.setId(id);
      session = HibernateUtil.getSessionFactory().openSession();
      session.beginTransaction();
      session.update(feature);
      session.getTransaction().commit();
    } catch (Exception ex) {
      Logger.getLogger(Feature.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }

  public void deleteFeature(int id) {
    Feature feature = null;
    Session session = null;
    try {
      feature = new Feature();
      feature.setId(id);
      session = HibernateUtil.getSessionFactory().openSession();
      session.beginTransaction();
      session.delete(feature);
      session.getTransaction().commit();
    } catch (Exception ex) {
      Logger.getLogger(Feature.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }
}
