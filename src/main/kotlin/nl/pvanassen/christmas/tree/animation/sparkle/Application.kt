package nl.pvanassen.christmas.tree.animation.sparkle

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("nl.pvanassen.christmas.tree.animation.sparkle.animation",
                        "nl.pvanassen.christmas.tree.animation.sparkle.configuration",
                        "nl.pvanassen.christmas.tree.animation.sparkle.model",
                        "nl.pvanassen.christmas.tree.animation.sparkle.controller")
                .mainClass(Application.javaClass)
                .start()
    }
}