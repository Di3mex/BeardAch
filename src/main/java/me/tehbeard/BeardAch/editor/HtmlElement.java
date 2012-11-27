package me.tehbeard.BeardAch.editor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Describes a HTML element for use in form generation
 * Follows the format of
 * [label] : [input[type] class=[name] value=[value]]
 * in generated HTML
 * @author james
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface HtmlElement {
    String label();
    String name();
    HtmlType type();
    String[] value() default "";

    public enum HtmlType{
        TEXTBOX,
        SELECT,
        CHECKBOX,
    }
}
