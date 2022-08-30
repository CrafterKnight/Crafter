/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks.autoreply;

import com.google.gson.JsonObject;

import net.wurstclient.util.json.JsonException;
import net.wurstclient.util.json.WsonObject;

public class AutoReplyChallenge
{
	private final String question;
	private final String answer;
	private final int extraDelay;
	
	public AutoReplyChallenge(String question, String answer, int extraDelay)
	{
		this.question = question;
		this.answer = answer;
		this.extraDelay = extraDelay;
	}
	
	public boolean matches(String question)
	{
		return question.contains(this.question);
	}
	
	public String getQuestion()
	{
		return question;
	}
	
	public String getAnswer()
	{
		return answer;
	}
	
	public int getExtraDelay()
	{
		return extraDelay;
	}
	
	public JsonObject toJson()
	{
		JsonObject json = new JsonObject();
		
		json.addProperty("q", question);
		json.addProperty("a", answer);
		
		if(extraDelay != 0)
			json.addProperty("extraDelay", extraDelay);
		
		return json;
	}
	
	public static AutoReplyChallenge fromJson(WsonObject json)
		throws JsonException
	{
		try
		{
			String q = json.getString("q");
			String a = json.getString("a");
			int extraDelay =
				json.has("extraDelay") ? json.getInt("extraDelay") : 0;
			return new AutoReplyChallenge(q, a, extraDelay);
			
		}catch(JsonException e)
		{
			throw new JsonException("Invalid AutoReplyChallenge: " + json, e);
		}
	}
	
	public static AutoReplyChallenge tryFromJson(WsonObject json)
	{
		try
		{
			return fromJson(json);
			
		}catch(JsonException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
