# Agent Instructions

- When the user asks to explain or demonstrate how the web works, create an HTML file in `agent-demos/` that demonstrates the concept.
- After creating an agent-generated communication/demo file, update `agent-demos/index.html`.
- `agent-demos/index.html` lists agent-generated communication/demo files only, not application files.
- Agent demos must live in `agent-demos/` and must not interleave with application code.
- Do not do unrelated work. Stay strictly on the user's current task.
- Prefer minimal solutions.
- Do not introduce indirection until we need it.
- Before starting to copy-paste direct code repeated multiple places, ask the user if indirection is warranted.
- When considering browser compatibility, include Chrome and Firefox.
