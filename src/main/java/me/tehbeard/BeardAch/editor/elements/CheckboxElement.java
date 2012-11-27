package me.tehbeard.BeardAch.editor.elements;

import me.tehbeard.BeardAch.editor.HtmlElement;
import me.tehbeard.BeardAch.editor.HtmlElement.HtmlType;

public class CheckboxElement implements ElementMaker {

	public HtmlType getType() {
		return HtmlType.CHECKBOX;
	} 

	public String makeHtml(HtmlElement annotation) {
		
		return "<span class=\"form-label\">" + annotation.label() + "</span> : " +
				"<input type=\"checkbox\" class=\"form-checkbox " + annotation.name() + "\" value=\"" + annotation.value()[0] + "\" " + (annotation.value().length > 1 ? "checked=\"yes\"" : "") + "/><br />";
	} 

}
