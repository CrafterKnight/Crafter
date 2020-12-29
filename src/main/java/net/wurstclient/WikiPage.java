/*
 * Copyright (c) 2014-2020 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient;

import java.util.ArrayList;
import java.util.Collection;

import net.wurstclient.command.Command;
import net.wurstclient.hack.Hack;
import net.wurstclient.hacks.KillauraHack;
import net.wurstclient.keybinds.Keybind;
import net.wurstclient.keybinds.KeybindList;
import net.wurstclient.settings.CheckboxSetting;
import net.wurstclient.settings.EnumSetting;
import net.wurstclient.settings.Setting;
import net.wurstclient.settings.SliderSetting;

public final class WikiPage
{
	private static final ArrayList<Keybind> defaultKeybinds =
		new ArrayList<>(KeybindList.DEFAULT_KEYBINDS);
	
	private final Feature feature;
	private String text = "";
	
	public WikiPage(Feature feature)
	{
		this.feature = feature;
		
		addHeading();
		addHackTable();
		addSettings();
		addChanges();
	}
	
	public static void main(String[] args)
	{
		WikiPage wikiPage = new WikiPage(new KillauraHack());
		System.out.println(wikiPage.getText());
	}
	
	private void addHeading()
	{
		text += "====== " + feature.getName() + " ======\n\n";
	}
	
	private void addHackTable()
	{
		String name = feature.getName();
		String picName =
			(name.startsWith(".") ? name.substring(1) : name).toLowerCase();
		String type = feature instanceof Hack ? "Hack"
			: feature instanceof Command ? "Command" : "Other Feature";
		String category = feature.getCategory() == null ? "No Category|none"
			: feature.getCategory().getName();
		String description = convertDescription(feature.getDescription());
		String keybind = getDefaultKeybind();
		
		text += "<WRAP 516px>\n";
		text += "^  " + name + "  ^^\n";
		text += "|{{ " + picName + ".webp?500 |}}||\n";
		text += "^Type|[[" + type + "]]|\n";
		text += "^Category|[[" + category + "]]|\n";
		text += "^In-game description|" + description + "|\n";
		text +=
			"^[[keybinds#default_keybinds|Default keybind]]|" + keybind + "|\n";
		text += "</WRAP>\n\n";
		
		String type2 = feature instanceof Hack ? "Minecraft hack"
			: feature instanceof Command ? "[[command|chat command]]"
				: "Wurst feature";
		text += name + " is a " + type2 + " that... FIXME\n\n";
	}
	
	private String getDefaultKeybind()
	{
		String name = feature.getName().toLowerCase().replace(" ", "_");
		if(name.startsWith("."))
			name = name.substring(1);
		
		for(Keybind keybind : defaultKeybinds)
			if(keybind.getCommands().toLowerCase().contains(name))
				return keybind.getKey().replace("key.keyboard.", "")
					.toUpperCase();
			
		return "none";
	}
	
	private void addSettings()
	{
		Collection<Setting> settings = feature.getSettings().values();
		if(settings.isEmpty())
			return;
		
		text += "===== Settings =====\n\n";
		
		for(Setting setting : settings)
			addSetting(setting);
	}
	
	private void addSetting(Setting setting)
	{
		text += "==== " + setting.getName() + " ====\n";
		text += "^  " + setting.getName() + "  ^^\n";
		
		String type = setting instanceof CheckboxSetting ? "Checkbox"
			: setting instanceof SliderSetting ? "Slider"
				: setting instanceof EnumSetting ? "Enum" : "FIXME";
		
		text += "^Type|" + type + "|\n";
		
		String description = convertDescription(setting.getDescription());
		text += "^In-game description|" + description + "|\n";
		
		if(setting instanceof CheckboxSetting)
		{
			CheckboxSetting checkbox = (CheckboxSetting)setting;
			
			String defaultValue =
				checkbox.isCheckedByDefault() ? "checked" : "not checked";
			text += "^Default value|" + defaultValue + "|\n";
			
		}else if(setting instanceof SliderSetting)
		{
			SliderSetting slider = (SliderSetting)setting;
			
			text += "^Default value|" + slider.getDefaultValue() + "|\n";
			text += "^Minimum|" + slider.getMinimum() + "|\n";
			text += "^Maximum|" + slider.getMaximum() + "|\n";
			text += "^Increment|" + slider.getIncrement() + "|\n";
			
		}else if(setting instanceof EnumSetting)
		{
			EnumSetting<?> enumSetting = (EnumSetting<?>)setting;
			
			text +=
				"^Default value|" + enumSetting.getDefaultSelected() + "|\n";
			
			Enum<?>[] enumValues = enumSetting.getValues();
			String values = enumValues[0].toString();
			for(int i = 1; i < enumValues.length; i++)
				values += ", " + enumValues[i];
			
			text += "^Possible values|" + values + "|\n";
		}
		
		if(description.equals("(none)"))
			text += "\nFIXME Describe here what \"" + setting.getName()
				+ "\" does, since it has no in-game description.\n";
		
		text += "\n";
	}
	
	private String convertDescription(String input)
	{
		if(input.isEmpty())
			return "(none)";
		
		return "\"" + input.replace("\n", "\\\\ ")
			.replaceAll("\u00a7l([\\w \\-_]+)\u00a7r", "**$1**") + "\"";
	}
	
	private void addChanges()
	{
		text += "===== Changes =====\n\n";
		
		String name = feature.getName();
		
		text += "^Version^Changes^\n";
		text += "|[[update:Wurst 1.2.3]] FIXME|Added " + name + ".|\n";
		text += "|[[update:Wurst 2.3.4]] FIXME|Changed " + name + ".|\n";
		text += "|[[update:Wurst 3.4.5]] FIXME|Removed " + name + ".|\n";
	}
	
	public String getText()
	{
		return text;
	}
}
