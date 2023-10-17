package com.aat.application.data.entity;

/**
 * H - Hour
 * K - KM
 * E - Each
 * @author jtrinidad
 *
 */
public enum ZJEUom {
	H("Hour"), K ("KM"), E ("Each");
	
    private final String name;

    ZJEUom(String name) {
        this.name = name;
    }

   @Override
   public String toString() {
	   return name;
   }
   
}