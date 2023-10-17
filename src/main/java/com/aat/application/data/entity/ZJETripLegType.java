package com.aat.application.data.entity;

/**
 * @author jtrinidad
 *
 */
public enum ZJETripLegType {
	LR("Laden Running"), LS ("Laden Standing"), UR ("Unladen Running"), US("Unladen Standing"); 
	
    private final String name;

    ZJETripLegType(String name) {
        this.name = name;
    }

   @Override
   public String toString() {
	   return name;
   }
   
}