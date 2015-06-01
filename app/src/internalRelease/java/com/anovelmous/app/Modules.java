package com.anovelmous.app;

final class Modules {
    static Object[] list(AnovelmousApp app) {
        return new Object[] {
                new AnovelmousModule(app),
                new InternalReleaseAnovelmousModules()
        };
    }

    private Modules() {

    }
}