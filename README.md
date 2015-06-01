# Anovelmous - Android

A quick and resilient client application for Anovelmous.

Realizes Functional Reactive Programming principles with the RxJava library.

## Development

Please adhere to the clear separation of concerns laid out in the 2010 [Google I/O Android REST client talk](https://www.youtube.com/watch?v=xHXn3Kg2IQE#t=2787)

The structure is based on the architecture shown in the [U+2020](https://github.com/JakeWharton/u2020)
sample application from [Square](http://square.github.io).

This structure allows for a convenient Debug Drawer that can,
during runtime, restart the application with mock/stage/production endpoints and allow for excellent instrumentation testing.
The dependency injection pattern also allows for ease in testing with mock objects without affecting data on the server.

## Technologies
* [Retrofit](http://square.github.io/retrofit/) - A REST client API defined with a Java Interface.
* [RxJava](https://github.com/ReactiveX/RxJava) - A reactive programming library that plays nicely with Retrofit.
* [Dagger](http://square.github.io/dagger/) - Lightweight, annotation processing dependency injection
* [Realm](http://realm.io) - A mobile threaded, transactional database with ORM support