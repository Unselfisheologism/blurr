# Puter Authentication and Onboarding Flow Test Plan

This document outlines the comprehensive test plan for verifying the complete authentication and onboarding flow in the Blurr Voice application, ensuring that the local asset file is properly used in WebView implementations and that bidirectional communication is working correctly.

## Test Objectives

1. Verify that external URLs have been replaced with local asset files in all WebView implementations
2. Confirm that bidirectional communication between WebView and native Android code is functioning
3. Validate that authentication completion properly triggers onboarding flow
4. Ensure state synchronization between WebView and native PuterService
5. Test proper WebView lifecycle management throughout the authentication process
6. Verify the complete end-to-end authentication and onboarding flow

## Test Environment

- Android device/emulator running Android 10 or higher
- Blurr Voice application with latest changes
- Local asset file `puterwebp.html` properly embedded in the app
- Internet connectivity for initial setup (if needed for local assets)

## Test Cases

### 1. WebView Implementation Verification

#### TC-001: Verify Local Asset Loading
**Objective:** Confirm that WebView loads the local asset file instead of external URL

**Steps:**
1. Launch the application
2. Navigate to the authentication screen
3. Observe WebView initialization
4. Verify that `file:///android_asset/puterwebp.html` is loaded

**Expected Result:** 
- WebView successfully loads the local asset file
- No external network requests to `https://puterwebp.vercel.app`
- Authentication UI is displayed correctly

#### TC-002: Verify All WebView Implementations Use Local Assets
**Objective:** Confirm that all WebView implementations use local assets

**Steps:**
1. Review all files containing WebView implementations:
   - `PuterWebViewActivity.kt`
   - `PuterService.kt`
   - `PuterBackgroundService.kt`
2. Verify that all `loadUrl()` calls use `file:///android_asset/puterwebp.html`

**Expected Result:**
- All WebView implementations reference local asset files
- No external URLs are used in WebView loading

### 2. Bidirectional Communication Testing

#### TC-003: Verify JavaScript Interface Registration
**Objective:** Confirm that JavaScript interfaces are properly registered

**Steps:**
1. Launch the application
2. Navigate to authentication screen
3. Check that JavaScript interfaces are registered:
   - `AndroidInterface` in `PuterWebViewActivity.kt`
   - `AndroidInterface` in `PuterService.kt`
   - `AndroidInterface` in `PuterBackgroundService.kt`

**Expected Result:**
- All JavaScript interfaces are properly registered
- No errors in interface registration

#### TC-004: Verify Native to JavaScript Communication
**Objective:** Confirm that native Android code can call JavaScript functions

**Steps:**
1. Launch the application
2. Navigate to authentication screen
3. Trigger a native method that should call JavaScript
4. Observe JavaScript execution

**Expected Result:**
- JavaScript functions are successfully called from native code
- Responses are properly handled

#### TC-005: Verify JavaScript to Native Communication
**Objective:** Confirm that JavaScript can call native Android methods

**Steps:**
1. Launch the application
2. Navigate to authentication screen
3. Interact with the web UI to trigger JavaScript-to-native calls
4. Observe native method execution

**Expected Result:**
- Native Android methods are successfully called from JavaScript
- Parameters are correctly passed
- Responses are properly handled

### 3. Authentication Flow Testing

#### TC-006: Verify Authentication Success Flow
**Objective:** Confirm that successful authentication triggers proper flow

**Steps:**
1. Launch the application
2. Navigate to authentication screen
3. Complete authentication process in WebView
4. Observe navigation to onboarding or main activity

**Expected Result:**
- Authentication success is detected
- User token is stored properly
- Navigation to appropriate screen occurs based on onboarding status

#### TC-007: Verify Authentication Error Handling
**Objective:** Confirm that authentication errors are properly handled

**Steps:**
1. Launch the application
2. Navigate to authentication screen
3. Simulate authentication failure
4. Observe error handling

**Expected Result:**
- Authentication errors are caught and handled gracefully
- User is notified of authentication failure
- Authentication UI is reset to allow retry

#### TC-008: Verify First-Time User Onboarding Flow
**Objective:** Confirm that first-time users proceed through onboarding

**Steps:**
1. Launch the application with a fresh installation
2. Complete authentication process
3. Observe navigation to onboarding flow
4. Complete onboarding steps
5. Verify navigation to main activity

**Expected Result:**
- First-time users are directed to onboarding after authentication
- Onboarding steps are displayed correctly
- User progresses through all onboarding steps
- Navigation to main activity occurs after onboarding completion

#### TC-009: Verify Returning User Flow
**Objective:** Confirm that returning users go directly to main activity

**Steps:**
1. Launch the application with existing authentication
2. Verify user is directed to main activity
3. If not authenticated, authenticate and complete onboarding
4. Close and relaunch the application
5. Observe direct navigation to main activity

