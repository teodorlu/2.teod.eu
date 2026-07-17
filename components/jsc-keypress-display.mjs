export default class JscKeypressDisplay extends HTMLElement {
  static observedAttributes = ["data-timestamp"];

  attributeChangedCallback() {
    this.dataset.visible = "";

    clearTimeout(this.hideTimeout);
    this.hideTimeout = setTimeout(() => {
      delete this.dataset.visible;
    }, 1000);
  }
}
