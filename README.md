# Puter.js Android Integration

This repository contains a complete implementation of the Puter.js SDK for Android applications. The integration provides access to all Puter.js features including AI capabilities, file system operations, key-value storage, and authentication.

## Features Implemented

### AI Capabilities
- **Chat**: Natural language conversations with streaming support
- **Text-to-Image**: Generate images from text descriptions
- **Image-to-Text**: Extract text from images (OCR)
- **Text-to-Speech**: Convert text to spoken audio

### File System Operations
- **Write**: Create and modify files
- **Read**: Retrieve file contents
- **Directory Management**: Create, list, and navigate directories
- **File Operations**: Move, copy, rename, and delete files
- **Metadata**: Get file statistics and storage space information

### Key-Value Storage
- **Set/Get**: Store and retrieve key-value pairs
- **Increment/Decrement**: Atomic operations on numeric values
- **List**: Enumerate all keys with optional value retrieval
- **Delete**: Remove individual keys or flush all data

### Authentication
- **Sign In/Out**: Secure user authentication
- **Session Management**: Check authentication status
- **User Information**: Retrieve user profile data

## Architecture

The implementation follows a layered architecture:

1. **Service Layer**: `PuterService` manages the persistent WebView that runs Puter.js
2. **Bridge Layer**: `PuterBridge` handles communication between native Android code and JavaScript
3. **API Layer**: `PuterManager` provides a high-level, easy-to-use API for all features
4. **JavaScript Layer**: HTML file with all Puter.js bridge functions

## Usage

### Initialization
```kotlin
val puterManager = PuterManager.getInstance(context)
puterManager.initialize()
```

### Chat Example
```kotlin
// Basic chat
puterManager.chat("Hello, how are you?")
    .thenApply { response ->
        // Handle response
    }
    .exceptionally { error ->
        // Handle error
    }

// Streaming chat
puterManager.chatStream("Tell me a story") { chunk ->
    // Handle each chunk as it arrives
}.thenApply { completeResponse ->
    // Handle complete response
}
```

### File System Example
```kotlin
// Write a file
puterManager.fsWrite("hello.txt", "Hello, world!")

// Read a file
puterManager.fsRead("hello.txt")
    .thenApply { content ->
        // Handle content
    }
```

### Key-Value Example
```kotlin
// Set a value
puterManager.kvSet("username", "john_doe")

// Get a value
puterManager.kvGet("username")
    .thenApply { value ->
        // Handle value
    }
```

## Security

- Authentication uses Chrome Custom Tabs for secure sign-in
- All data is stored in the user's secure Puter account
- Communication between layers is properly secured
- Each app operates in its own sandboxed environment

## Testing

A comprehensive test suite is included in `PuterFullTestActivity` that validates all implemented features.

## Documentation

Detailed documentation is available in the `docs` folder:
- [Implementation Summary](docs/puter-implementation-summary.md)
- [Error Handling Guide](docs/puter-error-handling.md)
- [Key-Value Implementation](docs/puter-kv-implementation.md)
- [Test Plan](docs/puter-implementation-test-plan.md)

## Requirements

- Android API level 21+
- Internet connectivity
- Puter.js account for users

## Dependencies

- `androidx.browser:browser` for Chrome Custom Tabs
- Android WebView component

## Contributing

Contributions are welcome! Please read the documentation and follow the established patterns.

## License

This implementation is provided as open source for educational and demonstration purposes.
