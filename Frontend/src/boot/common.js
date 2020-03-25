import Vue from "vue";

/*
 * This object contains functions that are globally available to every component
 */
const common = {
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
