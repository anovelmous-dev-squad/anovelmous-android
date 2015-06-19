package com.anovelmous.app;

import dagger.ObjectGraph;

/**
 * Created by Greg Ziegan on 6/19/15.
 */
public interface Injector {
    /**
     * Gets the object graph for this component.
     * @return the object graph
     */
    ObjectGraph getObjectGraph();

    /**
     * Injects a target object using this component's object graph.
     * @param target the target object
     */
    void inject(Object target);
}
