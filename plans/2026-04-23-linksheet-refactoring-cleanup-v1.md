# LinkSheet Refactoring Plan: Remove Network APIs, Flavor System, and Dead Code

## Objective

Strip the LinkSheet Android project down to its core: a local link handler with no network service dependencies, no multi-flavor system, no analytics, and no disabled feature module references. The goal is a clean, compilable codebase where every line of code serves a real purpose.

## Current State Analysis

### Critical Compilation Issue
The project **cannot compile in its current state**. `LinkSheetApp.kt` imports modules from 8 disabled feature modules (browser, devicecompat, downloader, engine, libredirect, profile, shizuku, wiki) that are not included in `settings.gradle.kts`. Only `features/app` is included.

### Dependency Map of Dead Code
```
LinkSheetApp.kt
  ├── AnalyticsServiceModule       → AptabaseAnalyticsClient (network)
  ├── RemoteConfigClientModule      → RemoteConfigClient → BuildConfig.API_HOST (network)
  ├── ShizukuServiceModule          → ShizukuServiceConnection (disabled feature)
  ├── ShizukuModule                 → features/shizuku (disabled)
  ├── LibRedirectFeatureModule      → features/libredirect (disabled)
  ├── LibRedirectMigratorModule     → features/libredirect (disabled)
  ├── LinkEngineFeatureModule       → features/engine (disabled)
  ├── WikiFeatureModule             → features/wiki (disabled)
  ├── DownloaderModule              → features/downloader (disabled)
  ├── PrivateBrowsingModule         → features/browser (disabled)
  ├── ProfileFeatureModule          → features/profile (disabled)
  ├── CompatModule                  → features/devicecompat (disabled)
  ├── VersionTrackerModule          → depends on BaseAnalyticsService
  ├── StatisticsModule              → depends on RemoteConfigClientModule
  └── WorkDelegatorServiceModule    → depends on RemoteConfigClientModule
```

---

## Implementation Plan

### Phase 1: Make the Project Compilable (Critical Path)

These changes are ordered to restore compilation first, then clean up.

- [ ] **1.1** Enable all referenced feature modules in `settings.gradle.kts`
  - File: `settings.gradle.kts:21-41`
  - Add `includeProject` entries for: `:feature-browser` (features/browser), `:feature-devicecompat` (features/devicecompat), `:feature-downloader` (features/downloader), `:feature-engine` (features/engine), `:feature-libredirect` (features/libredirect), `:feature-profile` (features/profile), `:feature-shizuku` (features/shizuku), `:feature-systeminfo` (features/systeminfo), `:feature-wiki` (features/wiki)
  - Also add any transitive dependencies the engine module needs: `:sdk-common` (sdk/common), `:test-core`, `:test-fake`, `:config`
  - Rationale: The app module imports all these features. Without them, compilation is impossible. We enable them first to get a green build, then remove them one by one.

- [ ] **1.2** Add missing `buildConfigField` entries to `app/build.gradle.kts`
  - File: `app/build.gradle.kts:14-24`
  - Currently missing: `FLAVOR_CONFIG`, `BUILT_AT`, `API_HOST`, `LINK_BUY_ME_A_COFFEE`, `LINK_DISCORD`, `GITHUB_WORKFLOW_RUN_ID`, `COMMIT`
  - Add placeholder values so BuildConfig compiles:
    - `FLAVOR_CONFIG` → `""` (empty string, no flavor)
    - `BUILT_AT` → `"0L"` (zero timestamp)
    - `API_HOST` → `""` (empty, no remote API)
    - `LINK_BUY_ME_A_COFFEE` → `""` (empty)
    - `LINK_DISCORD` → `""` (empty)
    - `GITHUB_WORKFLOW_RUN_ID` → `""` (empty)
    - `COMMIT` → `"unknown"` (placeholder)
  - Rationale: BuildConfig fields are referenced throughout the codebase. Adding placeholders lets us compile while we remove the references.

- [ ] **1.3** Verify the project compiles
  - Run: `./gradlew :app:compileDebugKotlin`
  - Fix any remaining compilation errors before proceeding
  - Rationale: All subsequent work depends on having a compilable baseline.

---

### Phase 2: Remove Analytics System (Aptabase + Telemetry)

