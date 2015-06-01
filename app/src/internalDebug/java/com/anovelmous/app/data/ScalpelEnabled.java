package com.anovelmous.app.data;


import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
/**
 * Created by Greg Ziegan on 5/31/15.
 */
@Qualifier @Retention(RUNTIME)
public @interface ScalpelEnabled {
}
