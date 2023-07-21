class ServerPositionUpdateMessage {
    constructor() {
      this.messageCode = '';
      this.player1Length = 0;
      this.player2Length = 0;
      this.player1Snake = null;
      this.player2Snake = null;
      this.apple = null;
      this.timeStamp = null; // Use JavaScript Date object instead of LocalDateTime
    }
  
    toJson() {
      const json = {
        messageCode: this.messageCode,
        player1Length: this.player1Length,
        player2Length: this.player2Length,
        player1Snake: this.player1Snake ? this.player1Snake.toJson() : null,
        player2Snake: this.player2Snake ? this.player2Snake.toJson() : null,
        apple: this.apple ? this.apple.toJson() : null,
        timeStamp: this.timeStamp ? TimeFormatter.formatDateTime(this.timeStamp) : null,
      };
      return JSON.stringify(json);
    }
  
    static fromJson(json) {
      const obj = JSON.parse(json);
      const message = new ServerPositionUpdateMessage();
      message.messageCode = obj.messageCode;
      message.player1Length = obj.player1Length;
      message.player2Length = obj.player2Length;
      message.player1Snake = obj.player1Snake ? Snake.fromJson(JSON.stringify(obj.player1Snake)) : null;
      message.player2Snake = obj.player2Snake ? Snake.fromJson(JSON.stringify(obj.player2Snake)) : null;
      message.apple = obj.apple ? Apple.fromJson(JSON.stringify(obj.apple)) : null;
      message.timeStamp = obj.timeStamp ? TimeFormatter.parseDateTime(obj.timeStamp) : null;
      return message;
    }
}