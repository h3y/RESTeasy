/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restful.api.resteasy.dao;

import com.restful.api.resteasy.model.Feature;
import com.restful.api.resteasy.model.requestParam;
import com.restful.api.resteasy.util.HibernateUtil;
import java.util.List;
import javax.ws.rs.core.UriInfo;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.simple.JSONObject;

/**
 *
 * @author slavik
 */
public class FeaturesDao {
  SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

  public Feature getFeatureById(int id) {
    Feature feature = null;
    Session session = null;
    try {
      session = sessionFactory.openSession();
      String s = "from Feature where id= :Id";
      Query q =  session.createQuery(s);  
      q.setInteger("Id", id);
       feature = (Feature)q.uniqueResult();
    } catch (Exception ex) {
    } finally {
      if (session != null) {
        session.close();
      }
    }
    if (feature == null) {
      return null;
    } else {
      return feature;
    }
  }

  public JSONObject getFeature(UriInfo context) {
    requestParam r = new requestParam(Integer.parseInt(context.getQueryParameters().getFirst("length")),
            Integer.parseInt(context.getQueryParameters().getFirst("start")),
            context.getQueryParameters().getFirst("column"),
            context.getQueryParameters().getFirst("dir"),
            context.getQueryParameters().getFirst("value"));

    List<Feature> features = null;
    Session session = null;
    JSONObject obj = new JSONObject();
    Object recordsFiltered = null;
    Object recordsTotal = null;
    try {
      session = sessionFactory.openSession();
    
      Criteria crit = session.createCriteria(Feature.class);
      if (r.getValue().length() != 0) {
        Criterion v1 = Restrictions.like("renderingengine", "%"+r.getValue()+"%");
        Criterion v3 = Restrictions.like("browser", "%"+r.getValue()+"%");
        Criterion v4 = Restrictions.like("platforms", "%"+r.getValue()+"%");
        Criterion v5 = Restrictions.like("engineversion", "%"+r.getValue()+"%");
        Criterion v6 = Restrictions.like("cssgrade", "%"+r.getValue()+"%");
        crit.add(Restrictions.or(v1, v3, v4, v5, v6));
      }
      
      if (r.getDir().length() != 0 && r.getColumn().length() !=0) {
        if("desc".equals(r.getDir().toLowerCase()))
          crit.addOrder(Order.desc(r.getColumn()) );//сортування
        else
          crit.addOrder(Order.asc(r.getColumn()) );//сортування
      }     
      recordsFiltered = crit.list().size();
      recordsTotal =  session.createCriteria(Feature.class).setProjection( Projections.rowCount()).list().get(0);
      crit.setMaxResults(r.getLength());
      crit.setFirstResult(r.getStart());
      features = crit.list();
    } catch (Exception ex) {
      
    } finally {
      if (session != null) {
        session.close();
      }
    }
    obj.put("recordsFiltered", recordsFiltered);
    obj.put("recordsTotal", recordsTotal);
    obj.put("data", features);
    if (features == null) {
      return null;
    }else{
        return obj;
    }
  }

  public List getFeatureGroupBy() {
    List features = null;
    Session session = null;

    try {
      session = sessionFactory.openSession();
      session.beginTransaction();
      features = session.createQuery("select renderingengine, count(renderingengine) from Feature group by renderingengine").list();
      session.getTransaction().commit();
    } catch (Exception ex) {
    } finally {
      if (session != null) {
        session.close();
      }
    }
    if (features == null) {
      return null;
    }else{
        return features;
    }
  }

  public boolean addFeature(Feature feature) {
    boolean Error = false;
    Session session = null;
    try {
      session = sessionFactory.openSession();
      session.beginTransaction();
      session.saveOrUpdate(feature);
      session.getTransaction().commit();
    } catch (Exception ex) {
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

  public boolean editFeature(int id, Feature feature) {
    boolean Error = false;
    Session session = null;
    feature.setId(id);
    try {
      session = sessionFactory.openSession();
      session.beginTransaction();
      session.saveOrUpdate(feature);
      session.getTransaction().commit();
    } catch (Exception ex) {
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
      session.createQuery("delete Feature  where id = :Id").setInteger("Id", id).executeUpdate();
      session.getTransaction().commit();
    } catch (Exception ex) {
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
