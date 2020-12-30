![Arch](logo.png)
Arch is a small and lightweight library that helps to architecture Android Applications, it is based on several concepts of the functional paradigm and  Spotify's Mobius library but instead of using RxJava, it uses **Coroutines, SharedFlow and StateFlow**

This library is built upon the Android's ViewModel class and takes full advantage of it


## Download
```groovy
implementation 'com.fededri.arch:arch:1.0.0-alpha01'
```

## ArchViewModel
It is an abstract class extending from *ViewModel* and contains the main logic of this library,

## State
Handling state properly in every application is critical, with **Arch** you must define a custom State data class for each of your ViewModels, this state will be **inmmutable**

## Actions
With **Arch** the only way of changing anything in the app is by dispatching an **Action**, usually actions will be dispatched by user interactions, but not always.

## Updater
Updater is an interface with just one **pure function**, it receives an action, creates a new state from the previous one and returns a `Next` object. 
`Next` is a sealed class with types:
- `Next.State`
- `Next.StateWithSideEffects`
- `Next.StateWithSideEffectsAndEvents`
- `Next.StateWithEvents`

Depending on what type of `Next` object the ***Updater** returns in addition to changing the state, you can dispatch **SideEffects** and **Events** 

## SideEffects
They are usually blocking or long running operations like database transactions or network requests. When a SideEffect is dispatched, a new coroutine is created in order to execute it. By default the coroutine will be cancelled if the ViewModel is cleared, but you can pass in a custom `CoroutineScope` if you want to keep alive the coroutine even after the ViewModel is cleared, my recommendation -if that is the case - is to create a new `CoroutineScope` inside your `Application` class and pass it to the SideEffect's constructor

 
## Events
Usually they represent something that you want to notify to your view layer like popups, animations and transitions. If you have a single-activity architecture with multiple fragments, you can observe the same events from several fragments.

In your ViewModel's constructor, you can pass into the parameters an ``EventsConfiguration``, it is not mandatory but passing this parameter allows you to configure yours events.
- `Replays`: the number of events replayed to new subscribers, default is zero
- `ExtraBufferCapacity`: the number of values buffered in addition to `replay`, events under the hood are backed by a `SharedFlow` so if you emit an event and there is no space remaining in your buffer then the collector will be suspended
- `OnBufferOverflow` you can configure what action to take in case of buffer overflow, the default behavior is suspending


