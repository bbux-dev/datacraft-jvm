package org.datacraft.annotations

/**
 * Annotation to specify the name for a caster.
 *
 * @property value the name of the caster
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CastName(val value: String)
