package com.anovelmous.app.data;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
import java.lang.annotation.Retention;
import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier @Retention(RUNTIME)
public @interface PixelGridEnabled {
}
