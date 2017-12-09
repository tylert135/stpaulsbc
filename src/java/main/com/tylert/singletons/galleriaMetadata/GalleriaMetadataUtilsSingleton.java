package com.tylert.singletons.galleriaMetadata;

public class GalleriaMetadataUtilsSingleton extends GalleriaMetadataEventsUtils
{
    private static GalleriaMetadataUtilsSingleton instance = null;

    private GalleriaMetadataUtilsSingleton() {
    }

    public static synchronized GalleriaMetadataUtilsSingleton getInstance() {
        if(instance == null) {
            instance = new GalleriaMetadataUtilsSingleton();
         }
         return instance;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }
    
}
