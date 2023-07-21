class ServerGameStartMessage {
    constructor() {
      this.messageCode = '';
      this.playerNumber = 0;
      this.timeStamp = null; // Use JavaScript Date object instead of LocalDateTime
    }
  
    toJson() {
      const json = {
        messageCode: this.messageCode,
        playerNumber: this.playerNumber,
        timeStamp: this.timeStamp ? TimeFormatter.formatDateTime(this.timeStamp) : null,
      };
      return JSON.stringify(json);
    }
  
    static fromJson(json) {
      const obj = JSON.parse(json);
      const message = new ServerGameStartMessage();
      message.messageCode = obj.messageCode;
      message.playerNumber = obj.playerNumber;
      message.timeStamp = obj.timeStamp ? TimeFormatter.parseDateTime(obj.timeStamp) : null;
      return message;
    }
}  