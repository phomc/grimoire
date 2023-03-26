# Grimoire

A Fabric server-side mod to enhance item upgrade mechanism.

Modrinth: https://modrinth.com/mod/grimoire

## Features
- Custom enchantments
- More item upgrades
- Fully server-sided
- Gameplay balanced
- Compatible with popular server-side mods

## Wiki
Link: https://docs.phomc.dev/grimoire/intro

## Discussion
PhoMC Discord server: https://discord.gg/tbJUamaCbP

## Contribution
### Rules for custom enchantment
1. Before PR a new enchantment, ensure a corresponding issue is created for discussion
2. Keep it balanced, no OP
3. Ensure it can't be abused. Idea: reduce item's durability, define conflicts
4. No "permanent effect" enchantment! e.g: grant "speed" while wearing armour
5. Chance is preferred over "cooldown"

## Locale
### * Enchantment description placeholders
Grammar: `{format:key}` or `{key}`

- Conditional property format:
  + i: Show icon tick or cross
  + yn: Show yes or no
  + Default: true or false (not localized)
- Integer property format:
  + I: Format as roman number
  + ts: Given ticks, convert ticks to seconds
  + Default: integer
- Decimal property format (formatted up to 2 decimal places)
  + ts: Given ticks, convert ticks to seconds
  + %: Given 0.0 - 1.0, multiply by 100.0 to get percentage
  + Default: decimal
