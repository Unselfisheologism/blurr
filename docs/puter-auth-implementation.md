# Puter.js Authentication Implementation Guide

## Overview

This document explains how Puter.js authentication is implemented in the Blurr Android app using Chrome Custom Tabs for the OAuth flow and a persistent WebView for API calls.

## Architecture

The implementation uses a hybrid approach combining native Android components with web technologies:

1. **Persistent WebView Service**: A background service hosts a WebView that runs Puter.js continuously
2. **Chrome Custom Tabs**: Authentication flows are handled through Chrome Custom Tabs instead of internal WebViews
3. **Deep Link Callback**: After authentication, users are redirected back to the app via a deep link
4. **Broadcast Communication**: Authentication results are communicated between components using Android broadcasts

## Components

### 1. PuterService

- Runs as a background service to maintain the persistent WebView
- Hosts the Puter.js environment for API calls
- Intercepts authentication URLs and redirects them to Chrome Custom Tabs
- Evaluates JavaScript functions to interact with Puter.js

### 2. PuterWebChromeClient

- Handles WebView window creation events
- Manages the communication bridge between web and native

### 3. PuterAuthCallbackActivity

- Receives the authentication callback via deep link
- Extracts the authentication token from the redirect URL
- Stores the token and sends a broadcast to notify the service

### 4. PuterManager

- Singleton manager to coordinate Puter.js operations
- Handles authentication state and token management
- Registers broadcast receivers for authentication events

## Authentication Flow

1. User initiates sign-in through the app
2. PuterService detects authentication URL and launches Chrome Custom Tab
3. User completes authentication in the secure browser environment
4. Puter.com redirects to the app's deep link (blurrputer://auth-callback)
5. PuterAuthCallbackActivity receives the redirect and extracts the token
6. A broadcast is sent to notify the PuterService of authentication success
7. The token is stored and injected into the persistent WebView
8. The authentication promise is resolved in JavaScript

## Key Features

### Chrome Custom Tabs Integration
- Provides a secure, full-browser experience for authentication
- Prevents the "stuck" issue that occurs with popup WebViews
- Maintains user trust by using the system browser

### Persistent State
- The main WebView remains active for continuous API access
- Authentication state is maintained across app sessions
- Token is stored in SharedPreferences for persistence

### Communication Bridge
- JavaScript-to-native communication via addJavascriptInterface
- Native-to-JavaScript communication via evaluateJavascript
- Broadcast-based authentication result handling

## Deep Link Configuration

The app registers the `blurrputer://` scheme in AndroidManifest.xml to receive authentication callbacks:

```xml
<intent-filter android:autoVerify="true">
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />
    <data android:scheme="blurrputer" />
</intent-filter>
```

## Security Considerations

- Authentication is performed in Chrome Custom Tabs for security
- Tokens are stored securely in SharedPreferences
- The WebView is properly configured with security settings
- Communication between components is validated

## Error Handling

- Failed authentication attempts are properly handled
- Network errors during API calls are caught and reported
- Invalid tokens are cleared from storage
- Appropriate user feedback is provided for all error states

## Dependencies

- `androidx.browser:browser:1.8.0` - For Chrome Custom Tabs support
- Puter.js v2 loaded from https://js.puter.com/v2/

## Testing Considerations

- Test authentication flow on different Android versions
- Verify token persistence across app restarts
- Check proper handling of authentication cancellation
- Validate API calls work correctly after authentication