// <t2-ref author="..." slug="...">link text</t2-ref>
//
// Renders a citation as a link. The href is looked up in the references table
// below by author and slug; the element's text becomes the link text.

const references = {
  "jack-rusher": {
    "homesteading": "https://jackrusher.com/journal/homesteading.html",
  },
  "geepaw-hill": {
    "many-more-much-smaller-steps":
      "https://www.geepawhill.org/series/many-more-much-smaller-steps/",
  },
};

export default class T2Ref extends HTMLElement {
  connectedCallback() {
    if (this.shadowRoot) return;

    const author = this.getAttribute("author");
    const slug = this.getAttribute("slug");
    const href = references[author]?.[slug];

    const element = document.createElement(href ? "a" : "span");
    if (href) element.href = href;
    element.textContent = this.textContent;

    this.attachShadow({ mode: "open" }).append(element);
  }
}