**Expected Result:**
- Authenticated users with completed onboarding go directly to main activity
- No redundant authentication or onboarding screens are shown

### 4. State Synchronization Testing

#### TC-010: Verify Authentication State Sync
**Objective:** Confirm that authentication state is synchronized between WebView and native service

**Steps:**
1. Launch the application
2. Complete authentication in WebView
3. Check authentication state in native PuterService
4. Verify states match

**Expected Result:**
- Authentication state changes in WebView are reflected in native service
- Token storage and retrieval work correctly
- State changes are immediate and consistent

#### TC-011: Verify Onboarding State Sync
**Objective:** Confirm that onboarding completion state is synchronized

**Steps:**
1. Launch the application as first-time user
2. Complete authentication
3. Complete onboarding flow
4. Check onboarding state in native service
5. Verify state is updated

**Expected Result:**
- Onboarding completion in WebView is reflected in native service
- State persists across app restarts
- Correct navigation occurs based on onboarding state

### 5. WebView Lifecycle Management Testing

#### TC-012: Verify WebView Stays Active During Auth Flow
**Objective:** Confirm that WebView remains active during authentication

**Steps:**
1. Launch the application
2. Navigate to authentication screen
3. Begin authentication process
4. Monitor WebView state during authentication
5. Complete authentication
6. Observe WebView cleanup

**Expected Result:**
- WebView remains active throughout authentication process
- No premature destruction of WebView during critical operations
- Proper cleanup occurs after authentication completion

#### TC-013: Verify Background Service WebView Management
**Objective:** Confirm that background service properly manages WebView

**Steps:**
1. Complete authentication process
2. Observe background service initialization
3. Verify WebView is transferred to background service
4. Check continued communication capability

**Expected Result:**
- WebView is properly transferred to background service
- Communication channel remains open
- Background service maintains WebView lifecycle appropriately

### 6. End-to-End Flow Testing

#### TC-014: Complete Authentication and Onboarding Flow
**Objective:** Verify the complete end-to-end flow for first-time users

**Steps:**
1. Fresh app installation
2. Launch application
3. Navigate through authentication
4. Complete onboarding
5. Reach main activity
6. Verify all components are functional

**Expected Result:**
- Smooth transition from authentication to onboarding to main activity
- All state changes are properly tracked
- User data is persisted correctly
- Application is fully functional after onboarding

#### TC-015: Returning User Flow
**Objective:** Verify the flow for returning users

**Steps:**
1. Launch application with existing authentication and onboarding
2. Observe direct navigation to main activity
3. Verify all services are properly initialized
4. Test core application functionality

**Expected Result:**
- Direct navigation to main activity for authenticated users
- All services initialize correctly
- Core functionality is available immediately
- No redundant authentication prompts

## Test Execution

### Manual Testing Steps

1. **Fresh Installation Test**
   - Uninstall existing app
   - Install fresh APK
   - Launch app and verify authentication screen appears
   - Complete authentication process
   - Verify onboarding flow begins
   - Complete onboarding steps
   - Verify navigation to main activity

2. **Returning User Test**
   - Close app after successful authentication and onboarding
   - Relaunch app
   - Verify direct navigation to main activity
   - Test core functionality

3. **Authentication Error Test**
   - Simulate authentication failure (cancel authentication)
   - Verify error handling
   - Verify ability to retry authentication

4. **WebView Inspection**
   - Use Android Debug Bridge (ADB) to inspect WebView traffic
   - Verify no external requests to `https://puterwebp.vercel.app`
   - Confirm all requests are to local assets

### Automated Testing

1. **Unit Tests**
   - Test PuterManager authentication state methods
   - Test WebView URL loading methods
   - Test JavaScript interface methods

2. **Integration Tests**
   - Test complete authentication flow
   - Test state synchronization between components
   - Test WebView lifecycle management

## Success Criteria

The implementation is considered successful if all of the following criteria are met:

1. All test cases pass without critical or high severity issues
2. No external network requests to `https://puterwebp.vercel.app` are observed
3. Authentication and onboarding flows work seamlessly for both first-time and returning users
4. Bidirectional communication between WebView and native code functions correctly
5. State synchronization between components is consistent and reliable
6. WebView lifecycle is properly managed without memory leaks or premature destruction
7. Application performs well with no noticeable delays or crashes during authentication flow

## Rollback Plan

If critical issues are discovered during testing:

1. Revert WebView URL changes to use external URL temporarily
2. Fix identified issues in local asset implementation
3. Re-test with fixed implementation
4. Document lessons learned for future implementations

## Post-Implementation Monitoring

After deployment, monitor:

1. Crash reports related to WebView or authentication flow
2. User feedback on authentication experience
3. Performance metrics during authentication process
4. Memory usage patterns with WebView lifecycle changes