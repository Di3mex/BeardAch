package me.tehbeard.BeardAch.editor.elements;

import me.tehbeard.BeardAch.editor.HtmlElement;

/**
 * Used to customize how a html form element is created
 * @author james
 *
 */
public interface ElementMaker {
	
	public HtmlElement.HtmlType getType();
	/**
	 * Parses a &#64;HtmlElement annotation into a html string 
	 * @param annotation
	 * @return
	 */
	public String makeHtml(HtmlElement annotation);
}
