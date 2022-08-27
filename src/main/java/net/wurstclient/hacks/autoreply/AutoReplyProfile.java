/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks.autoreply;

import java.util.Collections;
import java.util.List;

public class AutoReplyProfile
{
	private final List<AutoReplyChallenge> challenges;
	
	public AutoReplyProfile(List<AutoReplyChallenge> challenges)
	{
		this.challenges = Collections.unmodifiableList(challenges);
	}
	
	public AutoReplyChallenge findChallenge(String question)
	{
		return challenges.parallelStream().filter(c -> c.matches(question))
			.findAny().orElse(null);
	}
}
