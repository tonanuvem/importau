#!/bin/bash

TOKEN=$(cat ~/environment/_github_token_tonanuvem)
REPO_URL=$(git remote get-url origin | sed 's|https://|https://tonanuvem:'"$TOKEN"'@|')

git add .
git commit -m "Update: $(date '+%Y-%m-%d %H:%M:%S')"
git push "$REPO_URL" main
