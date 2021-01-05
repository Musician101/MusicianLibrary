MusicianLibrary
===================

A group of common classes used for my various projects.

```Groovy
repositories {
    mavenCentral()
    maven {
        name = 'GitHubPackages'
        url = 'https://maven.pkg.github.com/Musician101/MusicianLibrary'
    }
}

dependencies {
    //Replace MODULE with one of the modules and VERSION with the version
    compile 'io.musician101:musicianlibrary-MODULE:VERSION'
}
```
