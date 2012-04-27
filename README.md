zdungeon
========

Zombie Dungeon

A game set in a dungeon full of winding corridors leading to open spaces, completely infested with zombies!
Your only weapon is a gun that shoots mathematical symbols, fueled by mana.

The aim is to have an RPG style game, with the player and zombies each having Strength, Defense, etc ratings, which feed
into the random number generator responsible for deciding the combat rolls. (TODO)

So far, the dungeon generator works,  but needs extending. It currently creates a few rooms of random size and position 
(non overlapping) with a fixed player starting zone in the top left. Then a few tunnellers set off to link the rooms
via corridors. Zombie spawners are sprinkled liberally through the rooms, and spawn zombies up to a maximum of 128.

In the first few rooms, there might not be many zombies, but as time goes on, the limit stays set and the number of 
zombie filled rooms reduces, meaning LOTS of zombies in the latter rooms.

Items are currently consumed instantly, but this should be changed to be stored in an 'inventory' and used at will.



Bugs:

The pathfinding code struggles when a 1 block thick wall comes between you and a zombie. The zombie seems to think 
it's actually IN the wall, and so just runs straight at it. It's probably some subtle coordinate bug I've missed.

The Game Over screen doesn't interrupt the 'Game' thread properly. I should probably learn to use threads better...