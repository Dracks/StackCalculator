# StackCalculator

An RPN (stack) calculator for Android.

## Building

Prerequisites:

- Android SDK with platform `android-36` and build-tools `36.0.0` installed.
- JDK 17+ (set `JAVA_HOME` before building).

Setup:

1. Copy `local.properties.example` to `local.properties` and set `sdk.dir` to your Android SDK location.

Build:

```sh
export JAVA_HOME=/path/to/jdk-17
./gradlew assembleDebug
```

The debug APK is produced at `app/build/outputs/apk/debug/app-debug.apk`.
