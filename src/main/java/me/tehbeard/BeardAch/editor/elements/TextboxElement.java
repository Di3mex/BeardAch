package me.tehbeard.BeardAch.editor.elements;

import me.tehbeard.BeardAch.editor.HtmlElement;
import me.tehbeard.BeardAch.editor.HtmlElement.HtmlType;

public class TextboxElement implements ElementMaker {

	

	public String makeHtml(HtmlElement annotation) {
		
		return "<span class=\"form-label\">" + annotation.label() + "</span> : " +
				"<input type=\"text\" class=\"form-textbox " + annotation.name() + "\" value=\"" + annotation.value()[0] + "\"/><br />";
	}

	public HtmlType getType() {
		return HtmlType.TEXTBOX;
	} 

}
