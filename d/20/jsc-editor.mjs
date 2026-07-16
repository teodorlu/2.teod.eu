// A structure editor for DOM nodes
//
// The JscEditor implements high-level editing commands. Private functions above
// are not part of the interface. Good commands represent user intent, good
// helper functions are clear.

function deleteBackwards(editor) {
  const cursorSlot = slot(editor, "cursor");

  if (cursorSlot === 0) {
    return;
  }

  sexps(editor)[cursorSlot - 1].remove();
  moveCursor(editor, cursorSlot - 1);
}

function deleteSelection(editor) {
  const start = Math.min(slot(editor, "cursor"), slot(editor, "mark"));

  for (const sexp of sexps(editor).filter((sexp) => "selected" in sexp.dataset)) {
    sexp.remove();
  }

  for (const sexp of sexps(editor)) {
    delete sexp.dataset.mark;
  }

  moveCursor(editor, start);
}

function hasPosition(editor, attribute) {
  return sexps(editor).some((sexp) => sexp.dataset[attribute]);
}

function moveCursor(editor, slot) {
  movePosition(editor, "cursor", slot);
  updateSelection(editor);
}

function moveMark(editor, slot) {
  movePosition(editor, "mark", slot);
  updateSelection(editor);
}

function movePosition(editor, attribute, slot) {
  const children = sexps(editor);
  const boundedSlot = Math.max(0, Math.min(slot, children.length));

  for (const child of children) {
    delete child.dataset[attribute];
  }

  if (children.length === 0) {
    return;
  }

  if (boundedSlot === children.length) {
    children[children.length - 1].dataset[attribute] = "after";
    return;
  }

  children[boundedSlot].dataset[attribute] = "before";
}

function sexps(editor) {
  return Array.from(editor.children);
}

function slot(editor, attribute) {
  for (const [index, sexp] of sexps(editor).entries()) {
    if (sexp.dataset[attribute] === "before") {
      return index;
    }

    if (sexp.dataset[attribute] === "after") {
      return index + 1;
    }
  }

  return 0;
}

function updateSelection(editor) {
  for (const sexp of sexps(editor)) {
    delete sexp.dataset.selected;
  }

  if (!hasPosition(editor, "mark")) {
    return;
  }

  const start = Math.min(slot(editor, "cursor"), slot(editor, "mark"));
  const end = Math.max(slot(editor, "cursor"), slot(editor, "mark"));

  for (const sexp of sexps(editor).slice(start, end)) {
    sexp.dataset.selected = "";
  }
}

export default class JscEditor extends HTMLElement {
  
  connectedCallback() {
    if (!this.dataset.layout) {
      this.dataset.layout = "horizontal";
    }

    if (!hasPosition(this, "cursor")) {
      moveCursor(this, 0);
    }
  }
  
  backspace() {
    if (sexps(this).some((sexp) => "selected" in sexp.dataset)) {
      deleteSelection(this);
      return;
    }

    deleteBackwards(this);
  }

  backwardSexp() {
    moveCursor(this, slot(this, "cursor") - 1);
  }

  deleteToEnd() {
    const cursorSlot = slot(this, "cursor");

    for (const sexp of sexps(this).slice(cursorSlot)) {
      sexp.remove();
    }

    moveCursor(this, cursorSlot);
  }

  forwardSexp() {
    moveCursor(this, slot(this, "cursor") + 1);
  }

  insertAtCursor(element) {
    const cursorSlot = slot(this, "cursor");
    this.insertBefore(element, sexps(this)[cursorSlot] || null);
    moveCursor(this, cursorSlot + 1);
    return element;
  }

  moveBeginning() {
    moveCursor(this, 0);
  }

  moveEnd() {
    moveCursor(this, sexps(this).length);
  }

  setMark() {
    moveMark(this, slot(this, "cursor"));
  }

  toggleLayout() {
    this.dataset.layout = this.dataset.layout === "vertical" ? "horizontal" : "vertical";
  }
}
