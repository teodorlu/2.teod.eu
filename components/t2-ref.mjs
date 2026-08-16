// <t2-ref d="26">link text</t2-ref>
// <t2-ref remote="jack-rusher/homesteading">link text</t2-ref>
//
// Renders a citation as a link. `d` is looked up in the generated document
// index, `remote` in the references table below. The element's text becomes
// the link text; omit it to use the text from the index. Text from the index
// gets " [Youtube]" appended for a `youtube/...` remote.

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
  "youtube/i-want-to-know-what-love-is": {
    href: "https://www.youtube.com/watch?v=r3Pr1_v7hsw",
    text: "I Want to Know What Love Is",
  },
  "youtube/the-show-must-go-on": {
    href: "https://www.youtube.com/watch?v=t99KH0TR-J4",
    text: "The Show Must Go On",
  },
  "youtube/dream-on": {
    href: "https://www.youtube.com/watch?v=89dGC8de0CA",
    text: "Dream On",
  },
};

export default class T2Ref extends HTMLElement {
  connectedCallback() {
    if (this.shadowRoot) return;

    const entry = d[this.getAttribute("d")] ?? references[this.getAttribute("remote")];
    const suffix = (this.getAttribute("remote") ?? "").startsWith("youtube/") ? " [Youtube]" : "";

    const element = document.createElement(entry ? "a" : "span");
    if (entry) element.href = entry.href;
    element.textContent = this.textContent.trim() || (entry ? entry.text + suffix : "");

    this.attachShadow({ mode: "open" }).append(element);
  }
}
