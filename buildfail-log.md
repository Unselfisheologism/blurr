Run ./gradlew clean assembleDebug --warning-mode=all --stacktrace
Downloading https://services.gradle.org/distributions/gradle-8.10.2-bin.zip
.............10%.............20%.............30%.............40%.............50%.............60%.............70%.............80%.............90%.............100%

Welcome to Gradle 8.10.2!

Here are the highlights of this release:
 - Support for Java 23
 - Faster configuration cache
 - Better configuration cache reports

For more details see https://docs.gradle.org/8.10.2/release-notes.html

Starting a Gradle Daemon (subsequent builds will be faster)
> Task :app:clean UP-TO-DATE
> Task :app:preBuild UP-TO-DATE
> Task :app:preDebugBuild UP-TO-DATE
> Task :app:mergeDebugNativeDebugMetadata NO-SOURCE
> Task :app:checkKotlinGradlePluginConfigurationErrors SKIPPED
> Task :app:dataBindingMergeDependencyArtifactsDebug
> Task :app:generateDebugResValues
> Task :app:generateDebugResources
> Task :app:packageDebugResources
> Task :app:generateDebugBuildConfig
> Task :app:mergeDebugResources
> Task :app:parseDebugLocalResources
> Task :app:checkDebugAarMetadata
> Task :app:dataBindingGenBaseClassesDebug
> Task :app:mapDebugSourceSetPaths
> Task :app:createDebugCompatibleScreenManifests
> Task :app:extractDeepLinksDebug
> Task :app:processDebugMainManifest
> Task :app:processDebugManifest
> Task :app:processDebugManifestForPackage
> Task :app:javaPreCompileDebug
> Task :app:mergeDebugShaders
> Task :app:compileDebugShaders NO-SOURCE
> Task :app:generateDebugAssets UP-TO-DATE
> Task :app:mergeDebugAssets
> Task :app:desugarDebugFileDependencies
> Task :app:compressDebugAssets
> Task :app:processDebugResources
> Task :app:checkDebugDuplicateClasses
> Task :app:kspDebugKotlin
> Task :app:mergeExtDexDebug
> Task :app:mergeDebugJniLibFolders
> Task :app:mergeLibDexDebug
> Task :app:mergeDebugNativeLibs
> Task :app:validateSigningDebug
> Task :app:writeDebugAppMetadata
> Task :app:writeDebugSigningConfigVersions

> Task :app:stripDebugDebugSymbols
Unable to strip the following libraries, packaging them as they are: libandroidx.graphics.path.so.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:69:35 Property delegate must have a 'getValue(ConversationalAgentService, KProperty1<ConversationalAgentService, ERROR CLASS: Cannot infer argument for type parameter T>)' method. None of the following functions is applicable:

