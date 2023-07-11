# Multiplayer

## Room
- [REQ] Client 1 requests Room
- [RES] Client 1 get Room id
- Client 1 is added to the room
- Client 1 tells Client 2 Room id
- [REQ] Client 2 joins and is added to the room

## Game Start
- Server inits game and positions of snakes and apple
- [RES] Server sends start Game and positions to Client 1 and Client 2
- Clients render them

# Game
- [REQ] Every Tick Server requests the input of the Clients
- Server updates Game Logic
- [RES] Server sends new positions, Game Over Status

# Game Stop
- If a Client stops connection, or presses stop Game
- [REQ] No information next Tick or information to stop Game
- [RES] Server sends Game Over to (remaining) Clients


# Message Structure
JSON
## Client Messages
### Room Request
```json
{
    "message-code" : "room-request",
    "client-name" : "<name>",
    "time-stamp" : "<dd.mm.yyyy-hh.mm.ss>"
}
```

### Room join
```json
{
    "message-code" : "room-join",
    "client-name" : "<name>",
    "room-id" : <room-id>,
    "time-stamp" : "<dd.mm.yyyy-hh.mm.ss>"
}
```

### Input
```json
{
    "message-code" : "client-input",
    "client-name" : "<name>",
    "room-id" : <room-id>,
    "input" : <Ascii Key Code>,
    "time-stamp" : "<dd.mm.yyyy-hh.mm.ss>"
}
```

### Game Stop
```json
{
    "message-code" : "game-stop",
    "client-name" : "<name>",
    "room-id" : <room-id>,
    "time-stamp" : "<dd.mm.yyyy-hh.mm.ss>"
}
```

## Server Messages
### Room id
```json
{
    "message-code" : "room-id",
    "room-id" : <room-id>,
    "time-stamp" : "<dd.mm.yyyy-hh.mm.ss>"
}
```

### Game Start
```json
{
    "message-code" : "game-start",
    "time-stamp" : "<dd.mm.yyyy-hh.mm.ss>"
}
```

### Position & Game Status Update
```json
{
    "message-code" : "position-update",
    "<client1-name>" : [
        <length>,
        {
            "isHead" : false,
            "x" : <x coordinate of segment>,
            "y" : <y coordinate of segment>
        }
        // Same for all tail segments
        {
            "isHead" : true,
            "x" : <x coordinate of segment>,
            "y" : <y coordinate of segment>
        }
    ],
    "<client2-name>" [
        <length>,
        {
            "isHead" : false,
            "x" : <x coordinate of segment>,
            "y" : <y coordinate of segment>
        }
        // Same for all tail segments
        {
            "isHead" : true,
            "x" : <x coordinate of segment>,
            "y" : <y coordinate of segment>
        }
    ],
    "apple" : [
        {
            "x" : <x coordinate of apple>,
            "y" : <y coordinate of apple>,
        }
    ],
    "game-over" : <true/false>
    "time-stamp" : "<dd.mm.yyyy-hh.mm.ss>"
}
```