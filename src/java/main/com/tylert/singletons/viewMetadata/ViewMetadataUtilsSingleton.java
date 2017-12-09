package com.tylert.singletons.viewMetadata;

public class ViewMetadataUtilsSingleton extends ViewMetadataEventsUtils
{
    private static ViewMetadataUtilsSingleton instance = null;

    private ViewMetadataUtilsSingleton() {
    }

    public static synchronized ViewMetadataUtilsSingleton getInstance() {
        if(instance == null) {
            instance = new ViewMetadataUtilsSingleton();
         }
         return instance;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }
    
}
