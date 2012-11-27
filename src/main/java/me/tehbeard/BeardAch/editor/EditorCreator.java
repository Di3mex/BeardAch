package me.tehbeard.BeardAch.editor;

import java.util.ArrayList;
import java.util.List;

import me.tehbeard.BeardAch.editor.elements.*;

/**
 * 
 * @author james
 *
 */
public class EditorCreator {

	private ElementMaker[] makers = {new TextboxElement(),new SelectElement(),new CheckboxElement()};
	
	private List<String> parsedElements = new ArrayList<String>();
	
	public EditorCreator(){
		
	}
	
	public void addElement(HtmlForm form){
		throw new UnsupportedOperationException();
	}
	
}
