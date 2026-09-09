# Agent Instructions

- Do not do unrelated work. Stay strictly on the user's current task.
- Prefer minimal solutions.
- Do not introduce indirection until we need it.
- Before starting to copy-paste direct code repeated multiple places, ask the user if indirection is warranted.
- When considering browser compatibility, include Chrome and Firefox.

## Communication

Be brief.
When asked a question, answer that question.

## Options

Contrasting options for a decision live in the `2.teod.eu-optionspace`
sidecar, per the optionspace rule in `tlh.factory-spec/sidecars.md`.

## Clojure REPL discipline

- Use (`nvk`) to evaluate code
- Invoker connects to a running JVM process. Use this. Do not stop this process; a human is likely using it.
- Use `nvk reload` to load new code after file changes have been made.
- Use `nvk test` to run the tests.

You shall never restart JVM processes without asking the user.

## Exploratory Clojure code

You may work within the current REPL context to learn and explore.
Feel free to create files in `src/llm/...`.
