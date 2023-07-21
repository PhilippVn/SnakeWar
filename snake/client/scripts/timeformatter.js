class TimeFormatter {

    static formatDateTime(dateTime) {
        const day = this.pad(dateTime.getDate());
        const month = this.pad(dateTime.getMonth() + 1);
        const year = dateTime.getFullYear();
        const hours = this.pad(dateTime.getHours());
        const minutes = this.pad(dateTime.getMinutes());
        const seconds = this.pad(dateTime.getSeconds());
        return `${day}.${month}.${year}-${hours}.${minutes}.${seconds}`;
    }

    static parseDateTime(dateTimeString) {
        const [datePart, timePart] = dateTimeString.split('-');
        const [day, month, year] = datePart.split('.');
        const [hours, minutes, seconds] = timePart.split('.');
        return new Date(year, month - 1, day, hours, minutes, seconds);
    }

    static pad(num) {
        return num.toString().padStart(2, '0');
    }
}