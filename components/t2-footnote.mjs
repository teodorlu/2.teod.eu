export class T2Footnote extends HTMLElement {
  static markers = ["*", "†", "‡", "§"];

  static assignMarkers() {
    for (const [index, footnote] of [...document.querySelectorAll("t2-footnote")].entries()) {
      if (footnote.dataset.marker) continue;

      footnote.dataset.marker = T2Footnote.markers[index] || `[${index + 1}]`;
    }
  }

  connectedCallback() {
    if (this._ready) return;

    this._ready = true;
    if (!this.dataset.marker) T2Footnote.assignMarkers();
    this._detailsHTML = this.innerHTML;
    this.replaceChildren();
    this._root = this.attachShadow({ mode: "closed" });
    this._root.innerHTML = `<style>
      :host {
        cursor: pointer;
        display: inline;
      }

      t2-footnote-marker {
        font-size: 0.75em;
        line-height: 0;
        padding: 0;
        position: relative;
        text-decoration: underline;
        text-decoration-thickness: 1px;
        text-underline-offset: 0.18em;
        top: -0.45em;
      }
    </style><t2-footnote-marker>${this.marker}</t2-footnote-marker>`;

    this.addEventListener("click", () => {
      document.querySelector("t2-footnote-display").select(this.id);
    });
  }

  get detailsHTML() {
    return this._detailsHTML;
  }

  get marker() {
    return this.dataset.marker || "[?]";
  }
}

export class T2FootnoteDisplay extends HTMLElement {
  connectedCallback() {
    if (this._ready) return;

    this._ready = true;
    this._footnotes = [...document.querySelectorAll("t2-footnote")];
    this._root = this.attachShadow({ mode: "closed" });
    this.render();
    this.syncFromUrl();
  }

  render() {
    this._root.innerHTML = `<style>
      :host {
        --bar-height: 0.35rem;
        background: white;
        border-top: var(--bar-height) solid black;
        bottom: 0;
        box-sizing: border-box;
        display: none;
        font-family: monospace;
        left: 0;
        overflow: visible;
        padding: 0.5rem;
        position: fixed;
        right: 0;
      }

      :host([selected]) {
        display: block;
      }

      header {
        display: flex;
        justify-content: flex-end;
      }

      button {
        background-color: white;
        border: 1px solid black;
        cursor: pointer;
        display: grid;
        font-family: monospace;
        height: 1.5rem;
        line-height: 1;
        padding: 0.2rem 0.45rem;
        place-items: center;
        position: absolute;
        right: 0.5rem;
        top: calc(-0.5 * var(--bar-height));
        transform: translateY(-50%);
        width: 1.5rem;
        z-index: 1;
      }

      .details {
        margin-left: -0.5rem;
        max-height: min(42vh, 18rem);
        overflow: auto;
        padding-left: 0.5rem;
      }

      ol {
        margin: 0;
        padding-left: 1.5rem;
      }

      t2-footnote-details {
        display: list-item;
        padding-left: 0.25rem;
        position: relative;
      }

      t2-footnote-details + t2-footnote-details {
        margin-top: 1lh;
      }

      t2-footnote-details {
        margin-top: 0.5lh;
        margin-bottom: 0.5lh;
      }

      t2-footnote-details[selected]::before {
        background: black;
        bottom: 0;
        content: "";
        left: calc(-1.5rem - 0.5rem);
        position: absolute;
        top: 0;
        width: 2px;
      }

      t2-footnote-details::marker {
        content: attr(marker) " ";
      }
    </style>
    <header><button type="button">✕</button></header>
    <div class="details"><ol></ol></div>`;

    const list = this._root.querySelector("ol");
    for (const footnote of this._footnotes) {
      const details = document.createElement("t2-footnote-details");
      details.setAttribute("for", footnote.id);
      details.setAttribute("marker", footnote.marker);
      details.innerHTML = footnote.detailsHTML;
      list.append(details);
    }

    this._root.querySelector("button").addEventListener("click", () => this.clear());
  }

  select(id) {
    const url = new URL(location.href);
    url.searchParams.set("footnote", id);
    history.replaceState(null, "", url);
    this.syncFromUrl();
  }

  clear() {
    const url = new URL(location.href);
    url.searchParams.delete("footnote");
    history.replaceState(null, "", url);
    this.syncFromUrl();
  }

  syncFromUrl() {
    const selected = new URL(location.href).searchParams.get("footnote");
    this.toggleAttribute("selected", Boolean(selected));
    for (const details of this._root.querySelectorAll("t2-footnote-details")) {
      details.toggleAttribute("selected", details.getAttribute("for") === selected);
    }
  }
}
