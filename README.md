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

Right now, ``kpm`` is in early development, so it is not actually usable at the moment. As the project progresses, I will update this README with more information.

However, if you are interested, here's an outline of how ``kpm`` will be used:

- **1. Create your ``kpm.kts`` file:**
    ```kotlin
    project {
        name = "hello-world"
    }
    ```

- **2. Initialize ``kpm`` using the CLI:**
    ```
    cbyrne@Conors-MacBook-Air % kpm
    [kpm] Loading project hello-world...
    [kpm] Initializing project hello-world...
    [kpm] Project hello-world initialized.
          Note: A .kpm directory has been created to speed up subsequent builds.
          If you make a change to your kpm.kts file, you must reinitialize your project.
    ```

- **3. Build your project (also using the CLI):**
    ```
    cbyrne@Conors-MacBook-Air % kpm build
    [kpm] hello-world loaded from cache.
    [kpm] Building project hello-world...
    [kpm] Project hello-world built. (./build/hello-world.jar)
    ```

### Notes

- KPM is not affiliated with JetBrains or Kotlin in any way
