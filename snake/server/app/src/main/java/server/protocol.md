# Multiplayer

## Room
- [REQ] Client 1 requests Room
- [RES] Client 1 get Room id
- Client 1 opens room link
- Client 1 tells Client 2 Room id
- Client 2 opens room link
- [RES] Client 1 & 2 sends his name to Room


## Game Start
- Server inits game and positions of snakes and apple
- [RES] Server sends start Game and positions to Client 1 and Client 2
- Clients render them

# Game
- [RES] Every Tick Clients send input
- Server updates Game Logic
- [RES] Server sends new positions or Game Over Status

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
    "message_code" : "room-request",
    "time_stamp" : "<dd.mm.yyyy-hh.mm.ss>"
}
```

### Name 
```json
{
    "message_code" : "client-name",
    "client_name" : "<name>",
    "time_stamp" : "<dd.mm.yyyy-hh.mm.ss>"
}
```

### Input
```json
{
    "message_code" : "client-input",
    "player_number" : <1,2>,
    "input" : <Ascii Key Code>,
    "time_stamp" : "<dd.mm.yyyy-hh.mm.ss>"
}
```

### Game Stop
```json
{
    "message_code" : "game-stop",
    "client_name" : "<name>",
    "time_stamp" : "<dd.mm.yyyy-hh.mm.ss>"
}
```

## Server Messages
### Room id
```json
{
    "message_code" : "room-id",
    "room_id" : "<room-id>",
    "time_stamp" : "<dd.mm.yyyy-hh.mm.ss>"
}
```

### Game Start
```json
{
    "message_code" : "game-start",
    "player_number" : <1,2>,
    "time_stamp" : "<dd.mm.yyyy-hh.mm.ss>"
}
```

### Position
```json
{
    "message_code" : "position-update",
    "player1_length" : <length>,
    "player2_length" : <length>,
    "player1_snake" : [
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
    "player2_snake" [
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
    "apple" :
        {
            "x" : <x coordinate of apple>,
            "y" : <y coordinate of apple>,
        }
    ,
    "time_stamp" : "<dd.mm.yyyy-hh.mm.ss>"
}
```

### Game Over
```JSON
{
    "message_code" : "game-over",
    "winner_exists" : <true/false>,
    "winner_name" : "playerName",
    "time_stamp" : "<dd.mm.yyyy-hh.mm.ss>"
}
```