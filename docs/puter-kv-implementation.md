# Puter.js Key-Value Store Implementation

## Overview

This document describes the implementation of the puter.kv store functionality in the Android application, which replaces Firebase's equivalent capabilities with Puter.js cloud-based key-value storage.

## Implemented Functions

### Core Key-Value Functions

1. **puter.kv.set(key, value)**
   - Saves or updates values in the key-value store
   - Parameters: `key` (String), `value` (String/Number/Boolean/Object/Array)
   - Returns: Promise resolving to `true` when successful

2. **puter.kv.get(key)**
   - Retrieves the value stored in a key
   - Parameters: `key` (String)
   - Returns: Promise resolving to the key's value or `null` if key doesn't exist

3. **puter.kv.del(key)**
   - Removes a key from the key-value store
   - Parameters: `key` (String)
   - Returns: Promise resolving to `true` when successful

4. **puter.kv.list(pattern, returnValues)**
   - Retrieves all keys from the app's key-value store
   - Parameters: `pattern` (String, optional, default: "*"), `returnValues` (Boolean, optional, default: false)
   - Returns: Promise resolving to an array of keys (and values if returnValues is true)

5. **puter.kv.incr(key, amount)**
   - Increments numeric values in the key-value store
   - Parameters: `key` (String), `amount` (Integer, optional, default: 1)
   - Returns: Promise resolving to the new value after increment

6. **puter.kv.decr(key, amount)**
   - Decrements numeric values in the key-value store
   - Parameters: `key` (String), `amount` (Integer, optional, default: 1)
   - Returns: Promise resolving to the new value after decrement

7. **puter.kv.flush()**
   - Removes all key-value pairs from the app's store
   - Returns: Promise resolving to `true` when successful

## Architecture

### Android Layer
- `PuterManager.kt`: Main interface for Android application to interact with puter.kv functions
- `PuterService.kt`: Background service that manages the WebView and executes JavaScript calls
- `PuterBridge.kt`: Handles JavaScript-to-Android communication bridge

### WebView Layer
- `puter_WebView.html`: Contains the JavaScript bridge functions that interface with Puter.js
- Uses the official Puter.js SDK (`https://js.puter.com/v2/`)

### Communication Flow
1. Android app calls a method in `PuterManager`
2. `PuterManager` calls the corresponding method in `PuterService`
3. `PuterService` executes JavaScript code in the WebView
4. JavaScript code calls the appropriate `puter.kv.*` function
5. Results are passed back through the bridge to the Android app

## Authentication

- Only email authentication is supported (Google authentication has been removed)
- Uses `puter.auth.signIn()` and `puter.auth.isSignedIn()` for user authentication
- User data is stored in the user's own Puter account

## Key Features

- **Asynchronous Operations**: All operations return CompletableFuture for non-blocking execution
- **Error Handling**: Proper error handling with callbacks for both success and failure cases
- **Sandboxed Storage**: Each app has its own private key-value store within each user's account
- **Size Limits**: Keys limited to 1KB, values limited to 400KB
- **User-Pays Model**: Users cover their own storage costs through their Puter account

## Migration from Firebase

- Replaced Firebase Realtime Database functionality with Puter.js Key-Value Store
- Maintained similar API interface for easy migration
- Removed all Google Services dependencies for authentication
- Updated privacy policy to reflect use of Puter.js instead of Firebase

## Security

- Apps can only access their own key-value store within a user's account
- No cross-app data access is possible
- All data is encrypted and stored in the user's Puter account
- Authentication required for all operations