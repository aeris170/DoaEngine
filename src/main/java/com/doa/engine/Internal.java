package com.doa.engine;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates an internal component of {@code DoaEngine} exposed to public due to language
 * limitations(lack of {@code internal} keyword). Types, fields, constructors and methods annotated
 * with {@link Internal} annotation should be left alone and not be tinkered with. Only play with
 * things annotated with {@link Internal} annotation if you certainly know what you are doing.
 *
 * @author Doga Oruc
 * @since DoaEngine 2.7
 * @version 2.7
 */
@Documented
@Retention(RUNTIME)
@Target({ TYPE, FIELD, METHOD, CONSTRUCTOR })
public @interface Internal {}
