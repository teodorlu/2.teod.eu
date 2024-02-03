#!/usr/bin/env sh

set -e

clj -X:nextjournal/clerk
git add public/build
git commit -m "build then deploy"
git push
