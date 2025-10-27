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
> Task :app:generateDebugResValues
> Task :app:dataBindingMergeDependencyArtifactsDebug
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
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:1301:34 Unresolved reference 'message'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/ConversationalAgentService.kt:1311:89 Unresolved reference 'message'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/LoginActivity.kt.kt:96:39 Unresolved reference 'await'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/LoginActivity.kt.kt:97:41 Unresolved reference 'await'.
> Task :app:compileDebugKotlin FAILED
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
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/agents/ClarificationAgent.kt:4:28 Unresolved reference 'LLMApi'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/agents/ClarificationAgent.kt:49:32 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/agents/ClarificationAgent.kt:50:17 Unresolved reference 'LLMApi'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/api/EmbeddingService.kt:55:60 No value passed for parameter 'context'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/data/MemoryExtractor.kt:120:35 None of the following candidates is applicable:
fun <R> Iterable<*>.filterIsInstance(klass: Class<R>): List<R>
fun <reified R> Iterable<*>.filterIsInstance(): List<R>
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/data/MemoryExtractor.kt:120:52 Unresolved reference 'TextPart'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/data/MemoryExtractor.kt:121:54 Unresolved reference 'it'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/services/PuterService.kt:12:17 Unresolved reference 'browser'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/services/PuterService.kt:99:36 Unresolved reference 'CustomTabsIntent'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/utilities/LLMHelperFunctions.kt:4:28 Unresolved reference 'LLMApi'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/utilities/LLMHelperFunctions.kt:48:12 Unresolved reference 'LLMApi'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/utilities/SpeechCoordinator.kt:149:37 Unresolved reference 'puterTtsSynthesize'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/v2/AgentService.kt:139:13 Argument type mismatch: actual type is 'kotlin.String', but 'android.content.Context' was expected.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/v2/AgentService.kt:140:13 No parameter with name 'apiKeyManager' found.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/v2/AgentService.kt:141:13 No value passed for parameter 'modelName'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/v2/AgentService.kt:314:26 Unresolved reference 'saveTaskToKvStore'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/v2/AgentService.kt:343:26 Unresolved reference 'saveTaskToKvStore'.
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/v2/llm/GeminiAPI.kt:89:16 Return type mismatch: expected 'kotlin.String', actual 'kotlin.String?'.
FAILURE: Build failed with an exception.
* What went wrong:
Execution failed for task ':app:compileDebugKotlin'.
> A failure occurred while executing org.jetbrains.kotlin.compilerRunner.GradleCompilerRunnerWithWorkers$GradleKotlinCompilerWorkAction
   > Compilation error. See log for more details