- [ ] **2.1** Delete the entire analytics module directory
  - Delete: `app/src/main/java/fe/linksheet/module/analytics/` (all 7 files)
    - `AnalyticsClient.kt`
    - `AnalyticsService.kt`
    - `BaseAnalyticsService.kt`
    - `BatchedEventQueue.kt`
    - `AnalyticsEvent.kt`
    - `TelemetryLevel.kt`
    - `TelemetryIdentity.kt`
    - `client/AptabaseAnalyticsClient.kt`
    - `client/DebugLogAnalyticsClient.kt`
  - Rationale: All analytics code is Supabase/Aptabase-dependent. No part of it is useful without a remote analytics backend.

- [ ] **2.2** Remove analytics from `LinkSheetApp.kt`
  - File: `app/src/main/java/fe/linksheet/LinkSheetApp.kt:34-35,139,141`
  - Remove imports: `AnalyticsServiceModule`, `DebugLogAnalyticsClient`
  - Remove from `provideKoinModules()`: `AnalyticsServiceModule`, `provideAnalyticsClient()`
  - Remove the `provideAnalyticsClient()` method entirely
  - Rationale: Analytics module is deleted; these references must go.

- [ ] **2.3** Remove analytics from `DebugLinkSheetApp.kt`
  - File: `app/src/debug/java/fe/linksheet/debug/DebugLinkSheetApp.kt:16-17,52-55`
  - Remove imports: `DebugLogAnalyticsClient`, `aptabaseAnalyticsClientModule`
  - Remove `provideAnalyticsClient()` override method
  - Rationale: Analytics module is deleted.

- [ ] **2.4** Remove `BaseAnalyticsService` dependency from `VersionTracker`
  - File: `app/src/main/java/fe/linksheet/module/versiontracker/VersionTracker.kt`
  - Remove `BaseAnalyticsService` parameter and all analytics-related code
  - Remove the `createAppStartEvent()` function and `analyticsService.enqueue()` calls
  - Keep the version tracking logic (storing last version) as it's useful locally
  - Simplify `VersionTrackerModule` to not depend on analytics
  - Rationale: Version tracking is useful for detecting first-run/updates, but the analytics event emission is not.

- [ ] **2.5** Remove analytics from `MainViewModel`
  - File: `app/src/main/java/fe/linksheet/module/viewmodel/MainViewModel.kt:19-21,43,53-55,122-129`
  - Remove `BaseAnalyticsService` dependency
  - Remove `telemetryLevel`, `telemetryShowInfoDialog` fields
  - Remove `enqueueNavEvent()`, `updateTelemetryLevel()` methods
  - Rationale: ViewModel no longer needs to interact with analytics.

- [ ] **2.6** Remove analytics from `PrivacySettingsViewModel`
  - File: `app/src/main/java/fe/linksheet/module/viewmodel/PrivacySettingsViewModel.kt`
  - Remove `BaseAnalyticsService` dependency
  - Remove `telemetryLevel`, `updateTelemetryLevel()`, `resetIdentifier()`
  - Rationale: Privacy settings no longer manage telemetry.

- [ ] **2.7** Remove analytics from `MainActivity`
  - File: `app/src/main/java/fe/linksheet/activity/main/MainActivity.kt:69-87`
  - Remove the entire `if (Build.IsDebug)` block that handles telemetry dialog
  - Remove `rememberAnalyticDialog` import and usage
  - Rationale: Analytics dialog no longer exists.

- [ ] **2.8** Remove analytics from `PrivacySettingsRoute`
  - File: `app/src/main/java/fe/linksheet/composable/page/settings/privacy/PrivacySettingsRoute.kt`
  - Remove telemetry level display and configuration UI
  - Remove `rememberAnalyticDialog` import
  - Keep only the "Show as referrer" setting
  - Rationale: Privacy page no longer shows telemetry controls.

- [ ] **2.9** Delete `AnalyticsDialog.kt`
  - Delete: `app/src/main/java/fe/linksheet/composable/page/settings/privacy/analytics/AnalyticsDialog.kt`
  - Delete the entire `analytics/` directory
  - Rationale: Dialog for configuring telemetry levels; no longer needed.

- [ ] **2.10** Remove analytics-related preferences from `AppPreferences`
  - File: `app/src/main/java/fe/linksheet/module/preference/app/AppPreferences.kt:77-84,96,107,127`
  - Remove: `telemetryId`, `telemetryIdentity`, `telemetryLevel`, `telemetryShowInfoDialog`, `enableAnalytics` experiment
  - Remove `TelemetryIdentity` and `TelemetryLevel` imports
  - Remove from `sensitivePreferences` set
  - Rationale: These preferences only exist for analytics.

