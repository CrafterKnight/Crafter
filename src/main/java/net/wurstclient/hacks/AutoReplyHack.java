/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks;

import java.io.IOException;
import java.nio.file.Path;

import net.wurstclient.Category;
import net.wurstclient.SearchTags;
import net.wurstclient.events.ChatInputListener;
import net.wurstclient.hack.Hack;
import net.wurstclient.hacks.autoreply.AutoReplyChallenge;
import net.wurstclient.hacks.autoreply.AutoReplyProfile;
import net.wurstclient.hacks.autoreply.WitchChatProfileBuilder;
import net.wurstclient.settings.CheckboxSetting;
import net.wurstclient.settings.FileSetting;
import net.wurstclient.settings.SliderSetting;
import net.wurstclient.settings.SliderSetting.ValueDisplay;
import net.wurstclient.util.ChatUtils;
import net.wurstclient.util.json.JsonException;

@SearchTags({"auto reply"})
public final class AutoReplyHack extends Hack implements ChatInputListener
{
	private final FileSetting profileSetting = new FileSetting("Profile",
		"Tunes AutoReply for a specific server. Each profile contains a list of known questions and answers, as well as rules for detecting which messages belong to the minigame.",
		"autoreply", folder -> new WitchChatProfileBuilder()
			.save(folder.resolve("witchchat.json")));
	
	private final CheckboxSetting dontAnswerIfWon =
		new CheckboxSetting("Don't answer if someone else won", false);
	
	private final SliderSetting minDelay = new SliderSetting("Min delay", 2200,
		0, 6000, 100, ValueDisplay.INTEGER.withSuffix("ms"));
	
	private final SliderSetting maxDelay = new SliderSetting("Max delay", 2200,
		100, 6000, 100, ValueDisplay.INTEGER.withSuffix("ms"));
	
	private AutoReplyProfile profile;
	private Thread answerThread;
	
	public AutoReplyHack()
	{
		super("AutoReply");
		setCategory(Category.CHAT);
		
		addSetting(profileSetting);
		addSetting(dontAnswerIfWon);
		addSetting(minDelay);
		addSetting(maxDelay);
	}
	
	@Override
	public void onEnable()
	{
		if(profile == null || !profileSetting.isSelected(profile.getPath()))
		{
			Path path = profileSetting.getSelectedFile();
			
			try
			{
				profile = AutoReplyProfile.load(path);
				profile.save();
				
			}catch(IOException | JsonException e)
			{
				Path fileName = path.getFileName();
				ChatUtils.error("Couldn't load profile: " + fileName);
				
				String simpleClassName = e.getClass().getSimpleName();
				String message = e.getMessage();
				ChatUtils.message(simpleClassName + ": " + message);
				
				e.printStackTrace();
				setEnabled(false);
				return;
			}
		}
		
		EVENTS.add(ChatInputListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		EVENTS.remove(ChatInputListener.class, this);
		
		if(answerThread != null && answerThread.isAlive())
		{
			answerThread.interrupt();
			ChatUtils.message(
				"\u00a74Answer cancelled because AutoReply is no longer enabled.");
		}
		
		profile = null;
	}
	
	@Override
	public void onReceivedMessage(ChatInputEvent event)
	{
		String incomingMsg = event.getComponent().getString();
		
		answer(incomingMsg);
		check(incomingMsg);
	}
	
	private void check(String incomingMsg)
	{
		// TODO: Move won message detection to profile
		if(incomingMsg.contains("[WitchChat]") && incomingMsg.contains("Won in")
			&& incomingMsg.contains("seconds!") && dontAnswerIfWon.isChecked())
		{
			if(answerThread == null)
				return;
			
			answerThread.interrupt();
			ChatUtils.message(
				"\u00a74Answer cancelled because someone already won.");
		}
	}
	
	private void answer(String incomingMsg)
	{
		AutoReplyChallenge challenge = profile.findChallenge(incomingMsg);
		if(challenge == null)
			return;
		
		say(challenge.getAnswer(), challenge.getExtraDelay());
	}
	
	private void say(String message, int extraDelay)
	{
		int max = maxDelay.getValueI();
		int min = minDelay.getValueI();
		int randomDelay = (int)(Math.random() * (max - min) + min);
		int finalDelay = extraDelay + randomDelay;
		
		if(extraDelay != 0)
		{
			String delayExplanation =
				randomDelay + (extraDelay >= 0 ? "+" : "") + extraDelay;
			ChatUtils.message("\u00a7bAnswer will be sent in " + finalDelay
				+ "ms. (" + delayExplanation + ")");
		}else
			ChatUtils.message(
				"\u00a7b Answer will be sent in " + randomDelay + "ms.");
		
		answerThread = new Thread(() -> {
			try
			{
				Thread.sleep(finalDelay);
				MC.player.sendChatMessage(message, null);
				
			}catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		});
		answerThread.start();
	}
}
