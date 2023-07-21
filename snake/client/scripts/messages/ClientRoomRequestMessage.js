class ClientRoomRequestMessage {
    constructor() {
      this.messageCode = '';
      this.timeStamp = null; // Use JavaScript Date object instead of LocalDateTime
    }

    
  
    toJson() {
      const json = {
        messageCode: this.messageCode,
        timeStamp: this.timeStamp ? TimeFormatter.formatDateTime(this.timeStamp) : null,
      };
      return JSON.stringify(json);
    }
  
    static fromJson(json) {
      const obj = JSON.parse(json);
      const message = new ClientRoomRequestMessage();
      message.messageCode = obj.messageCode;
      message.timeStamp = obj.timeStamp ? TimeFormatter.parseDateTime(obj.timeStamp) : null;
      return message;
    }
}