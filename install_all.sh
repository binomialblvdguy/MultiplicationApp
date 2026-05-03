#!/usr/bin/env bash
set -e

APK="app/build/outputs/apk/debug/app-debug.apk"
PKG="com.multiplication.two_digit"

./gradlew assembleDebug
adb start-server >/dev/null

for d in $(adb devices | awk 'NR>1 && $2=="device" {print $1}'); do
  echo "Installing on $d ..."
  adb -s "$d" install -r "$APK"
  echo "Launching on $d ..."
  adb -s "$d" shell monkey -p "$PKG" -c android.intent.category.LAUNCHER 1 >/dev/null
done

echo "Done."
