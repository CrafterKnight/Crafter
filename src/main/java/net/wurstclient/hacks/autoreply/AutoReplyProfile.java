/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks.autoreply;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.wurstclient.util.json.JsonException;
import net.wurstclient.util.json.JsonUtils;
import net.wurstclient.util.json.WsonObject;

public class AutoReplyProfile
{
	private final Path path;
	private final List<AutoReplyChallenge> challenges;
	
	public AutoReplyProfile(Path path, List<AutoReplyChallenge> challenges)
	{
		this.path = Objects.requireNonNull(path);
		this.challenges = Collections.unmodifiableList(challenges);
	}
	
	public AutoReplyChallenge findChallenge(String question)
	{
		return challenges.parallelStream().filter(c -> c.matches(question))
			.findAny().orElse(null);
	}
	
	public Path getPath()
	{
		return path;
	}
	
	public void save()
	{
		JsonObject json = toJson();
		
		try
		{
			JsonUtils.toJson(json, path);
			
		}catch(IOException | JsonException e)
		{
			System.out.println("Couldn't save " + path.getFileName());
			e.printStackTrace();
		}
	}
	
	public JsonObject toJson()
	{
		JsonObject json = new JsonObject();
		
		JsonArray jsonChallenges = new JsonArray(challenges.size());
		challenges.forEach(c -> jsonChallenges.add(c.toJson()));
		json.add("challenges", jsonChallenges);
		
		return json;
	}
	
	public static AutoReplyProfile load(Path path)
		throws IOException, JsonException
	{
		return fromJson(JsonUtils.parseFileToObject(path)).build(path);
	}
	
	public static AutoReplyProfileBuilder fromJson(WsonObject json)
		throws JsonException
	{
		AutoReplyProfileBuilder builder = new AutoReplyProfileBuilder();
		
		json.getArray("challenges").getAllObjects().stream()
			.map(AutoReplyChallenge::tryFromJson).filter(Objects::nonNull)
			.forEach(builder::add);
		
		return builder;
	}
}
