package com.mingchao.snsspider.storage.spi;

import java.io.Serializable;
import java.util.List;

public interface StorageSpi {
  void insert(Object object);

  void insert(List<?> list);

  void insertIgnore(Object object);

  void insertIgnore(List<?> list);

  void insertDuplicate(Object object);

  void insertDuplicate(List<?> list);

  Object get(Class<?> c,Serializable id);
  
  /**
   * 
   * @param c
   * @param idStart 开始id，包含
   * @param idEnd 借宿id，不包含
   * @return
   */
   List<Object> get(Class<?> c,Serializable idStart, Serializable idEnd);

   Boolean hasMore(Class<?> c, Serializable idStart);

   void delete(Class<?> c, Serializable idStart, Serializable idEnd);
   
   void close();
}
