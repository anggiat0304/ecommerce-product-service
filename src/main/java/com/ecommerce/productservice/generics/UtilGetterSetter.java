package com.ecommerce.productservice.generics;

import java.util.List;

public class UtilGetterSetter <T>{
    private T obj;
    private List<T> listObj;

    public UtilGetterSetter(T obj,List<T> listObj) {
        this.obj = obj;
        this.listObj = listObj;
    }
    public T getObj(){
        return obj;
    }
    public void setObj(T obj){
        this.obj = obj;
    }

    public List<T> getListObj(){
        return listObj;
    }
    public void setListObj(List<T> listObj){
        this.listObj = listObj;
    }
}
