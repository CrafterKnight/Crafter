/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks;

import net.wurstclient.Category;
import net.wurstclient.SearchTags;
import net.wurstclient.events.ChatInputListener;
import net.wurstclient.hack.Hack;
import net.wurstclient.settings.CheckboxSetting;
import net.wurstclient.settings.SliderSetting;
import net.wurstclient.settings.SliderSetting.ValueDisplay;
import net.wurstclient.util.ChatUtils;

@SearchTags({"auto reply", "Auto reply", "auto Reply", "Auto Reply",
	"autoreply", "Autoreply", "autoReply", "AutoReply"})
public final class AutoReplyHack extends Hack implements ChatInputListener
{
	private final CheckboxSetting dontAnswerIfWon =
		new CheckboxSetting("Don't answer if someone else won", false);
	
	private final SliderSetting minDelay = new SliderSetting("Min delay", 2200,
		0, 6000, 100, ValueDisplay.INTEGER.withSuffix("ms"));
	
	private final SliderSetting maxDelay = new SliderSetting("Max delay", 2200,
		100, 6000, 100, ValueDisplay.INTEGER.withSuffix("ms"));
	
	private Thread answerThread;
	
	public AutoReplyHack()
	{
		super("AutoReply");
		setCategory(Category.CHAT);
		
		addSetting(dontAnswerIfWon);
		addSetting(minDelay);
		addSetting(maxDelay);
	}
	
	@Override
	public void onEnable()
	{
		EVENTS.add(ChatInputListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		EVENTS.remove(ChatInputListener.class, this);
		
		if(answerThread != null)
			answerThread.interrupt();
	}
	
	@Override
	public void onReceivedMessage(ChatInputEvent event)
	{
		answer(event);
		check(event);
	}
	
	private void check(ChatInputEvent event)
	{
		String incomingMsg = event.getComponent().getString();
		
		if(incomingMsg.contains("[WitchChat]") && incomingMsg.contains("Won in")
			&& incomingMsg.contains("seconds!") && dontAnswerIfWon.isChecked())
		{
			ChatUtils.message(
				"\u00a74AutoReply cancelled because someone already won.");
			
			if(answerThread != null)
				answerThread.interrupt();
		}
	}
	
	private void answer(ChatInputEvent event)
	{
		String incomingMsg = event.getComponent().getString();
		
		if(incomingMsg.contains("[WitchChat] Type this sjfgjsoidsoiadoia"))
			say("sjfgjsoidsoiadoia", 3000);
		if(incomingMsg.contains("[WitchChat] Type this asndbfq"))
			say("asndbfq", 800);
		if(incomingMsg.contains("[WitchChat] School"))
			say("Shooter", 0);
		if(incomingMsg.contains("[WitchChat] We live in a..."))
			say("society", 0);
		if(incomingMsg.contains("[WitchChat] You are..."))
			say("black.", 0);
		if(incomingMsg
			.contains("[WitchChat] How many dungeons the server has?"))
			say("11", -600);
		if(incomingMsg.contains("[WitchChat] Write the haha number"))
			say("69", -600);
		if(incomingMsg.contains(
			"[WitchChat] What can you wear to stop an Enderman attacking you?"))
			say("pumpkin", 0);
		if(incomingMsg.contains("[WitchChat] LMFAO is working with?"))
			say("lmao", 0);
		if(incomingMsg.contains(
			"[WitchChat] What ore can you build complicated machines with?"))
			say("Redstone", 0);
		if(incomingMsg.contains("[WitchChat] Popular internet phrase"))
			say("simp", 0);
		if(incomingMsg.contains(
			"[WitchChat] What name can u give a mob to turn it upside down?"))
			say("grumm", 0);
		if(incomingMsg.contains("[WitchChat] Name one of the famous dere type"))
			say("yandere", 0);
		if(incomingMsg.contains("[WitchChat] Wingardium ..."))
			say("leviosa", 0);
		if(incomingMsg.contains("[WitchChat] What game is minecraft's enemy?"))
			say("Fortnite", 0);
		if(incomingMsg.contains("[WitchChat] Never gonna..."))
			say("Give you up.", 1000);
		if(incomingMsg.contains("[WitchChat] Allahu ..."))
			say("akbar", 0);
		if(incomingMsg.contains("[WitchChat] step-brother help me im..."))
			say("stuck", 0);
		if(incomingMsg.contains("[WitchChat] Praise ..."))
			say("The sun.", 500);
		if(incomingMsg.contains("[WitchChat] Life"))
			say("death.", 0);
		if(incomingMsg.contains("[WitchChat] Say..."))
			say("cheese", 0);
		if(incomingMsg.contains("[WitchChat] A witch has a..."))
			say("cat", 0);
		if(incomingMsg.contains("[WitchChat] What is love?"))
			say("Baby dont hurt me.", 1600);
		if(incomingMsg.contains("[WitchChat] What are Creepers scared of?"))
			say("cats", 0);
		if(incomingMsg.contains("[WitchChat] Chicken"))
			say("attack.", 0);
		if(incomingMsg.contains("[WitchChat] Bible"))
			say("black.", 0);
		if(incomingMsg.contains("[WitchChat] Akio ..."))
			say("bum", -400);
		if(incomingMsg.contains("[WitchChat] League of Legends is ..."))
			say("cancer", 0);
		if(incomingMsg.contains("[WitchChat] What is the name of the bear?"))
			say("winnie.", 0);
		if(incomingMsg.contains("[WitchChat] How tall is Steve?"))
			say("2 blocks.", 0);
		if(incomingMsg
			.contains("[WitchChat] What goes hand in hand with anime?"))
			say("hentai.", 0);
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
