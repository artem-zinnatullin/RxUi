### RxUi: Observable <-> Observable

Minimal implementation of [the concept of talking to Android View layer in a Reactive way described here](https://artemzin.com/blog/rxui-talking-to-android-view-layer-in-a-reactive-way/).

##### RxUi [solves 3 main problems of communication](https://artemzin.com/blog/rxui-talking-to-android-view-layer-in-a-reactive-way/) between Presenters/ViewModels/etc and View layer in Android applications:

1. `Main Thread` should be part of View layer, Presenters/ViewModels/etc should not know about it.
2. Action posted to `Main Thread` should be part of `Disposable` so you could `dispose()` it and prevent execution.
3. Backpressure occurred on the View layer should be detected and handled on Presenter/ViewModel/etc layer above.

---

### Sample Apps

Check [Sample app written in Java](rxui-sample-java/src/main/java/com/artemzin/rxui/sample/java) and [Sample app written in Kotlin](rxui-sample-kotlin/src/main/kotlin/com/artemzin/rxui/sample/kotlin). 

>RxUi is so tiny that you may not even see it in the code at the first sight!

---

##### Basically, RxUi is just two main functions:

1. `bind(Observable<T>): Disposable`
2. `ui(Consumer<T>): Function<Observable<T>, Disposable>`

And concept of `Observable <-> Observable` in the View layer when View only produces `Observable`s and consumes `Observable`s.

```java
interface SignInView {
  // Produces.
  Observable<String> login();
  Observable<String> password();
  Observable<Object> signInClicks();
  
  // Consumes.
  Function<Observable<Boolean>,      Disposable> signInEnable();
  Function<Observable<SignInResult>, Disposable> signInResult();
}
```

#### Download

##### RxUi

Only two functions at the moment: `RxUi.bind()` (use it in Presenters/ViewModels) and `RxUi.ui()` use it in `View` layer.

```groovy
compile 'com.artemzin.rxui2:rxui:2.0.0'
```
&nbsp;
##### RxUi Test

Only one function at the moment: `TestRxUi.testUi()`, basically same as `RxUi.ui()` except that it's synchronous and does not know about Main Thread.

```groovy
testCompile 'com.artemzin.rxui2:rxui-test:2.0.0'
```

&nbsp;
##### RxUi Kotlin

Only one extension function at the moment: `Observable.bind()`, absolutely same as `RxUi.bind()` but easier to use in Kotlin.

```groovy
compile 'com.artemzin.rxui2:rxui-kotlin:2.0.0'
```

&nbsp;

---

<font size="3">Made with :heart: by [Artem Zinnatullin](https://twitter.com/artem_zin), discussed with [Juno Android Team](https://gojuno.com) and [@FE_Hudl](https://twitter.com/FE_Hudl).</font>
