class Apple {
    x;
    y;

    constructor(x, y) {
        this.x = x;
        this.y = y;
    }

    toJson() {
        const json = {
          x: this.x,
          y: this.y,
        };
        return JSON.stringify(json);
      }
    
      static fromJson(json) {
        const obj = JSON.parse(json);
        const apple = new Apple(obj.x, obj.y);
        return apple;
      }

    get getX() {
        return this.x;
    }

    get getY() {
        return this.y;
    }

    draw() {
        ctx.fillStyle = "red";
        ctx.beginPath();
        ctx.arc(this.x + 12.5, this.y + 12.5, 10, 0, Math.PI * 2);
        ctx.fill();
    }

    detectCollision(x, y) {
        return this.x == x && this.y == y;
    }
}

function spawnApple(segments) {
    let allSpawnLocations = [];
    let possibleSpawnLocations = [];

    for (i = 0; i < 28; ++i) {
        for (j = 0; j < 40; ++j) {
            allSpawnLocations.push([j * 25, i * 25]);
        }
    }

    possibleSpawnLocations = allSpawnLocations.filter((e) => {
        possible = true;
        for (i = 0; i < segments.length; ++i) {
            segment = segments[i];
            if (segment.getX == e[0] && segment.getY == e[1]) {
                possible = false;
            }
        }
        return possible;
    });

    r = Math.floor(Math.random() * possibleSpawnLocations.length);

    return possibleSpawnLocations[r];
}