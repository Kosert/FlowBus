# FlowBus

[![](https://jitpack.io/v/Kosert/FlowBus.svg)](https://jitpack.io/#Kosert/FlowBus)

FlowBus is a kotlin event bus implementation.

- **Powered by Kotlin Coroutines and Flows**
- **Android MainThread-aware**
- **Fully operable from Java code**

## Example

### Subscribing

Most common Android use case will consist of subscribing in your Activity/Fragment `onStart` method and unsubscribing in `onStop`.
You can also use `bindLifecycle` extension to automatically unsubscribe when lifecycle reaches STOPPED state.

```kotlin
class SomeActivity : AppCompatActivity() {

    private val receiver = EventsReceiver()

    override fun onStart() {
        super.onStart()
        receiver
            .bindLifecycle(this)
            .subscribe { event: MyEvent ->
                // handle new event
            }
    }
```

### Posting

Instance of any class can posted as an event.

```kotlin
GlobalBus.post(MyEvent())
```
For more examples [see example app](https://github.com/Kosert/FlowBus/tree/master/app)  
For detailed documentation [check the wiki](https://github.com/Kosert/FlowBus/wiki).

## Include in your project

FlowBus is available on Jitpack.  
Add jitpack repo in your project root `build.gradle`:

```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

In your module's `build.gradle`:

```gradle
dependencies {
    // core package:
    implementation 'com.github.Kosert.FlowBus:FlowBus:1.1'
    
    // android extensions:
    implementation 'com.github.Kosert.FlowBus:FlowBus-android:1.1'
}
```

## License

```
Copyright 2021 Robert Kosakowski

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
