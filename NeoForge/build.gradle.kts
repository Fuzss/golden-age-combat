plugins {
    id("fuzs.multiloader.multiloader-convention-plugins-neoforge")
}

dependencies {
    modApi(sharedLibs.puzzleslib.neoforge)
}

multiloader {
    mixins {
        mixin("ResourceManagerRegistryLoadTaskNeoForgeMixin")
    }
}
