package io.github.fededri.arch

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}