> Task :app:compileDebugKotlin
fun <T> Lazy<T>.getValue(thisRef: Any?, property: KProperty<*>): T
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:69:38 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:69:38 Not enough information to infer type argument for 'T'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:69:38 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:69:38 Not enough information to infer type argument for 'T'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:69:45 Unresolved reference 'PandaStateManager'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:71:42 Property delegate must have a 'getValue(ConversationalAgentService, KProperty1<ConversationalAgentService, ERROR CLASS: Cannot infer argument for type parameter T>)' method. None of the following functions is applicable:
fun <T> Lazy<T>.getValue(thisRef: Any?, property: KProperty<*>): T
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:71:45 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:71:45 Not enough information to infer type argument for 'T'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:71:45 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:71:45 Not enough information to infer type argument for 'T'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:71:52 Unresolved reference 'ServicePermissionManager'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:126:27 Unresolved reference 'startMonitoring'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:127:27 Unresolved reference 'setState'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:163:27 Unresolved reference 'setState'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:202:39 Unresolved reference 'isMicrophonePermissionGranted'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:219:31 Unresolved reference 'setState'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:261:35 Unresolved reference 'setState'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:288:35 Unresolved reference 'triggerErrorState'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:304:60 Too many arguments for 'fun trackMessage(role: String, messageType: String = ...): Unit'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:320:39 Unresolved reference 'setState'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:324:43 Unresolved reference 'setState'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:340:27 Unresolved reference 'setState'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:356:35 Unresolved reference 'setState'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:383:35 Unresolved reference 'triggerErrorState'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:399:60 Too many arguments for 'fun trackMessage(role: String, messageType: String = ...): Unit'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:414:39 Unresolved reference 'setState'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:418:43 Unresolved reference 'setState'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:509:45 Too many arguments for 'fun trackMessage(role: String, messageType: String = ...): Unit'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:522:55 Too many arguments for 'fun trackMessage(role: String, messageType: String = ...): Unit'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:526:35 Unresolved reference 'setState'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:550:55 Unresolved reference 'isAccessibilityServiceEnabled'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:590:70 Too many arguments for 'fun trackMessage(role: String, messageType: String = ...): Unit'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:603:71 Too many arguments for 'fun trackMessage(role: String, messageType: String = ...): Unit'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:616:67 Too many arguments for 'fun trackMessage(role: String, messageType: String = ...): Unit'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:632:67 Too many arguments for 'fun trackMessage(role: String, messageType: String = ...): Unit'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:636:66 Too many arguments for 'fun trackMessage(role: String, messageType: String = ...): Unit'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:651:67 Too many arguments for 'fun trackMessage(role: String, messageType: String = ...): Unit'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:655:67 Too many arguments for 'fun trackMessage(role: String, messageType: String = ...): Unit'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:665:35 Unresolved reference 'triggerErrorState'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:1257:34 Unresolved reference 'message'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:1267:89 Unresolved reference 'message'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:1327:27 Unresolved reference 'setState'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:1328:27 Unresolved reference 'stopMonitoring'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/LoginActivity.kt.kt:96:39 Unresolved reference 'await'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/LoginActivity.kt.kt:97:41 Unresolved reference 'await'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/MainActivity.kt:26:17 Unresolved reference 'lifecycleScope'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/MainActivity.kt:162:9 Unresolved reference 'lifecycleScope'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/MainActivity.kt:278:23 Unresolved reference 'isNullOrEmpty'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/MainActivity.kt:419:9 Unresolved reference 'lifecycleScope'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/MomentsActivity.kt:57:48 Unresolved reference 'getTaskHistory'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/MomentsActivity.kt:62:25 Unresolved reference 'it'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/MyApplication.kt:25:22 Unresolved reference 'initialize'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/activities/PuterAuthActivity.kt:8:17 Unresolved reference 'browser'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/activities/PuterAuthActivity.kt:32:36 Unresolved reference 'CustomTabsIntent'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/activities/PuterTestActivity.kt:22:44 Unresolved reference 'resultText'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/activities/PuterTestActivity.kt:23:52 Unresolved reference 'testButton'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/api/EmbeddingService.kt:55:60 No value passed for parameter 'context'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/api/MemoryService.kt:24:38 Unresolved reference 'MEM0_API'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/data/MemoryExtractor.kt:120:35 None of the following candidates is applicable:
fun <R> Iterable<*>.filterIsInstance(klass: Class<R>): List<R>
fun <reified R> Iterable<*>.filterIsInstance(): List<R>
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/data/MemoryExtractor.kt:120:52 Unresolved reference 'TextPart'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/data/MemoryExtractor.kt:121:54 Unresolved reference 'it'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/services/PuterService.kt:12:17 Unresolved reference 'browser'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/services/PuterService.kt:99:36 Unresolved reference 'CustomTabsIntent'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/utilities/SpeechCoordinator.kt:149:37 Unresolved reference 'puterTtsSynthesize'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/v2/AgentService.kt:139:13 Argument type mismatch: actual type is 'kotlin.String', but 'android.content.Context' was expected.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/v2/AgentService.kt:140:13 No parameter with name 'apiKeyManager' found.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/v2/AgentService.kt:141:13 No value passed for parameter 'modelName'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/v2/AgentService.kt:314:26 Unresolved reference 'saveTaskToKvStore'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/v2/AgentService.kt:343:26 Unresolved reference 'saveTaskToKvStore'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/v2/llm/GeminiAPI.kt:89:16 Return type mismatch: expected 'kotlin.String', actual 'kotlin.String?'.

> Task :app:compileDebugKotlin FAILED

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':app:compileDebugKotlin'.
> A failure occurred while executing org.jetbrains.kotlin.compilerRunner.GradleCompilerRunnerWithWorkers$GradleKotlinCompilerWorkAction
   > Compilation error. See log for more details

* Try:
> Run with --info or --debug option to get more log output.
> Run with --scan to get full insights.
> Get more help at https://help.gradle.org.
