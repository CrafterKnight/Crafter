# Wurst Wiki helper

Automatically generates a [Wurst Wiki](https://wiki.wurstclient.net/) page for each feature in the current Wurst version.

These pages can be used as a **starting point** to create Wurst Wiki pages more quickly, but should never be copy-pasted into the Wurst Wiki without **manual fixes**.

## Instructions

1. Set up Wurst as you normally would (see the [README file](https://github.com/Wurst-Imperium/Wurst7/blob/master/README.md) in "master" for how to do that).

2. Run Wurst and wait until you see the Minecraft title screen.

3. Done! Your generated wiki pages are in the "wurst/wiki" folder. By default, you'll find it at:  
`(where you saved this repo)/run/wurst/wiki`

## Limitations

- This isn't some kind of advanced AI that writes entire wiki articles automatically. It just fills out the data tables and adds some placeholders.

- All entries are based entirely on the current version. You should always check to see if something is different in Wurst 6 or ForgeWurst.

- The picture in the hack table is added by simply guessing the filename and hoping that it exists.

- Only checkboxes, sliders and enum settings will have all the data filled out automatically. For other types of settings, only the name and in-game description are added automatically.

- The "Changes" section is just a placeholder, for now.

## Things to add

**Disclaimer:** I'm not promising to add any of these things. They are just ideas that I think would be feasible and useful. Feel free to make a PR if you want to add any of this.

- Try to generate at least a rough draft of the changes section by parsing the [old changelogs](https://github.com/Wurst-Imperium/WurstClient.net/tree/gh-pages/_updates). This probably wouldn't be very accurate, but would still save a lot of time vs. finding all the changes manually.

- Auto-generate data for all types of settings. Currently missing:
  - BlockSetting
  - BlockListSetting
  - FileSetting
  - ItemListSetting
