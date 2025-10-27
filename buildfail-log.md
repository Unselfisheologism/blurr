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
> Configure project :app
w: file:///home/runner/work/blurr/blurr/app/build.gradle.kts:71:9: 'jvmTarget: String' is deprecated. Please migrate to the compilerOptions DSL. More details are here: https://kotl.in/u1r8ln
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
> Task :app:javaPreCompileDebug
> Task :app:mergeDebugShaders
> Task :app:compileDebugShaders NO-SOURCE
> Task :app:generateDebugAssets UP-TO-DATE
> Task :app:mergeDebugAssets
> Task :app:processDebugManifestForPackage
> Task :app:desugarDebugFileDependencies
> Task :app:compressDebugAssets
> Task :app:processDebugResources
> Task :app:checkDebugDuplicateClasses
> Task :app:kspDebugKotlin
e: [ksp] /home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt: (63, 47): Function declaration must have a name
e: Error occurred in KSP, check log for detail
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:30:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:31:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:32:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:48:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:49:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:50:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:63:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:63:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:63:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:63:39 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:63:40 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:63:45 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:63:47 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:63:58 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:66:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:68:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:68:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:69:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:70:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:81:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:81:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:81:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:81:42 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:81:43 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:81:49 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:81:51 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:81:62 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:84:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:86:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:86:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:87:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:88:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:99:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:99:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:99:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:99:42 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:99:43 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:99:52 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:99:54 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:99:65 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:102:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:104:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:104:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:105:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:106:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:117:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:117:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:117:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:117:45 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:117:46 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:117:50 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:117:52 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:117:63 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:120:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:122:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:122:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:123:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:124:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:135:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:135:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:135:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:135:33 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:135:34 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:135:37 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:135:39 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:135:50 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:138:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:140:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:140:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:141:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:142:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:153:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:153:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:153:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:153:33 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:153:34 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:153:37 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:153:44 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:153:46 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:153:57 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:156:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:158:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:158:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:159:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:160:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:171:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:171:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:171:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:171:33 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:171:34 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:171:37 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:171:39 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:171:50 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:174:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:176:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:176:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:177:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:178:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:189:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:189:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:189:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:189:34 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:189:35 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:189:42 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:189:44 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:189:56 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:189:58 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:189:69 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:192:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:194:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:194:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:195:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:196:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:207:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:207:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:207:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:207:34 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:207:35 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:207:38 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:207:40 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:207:46 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:207:48 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:207:59 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:210:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:212:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:212:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:213:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:214:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:225:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:225:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:225:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:225:34 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:225:35 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:225:38 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:225:40 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:225:46 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:225:48 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:225:59 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:228:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:230:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:230:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:231:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:232:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:243:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:243:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:243:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:243:36 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:243:47 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:246:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:248:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:248:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:249:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:250:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:260:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:260:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:261:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:262:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:272:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:272:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:273:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:274:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:285:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:285:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:285:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:285:40 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:285:51 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:288:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:290:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:290:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:291:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:292:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:301:14 Expecting 'catch' or 'finally'
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:302:11 Expecting ')'
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:302:11 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:306:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:307:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:307:2 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:307:3 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:307:17 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:307:29 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:311:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:312:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:312:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:313:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:314:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:323:14 Expecting 'catch' or 'finally'
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:324:11 Expecting ')'
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:324:11 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:328:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:329:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:329:2 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:329:3 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:329:17 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:329:29 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:333:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:334:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:334:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:335:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:336:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:339:46 Expecting '"'
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:340:2 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:340:3 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:341:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:342:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:349:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:357:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:358:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:373:2 Expecting 'catch' or 'finally'
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:373:3 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:379:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:379:3 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:379:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:379:10 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:379:11 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:379:13 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:379:22 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:379:24 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:382:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:383:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:384:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:386:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:386:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:386:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:386:45 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:386:46 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:386:51 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:386:53 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:386:68 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:386:70 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:386:81 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:389:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:391:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:391:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:392:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:393:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:405:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:405:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:405:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:405:42 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:405:43 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:405:47 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:405:53 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:405:55 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:405:66 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:405:68 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:405:79 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:408:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:410:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:410:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:411:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:412:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:424:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:424:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:424:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:424:41 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:424:42 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:424:46 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:424:48 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:424:59 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:424:61 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:424:72 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:427:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:429:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:429:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:430:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:431:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:443:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:443:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:443:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:443:42 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:443:43 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:443:47 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:443:49 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:443:60 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:443:62 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:443:73 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:446:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:448:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:448:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:449:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:450:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:461:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:461:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:461:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:461:44 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:461:45 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:461:49 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:461:51 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:461:62 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:464:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:466:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:466:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:467:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:468:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:480:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:480:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:480:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:480:43 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:480:44 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:480:48 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:480:50 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:480:61 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:480:63 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:480:74 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:483:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:485:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:485:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:486:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:487:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:499:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:499:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:499:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:499:41 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:499:42 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:499:48 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:499:50 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:499:61 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:499:63 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:499:74 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:499:76 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:499:87 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:502:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:504:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:504:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:505:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:506:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:518:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:518:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:518:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:518:41 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:518:42 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:518:48 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:518:50 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:518:61 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:518:63 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:518:74 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:518:76 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:518:87 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:521:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:523:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:523:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:524:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:525:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:536:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:536:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:536:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:536:43 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:536:44 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:536:48 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:536:50 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:536:57 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:536:59 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:536:70 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:539:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:541:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:541:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:542:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:543:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:554:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:554:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:554:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:554:41 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:554:42 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:554:46 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:554:48 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:554:59 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:557:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:559:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:559:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:560:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:561:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:572:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:572:21 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:572:23 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:572:43 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:572:54 Unexpected tokens (use ';' to separate expressions on the same line)
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:575:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:577:9 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:577:16 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:578:5 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:579:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:586:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:592:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:598:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:604:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:610:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:616:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:622:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:628:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:629:1 Expecting a top level declaration
e: file:///home/runner/work/blurr/blurr/app/src/main/java/com/blurr/voice/managers/PuterManager.kt:630:1 Expecting a top level declaration
> Task :app:mergeExtDexDebug
> Task :app:kspDebugKotlin FAILED
FAILURE: Build failed with an exception.
* What went wrong:
Execution failed for task ':app:kspDebugKotlin'.
> A failure occurred while executing org.jetbrains.kotlin.compilerRunner.GradleCompilerRunnerWithWorkers$GradleKotlinCompilerWorkAction
   > Compilation error. See log for more details
