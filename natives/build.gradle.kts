plugins {
    id("fr.stardustenterprises.rust.wrapper") version "3.2.5"
}

rust {
    release.set(true)
    command.set("cargo")
    targets {
        this += defaultTarget()
    }
}
