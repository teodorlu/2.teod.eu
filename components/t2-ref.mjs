// <t2-ref remote="jack-rusher/homesteading">link text</t2-ref>
//
// Renders a citation as a link. The href is looked up in the references table
// below by remote; the element's text becomes the link text.

const references = {
  "jack-rusher/homesteading": "https://jackrusher.com/journal/homesteading.html",
  "geepaw-hill/many-more-much-smaller-steps": "https://www.geepawhill.org/series/many-more-much-smaller-steps/",
  "parenteser/demoscener": "https://parenteser.mattilsynet.io/demoscener/"
};

export default class T2Ref extends HTMLElement {
  connectedCallback() {
    if (this.shadowRoot) return;

    const href = references[this.getAttribute("remote")];

    const element = document.createElement(href ? "a" : "span");
    if (href) element.href = href;
    element.textContent = this.textContent;

    this.attachShadow({ mode: "open" }).append(element);
  }
}
