class ServerRoomIdMessage {
    constructor() {
      this.messageCode = '';
      this.roomId = '';
      this.timeStamp = null; // Use JavaScript Date object instead of LocalDateTime
    }
  
    toJson() {
      const json = {
        messageCode: this.messageCode,
        roomId: this.roomId,
        timeStamp: this.timeStamp ? TimeFormatter.formatDateTime(this.timeStamp) : null,
      };
      return JSON.stringify(json);
    }
  
    static fromJson(json) {
      const obj = JSON.parse(json);
      const message = new ServerRoomIdMessage();
      message.messageCode = obj.messageCode;
      message.roomId = obj.roomId;
      message.timeStamp = obj.timeStamp ? TimeFormatter.parseDateTime(obj.timeStamp) : null;
      return message;
    }
}