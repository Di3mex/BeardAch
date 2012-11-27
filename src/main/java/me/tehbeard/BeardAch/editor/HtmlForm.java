package me.tehbeard.BeardAch.editor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a html form for a class
 * @author james
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HtmlForm {
String[] config() default {};
HtmlElement[] elements() default {};
}
