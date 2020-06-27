# Mobslayer v2018
**THIS PROJECT IS OUTDATED AND ABANDONED**

(the coding is horrible, please use with caution)

## Structure
MobSlayer consists of five different spigot plugins performing specific tasks explained below.

#### MobSlayerCore
MobSlayerCore is the most basic plugin which includes user loading, scoreboard management, etc.
This plugin is required by MobSlayerGame, MobSlayerSetup and MobSlayerLobby.

#### MobSlayerGame
MobSlayerGame is responsible for all the game logic like spawning custom mobs, wave management, map loading, etc.
This plugin is required by MobSlayerSetup.

#### MobSlayerLobby
MobSlayerLobby has been of little use. There's some kind of approach of server sign implemention but I'm not sure if it's working.

#### MobSlayerSetup
MobSlayerSetup is being used as an interface to create new maps and set locations. The implementation is horrible.

#### MobSlayerBungee
MobSlayerBungee is managing all the proxy stuff needed (especially alpha access).

## Usage
What you'll need:
- Java 8
- Spigot (1.8.9)
- BungeeCord
- MySQL Database
- ProtocolLib

All plugins were coded in eclipse, I guess it is the easiest way to import them into eclipse and just export them. Sadly no maven or gradle.

The database credentials of MobSlayerBungee need to be hardcoded. All the other plugins do use configs.

Some files are encoded in windows-1252, others in UTF-8. Good luck.