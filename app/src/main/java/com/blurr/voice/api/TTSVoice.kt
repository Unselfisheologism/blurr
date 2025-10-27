package com.blurr.voice.api

/**
 * Available voice options for TTS, using all provided Chirp3-HD voices.
 */
enum class TTSVoice(val displayName: String, val voiceName: String, val description: String) {
    CHIRP_ACHERNAR("Achernar", "en-US-Chirp3-HD-Achernar", "High-definition female voice."),
    CHIRP_ACHIRD("Achird", "en-US-Chirp3-HD-Achird", "High-definition male voice."),
    CHIRP_ALGENIB("Algenib", "en-US-Chirp3-HD-Algenib", "High-definition male voice."),
    CHIRP_ALGIEBA("Algieba", "en-US-Chirp3-HD-Algieba", "High-definition male voice."),
    CHIRP_ALNILAM("Alnilam", "en-US-Chirp3-HD-Alnilam", "High-definition male voice."),
    CHIRP_AOEDE("Aoede", "en-US-Chirp3-HD-Aoede", "High-definition female voice."),
    CHIRP_AUTONOE("Autonoe", "en-US-Chirp3-HD-Autonoe", "High-definition female voice."),
    CHIRP_CALLIRRHOE("Callirrhoe", "en-US-Chirp3-HD-Callirrhoe", "High-definition female voice."),
    CHIRP_CHARON("Charon", "en-US-Chirp3-HD-Charon", "High-definition male voice."),
    CHIRP_DESPINA("Despina", "en-US-Chirp3-HD-Despina", "High-definition female voice."),
    CHIRP_ENCELADUS("Enceladus", "en-US-Chirp3-HD-Enceladus", "High-definition male voice."),
    CHIRP_ERINOME("Erinome", "en-US-Chirp3-HD-Erinome", "High-definition female voice."),
    CHIRP_FENRIR("Fenrir", "en-US-Chirp3-HD-Fenrir", "High-definition male voice."),
    CHIRP_GACRUX("Gacrux", "en-US-Chirp3-HD-Gacrux", "High-definition female voice."),
    CHIRP_IAPETUS("Iapetus", "en-US-Chirp3-HD-Iapetus", "High-definition male voice."),
    CHIRP_KORE("Kore", "en-US-Chirp3-HD-Kore", "High-definition female voice."),
    CHIRP_LAOMEDEIA("Laomedeia", "en-US-Chirp3-HD-Laomedeia", "High-definition female voice."),
    CHIRP_LEDA("Leda", "en-US-Chirp3-HD-Leda", "High-definition female voice."),
    CHIRP_ORUS("Orus", "en-US-Chirp3-HD-Orus", "High-definition male voice."),
    CHIRP_PUCK("Puck", "en-US-Chirp3-HD-Puck", "High-definition male voice."),
    CHIRP_PULCHERRIMA("Pulcherrima", "en-US-Chirp3-HD-Pulcherrima", "High-definition female voice."),
    CHIRP_RASALGETHI("Rasalgethi", "en-US-Chirp3-HD-Rasalgethi", "High-definition male voice."),
    CHIRP_SADACHBIA("Sadachbia", "en-US-Chirp3-HD-Sadachbia", "High-definition male voice."),
    CHIRP_SADALTAGER("Sadaltager", "en-US-Chirp3-HD-Sadaltager", "High-definition male voice."),
    CHIRP_SCHADAR("Schedar", "en-US-Chirp3-HD-Schedar", "High-definition male voice."),
    CHIRP_SULAFAT("Sulafat", "en-US-Chirp3-HD-Sulafat", "High-definition female voice."),
    CHIRP_UMBRIEL("Umbriel", "en-US-Chirp3-HD-Umbriel", "High-definition male voice."),
    CHIRP_VINDEMIATRIX("Vindemiatrix", "en-US-Chirp3-HD-Vindemiatrix", "High-definition female voice."),
    CHIRP_ZEPHYR("Zephyr", "en-US-Chirp3-HD-Zephyr", "High-definition female voice."),
    CHIRP_ZUBENELGENUBI("Zubenelgenubi", "en-US-Chirp3-HD-Zubenelgenubi", "High-definition male voice."),
    // Default voice that will be used with Puter.js
    PUTER_DEFAULT("Default", "puter-default", "Puter.js default voice")
}