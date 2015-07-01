package com.anovelmous.app;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
final class Modules {
    static Object[] list(AnovelmousApp app) {
        return new Object[] {
            new AnovelmousModule(app, app),
            new DebugAnovelmousModule()
        };
    }

    private Modules() {

    }
}
