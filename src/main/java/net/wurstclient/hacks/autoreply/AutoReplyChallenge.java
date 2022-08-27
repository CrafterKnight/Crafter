/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks.autoreply;

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
}
