package arden.tests.specification.testcompiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Builder for generating arden code from a template.
 */
public class ArdenCodeBuilder {
	private static String template = readTemplate("Template.mlm");
	private StringBuilder resultBuilder;

	private static String readTemplate(String filename) {
		InputStream stream = ArdenCodeBuilder.class.getResourceAsStream(filename);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
		StringBuilder stringBuilder = new StringBuilder();

		try {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line).append('\n');
			}
		} catch (IOException e) {
			throw new ExceptionInInitializerError("Could not open template file (" + filename + ")");
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				throw new ExceptionInInitializerError("Could not close template file reader");
			}
		}

		return stringBuilder.toString();
	}

	public ArdenCodeBuilder() {
		resultBuilder = new StringBuilder(template);
	}
	
	public ArdenCodeBuilder(String template) {
		resultBuilder = new StringBuilder(template);
	}
	
	
	private void checkSlotIndex(int index, String slotname) {
		if (index == -1) {
			throw new IllegalArgumentException("The \"" + slotname + "\" slot could not be found in the template");
		}
	}
	
	public ArdenCodeBuilder removeSlot(String slotname) {
		int slotIndex = resultBuilder.indexOf(slotname.toLowerCase());
		checkSlotIndex(slotIndex, slotname);
		
		int endIndex = resultBuilder.indexOf(";;", slotIndex);
		checkSlotIndex(endIndex, slotname);
		endIndex += ";;".length();

		resultBuilder.delete(slotIndex, endIndex);
		return this;
	}
	
	public ArdenCodeBuilder renameSlot(String slotname, String newname) {
		int slotIndex = resultBuilder.indexOf(slotname.toLowerCase());
		checkSlotIndex(slotIndex, slotname);
		
		int endIndex = slotIndex + slotname.length();
		
		resultBuilder.replace(slotIndex, endIndex, newname);
		return this;
	}
	
	/**
	 * @param slotname
	 *            e.g. <code>"title:"</code>
	 * @param slotcontent
	 *            e.g. <code>"My Test MLM"</code>
	 */
	private ArdenCodeBuilder insertSlotContent(String slotname, String content, boolean append) {
		int slotIndex = resultBuilder.indexOf(slotname.toLowerCase());
		checkSlotIndex(slotIndex, slotname);
		int startOfContent = slotIndex + slotname.length();
		
		int endOfContent = resultBuilder.indexOf(";;", startOfContent);
		checkSlotIndex(endOfContent, slotname);

		String paddedContent = " " + content + " "; // to prevent illegal ";;;"
		if(append) {
			resultBuilder.insert(endOfContent, paddedContent).toString();
		} else {
			resultBuilder.replace(startOfContent, endOfContent, paddedContent).toString();
		}
		
		return this;
	}
	
	public ArdenCodeBuilder replaceSlotContent(String slotname, String content) {
		return insertSlotContent(slotname, content, false);
	}
	
	public ArdenCodeBuilder appendSlotContent(String slotname, String content) {
		return insertSlotContent(slotname, content, true);
	}
	
	public ArdenCodeBuilder clearSlotContent(String slotname) {
		return insertSlotContent(slotname, "", false);
	}
	
	public ArdenCodeBuilder addData(String dataCode) {
		return appendSlotContent("data:", dataCode);
	}
	
	public ArdenCodeBuilder addLogic(String actionCode) {
		return appendSlotContent("logic:", actionCode);
	}
	
	public ArdenCodeBuilder addEvoke(String actionCode) {
		return appendSlotContent("evoke:", actionCode);
	}

	public ArdenCodeBuilder addAction(String actionCode) {
		return appendSlotContent("action:", actionCode);
	}

	public ArdenCodeBuilder addExpression(String expression) {
		return addData("return_expression__ := " + expression + ";").addAction("RETURN return_expression__;");
	}
	
	public ArdenCodeBuilder addMlm(String mlmCode) {
		resultBuilder.append('\n');
		resultBuilder.append(mlmCode);
		return this;
	}
	
	@Override
	public String toString() {
		return resultBuilder.toString();
	}
	
}
