#!/usr/bin/env bash
# Starts an Appium server, runs the Maven/TestNG suite against the device that is
# already attached (emulator or phone), then always stops Appium again.
#
# Usage (repo root):  bash scripts/run-appium-tests.sh
# Optional env: APP_PATH (APK to install), PLATFORM_VERSION, DEVICE_NAME, APPIUM_PORT
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
PORT="${APPIUM_PORT:-4723}"
export APPIUM_SERVER_URL="http://127.0.0.1:${PORT}"
export APP_PATH="${APP_PATH:-$ROOT/android-app/app/build/outputs/apk/debug/app-debug.apk}"
export PLATFORM_VERSION="${PLATFORM_VERSION:-14}"

if [ ! -f "$APP_PATH" ]; then
  echo "APK not found at $APP_PATH. Build it first: (cd android-app && ./gradlew assembleDebug)" >&2
  exit 1
fi

echo "Attached devices:"
adb devices

if ! command -v appium >/dev/null 2>&1; then
  echo "Installing Appium and the UiAutomator2 driver"
  # Unpinned on purpose: the current driver requires the current server, and a pinned old server is rejected.
  npm install -g appium
fi
appium driver list --installed 2>&1 | grep -q uiautomator2 || appium driver install uiautomator2
echo "Appium version: $(appium --version)"
appium driver list --installed 2>&1 || true

appium --address 127.0.0.1 --port "$PORT" --log-no-colors > "$ROOT/appium.log" 2>&1 &
APPIUM_PID=$!
trap 'kill "$APPIUM_PID" 2>/dev/null || true' EXIT

echo "Waiting for Appium on port $PORT"
for _ in $(seq 1 45); do
  if curl -s "http://127.0.0.1:${PORT}/status" | grep -q '"ready":true'; then
    echo "Appium is ready"
    break
  fi
  if ! kill -0 "$APPIUM_PID" 2>/dev/null; then
    echo "Appium exited early:" >&2
    cat "$ROOT/appium.log" >&2
    exit 1
  fi
  sleep 2
done
curl -sf "http://127.0.0.1:${PORT}/status" > /dev/null

cd "$ROOT/appium-tests"
mvn -B -ntp clean test
