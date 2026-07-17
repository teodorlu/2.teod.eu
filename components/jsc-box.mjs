export default class JscBox extends HTMLElement {
  static observedAttributes = ["data-color"];

  connectedCallback() {
    this.render();
  }

  attributeChangedCallback() {
    this.render();
  }

  render() {
    const color = this.dataset.color || "teal";
    this.style.backgroundColor = color;
  }
}
