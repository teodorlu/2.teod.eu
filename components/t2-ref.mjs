// <t2-ref d="26">link text</t2-ref>
// <t2-ref remote="jack-rusher/homesteading">link text</t2-ref>
//
// Renders a citation as a link. `d` is looked up in the generated document
// index, `remote` in the references table below. The element's text becomes
// the link text; omit it to use the text from the index.

import { d } from "/js/index.mjs";

const references = {
  "jack-rusher/homesteading": {
    href: "https://jackrusher.com/journal/homesteading.html",
    text: "Homesteading",
  },
  "geepaw-hill/many-more-much-smaller-steps": {
    href: "https://www.geepawhill.org/series/many-more-much-smaller-steps/",
    text: "Many More Much Smaller Steps",
  },
  "parenteser/demoscener": {
    href: "https://parenteser.mattilsynet.io/demoscener/",
    text: "Demoscener",
  },
};

export default class T2Ref extends HTMLElement {
  connectedCallback() {
    if (this.shadowRoot) return;

    const entry = d[this.getAttribute("d")] ?? references[this.getAttribute("remote")];

    const element = document.createElement(entry ? "a" : "span");
    if (entry) element.href = entry.href;
    element.textContent = this.textContent.trim() || entry?.text || "";

    this.attachShadow({ mode: "open" }).append(element);
  }
}
