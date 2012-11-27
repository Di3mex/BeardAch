package me.tehbeard.BeardAch.editor.elements;

import me.tehbeard.BeardAch.editor.HtmlElement;
import me.tehbeard.BeardAch.editor.HtmlElement.HtmlType;

public class SelectElement implements ElementMaker {

	public HtmlType getType() {
		return HtmlType.SELECT;
	} 

	public String makeHtml(HtmlElement annotation) {
		
		String o = "<span class=\"form-label\">" + annotation.label() + "</span> : " +
				"<select class=\"form-select " + annotation.name() + "\" >";
		for(String option : annotation.value()){
			String[] a = option.split("|");
			String v = a[0];
			String n = a.length > 1 ? a[1]  : a[0];
			o += "<option value=\"" + v + "\">" + n + "</option>";
		}
		o += "</select>";
		return o;
	} 

}