* Try:
> Run with --info or --debug option to get more log output.
> Run with --scan to get full insights.
> Get more help at https://help.gradle.org.
* Exception is:
org.gradle.api.tasks.TaskExecutionException: Execution failed for task ':app:kspDebugKotlin'.
	at org.gradle.api.internal.tasks.execution.ExecuteActionsTaskExecuter.lambda$executeIfValid$1(ExecuteActionsTaskExecuter.java:130)
	at org.gradle.internal.Try$Failure.ifSuccessfulOrElse(Try.java:293)
	at org.gradle.api.internal.tasks.execution.ExecuteActionsTaskExecuter.executeIfValid(ExecuteActionsTaskExecuter.java:128)
	at org.gradle.api.internal.tasks.execution.ExecuteActionsTaskExecuter.execute(ExecuteActionsTaskExecuter.java:116)
	at org.gradle.api.internal.tasks.execution.FinalizePropertiesTaskExecuter.execute(FinalizePropertiesTaskExecuter.java:46)
	at org.gradle.api.internal.tasks.execution.ResolveTaskExecutionModeExecuter.execute(ResolveTaskExecutionModeExecuter.java:51)
	at org.gradle.api.internal.tasks.execution.SkipTaskWithNoActionsExecuter.execute(SkipTaskWithNoActionsExecuter.java:57)
	at org.gradle.api.internal.tasks.execution.SkipOnlyIfTaskExecuter.execute(SkipOnlyIfTaskExecuter.java:74)
	at org.gradle.api.internal.tasks.execution.CatchExceptionTaskExecuter.execute(CatchExceptionTaskExecuter.java:36)
	at org.gradle.api.internal.tasks.execution.EventFiringTaskExecuter$1.executeTask(EventFiringTaskExecuter.java:77)
	at org.gradle.api.internal.tasks.execution.EventFiringTaskExecuter$1.call(EventFiringTaskExecuter.java:55)
	at org.gradle.api.internal.tasks.execution.EventFiringTaskExecuter$1.call(EventFiringTaskExecuter.java:52)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$CallableBuildOperationWorker.execute(DefaultBuildOperationRunner.java:209)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$CallableBuildOperationWorker.execute(DefaultBuildOperationRunner.java:204)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:66)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:166)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.call(DefaultBuildOperationRunner.java:53)
	at org.gradle.api.internal.tasks.execution.EventFiringTaskExecuter.execute(EventFiringTaskExecuter.java:52)
	at org.gradle.execution.plan.LocalTaskNodeExecutor.execute(LocalTaskNodeExecutor.java:42)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$InvokeNodeExecutorsAction.execute(DefaultTaskExecutionGraph.java:331)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$InvokeNodeExecutorsAction.execute(DefaultTaskExecutionGraph.java:318)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$BuildOperationAwareExecutionAction.lambda$execute$0(DefaultTaskExecutionGraph.java:314)
	at org.gradle.internal.operations.CurrentBuildOperationRef.with(CurrentBuildOperationRef.java:85)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$BuildOperationAwareExecutionAction.execute(DefaultTaskExecutionGraph.java:314)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$BuildOperationAwareExecutionAction.execute(DefaultTaskExecutionGraph.java:303)
	at org.gradle.execution.plan.DefaultPlanExecutor$ExecutorWorker.execute(DefaultPlanExecutor.java:459)
	at org.gradle.execution.plan.DefaultPlanExecutor$ExecutorWorker.run(DefaultPlanExecutor.java:376)
	at org.gradle.execution.plan.DefaultPlanExecutor.process(DefaultPlanExecutor.java:111)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph.executeWithServices(DefaultTaskExecutionGraph.java:138)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph.execute(DefaultTaskExecutionGraph.java:123)
	at org.gradle.execution.SelectedTaskExecutionAction.execute(SelectedTaskExecutionAction.java:35)
	at org.gradle.execution.DryRunBuildExecutionAction.execute(DryRunBuildExecutionAction.java:51)
	at org.gradle.execution.BuildOperationFiringBuildWorkerExecutor$ExecuteTasks.call(BuildOperationFiringBuildWorkerExecutor.java:54)
	at org.gradle.execution.BuildOperationFiringBuildWorkerExecutor$ExecuteTasks.call(BuildOperationFiringBuildWorkerExecutor.java:43)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$CallableBuildOperationWorker.execute(DefaultBuildOperationRunner.java:209)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$CallableBuildOperationWorker.execute(DefaultBuildOperationRunner.java:204)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:66)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:166)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.call(DefaultBuildOperationRunner.java:53)
	at org.gradle.execution.BuildOperationFiringBuildWorkerExecutor.execute(BuildOperationFiringBuildWorkerExecutor.java:40)
	at org.gradle.internal.build.DefaultBuildLifecycleController.lambda$executeTasks$10(DefaultBuildLifecycleController.java:313)
	at org.gradle.internal.model.StateTransitionController.doTransition(StateTransitionController.java:266)
	at org.gradle.internal.model.StateTransitionController.lambda$tryTransition$8(StateTransitionController.java:177)
	at org.gradle.internal.work.DefaultSynchronizer.withLock(DefaultSynchronizer.java:44)
	at org.gradle.internal.model.StateTransitionController.tryTransition(StateTransitionController.java:177)
	at org.gradle.internal.build.DefaultBuildLifecycleController.executeTasks(DefaultBuildLifecycleController.java:304)
	at org.gradle.internal.build.DefaultBuildWorkGraphController$DefaultBuildWorkGraph.runWork(DefaultBuildWorkGraphController.java:220)
	at org.gradle.internal.work.DefaultWorkerLeaseService.withLocks(DefaultWorkerLeaseService.java:263)
	at org.gradle.internal.work.DefaultWorkerLeaseService.runAsWorkerThread(DefaultWorkerLeaseService.java:127)
	at org.gradle.composite.internal.DefaultBuildController.doRun(DefaultBuildController.java:181)
	at org.gradle.composite.internal.DefaultBuildController.access$000(DefaultBuildController.java:50)
	at org.gradle.composite.internal.DefaultBuildController$BuildOpRunnable.lambda$run$0(DefaultBuildController.java:198)
	at org.gradle.internal.operations.CurrentBuildOperationRef.with(CurrentBuildOperationRef.java:85)
	at org.gradle.composite.internal.DefaultBuildController$BuildOpRunnable.run(DefaultBuildController.java:198)
	at org.gradle.internal.concurrent.ExecutorPolicy$CatchAndRecordFailures.onExecute(ExecutorPolicy.java:64)
	at org.gradle.internal.concurrent.AbstractManagedExecutor$1.run(AbstractManagedExecutor.java:48)
