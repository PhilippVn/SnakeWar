class ServerGameOverMessage {
    constructor() {
      this.messageCode = '';
      this.winnerExists = false;
      this.winnerName = '';
      this.timeStamp = null; // Use JavaScript Date object instead of LocalDateTime
    }
  
    toJson() {
      const json = {
        messageCode: this.messageCode,
        winnerExists: this.winnerExists,
        winnerName: this.winnerName,
        timeStamp: this.timeStamp ? TimeFormatter.formatDateTime(this.timeStamp) : null,
      };
      return JSON.stringify(json);
    }
  
    static fromJson(json) {
      const obj = JSON.parse(json);
      const message = new ServerGameOverMessage();
      message.messageCode = obj.messageCode;
      message.winnerExists = obj.winnerExists;
      message.winnerName = obj.winnerName;
      message.timeStamp = obj.timeStamp ? TimeFormatter.parseDateTime(obj.timeStamp) : null;
      return message;
    }
}