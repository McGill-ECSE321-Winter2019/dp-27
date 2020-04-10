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
  getCoopStatusColor: function(status) {
    if (status === "UNDER_REVIEW") return "orange";
    if (status === "IN_PROGRESS") return "blue";
    if (status === "FUTURE") return "light-blue";
    if (status === "INCOMPLETE") return "primary";
    else return "black";
  },
  /*
   * Used for sorting coop objects by term
   */
  compareCoopTerms: function(coop1, coop2) {
    const term1 = `${coop1.courseOffering.season} ${coop1.courseOffering.year}`;
    const term2 = `${coop2.courseOffering.season} ${coop2.courseOffering.year}`;

    if (term1 === term2) return 0;
    else {
      const term1Split = term1.split(" ");
      const term2Split = term2.split(" ");

      if (parseInt(term1Split[1]) > parseInt(term2Split[1])) return 1;
      else if (parseInt(term1Split[1]) < parseInt(term2Split[1])) return -1;
      else {
        // years are the same, compare by season
        if (
          (term1Split[0] === "WINTER" &&
            (term2Split[0] === "SUMMER" || term2Split[0] === "FALL")) ||
          (term1Split[0] === "SUMMER" && term2Split[0] === "FALL")
        ) {
          return -1;
        } else {
          return 1;
        }
      }
    }
  },
  /*
   * Used for sorting notifications by timestamp. Returns most recent
   * notifications first.
   */
  compareNotificationTimestamps: function(notif1, notif2) {
    return notif2.timeStamp - notif1.timeStamp;
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
