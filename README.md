## kpm

``kpm`` (Kotlin Project Manager) is a lightweight tool for managing and building Kotlin projects.

### Why should I use kpm?

- **Gradle is absolutely massive**, and a lot of the fancy gradle features either get in the way, or are not used by
  small projects. Yes ``kpm`` does use Gradle as a build system, but that is because there is no other alternatives at
  the moment. as ``kpm`` matures, we will switch to using ``kpm``.
- As for Maven, well... **Do you like working with XML?** If you say yes, you're lying to yourself.

### How do I use it?

``kpm`` is in early development, however it is usable! If you run into any problems, please open
an [issue](https://github.com/cbyrneee/kpm/issues?q=is%3Aissue+is%3Aopen+sort%3Aupdated-desc).

- **1. Install kpm**:

  *Instructions coming soon*


- **2. Create a project**:
     ```
    cbyrne@Conors-MacBook-Air % kpm init
    What is your project's name?: my-kpm-application
    [kpm] Creating project: my-kpm-application
    [kpm] Project created! Run kpm build to build your project.
          $ cd ./my-kpm-application
          $ kpm build
    ```
  When you use the ``kpm init`` command, ``kpm`` will create your ``kpm.kts`` script automatically.

  A simple ``Main.kt`` file will also be created for you.


- **3. Build your project:**
    ```
    cbyrne@Conors-Macbook-Air % cd ./my-kpm-application
    cbyrne@Conors-MacBook-Air % kpm build
    [kpm] Evaluating project script...
    [kpm] Evaluated project my-kpm-application!
    [kpm] Resolved org.jetbrains.kotlin:kotlin-stdlib:1.5.31 -> .../kotlin-stdlib-1.5.31.jar
    [kpm] Creating package...
    [kpm] Build successful! (./package/my-kpm-application.jar)
    ```

### Notes

- KPM is not affiliated with JetBrains or Kotlin in any way
