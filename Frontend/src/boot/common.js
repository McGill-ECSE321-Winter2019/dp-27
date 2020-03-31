import Vue from "vue";
import moment from "moment";

/*
 * This object contains functions that are globally available to every component
 */
const common = {
  getYears: function() {
    var current = new Date().getFullYear();
    var yrs = [];
    yrs.push(current + 1);
    for (var i = 0; i < 9; i++) {
      yrs.push(current - i);
    }
    return yrs;
  },
  getCurrentYear: function() {
    return new Date().getFullYear();
  },
  getCurrentTerm: function() {
    var date = new Date();
    var month = date.getMonth();
    if (month >= 0 || month <= 3) {
      return "WINTER";
    } else if (month >= 4 || month <= 7) {
      return "SUMMER";
    } else if (month >= 8 || month <= 11) {
      term = "FALL";
    }
    return "";
  },
  /*
   * Converts a base64-encoded string to a blob; used for rendering PDFs
   *
   * Found on SO: https://stackoverflow.com/a/57767153
   */
  b64toBlob: function(content, contentType) {
    contentType = contentType || "";
    const sliceSize = 512;
    // method which converts base64 to binary
    const byteCharacters = window.atob(content);

    const byteArrays = [];
    for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
      const slice = byteCharacters.slice(offset, offset + sliceSize);
      const byteNumbers = new Array(slice.length);
      for (let i = 0; i < slice.length; i++) {
        byteNumbers[i] = slice.charCodeAt(i);
      }
      const byteArray = new Uint8Array(byteNumbers);
      byteArrays.push(byteArray);
    }

    return new Blob(byteArrays, {
      type: contentType
    });
  },
  /**
   * Converts enum text (e.g. UNDER_REVIEW) to a readable format
   * (e.g. Under Review)
   */
  convertEnumTextToReadableString: function(enumText) {
    const capitalize = s => {
      if (typeof s !== "string") return "";
      let lower = s.toLowerCase();
      return lower.charAt(0).toUpperCase() + lower.slice(1);
    };

    let words = enumText.split("_");
    let result = "";
    words.forEach(word => {
      result += capitalize(word) + " ";
    });
    return result.trim();
  },
  /**
   * Converts a SQL-formatted date (e.g. 2020-01-01) to a readable format
   * (e.g. January 1st, 2020)
   */
  convertSQLDateToReadableString: function(date) {
    return moment(date)
      .utc()
      .format("MMMM Do, YYYY");
  },
  /**
   * Converts a dollar value in cents (e.g. 2000) to a normal dollar
   * representation (e.g. $20.00)
   */
  convertCentsToDollarAmount: function(amount) {
    let dollarAmount = amount / 100.0;
    if (dollarAmount >= 0) {
      return "$" + dollarAmount.toFixed(2);
    } else {
      dollarAmount = dollarAmount * -1;
      return "-$" + dollarAmount.toFixed(2);
    }
  },
  convertTimestampLongToString: function(timestamp) {
    return moment(timestamp)
      .utc()
      .format("MMMM Do, YYYY HH:mm");
  }
};

Vue.prototype.$common = common;
