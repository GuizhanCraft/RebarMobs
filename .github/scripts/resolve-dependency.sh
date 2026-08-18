#!/bin/bash
# resolve-dependency.sh - Resolve a Guizhan Resources dependency version ID

set -euo pipefail

if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <base_url> <project_slug> <version>" >&2
    exit 1
fi

BASE_URL="$1"
PROJECT_SLUG="$2"
VERSION="$3"
URL="${BASE_URL%/}/api/v1/projects/${PROJECT_SLUG}/versions/${VERSION}"

RESPONSE=""
if ! RESPONSE=$(curl -sS --connect-timeout 10 --max-time 10 -w '\n%{http_code}' "$URL"); then
    echo "::warning::Dependency version not found for ${PROJECT_SLUG}@${VERSION}; falling back to name" >&2
    exit 0
fi

HTTP_STATUS="${RESPONSE##*$'\n'}"
RESPONSE_BODY="${RESPONSE%$'\n'*}"
if [[ "$HTTP_STATUS" != 2[0-9][0-9] ]]; then
    echo "::warning::Dependency version not found for ${PROJECT_SLUG}@${VERSION} (HTTP ${HTTP_STATUS}); falling back to name" >&2
    exit 0
fi

VERSION_ID=""
if ! VERSION_ID=$(printf '%s' "$RESPONSE_BODY" | python3 -c 'import json, sys; data = json.load(sys.stdin); value = data.get("data", {}).get("id"); print(value if value is not None else "")'); then
    echo "::warning::Dependency version response was invalid for ${PROJECT_SLUG}@${VERSION}; falling back to name" >&2
    exit 0
fi

if [ -n "$VERSION_ID" ]; then
    printf '%s\n' "$VERSION_ID"
fi
