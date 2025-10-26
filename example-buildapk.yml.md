name: Build App

on:
  push:
    # Trigger only for main branch.
    branches: [ main, master ]
  # Allows you to run this workflow manually from the Actions tab
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Setup Gradle Cache
        uses: actions/cache@v3
        with:
          path: |
            ~/.gradle/caches
            ~/.gradle/wrapper
          key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
          restore-keys: |
            ${{ runner.os }}-gradle-

      # Get the app source
      - name: Get the source
        uses: actions/checkout@v3

      - name: Setup JDK
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: gradle

      # Clean up any backup files that might interfere with the build
      - name: Clean up backup files
        run: |
          # Remove any backup files that might interfere with the build
          find . -name "*.bak" -type f -delete || true
          find . -name "*~" -type f -delete || true
          
          # Fix any existing gradlew file to ensure proper Unix line endings
          sed -i 's/\r$//' gradlew
          chmod +x gradlew

      # Clean and compile the apk
      - name: Clean and Generate Debug APK
        run: ./gradlew clean assembleDebug

      # Upload the action artifact
      - name: Upload Debug APK (artifact)
        uses: actions/upload-artifact@v4
        with:
          name: debug-apk
          path: app/build/outputs/apk/debug/app-debug.apk

      # Generate Release APK (without signing for now)
      - name: Generate Release APK
        run: ./gradlew clean assembleRelease

      # Upload Release APK
      - name: Upload Release APK (artifact)
        uses: actions/upload-artifact@v4
        with:
          name: release-apk
          path: app/build/outputs/apk/release/app-release.apk