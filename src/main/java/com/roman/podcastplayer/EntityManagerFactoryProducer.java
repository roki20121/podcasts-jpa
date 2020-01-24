package com.roman.podcastplayer;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerFactoryProducer {

    private static EntityManagerFactory factory;

    public static EntityManagerFactory getFactory() {

        if (factory == null) {
            synchronized (EntityManagerFactoryProducer.class) {
                if (factory == null) {
                    factory = Persistence.createEntityManagerFactory("com.roman.podcasts");
                }
            }
        }

        return factory;
    }
}
