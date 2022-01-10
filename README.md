# Android coroutine tests with 1.6.0 library

## Tracker bug

https://github.com/Kotlin/kotlinx.coroutines/issues/3120

## Issue

The given sample provide a standard case with a ViewModel that emit a state, and having one action. A test is checking that we receive state
in the right order and way. We are using TestCoroutineDispatcher which has been deprecated.

With the coroutine lib 1.5.2, the test is work perfectly. With 1.6.0, the result is return directly the last emitted value.

You can reproduce by launching the test of TestCustomViewModel class.
Try to change build.gradle coroutine_version to 1.5.2 and the test will pass.

## Cause

TestCoroutineDispatcher seems to have changed with 1.6.0 release, as it doesn't behave the same as with 1.5.2. The other issue is that I
haven't find a simple way to use the last test API as it result in the same or other issue. (timeout, wrong expected result).