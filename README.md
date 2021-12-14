# Minecraft Strongholds Locator
For Minecraft Java Edition Versions 1.9 and later assuming there are no major changes to Stronghold generation.
This is NOT a Minecraft Speedrun Stronghold location calculator, unless the speedrun is All Portals 1.9+

## What It Does
- This program will take the coordinates of a single stronghold found by the player and give apporximated coordinates for every other stronghold in that ring. 
- This is mostly useful for servers where access to /seed is not allowed, so other resources like [Chunkbase](https://www.chunkbase.com/apps/stronghold-finder) would not work.
- Since the program does not read from the game files and simply does calculations, it could technically be used in speedruns where /seed is not allowed

## Stronghold Generation Quick Summary
- Strongholds in Minecraft Java Edition generate in 8 rings with the origin (0,0) as the center point.
- These rings have (in order from closest to farthest from the origin) 3, 6, 10, 15, 21, 28, 36, and 9 Strongholds for a total of 128 Strongholds.
- All Strongholds will generate at a random distance within the bounds of each ring, with each ring having a thickness of about 1536 blocks.
- Each Stronghold in a ring will generate at an about equal angle apart when measuring from the origin.

For more information and details about Stronghold Generation visit [The Minecraft Wiki page on Strongholds](https://minecraft.fandom.com/wiki/Stronghold).

![Minecraft Stronghold Generation rings with the distances from the origin and number of strongholds in each ring](https://static.wikia.nocookie.net/minecraft_gamepedia/images/9/9d/Strongholds_1.9.png/revision/latest/scale-to-width-down/400?cb=20210212055544)

## How It Works
The program uses the X and Z coordinates input by the player of the location of a Stronghold and performs simple Trigonometric calculations to determine the approximate location of the other Strongholds in the same ring as the given Stronghold. The user can display these coordinates in Nether Coordinates in order to travel to the calculated location faster. A summary of the process is outlined below.

1. The user provides the program with the X and Z coordinates of a Stronghold that has already been located
2. The user decides whether to display the Overworld coordinates or Nether coordinates and presses the calculate button
3. The program uses the Pythagorean theorem to determine the distance the Stronghold is from the origin and therefore what ring it is in
4. The program then uses the ring number to determine the number of Strongholds in the ring and therefore the angle between them
5. The program calculates the mean distance from the origin Strongholds can generate in the ring and uses it as the hypotenuse of a right triangle to then do more calculations
6. Using Trig properties for right triangles (SOHCAHTOA) The program calculates the apporximate coordiantes of the other Strongholds in the ring

The user can remove coordinates from the list as they find Strongholds and reset the UI for their next calculation.

## Notes
- If a Stronghold tries to generate in certain places such as an Ocean, Swamp, or River, it may "snap" to a different biome it is allowed to generate in if it is close enough. This can have 2 effects:
  - The Stronghold position might slightly outside one of the rings
  - The angle used to calculate might be incorrect and lead to the distance between the Stronghold position and calculated position being farther than normal
- If the given Stronghold coordinates are calculated to be outside of a ring, the program will ask the user if they want to enter the ring manually, have the program guess which ring it is in, or try to enter the coordinates again in case a mistake was made.
- Currently there is no method to prevent calculation errors if the angle is calculated wrong due to the Stronghold snapping to a different location. In rare cases this can lead to two calculated coordinate positions having the Eye of Ender point to the same Stronghold.

Please report any issues / bugs to me on discord at Trashpanda#6664 and I will try to fix them in a timely manner but cannot promise a deadline if it is during a school semeseter.
