### RxUi

Minimal implementation of [the concept described here](http://artemzin.com/blog/rxui-talking-to-android-view-layer-in-a-reactive-way/).

#####RxUi [solves 3 main problems of communication](http://a.com) between Presenters/ViewModels/etc and View layer in Android applications:

1. `Main Thread` should be part of View layer, Presenters/ViewModels/etc should not know about it.
2. Action posted to `Main Thread` should be part of `Subscription` so you could `unsubscribe()` it.
3. Backpressure occurred on the View layer should be detected and handled on Presenter/ViewModel/etc layer above.

---

&nbsp;
###Sample Apps

Check [Sample app written in Java](rxui-sample-java/src/main/java/com/artemzin/rxui/sample/java) and [Sample app written in Kotlin](rxui-sample-kotlin/src/main/kotlin/com/artemzin/rxui/sample/kotlin). Usages of RxUi are so tiny that you may not even see it at the first sight!

---

Basically, RxUi is just two main functions:

1. `bind(Observable<T>): Subscription`
2. `ui(Action1<T>): Func1<Observable<T>, Subscription>`

And concept of `Observable <-> Observable` in the View layer when View only produces `Observable`s and consumes `Observable`s.

```java
interface SignIn {
  // Produces.
  Observable<String> login();
  Observable<String> password();
  Observable<Void>   signInClicks();
  
  // Consumes.
  Func1<Observable<Void>,    Subscription> signInEnable();
  Func1<Observable<Void>,    Subscription> signInDisable(); 
  Func1<Observable<Void>,    Subscription> signInSuccess();
  Func1<Observable<Failure>, Subscription> signInFailure();
}
```

#####RxUi

Only two functions at the moment: `RxUi.bind()` (use it in Presenters/ViewModels) and `RxUi.ui()` use it in `View` layer.

```groovy
compile 'com.artemzin.rxui:rxui:1.0.0'
```
&nbsp;
#####RxUi Test

Only one function at the moment: `TestRxUi.testUi()`, basically same as `RxUi.ui()` except that it's synchronous and does not know about Main Thread.

```groovy
testCompile 'com.artemzin.rxui:rxui-test:1.0.0'
```

&nbsp;
#####RxUi Kotlin

Only one extension function at the moment: `Observable.bind()`, absolutely same as `RxUi.bind()` but easier to use in Kotlin.

```groovy
compile 'com.artemzin.rxui:rxui-kotlin:1.0.0'
```
