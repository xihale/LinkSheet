# LinkSheet Makefile

GRADLEW := ./gradlew
ADB := adb

# Build variants
DEBUG_TASK := assembleDebug
FOSS_NIGHTLY_TASK := assembleFossNightly
FOSS_RELEASE_DEBUG_TASK := assembleFossReleaseDebug

# Output directories (relative to app/build/outputs/apk)
DEBUG_APK_DIR := app/build/outputs/apk/debug
FOSS_NIGHTLY_APK_DIR := app/build/outputs/apk/foss/nightly
FOSS_RELEASE_DEBUG_APK_DIR := app/build/outputs/apk/foss/releaseDebug

.PHONY: all help debug foss-nightly foss-release-debug clean install-debug install-foss-nightly install-foss-release-debug

all: install-debug

help:
	@echo "LinkSheet Build System"
	@echo ""
	@echo "Usage:"
	@echo "  make debug                      - Build debug APK"
	@echo "  make foss-nightly               - Build Foss Nightly APK"
	@echo "  make foss-release-debug         - Build Foss Release Debug APK"
	@echo "  make install-debug              - Build and install debug APK to device"
	@echo "  make install-foss-nightly       - Build and install Foss Nightly APK to device"
	@echo "  make install-foss-release-debug - Build and install Foss Release Debug APK to device"
	@echo "  make clean                      - Clean build artifacts"
	@echo ""

debug:
	@echo "Building Debug APK..."
	$(GRADLEW) $(DEBUG_TASK)

foss-nightly:
	@echo "Building Foss Nightly APK..."
	$(GRADLEW) $(FOSS_NIGHTLY_TASK)

foss-release-debug:
	@echo "Building Foss Release Debug APK..."
	$(GRADLEW) $(FOSS_RELEASE_DEBUG_TASK)

install-debug: 
	@echo "Installing Debug APK..."
	@find $(DEBUG_APK_DIR) -name "*.apk" -print0 | xargs -0 ls -1 -t | head -1 | xargs $(ADB) install -r

install-foss-nightly: 
	@echo "Installing Foss Nightly APK..."
	@find $(FOSS_NIGHTLY_APK_DIR) -name "*.apk" -print0 | xargs -0 ls -1 -t | head -1 | xargs $(ADB) install -r

install-foss-release-debug:
	@echo "Installing Foss Release Debug APK..."
	@find $(FOSS_RELEASE_DEBUG_APK_DIR) -name "*.apk" -print0 | xargs -0 ls -1 -t | head -1 | xargs $(ADB) install -r

clean:
	@echo "Cleaning project..."
	$(GRADLEW) clean
