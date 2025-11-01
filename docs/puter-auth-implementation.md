# Puter.js Authentication Implementation Guide

## Overview

This document explains how Puter.js authentication is implemented in the Blurr Android app using a single WebView with popup support for the OAuth flow and persistent API communication.

## Architecture

The implementation uses a single WebView approach combining native Android components with web technologies:

1. **Persistent WebView Service**: A background service hosts a WebView that runs Puter.js continuously
2. **Popup Windows**: Authentication flows are handled through popup windows created by the WebView
3. **JavaScript Bridge**: Communication between the WebView and native Android code
4. **PostMessage API**: Used for communication between the main WebView and authentication popup

## Components

### 1. PuterService

- Runs as a background service to maintain the persistent WebView
- Hosts the Puter.js environment for API calls
- Handles authentication callbacks through signInCallback property
- Evaluates JavaScript functions to interact with Puter.js

### 2. PuterWebChromeClient

- Handles WebView window creation events (onCreateWindow)
- Manages popup windows for authentication
- Creates dialogs to display popup WebViews
- Properly cleans up popup resources

### 3. PuterManager

- Singleton manager to coordinate Puter.js operations
- Handles authentication state and token management
- Provides high-level API for authentication

## Authentication Flow

1. User initiates sign-in through the app
2. PuterManager calls puter.auth.signIn() via JavaScript evaluation
3. Puter.js opens a popup window for authentication (handled by PuterWebChromeClient)
4. User completes authentication in the popup window
5. Popup sends authentication token back to main WebView via postMessage
6. The PuterService receives the token and completes the authentication
7. The authentication promise is resolved in the PuterManager

## Key Features

### Popup Window Support
- WebView configured with setSupportMultipleWindows(true)
- JavaScript can open windows automatically (javaScriptCanOpenWindowsAutomatically = true)
- PuterWebChromeClient handles popup creation and display in dialogs

### Persistent State
- The main WebView remains active for continuous API access
- Authentication state is maintained across app sessions
- Token is stored in localStorage and SharedPreferences

### Communication Bridge
- JavaScript-to-native communication via addJavascriptInterface
- Native-to-JavaScript communication via evaluateJavascript
- PostMessage API for popup communication

## Security Considerations

- Authentication is performed in popup WebViews with proper security settings
- Tokens are stored securely in both WebView localStorage and app SharedPreferences
- The WebView is properly configured with security settings
- Communication between components is validated

## Error Handling

- Failed authentication attempts are properly handled
- Network errors during API calls are caught and reported
- Invalid tokens are cleared from storage
- Appropriate user feedback is provided for all error states

## Dependencies

- Puter.js v2 loaded from https://js.puter.com/v2/

## Testing Considerations

- Test authentication flow on different Android versions
- Verify token persistence across app restarts
- Check proper handling of authentication cancellation
- Validate API calls work correctly after authentication