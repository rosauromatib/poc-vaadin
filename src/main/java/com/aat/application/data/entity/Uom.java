package com.aat.application.data.entity;

/**
 * H - Hour
 * K - KM
 * E - Each
 * @author jtrinidad
 *
 */
public enum Uom {
	H("Hour"), K ("KM"), E ("Each");
	
    private String name;

    private Uom(String name) {
        this.name = name;
    }

   @Override
   public String toString() {
	   return name;
   }
   
}