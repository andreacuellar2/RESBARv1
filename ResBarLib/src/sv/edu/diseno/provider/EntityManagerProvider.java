/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.diseno.provider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author LuisEnrique
 */
public class EntityManagerProvider {
 
    private final static String puName="ResBarLibPU";
    
    public static EntityManager getEntityManager(){
       EntityManagerFactory emf = Persistence.createEntityManagerFactory(puName);
       return emf.createEntityManager();
    }
    
}
