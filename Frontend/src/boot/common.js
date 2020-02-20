import Vue from "vue";

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
    if (month == 0 || month == 1 || month == 2 || month == 3) {
      return "WINTER";
    } else if (month == 4 || month == 5 || month == 6 || month == 7) {
      return "SUMMER";
    } else if (month == 8 || month == 9 || month == 10 || month == 11) {
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
  }
};

Vue.prototype.$common = common;
