# Zone Commands
This is a spigot plugin that can teleport players and execute commands when players enter and exit zones.

## Configuration
```yaml
zones:
  zone-1:
    type: zone # type is either zone or boundary.
    world: world # name of the world this zone is effective in.
    from: # first corner of a rectangular area.
      x: -10
      y: 0
      z: -10
    to: # first corner of a rectangular area.
      x: 10
      y: 255
      z: 10
    enter: # event handler used when a player enters this zone.
      command: 'say {player} entered zone-1' # A string or list of strings, which will be executed as commands.
    exit: # event handler used when a player exits this zone.
      command: 'say {player} left zone-1'
  boundary-1: # boundaries define a general condition instead of a specific rectangular area.
    type: boundary
    world: world
    condition: '{player.y} < 0'
    enter:
      tp: # A teleport command can also be defined to move players somewhere else, even across dimensions.
        world: world_nether
        location:
          x: '{player.x} / 8'
          y: 128
          z: '{player.z} / 8'
        motion:
          y: 0
```
### Value expansion
In the above configuration, there are a few expressions that are surrounded by `{}`. These expressions are later expanded to their corresponding values.
- `{player}` == player name
- `{player.x}` == player x location
- `{player.y}` == player y location
- `{player.z}` == player z location
### commands
All commands are run through value expansion before being run. This allows one to use commands on the player that triggered the zone command.
### condition, location and motion
After being run through the value expansion, condition, location, and motion are run through a solver. The solver currently supports the following symbols: `^, *, /, +, -, >, >=, <, <=, ==, !=, &&, ||`.