Caused by: org.gradle.workers.internal.DefaultWorkerExecutor$WorkExecutionException: A failure occurred while executing org.jetbrains.kotlin.compilerRunner.GradleCompilerRunnerWithWorkers$GradleKotlinCompilerWorkAction
	at org.gradle.workers.internal.DefaultWorkerExecutor$WorkItemExecution.waitForCompletion(DefaultWorkerExecutor.java:287)
	at org.gradle.internal.work.DefaultAsyncWorkTracker.lambda$waitForItemsAndGatherFailures$2(DefaultAsyncWorkTracker.java:130)
	at org.gradle.internal.Factories$1.create(Factories.java:31)
	at org.gradle.internal.work.DefaultWorkerLeaseService.withoutLocks(DefaultWorkerLeaseService.java:335)
	at org.gradle.internal.work.DefaultWorkerLeaseService.withoutLocks(DefaultWorkerLeaseService.java:318)
	at org.gradle.internal.work.DefaultWorkerLeaseService.withoutLock(DefaultWorkerLeaseService.java:323)
	at org.gradle.internal.work.DefaultAsyncWorkTracker.waitForItemsAndGatherFailures(DefaultAsyncWorkTracker.java:126)
	at org.gradle.internal.work.DefaultAsyncWorkTracker.waitForItemsAndGatherFailures(DefaultAsyncWorkTracker.java:92)
	at org.gradle.internal.work.DefaultAsyncWorkTracker.waitForAll(DefaultAsyncWorkTracker.java:78)
	at org.gradle.internal.work.DefaultAsyncWorkTracker.waitForCompletion(DefaultAsyncWorkTracker.java:66)
	at org.gradle.api.internal.tasks.execution.TaskExecution$3.run(TaskExecution.java:252)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$1.execute(DefaultBuildOperationRunner.java:29)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$1.execute(DefaultBuildOperationRunner.java:26)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:66)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:166)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.run(DefaultBuildOperationRunner.java:47)
	at org.gradle.api.internal.tasks.execution.TaskExecution.executeAction(TaskExecution.java:229)
	at org.gradle.api.internal.tasks.execution.TaskExecution.executeActions(TaskExecution.java:212)
	at org.gradle.api.internal.tasks.execution.TaskExecution.executeWithPreviousOutputFiles(TaskExecution.java:195)
	at org.gradle.api.internal.tasks.execution.TaskExecution.execute(TaskExecution.java:162)
	at org.gradle.internal.execution.steps.ExecuteStep.executeInternal(ExecuteStep.java:105)
	at org.gradle.internal.execution.steps.ExecuteStep.access$000(ExecuteStep.java:44)
	at org.gradle.internal.execution.steps.ExecuteStep$1.call(ExecuteStep.java:59)
	at org.gradle.internal.execution.steps.ExecuteStep$1.call(ExecuteStep.java:56)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$CallableBuildOperationWorker.execute(DefaultBuildOperationRunner.java:209)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$CallableBuildOperationWorker.execute(DefaultBuildOperationRunner.java:204)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:66)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:166)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.call(DefaultBuildOperationRunner.java:53)
	at org.gradle.internal.execution.steps.ExecuteStep.execute(ExecuteStep.java:56)
	at org.gradle.internal.execution.steps.ExecuteStep.execute(ExecuteStep.java:44)
	at org.gradle.internal.execution.steps.CancelExecutionStep.execute(CancelExecutionStep.java:42)
	at org.gradle.internal.execution.steps.TimeoutStep.executeWithoutTimeout(TimeoutStep.java:75)
	at org.gradle.internal.execution.steps.TimeoutStep.execute(TimeoutStep.java:55)
	at org.gradle.internal.execution.steps.PreCreateOutputParentsStep.execute(PreCreateOutputParentsStep.java:50)
	at org.gradle.internal.execution.steps.PreCreateOutputParentsStep.execute(PreCreateOutputParentsStep.java:28)
	at org.gradle.internal.execution.steps.RemovePreviousOutputsStep.execute(RemovePreviousOutputsStep.java:67)
	at org.gradle.internal.execution.steps.RemovePreviousOutputsStep.execute(RemovePreviousOutputsStep.java:37)
	at org.gradle.internal.execution.steps.BroadcastChangingOutputsStep.execute(BroadcastChangingOutputsStep.java:61)
	at org.gradle.internal.execution.steps.BroadcastChangingOutputsStep.execute(BroadcastChangingOutputsStep.java:26)
	at org.gradle.internal.execution.steps.CaptureOutputsAfterExecutionStep.execute(CaptureOutputsAfterExecutionStep.java:69)
	at org.gradle.internal.execution.steps.CaptureOutputsAfterExecutionStep.execute(CaptureOutputsAfterExecutionStep.java:46)
	at org.gradle.internal.execution.steps.ResolveInputChangesStep.execute(ResolveInputChangesStep.java:40)
	at org.gradle.internal.execution.steps.ResolveInputChangesStep.execute(ResolveInputChangesStep.java:29)
	at org.gradle.internal.execution.steps.BuildCacheStep.executeWithoutCache(BuildCacheStep.java:189)
	at org.gradle.internal.execution.steps.BuildCacheStep.executeAndStoreInCache(BuildCacheStep.java:145)
	at org.gradle.internal.execution.steps.BuildCacheStep.lambda$executeWithCache$4(BuildCacheStep.java:101)
	at org.gradle.internal.execution.steps.BuildCacheStep.lambda$executeWithCache$5(BuildCacheStep.java:101)
