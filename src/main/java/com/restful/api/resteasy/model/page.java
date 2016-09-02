/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.restful.api.resteasy.model;

import java.util.List;

/**
 *
 * @author slavik
 */
public class page {
  
  private List data;
  private long recordsTotal;
  private long recordsFiltered;


  public page(List data, long recordsTotal, long recordsFiltered) {
    this.data = data;
    this.recordsTotal = recordsTotal;
    this.recordsFiltered = recordsFiltered;
  }

  public List getData() {
    return data;
  }

  public void setData(List data) {
    this.data = data;
  }

  public long getRecordsTotal() {
    return recordsTotal;
  }

  public void setRecordsTotal(long recordsTotal) {
    this.recordsTotal = recordsTotal;
  }

  public long getRecordsFiltered() {
    return recordsFiltered;
  }

  public void setRecordsFiltered(long recordsFiltered) {
    this.recordsFiltered = recordsFiltered;
  }

}
