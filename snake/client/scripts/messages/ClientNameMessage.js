class ClientNameMessage {
    constructor() {
      this.messageCode = '';
      this.clientName = '';
      this.timeStamp = null; // Use JavaScript Date object instead of LocalDateTime
    }
  
    toJson() {
      const json = {
        messageCode: this.messageCode,
        clientName: this.clientName,
        timeStamp: this.timeStamp ? TimeFormatter.formatDateTime(this.timeStamp) : null,
      };
      return JSON.stringify(json);
    }
  
    static fromJson(json) {
      const obj = JSON.parse(json);
      const message = new ClientNameMessage();
      message.messageCode = obj.messageCode;
      message.clientName = obj.clientName;
      message.timeStamp = obj.timeStamp ? TimeFormatter.parseDateTime(obj.timeStamp) : null;
      return message;
    }
}