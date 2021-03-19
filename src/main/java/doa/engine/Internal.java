package doa.engine;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates an internal component of DoaEngine exposed to public due to
 * language limitations(i.e. lack of an internal keyword or friend classes).
 * Types, fields, constructors and methods annotated with Internal annotation
 * should not be considered part of the public API, they should be left alone
 * and not be tinkered with. Only play with things annotated with Internal
 * annotation if you certainly know what you are doing.
 * <p>
 * <strong> YOU HAVE BEEN WARNED!</strong>
 *
 * @author Doga Oruc
 * @since DoaEngine 2.7
 * @version 3.0
 */
@Documented
@Retention(RUNTIME)
@Target({ TYPE, FIELD, METHOD, CONSTRUCTOR })
public @interface Internal {}
