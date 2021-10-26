## kpm

``kpm`` (Kotlin Project Manager) is a lightweight tool for managing and building Kotlin projects.

### What is kpm?

Essentially, ``kpm`` is going to be a lightweight alternative to Gradle for simple Kotlin projects.

### Why should I use kpm?

- **Gradle is absolutely massive**, and a lot of the fancy gradle features either get in the way, or are not used by
  small projects. Yes ``kpm`` does use Gradle as a build system, but that is because there is no other alternatives at
  the moment. as ``kpm`` matures, we will switch to using ``kpm``.
- As for Maven, well... **Do you like working with XML?** If you say yes, you're lying to yourself.

### How do I use it?

Right now, ``kpm`` is in early development. While it does *somewhat* work, your mileage may vary when it comes to
projects that are more complex than a simple hello world.

- **1. Create your ``kpm.kts`` file:**
    ```kotlin
    project {
        name = "hello-world"
    }
    ```

- **2. Create your source files**:

  src/Hello.kt
    ```kotlin
    fun main() {
        println("Hello, world!")
    }
    ```

- **3. Build your project:**
    ```
    cbyrne@Conors-MacBook-Air % kpm build
    [kpm] Evaluating project from kpm.kts...
    [kpm] Evaluated project hello-world!
    [kpm] Building...
    [kpm] Build successful! (./kpm/package/hello-world.jar)
    ```

### Notes

- KPM is not affiliated with JetBrains or Kotlin in any way