- [ ] **2.11** Remove analytics-related strings from all locale files
  - Files: `app/src/main/res/values*/strings.xml` (28+ locale files)
  - Remove strings with keys containing: `telemetry`, `analytics`, `aptabase`
  - Rationale: Dead string resources waste space and confuse translators.

- [ ] **2.12** Remove `APTABASE_API_KEY` and `ANALYTICS_SUPPORTED` from build config
  - File: `app/build.gradle.kts:22-23`
  - Remove: `buildConfigField("String", "APTABASE_API_KEY", "\"\"")` and `buildConfigField("Boolean", "ANALYTICS_SUPPORTED", "true")`
  - Rationale: No longer referenced after analytics removal.

- [ ] **2.13** Remove `NetworkStateService` references from analytics code
  - `NetworkStateService` is used by `BatchedEventQueue` and `AnalyticsService` for network-aware event sending
  - Check if `NetworkStateService` is used elsewhere (it's referenced in `ImprovedIntentResolver`, `RealLinkEngine`, etc.)
  - If only used by analytics, delete `NetworkStateServiceModule` from `LinkSheetApp.kt:123`
  - If used elsewhere, keep but remove analytics dependency
  - Rationale: Remove unused infrastructure.

---

### Phase 3: Remove Remote Config System

- [ ] **3.1** Delete the remote config module directory
  - Delete: `app/src/main/java/fe/linksheet/module/remoteconfig/` (all 3 files)
    - `RemoteConfigClient.kt`
    - `RemoteConfigRepository.kt`
    - `RemoteConfigPreferences.kt`
  - Rationale: Remote config fetches from a Supabase-hosted API. No local fallback exists.

- [ ] **3.2** Delete `WorkDelegatorService` and its module
  - Delete: `app/src/main/java/fe/linksheet/module/workmanager/WorkDelegatorService.kt`
  - Remove `WorkDelegatorServiceModule` from `LinkSheetApp.kt:57,147`
  - Rationale: WorkDelegatorService's sole purpose is scheduling the remote config fetch worker.

- [ ] **3.3** Remove remote config from `MainViewModel`
  - File: `app/src/main/java/fe/linksheet/module/viewmodel/MainViewModel.kt:48,56-57,132-136`
  - Remove `WorkDelegatorService` dependency
  - Remove `remoteConfigDialogDismissed`, `remoteConfig`, `setRemoteConfig()`
  - Rationale: Remote config feature is deleted.

- [ ] **3.4** Remove remote config from `MainActivity`
  - File: `app/src/main/java/fe/linksheet/activity/main/MainActivity.kt:55-67`
  - Remove `remoteConfigDialogDismissed` and `remoteConfigDialog` logic
  - Remove `rememberRemoteConfigDialog` import
  - Rationale: Remote config dialog no longer exists.

- [ ] **3.5** Delete `RemoteConfigDialog.kt`
  - Delete: `app/src/main/java/fe/linksheet/composable/page/settings/privacy/remoteconfig/RemoteConfigDialog.kt`
  - Delete the entire `remoteconfig/` directory
  - Rationale: UI for remote config; feature deleted.

- [ ] **3.6** Remove `remoteConfig` preference from `AppPreferences`
  - File: `app/src/main/java/fe/linksheet/module/preference/app/AppPreferences.kt:96`
  - Remove: `val remoteConfig = boolean("remote_config", false)`
  - Rationale: Preference no longer consumed.

- [ ] **3.7** Remove remote config from `ThemeSettingsViewModel`
  - File: `app/src/main/java/fe/linksheet/module/viewmodel/ThemeSettingsViewModel.kt`
  - Remove `RemoteConfigRepository` dependency if present
  - Rationale: Theme settings should not depend on remote config.

- [ ] **3.8** Remove `API_HOST` from build config
  - File: `app/build.gradle.kts`
  - Remove: `buildConfigField` for `API_HOST`
  - Rationale: No longer referenced after remote config removal.

- [ ] **3.9** Remove remote config test files
  - Delete: `app/src/test/java/fe/linksheet/module/remoteconfig/` (all 3 test files)
    - `RemoteConfigPreferencesTest.kt`
    - `RemoteAssetWorkerTest.kt`
    - `RemoteAssetFetcherTest.kt`
  - Rationale: Tests for deleted code.

- [ ] **3.10** Clean up `LinkConstants.kt` remote config references
  - File: `app/src/main/java/fe/linksheet/util/LinkConstants.kt`
  - Remove `supabase`, `aptabase`, `shizuku`, `downloader`, `telemetry` links from `deprecated` map
  - Remove `ShizukuDownload` constant
  - Rationale: These link IDs map to remote-fetched URLs for deleted features.

---

### Phase 4: Remove Remote Resolver (Supabase API)

- [ ] **4.1** Delete `RemoteResolveRequest.kt`
  - Delete: `app/src/main/java/fe/linksheet/module/resolver/urlresolver/RemoteResolveRequest.kt`
  - Rationale: Makes authenticated API calls to Supabase host for URL resolution.

- [ ] **4.2** Remove `RemoteResolver` from `UrlResolver`
  - File: `app/src/main/java/fe/linksheet/module/resolver/urlresolver/base/UrlResolver.kt`
  - Remove `remoteResolver` constructor parameter
  - Remove `canResolveExternally()` method
  - Remove the external service branch in `resolve()` (lines 177-192)
  - Simplify: always use local resolution
  - Rationale: Without Supabase, remote resolution is impossible.

- [ ] **4.3** Update `UrlResolverModule` to remove Supabase references
  - File: `app/src/main/java/fe/linksheet/module/resolver/urlresolver/UrlResolverModule.kt`
  - Remove `LinkSheetAppConfig.supabaseHost()` and `LinkSheetAppConfig.supabaseApiKey()` usage
  - Remove `RemoteResolver` instantiation
  - Remove `import fe.linksheet.util.buildconfig.LinkSheetAppConfig`
  - Rationale: Module no longer wires up remote resolver.

- [ ] **4.4** Delete `old_Amp2HtmlResolveRequest.kt`
  - Delete: `app/src/main/java/fe/linksheet/module/resolver/urlresolver/amp2html/old_Amp2HtmlResolveRequest.kt`
  - Rationale: Already entirely commented out, references Supabase.

- [ ] **4.5** Remove "external service" toggle from settings UI
  - File: `app/src/main/java/fe/linksheet/composable/page/settings/link/redirect/FollowRedirectsSettingsRoute.kt:50,98-109`
  - File: `app/src/main/java/fe/linksheet/composable/page/settings/link/amp2html/Amp2HtmlSettingsRoute.kt:44,78-89`
  - Remove the `LinkSheetAppConfig.isPro()` gated "external service" switch
  - Remove `LinkSheetAppConfig` import
  - Rationale: External service requires Supabase; no longer available.

- [ ] **4.6** Remove external service preferences
  - File: `app/src/main/java/fe/linksheet/module/preference/app/AppPreferences.kt` (and related preference classes)
  - Remove `externalService` preferences from `FollowRedirects` and `Amp2Html` preference objects
  - Rationale: No external service to toggle.

- [ ] **4.7** Remove external service logic from `DefaultAppPreferenceRepository`
  - File: `app/src/main/java/fe/linksheet/module/preference/app/DefaultAppPreferenceRepository.kt:22-26`
  - Remove the `if (!LinkSheetAppConfig.isPro())` block that disables external services
  - Remove `LinkSheetAppConfig` import
  - Rationale: No flavor/pro distinction, no external service.

---

### Phase 5: Remove Flavor System

- [ ] **5.1** Delete `FlavorConfig.kt`
  - Delete: `app/src/main/java/fe/linksheet/util/buildconfig/FlavorConfig.kt`
  - Rationale: Parses base64 JSON containing `isPro`, `supabaseHost`, `supabaseApiKey`. All dead.

- [ ] **5.2** Delete `LinkSheetAppConfig.kt`
  - Delete: `app/src/main/java/fe/linksheet/util/buildconfig/LinkSheetAppConfig.kt`
  - Rationale: Wraps FlavorConfig; provides `isPro()`, `showDonationBanner()`, `supabaseHost()`, `supabaseApiKey()`. All dead.

- [ ] **5.3** Simplify `BuildType.kt`
  - File: `app/src/main/java/fe/linksheet/util/buildconfig/BuildType.kt`
  - Remove the `BuildType` enum entirely (or simplify to just Debug/Release)
  - Keep `Build.IsDebug` as it's used legitimately for debug-only features
  - Rationale: Nightly/ReleaseDebug types are CI pipeline artifacts. Only Debug vs Release matters.

- [ ] **5.4** Delete `LinkSheetInfo.kt`
  - Delete: `app/src/main/java/fe/linksheet/util/buildconfig/LinkSheetInfo.kt`
  - Rationale: Marked `@Deprecated`. Duplicate of `SystemInfoServiceModule`'s `BuildInfo`. References `BuildConfig.FLAVOR` and `BUILT_AT`.

- [ ] **5.5** Update all `LinkSheetInfo` references to use `SystemInfoService.buildInfo`
  - Files referencing `LinkSheetInfo`:
    - `app/src/main/java/fe/linksheet/module/viewmodel/AboutSettingsViewModel.kt`
    - `app/src/main/java/fe/linksheet/module/viewmodel/util/LogViewCommon.kt`
    - `app/src/main/java/fe/linksheet/composable/page/settings/about/VersionSettingsRoute.kt`
    - `app/src/main/java/fe/linksheet/composable/page/settings/about/AboutSettingsRoute.kt`
  - Replace `LinkSheetInfo.buildInfo` with injected `SystemInfoService.buildInfo`
  - Rationale: SystemInfoService already provides the same data without flavor references.

- [ ] **5.6** Remove `FLAVOR` build config field
  - File: `app/build.gradle.kts:21`
  - Remove: `buildConfigField("String", "FLAVOR", "\"Full\"")`
  - Update all `BuildConfig.FLAVOR` references (in `TelemetryIdentity`, `AptabaseAnalyticsClient`, `SystemInfoServiceModule`, `LinkSheetInfo`)
  - Rationale: No flavor system means no flavor field.

- [ ] **5.7** Remove `FLAVOR_CONFIG` build config field
  - File: `app/build.gradle.kts`
  - Remove the placeholder added in Phase 1
  - Rationale: Only consumed by `LinkSheetAppConfig` which is deleted.

- [ ] **5.8** Remove donation banner logic
  - File: `app/src/main/java/fe/linksheet/composable/page/home/MainRoute.kt:119-125`
  - Remove `LinkSheetAppConfig.showDonationBanner()` check and donation text
  - File: `app/src/main/java/fe/linksheet/composable/page/settings/about/AboutSettingsRoute.kt:93-102`
  - Remove donation link section gated by `LinkSheetAppConfig.showDonationBanner()`
  - Remove `BuildConfig.LINK_BUY_ME_A_COFFEE` reference
  - Rationale: Donation banner is a pro/free flavor distinction. No flavor = no banner.

- [ ] **5.9** Delete `FlavorConfigTest.kt`
  - Delete: `app/src/test/java/fe/linksheet/util/buildconfig/FlavorConfigTest.kt`
  - Rationale: Tests for deleted class.

- [ ] **5.10** Delete `AppSignature.kt`
  - Delete: `app/src/main/java/fe/linksheet/util/AppSignature.kt`
  - Rationale: References `app.linksheet.lib.flavors` from a disabled/removed flavor module. Signature checking is a CI/release concern, not app logic.

---

### Phase 6: Remove Shizuku Integration

- [ ] **6.1** Remove Shizuku from `LinkSheetApp.kt`
  - File: `app/src/main/java/fe/linksheet/LinkSheetApp.kt:52,124,149`
  - Remove imports: `ShizukuModule`, `ShizukuServiceModule`
  - Remove from `provideKoinModules()`: `ShizukuServiceModule`, `ShizukuModule`
  - Rationale: Shizuku feature module is disabled.

- [ ] **6.2** Delete Shizuku app-level code
  - Delete: `app/src/main/java/dev/zwander/shared/ShizukuUtil.kt`
  - Delete: `app/src/main/java/dev/zwander/shared/shizuku/ShizukuService.kt`
  - Delete: `app/src/main/java/fe/linksheet/module/shizuku/ShizukuStatus.kt`
  - Delete: `app/src/main/java/fe/linksheet/module/shizuku/ShizukuServiceConnection.kt`
  - Delete: `app/src/main/aidl/dev/zwander/shared/IShizukuService.aidl`
  - Rationale: Shizuku integration code. Feature module disabled.

- [ ] **6.3** Delete `ShizukuCard.kt`
  - Delete: `app/src/main/java/fe/linksheet/composable/page/home/card/ShizukuCard.kt`
  - Rationale: UI for Shizuku status; feature disabled.

- [ ] **6.4** Remove Shizuku from `MainRoute.kt`
  - File: `app/src/main/java/fe/linksheet/composable/page/home/MainRoute.kt:32,42-43,66-74,166-175`
  - Remove `ShizukuCard` import and usage
  - Remove `shizukuInstalled`, `shizukuRunning` state
  - Remove `BuildType` import for debug-only Shizuku card
  - Rationale: Shizuku card deleted.

- [ ] **6.5** Remove Shizuku from `MainViewModel`
  - File: `app/src/main/java/fe/linksheet/module/viewmodel/MainViewModel.kt:18,84-94`
  - Remove `ShizukuUtil` import
  - Remove `_shizukuRunning`, `shizukuRunning`, `_shizukuInstalled`, `shizukuInstalled` fields
  - Rationale: Shizuku state tracking deleted.

- [ ] **6.6** Remove Shizuku permissions from AndroidManifest.xml
  - File: `app/src/main/AndroidManifest.xml:23-25,57-63`
  - Remove: `moe.shizuku.manager.permission.*` permissions
  - Remove: `rikka.shizuku.ShizukuProvider` declaration
  - Rationale: No Shizuku integration means no permissions needed.

- [ ] **6.7** Remove Shizuku from `VerifiedLinkHandlersViewModel` and `DevSettingsViewModel`
  - Files that reference `ShizukuServiceConnection` or `ShizukuService`
  - Remove Shizuku-related domain verification logic
  - Rationale: Shizuku was used for programmatic domain verification; feature disabled.

- [ ] **6.8** Remove Shizuku strings from all locale files
  - Files: `app/src/main/res/values*/strings.xml`
  - Remove strings with keys containing: `shizuku`
  - Rationale: Dead string resources.

- [ ] **6.9** Remove Shizuku-related preferences
  - File: `app/src/main/java/fe/linksheet/module/preference/app/AppPreferences.kt:8,108`
  - Remove: `shizukuPreferences(registry)` and related import
  - Rationale: Shizuku preferences no longer consumed.

---

### Phase 7: Remove Statistics Service

- [ ] **7.1** Delete `StatisticsService.kt`
  - Delete: `app/src/main/java/fe/linksheet/module/statistic/StatisticsService.kt`
  - Rationale: Tracks usage time via preferences. Depends on `RemoteConfigClientModule`. Not useful without analytics.

- [ ] **7.2** Remove `StatisticsModule` from `LinkSheetApp.kt`
  - File: `app/src/main/java/fe/linksheet/LinkSheetApp.kt:53,142`
  - Remove import and module inclusion
  - Rationale: Module deleted.

- [ ] **7.3** Remove `useTimeMs` preference from `AppPreferences`
  - File: `app/src/main/java/fe/linksheet/module/preference/app/AppPreferences.kt:67-68`
  - Remove: `val useTimeMs = long("use_time", 0)`
  - Remove from `sensitivePreferences` set
  - Rationale: Only consumed by StatisticsService.

---

### Phase 8: Remove Unused Build Config Fields

- [ ] **8.1** Remove `BUILT_AT` build config field
  - Used by: `LinkSheetInfo` (deleted), `SystemInfoServiceModule`, `AboutSettingsRoute`, `VersionSettingsRoute`
  - Replace `BuildConfig.BUILT_AT` in remaining files with a simple timestamp or remove the feature
  - Rationale: CI-specific field. For a local build, a build timestamp can be generated differently.

- [ ] **8.2** Remove `GITHUB_WORKFLOW_RUN_ID` build config field
  - Used by: `LinkSheetInfo` (deleted), `SystemInfoServiceModule`
  - Replace with empty/null in `BuildInfo` constructor
  - Rationale: CI-specific field.

- [ ] **8.3** Remove `LINK_DISCORD` and `LINK_BUY_ME_A_COFFEE` build config fields
  - Used by: `AboutSettingsRoute.kt` (Discord link, donation link)
  - Hardcode or remove the Discord/donation links from About page
  - Rationale: External link constants that are CI-injected. Can be hardcoded.

- [ ] **8.4** Remove `COMMIT` build config field
  - Used by: `TelemetryIdentity` (deleted)
  - Rationale: Only used by deleted analytics code.

---

### Phase 9: Clean Up Disabled Feature Module Directories

- [ ] **9.1** Decide fate of each disabled feature module directory
  - `features/browser/` - **Keep**: Referenced by `PrivateBrowsingModule` in LinkSheetApp. Must be included in settings.gradle.kts OR its references removed from app.
  - `features/devicecompat/` - **Keep**: Referenced by `CompatModule` in LinkSheetApp. Active MIUI/OneUI compat.
  - `features/downloader/` - **Evaluate**: Referenced by `DownloaderModule` in LinkSheetApp. If downloader feature is wanted, include it. If not, remove from LinkSheetApp.
  - `features/engine/` - **Evaluate**: Referenced by `LinkEngineFeatureModule`. Experimental feature. Include or remove.
  - `features/libredirect/` - **Evaluate**: Referenced by `LibRedirectFeatureModule`. Include or remove.
  - `features/profile/` - **Keep**: Referenced by `ProfileFeatureModule` in ViewModelModule. Profile switching is active.
  - `features/shizuku/` - **Remove from app**: If Shizuku is being removed (Phase 6), remove `ShizukuModule` from LinkSheetApp. The directory can stay for future use.
  - `features/systeminfo/` - **Keep**: Referenced by `SystemInfoServiceModule`. Provides device info.
  - `features/wiki/` - **Evaluate**: Referenced by `WikiFeatureModule`. If wiki viewer is wanted, include it.
  - `lib/bottom-sheet/` and `lib/bottom-sheet-new/` - **Delete**: Not included in settings.gradle.kts, use old build system, not referenced by any active code.
  - Rationale: Each module must be either properly included or have all references removed.

- [ ] **9.2** Delete unused lib directories
  - Delete: `lib/bottom-sheet/` (entire directory)
  - Delete: `lib/bottom-sheet-new/` (entire directory)
  - Rationale: Not included in build, uses incompatible build system, not referenced.

- [ ] **9.3** Delete `buildSrc_disabled/` directory
  - Rationale: Already disabled, old build logic.

---

### Phase 10: Clean Up Remaining Dead Code

- [ ] **10.1** Remove `Experiments.enableAnalytics`
  - File: `app/src/main/java/fe/linksheet/module/preference/experiment/Experiments.kt:36-39`
  - Remove `enableAnalytics` experiment
  - Remove the migration at line 99
  - Rationale: Analytics experiment for a deleted feature.

- [ ] **10.2** Remove `Experiments.newShizuku`
  - File: `app/src/main/java/fe/linksheet/module/preference/experiment/Experiments.kt:57-59`
  - Remove `newShizuku` experiment
  - Rationale: Shizuku feature is removed.

- [ ] **10.3** Clean up `RemoteConfigPreferences` references in `LinkConstants.kt`
  - File: `app/src/main/java/fe/linksheet/util/LinkConstants.kt`
  - Remove the `deprecated` map entries for deleted features
  - Simplify `LinkSheetLinkTags` class
  - Rationale: Link tag lookups for deleted features.

- [ ] **10.4** Remove duplicate `collectAsStateWithLifecycle` imports in `MainActivity.kt`
  - File: `app/src/main/java/fe/linksheet/activity/main/MainActivity.kt:1,15-17`
  - Lines 1, 15, and 17 have duplicate imports
  - Rationale: Code hygiene.

- [ ] **10.5** Remove duplicate `collectAsStateWithLifecycle` import in `MainRoute.kt`
  - File: `app/src/main/java/fe/linksheet/composable/page/home/MainRoute.kt:1,24,28`
  - Rationale: Code hygiene.

- [ ] **10.6** Remove duplicate `collectAsStateWithLifecycle` imports in other files
  - Check: `FollowRedirectsSettingsRoute.kt:1,12`, `Amp2HtmlSettingsRoute.kt:1,10`
  - Rationale: Code hygiene.

- [ ] **10.7** Remove `NetworkStateServiceModule` from `LinkSheetApp.kt` if no longer needed
  - File: `app/src/main/java/fe/linksheet/LinkSheetApp.kt:123`
  - After analytics removal, check if `NetworkStateService` is still used by `ImprovedIntentResolver`, `RealLinkEngine`, etc.
  - If only analytics used it, remove the module
  - Rationale: Dead DI module.

- [ ] **10.8** Update `KoinModuleCheckTest.kt`
  - File: `app/src/test/java/fe/linksheet/koin/KoinModuleCheckTest.kt`
  - Remove all deleted module references from `injections` list
  - Remove: analytics, remote config, shizuku, statistics service definitions
  - Rationale: Test must match current DI graph.

- [ ] **10.9** Remove `PrivateBrowsingModule` duplicate in `LinkSheetApp.kt`
  - File: `app/src/main/java/fe/linksheet/LinkSheetApp.kt:120,153`
  - `PrivateBrowsingModule` is included twice in the module list
  - Rationale: Duplicate module inclusion.

---

### Phase 11: Remove Supabase/Analytics String Resources

- [ ] **11.1** Audit and remove supabase-related strings from all locale files
  - Files: `app/src/main/res/values*/strings.xml` (28+ files)
  - Search for keys containing: `supabase`, `aptabase`, `telemetry`, `remote_config`
  - Remove matching string entries
  - Rationale: Dead translations for deleted features.

- [ ] **11.2** Remove shizuku-related strings from all locale files
  - Search for keys containing: `shizuku`
  - Remove matching string entries
  - Rationale: Dead translations for removed feature.

---

### Phase 12: Final Verification

- [ ] **12.1** Run full compilation
  - `./gradlew :app:assembleDebug`
  - Fix any remaining compilation errors

- [ ] **12.2** Run unit tests
  - `./gradlew :app:testDebugUnitTest`
  - Fix any test failures from removed modules

- [ ] **12.3** Verify no remaining references to deleted code
  - Search for: `supabase`, `aptabase`, `AptabaseAnalytics`, `RemoteConfig`, `FlavorConfig`, `LinkSheetAppConfig`, `ShizukuUtil`, `ShizukuService`, `NetworkStateService` (if removed), `StatisticsService`, `BaseAnalyticsService`, `TelemetryLevel`, `TelemetryIdentity`
  - All searches should return zero results in `app/src/main/`

- [ ] **12.4** Verify no remaining unused BuildConfig fields
  - Search for: `BuildConfig.FLAVOR`, `BuildConfig.FLAVOR_CONFIG`, `BuildConfig.APTABASE_API_KEY`, `BuildConfig.ANALYTICS_SUPPORTED`, `BuildConfig.API_HOST`, `BuildConfig.COMMIT`
  - All should return zero results

---

## Verification Criteria

1. **Compilation**: `./gradlew :app:assembleDebug` succeeds without errors
2. **No Supabase references**: `grep -r "supabase" app/src/main/` returns zero results
3. **No Aptabase references**: `grep -r "aptabase\|Aptabase" app/src/main/` returns zero results
4. **No flavor system**: `grep -r "FlavorConfig\|LinkSheetAppConfig\|isPro" app/src/main/` returns zero results
5. **No Shizuku references**: `grep -r "Shizuku\|shizuku" app/src/main/` returns zero results (excluding string resources if kept)
6. **No analytics references**: `grep -r "AnalyticsService\|BaseAnalyticsService\|TelemetryLevel\|TelemetryIdentity" app/src/main/` returns zero results
7. **No remote config references**: `grep -r "RemoteConfig\|RemoteAsset" app/src/main/` returns zero results
8. **Tests pass**: `./gradlew :app:testDebugUnitTest` passes

## Potential Risks and Mitigations

1. **Feature module dependency chains**
   - Risk: features/engine depends on features/browser and features/downloader, which depend on other disabled modules
   - Mitigation: Enable all referenced modules first (Phase 1), then remove them one at a time with compilation checks

2. **Database migration issues**
   - Risk: Removing feature modules may leave orphaned Room database tables
   - Mitigation: Add migration to drop unused tables, or accept that old data is ignored

3. **String resource key errors**
   - Risk: Removing strings that are still referenced somewhere
   - Mitigation: Only remove strings after confirming all code references are removed; use compilation as final check

4. **Koin DI graph validation**
   - Risk: Removing modules may break dependency chains for remaining modules
   - Mitigation: `KoinModuleCheckTest` validates the DI graph; ensure it's updated and passes

5. **InterconnectService2 depends on PreferredAppRepository**
   - Risk: This service is declared in AndroidManifest and may be expected by external apps
   - Mitigation: Keep InterconnectService2 as it's a local feature (domain selection), not a network dependency

## Alternative Approaches

1. **Incremental removal vs. big bang**: This plan uses incremental removal (analytics first, then remote config, then flavors). An alternative is to delete everything at once, which is faster but harder to debug when compilation breaks.

2. **Keep feature modules, remove only network dependencies**: Instead of removing entire feature modules, strip only the network-dependent parts. This preserves more functionality but leaves more code to maintain.

3. **Replace remote resolver with local-only resolver**: Instead of deleting RemoteResolver entirely, replace it with a stub that always falls back to local resolution. This minimizes changes to UrlResolver's API.
