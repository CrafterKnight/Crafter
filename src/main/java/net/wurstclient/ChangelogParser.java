/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public enum ChangelogParser
{
	;
	
	private static final TreeMap<String, List<String>> changelogs =
		new TreeMap<>();
	
	private static final Pattern versionRegex =
		Pattern.compile("wurst-version: \"(.+)\"");
	
	public static void parseFolder(Path folder)
	{
		try(Stream<Path> stream = Files.walk(folder))
		{
			stream.filter(Files::isRegularFile)
				.forEach(path -> parseFile(path));
			
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private static void parseFile(Path path)
	{
		List<String> lines;
		
		try
		{
			lines = Files.readAllLines(path);
			
		}catch(IOException e)
		{
			new IOException("Coudln't read " + path, e).printStackTrace();
			return;
		}
		
		String version = null;
		int endOfFrontmatter = 0;
		
		// read metadata
		for(int i = 1; i < lines.size(); i++)
		{
			String line = lines.get(i);
			Matcher matcher = versionRegex.matcher(line);
			
			if(version == null && matcher.find())
			{
				version = matcher.group(1);
				continue;
			}
			
			if(line.equals("---"))
			{
				endOfFrontmatter = i;
				break;
			}
		}
		
		if(version == null)
			throw new IllegalStateException(
				"Couldn't parse version of " + path);
		
		if(endOfFrontmatter == 0)
			throw new IllegalStateException(
				"Couldn't parse frontmatter of " + path);
		
		List<String> changes = Collections.unmodifiableList(
			lines.subList(endOfFrontmatter + 1, lines.size()));
		
		changelogs.put(version, changes);
	}
	
	public static Map<String, List<String>> getChangelogs()
	{
		return Collections.unmodifiableMap(changelogs);
	}
}
