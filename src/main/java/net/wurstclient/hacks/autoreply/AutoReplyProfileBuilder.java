/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks.autoreply;

import java.nio.file.Path;
import java.util.ArrayList;

public class AutoReplyProfileBuilder
{
	private final ArrayList<AutoReplyChallenge> tmpList = new ArrayList<>();
	
	public AutoReplyProfile build(Path path)
	{
		return new AutoReplyProfile(path, tmpList);
	}
	
	public void save(Path path)
	{
		new AutoReplyProfile(path, tmpList).save();
	}
	
	public void add(AutoReplyChallenge challenge)
	{
		tmpList.add(challenge);
	}
	
	public void add(String q, String a, int extraDelay)
	{
		add(new AutoReplyChallenge(q, a, extraDelay));
	}
	
	public void add(String q, String a)
	{
		add(q, a, 0);
	}
}