25 actionable tasks: 24 executed, 1 up-to-date
	at org.gradle.internal.Try$Success.map(Try.java:175)
	at org.gradle.internal.execution.steps.BuildCacheStep.executeWithCache(BuildCacheStep.java:85)
	at org.gradle.internal.execution.steps.BuildCacheStep.lambda$execute$0(BuildCacheStep.java:74)
	at org.gradle.internal.Either$Left.fold(Either.java:115)
	at org.gradle.internal.execution.caching.CachingState.fold(CachingState.java:62)
	at org.gradle.internal.execution.steps.BuildCacheStep.execute(BuildCacheStep.java:73)
	at org.gradle.internal.execution.steps.BuildCacheStep.execute(BuildCacheStep.java:48)
	at org.gradle.internal.execution.steps.StoreExecutionStateStep.execute(StoreExecutionStateStep.java:46)
	at org.gradle.internal.execution.steps.StoreExecutionStateStep.execute(StoreExecutionStateStep.java:35)
	at org.gradle.internal.execution.steps.SkipUpToDateStep.executeBecause(SkipUpToDateStep.java:75)
	at org.gradle.internal.execution.steps.SkipUpToDateStep.lambda$execute$2(SkipUpToDateStep.java:53)
	at org.gradle.internal.execution.steps.SkipUpToDateStep.execute(SkipUpToDateStep.java:53)
	at org.gradle.internal.execution.steps.SkipUpToDateStep.execute(SkipUpToDateStep.java:35)
	at org.gradle.internal.execution.steps.legacy.MarkSnapshottingInputsFinishedStep.execute(MarkSnapshottingInputsFinishedStep.java:37)
	at org.gradle.internal.execution.steps.legacy.MarkSnapshottingInputsFinishedStep.execute(MarkSnapshottingInputsFinishedStep.java:27)
	at org.gradle.internal.execution.steps.ResolveIncrementalCachingStateStep.executeDelegate(ResolveIncrementalCachingStateStep.java:49)
	at org.gradle.internal.execution.steps.ResolveIncrementalCachingStateStep.executeDelegate(ResolveIncrementalCachingStateStep.java:27)
	at org.gradle.internal.execution.steps.AbstractResolveCachingStateStep.execute(AbstractResolveCachingStateStep.java:71)
	at org.gradle.internal.execution.steps.AbstractResolveCachingStateStep.execute(AbstractResolveCachingStateStep.java:39)
	at org.gradle.internal.execution.steps.ResolveChangesStep.execute(ResolveChangesStep.java:65)
	at org.gradle.internal.execution.steps.ResolveChangesStep.execute(ResolveChangesStep.java:36)
	at org.gradle.internal.execution.steps.ValidateStep.execute(ValidateStep.java:107)
	at org.gradle.internal.execution.steps.ValidateStep.execute(ValidateStep.java:56)
	at org.gradle.internal.execution.steps.AbstractCaptureStateBeforeExecutionStep.execute(AbstractCaptureStateBeforeExecutionStep.java:64)
	at org.gradle.internal.execution.steps.AbstractCaptureStateBeforeExecutionStep.execute(AbstractCaptureStateBeforeExecutionStep.java:43)
	at org.gradle.internal.execution.steps.AbstractSkipEmptyWorkStep.executeWithNonEmptySources(AbstractSkipEmptyWorkStep.java:125)
	at org.gradle.internal.execution.steps.AbstractSkipEmptyWorkStep.execute(AbstractSkipEmptyWorkStep.java:61)
	at org.gradle.internal.execution.steps.AbstractSkipEmptyWorkStep.execute(AbstractSkipEmptyWorkStep.java:36)
	at org.gradle.internal.execution.steps.legacy.MarkSnapshottingInputsStartedStep.execute(MarkSnapshottingInputsStartedStep.java:38)
	at org.gradle.internal.execution.steps.LoadPreviousExecutionStateStep.execute(LoadPreviousExecutionStateStep.java:36)
	at org.gradle.internal.execution.steps.LoadPreviousExecutionStateStep.execute(LoadPreviousExecutionStateStep.java:23)
	at org.gradle.internal.execution.steps.HandleStaleOutputsStep.execute(HandleStaleOutputsStep.java:75)
	at org.gradle.internal.execution.steps.HandleStaleOutputsStep.execute(HandleStaleOutputsStep.java:41)
	at org.gradle.internal.execution.steps.AssignMutableWorkspaceStep.lambda$execute$0(AssignMutableWorkspaceStep.java:35)
	at org.gradle.api.internal.tasks.execution.TaskExecution$4.withWorkspace(TaskExecution.java:289)
	at org.gradle.internal.execution.steps.AssignMutableWorkspaceStep.execute(AssignMutableWorkspaceStep.java:31)
	at org.gradle.internal.execution.steps.AssignMutableWorkspaceStep.execute(AssignMutableWorkspaceStep.java:22)
	at org.gradle.internal.execution.steps.ChoosePipelineStep.execute(ChoosePipelineStep.java:40)
	at org.gradle.internal.execution.steps.ChoosePipelineStep.execute(ChoosePipelineStep.java:23)
	at org.gradle.internal.execution.steps.ExecuteWorkBuildOperationFiringStep.lambda$execute$2(ExecuteWorkBuildOperationFiringStep.java:67)
	at org.gradle.internal.execution.steps.ExecuteWorkBuildOperationFiringStep.execute(ExecuteWorkBuildOperationFiringStep.java:67)
	at org.gradle.internal.execution.steps.ExecuteWorkBuildOperationFiringStep.execute(ExecuteWorkBuildOperationFiringStep.java:39)
	at org.gradle.internal.execution.steps.IdentityCacheStep.execute(IdentityCacheStep.java:46)
	at org.gradle.internal.execution.steps.IdentityCacheStep.execute(IdentityCacheStep.java:34)
	at org.gradle.internal.execution.steps.IdentifyStep.execute(IdentifyStep.java:48)
	at org.gradle.internal.execution.steps.IdentifyStep.execute(IdentifyStep.java:35)
	at org.gradle.internal.execution.impl.DefaultExecutionEngine$1.execute(DefaultExecutionEngine.java:61)
	at org.gradle.api.internal.tasks.execution.ExecuteActionsTaskExecuter.executeIfValid(ExecuteActionsTaskExecuter.java:127)
	at org.gradle.api.internal.tasks.execution.ExecuteActionsTaskExecuter.execute(ExecuteActionsTaskExecuter.java:116)
	at org.gradle.api.internal.tasks.execution.FinalizePropertiesTaskExecuter.execute(FinalizePropertiesTaskExecuter.java:46)
	at org.gradle.api.internal.tasks.execution.ResolveTaskExecutionModeExecuter.execute(ResolveTaskExecutionModeExecuter.java:51)
	at org.gradle.api.internal.tasks.execution.SkipTaskWithNoActionsExecuter.execute(SkipTaskWithNoActionsExecuter.java:57)
	at org.gradle.api.internal.tasks.execution.SkipOnlyIfTaskExecuter.execute(SkipOnlyIfTaskExecuter.java:74)
	at org.gradle.api.internal.tasks.execution.CatchExceptionTaskExecuter.execute(CatchExceptionTaskExecuter.java:36)
	at org.gradle.api.internal.tasks.execution.EventFiringTaskExecuter$1.executeTask(EventFiringTaskExecuter.java:77)
	at org.gradle.api.internal.tasks.execution.EventFiringTaskExecuter$1.call(EventFiringTaskExecuter.java:55)
	at org.gradle.api.internal.tasks.execution.EventFiringTaskExecuter$1.call(EventFiringTaskExecuter.java:52)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$CallableBuildOperationWorker.execute(DefaultBuildOperationRunner.java:209)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$CallableBuildOperationWorker.execute(DefaultBuildOperationRunner.java:204)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:66)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:166)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.call(DefaultBuildOperationRunner.java:53)
	at org.gradle.api.internal.tasks.execution.EventFiringTaskExecuter.execute(EventFiringTaskExecuter.java:52)
	at org.gradle.execution.plan.LocalTaskNodeExecutor.execute(LocalTaskNodeExecutor.java:42)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$InvokeNodeExecutorsAction.execute(DefaultTaskExecutionGraph.java:331)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$InvokeNodeExecutorsAction.execute(DefaultTaskExecutionGraph.java:318)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$BuildOperationAwareExecutionAction.lambda$execute$0(DefaultTaskExecutionGraph.java:314)
	at org.gradle.internal.operations.CurrentBuildOperationRef.with(CurrentBuildOperationRef.java:85)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$BuildOperationAwareExecutionAction.execute(DefaultTaskExecutionGraph.java:314)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph$BuildOperationAwareExecutionAction.execute(DefaultTaskExecutionGraph.java:303)
	at org.gradle.execution.plan.DefaultPlanExecutor$ExecutorWorker.execute(DefaultPlanExecutor.java:459)
	at org.gradle.execution.plan.DefaultPlanExecutor$ExecutorWorker.run(DefaultPlanExecutor.java:376)
	at org.gradle.execution.plan.DefaultPlanExecutor.process(DefaultPlanExecutor.java:111)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph.executeWithServices(DefaultTaskExecutionGraph.java:138)
	at org.gradle.execution.taskgraph.DefaultTaskExecutionGraph.execute(DefaultTaskExecutionGraph.java:123)
	at org.gradle.execution.SelectedTaskExecutionAction.execute(SelectedTaskExecutionAction.java:35)
	at org.gradle.execution.DryRunBuildExecutionAction.execute(DryRunBuildExecutionAction.java:51)
	at org.gradle.execution.BuildOperationFiringBuildWorkerExecutor$ExecuteTasks.call(BuildOperationFiringBuildWorkerExecutor.java:54)
	at org.gradle.execution.BuildOperationFiringBuildWorkerExecutor$ExecuteTasks.call(BuildOperationFiringBuildWorkerExecutor.java:43)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$CallableBuildOperationWorker.execute(DefaultBuildOperationRunner.java:209)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$CallableBuildOperationWorker.execute(DefaultBuildOperationRunner.java:204)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:66)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:166)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.call(DefaultBuildOperationRunner.java:53)
	at org.gradle.execution.BuildOperationFiringBuildWorkerExecutor.execute(BuildOperationFiringBuildWorkerExecutor.java:40)
	at org.gradle.internal.build.DefaultBuildLifecycleController.lambda$executeTasks$10(DefaultBuildLifecycleController.java:313)
	at org.gradle.internal.model.StateTransitionController.doTransition(StateTransitionController.java:266)
	at org.gradle.internal.model.StateTransitionController.lambda$tryTransition$8(StateTransitionController.java:177)
	at org.gradle.internal.work.DefaultSynchronizer.withLock(DefaultSynchronizer.java:44)
	at org.gradle.internal.model.StateTransitionController.tryTransition(StateTransitionController.java:177)
	at org.gradle.internal.build.DefaultBuildLifecycleController.executeTasks(DefaultBuildLifecycleController.java:304)
	at org.gradle.internal.build.DefaultBuildWorkGraphController$DefaultBuildWorkGraph.runWork(DefaultBuildWorkGraphController.java:220)
	at org.gradle.internal.work.DefaultWorkerLeaseService.withLocks(DefaultWorkerLeaseService.java:263)
	at org.gradle.internal.work.DefaultWorkerLeaseService.runAsWorkerThread(DefaultWorkerLeaseService.java:127)
	at org.gradle.composite.internal.DefaultBuildController.doRun(DefaultBuildController.java:181)
	at org.gradle.composite.internal.DefaultBuildController.access$000(DefaultBuildController.java:50)
	at org.gradle.composite.internal.DefaultBuildController$BuildOpRunnable.lambda$run$0(DefaultBuildController.java:198)
	at org.gradle.internal.operations.CurrentBuildOperationRef.with(CurrentBuildOperationRef.java:85)
	at org.gradle.composite.internal.DefaultBuildController$BuildOpRunnable.run(DefaultBuildController.java:198)
	at org.gradle.internal.concurrent.ExecutorPolicy$CatchAndRecordFailures.onExecute(ExecutorPolicy.java:64)
	at org.gradle.internal.concurrent.AbstractManagedExecutor$1.run(AbstractManagedExecutor.java:48)
Caused by: org.jetbrains.kotlin.gradle.tasks.CompilationErrorException: Compilation error. See log for more details
	at org.jetbrains.kotlin.gradle.tasks.TasksUtilsKt.throwExceptionIfCompilationFailed(tasksUtils.kt:21)
	at org.jetbrains.kotlin.compilerRunner.GradleKotlinCompilerWork.run(GradleKotlinCompilerWork.kt:119)
	at org.jetbrains.kotlin.compilerRunner.GradleCompilerRunnerWithWorkers$GradleKotlinCompilerWorkAction.execute(GradleCompilerRunnerWithWorkers.kt:76)
	at org.gradle.workers.internal.DefaultWorkerServer.execute(DefaultWorkerServer.java:63)
	at org.gradle.workers.internal.NoIsolationWorkerFactory$1$1.create(NoIsolationWorkerFactory.java:66)
	at org.gradle.workers.internal.NoIsolationWorkerFactory$1$1.create(NoIsolationWorkerFactory.java:62)
	at org.gradle.internal.classloader.ClassLoaderUtils.executeInClassloader(ClassLoaderUtils.java:100)
	at org.gradle.workers.internal.NoIsolationWorkerFactory$1.lambda$execute$0(NoIsolationWorkerFactory.java:62)
	at org.gradle.workers.internal.AbstractWorker$1.call(AbstractWorker.java:44)
	at org.gradle.workers.internal.AbstractWorker$1.call(AbstractWorker.java:41)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$CallableBuildOperationWorker.execute(DefaultBuildOperationRunner.java:209)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$CallableBuildOperationWorker.execute(DefaultBuildOperationRunner.java:204)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:66)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:166)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.call(DefaultBuildOperationRunner.java:53)
	at org.gradle.workers.internal.AbstractWorker.executeWrappedInBuildOperation(AbstractWorker.java:41)
	at org.gradle.workers.internal.NoIsolationWorkerFactory$1.execute(NoIsolationWorkerFactory.java:59)
	at org.gradle.workers.internal.DefaultWorkerExecutor.lambda$submitWork$0(DefaultWorkerExecutor.java:174)
	at org.gradle.internal.work.DefaultConditionalExecutionQueue$ExecutionRunner.runExecution(DefaultConditionalExecutionQueue.java:194)
	at org.gradle.internal.work.DefaultConditionalExecutionQueue$ExecutionRunner.access$700(DefaultConditionalExecutionQueue.java:127)
	at org.gradle.internal.work.DefaultConditionalExecutionQueue$ExecutionRunner$1.run(DefaultConditionalExecutionQueue.java:169)
	at org.gradle.internal.Factories$1.create(Factories.java:31)
	at org.gradle.internal.work.DefaultWorkerLeaseService.withLocks(DefaultWorkerLeaseService.java:263)
	at org.gradle.internal.work.DefaultWorkerLeaseService.runAsWorkerThread(DefaultWorkerLeaseService.java:127)
	at org.gradle.internal.work.DefaultWorkerLeaseService.runAsWorkerThread(DefaultWorkerLeaseService.java:132)
	at org.gradle.internal.work.DefaultConditionalExecutionQueue$ExecutionRunner.runBatch(DefaultConditionalExecutionQueue.java:164)
	at org.gradle.internal.work.DefaultConditionalExecutionQueue$ExecutionRunner.run(DefaultConditionalExecutionQueue.java:133)
	... 2 more
BUILD FAILED in 4m 19s
Error: Process completed with exit code 1.