# Mechanic AI (मैकेनिक एआई)
> **Tagline:** Smart Vehicle Diagnostics, Made Simple.  
> **Package ID:** `com.aistudio.mechanicai.kqzvrt`

---

## 1. Project Overview
Mechanic AI is a production-grade automotive diagnostic application engineered for automobile mechanics, workshop owners, diagnostic technicians, and vehicle owners. It combines deep automotive engineering rules with AI-powered fault code reasoning to deliver step-by-step diagnostic procedures, multimeter test values, sensor pinouts, and technical repair libraries.

---

## 2. Key Features
- **10-Point Structured Diagnostic Engine**:
  1. Fault Code Meaning & Severity
  2. Systems Potentially Involved
  3. Common Symptoms
  4. Possible Root Causes
  5. Step-by-Step Diagnostic & Inspection Procedure
  6. Relevant Sensors, Wiring, Connectors & Relays to Test
  7. Expected Multimeter & Sensor Values (Idle & Load specs)
  8. Recommended Repairs & Replacements
  9. Required Tools & Diagnostic Equipment
  10. Critical Automotive Safety Precautions
- **Hybrid AI & Verified Automotive Knowledge Engine**:
  - Direct integration with Google Gemini AI (`gemini-2.5-flash`) via Retrofit and secure `BuildConfig.GEMINI_API_KEY`.
  - Built-in High-Accuracy Automotive Knowledge Base for instant, deep diagnostics even when offline or before configuring an API key.
- **Bilingual Hindi & English (हिंदी / English) Support**:
  - Full instant language toggle accessible across the top bar and profile settings.
- **Persistent Local Database (Room SQLite)**:
  - Stores all diagnostic scans with full vehicle details (make, model, year, fuel type, engine capacity, live sensor readings, user symptoms).
  - Search & filter history by code, brand, or date.
  - Personal repair notes editor with instant on-device saving.
  - Bookmarks for quick reference.
  - Delete individual records or clear history with confirmation dialog.
- **15 Technical Repair Libraries**:
  - Engine Fault Codes (P0300 misfires)
  - Diesel Diagnostics (Common Rail CRDi, rail pressure collapse)
  - Petrol Diagnostics (GDI carbon fouling, high pressure pump)
  - Fuel Injection (STFT & LTFT fuel trims)
  - Turbocharger & Boost Control (VGT vanes, wastegate)
  - EGR & DPF Systems (Soot mass, differential pressure, forced regen)
  - MAP & MAF Sensors (Multimeter voltage & frequency testing)
  - ABS & Braking Systems (Active Hall vs Passive VR wheel speed sensors)
  - AC & Climate Control (High/Low side manifold gauge pressure diagnostics)
  - Battery & Charging System (Alternator AC diode ripple & parasitic drain)
  - Starter Motor (Cranking voltage drop testing)
  - Cooling System (Combustion gas head gasket test, thermostat)
  - Electrical Diagnostics (CAN-Bus 60Ω termination test & split termination)
  - Transmission Faults (Torque converter clutch shudder & solenoid testing)
  - Hybrid & EV Safety Basics (400V-800V high voltage orange cables, MSD disconnect)

---

## 3. Android Installation Pathways (Developing from Phone)

### Path A: Installable PWA (No Computer Needed)
1. Open the shared development or preview URL in **Google Chrome** on your Android phone.
2. Tap the Chrome three-dot menu (**⋮**) in the top right corner.
3. Tap **"Install app"** or **"Add to Home screen"**.
4. Confirm installation. The Mechanic AI icon with the custom electric blue & dark navy automotive theme will appear directly on your phone's home screen.
5. Launches in fullscreen standalone mode without browser chrome, providing a native app experience.

### Path B: Native Android APK
1. In Google AI Studio, tap the **Settings / Export** button in the header and choose **"Download Project as ZIP"**.
2. To compile an APK locally or on a server, run:
   ```bash
   gradle assembleDebug
   ```
   Or for release:
   ```bash
   gradle assembleRelease
   ```
3. The APK will be generated at `app/build/outputs/apk/debug/app-debug.apk`.
4. Transfer `app-debug.apk` to your Android phone via WhatsApp, Google Drive, or USB.
5. On your phone, tap the file in your Files app and confirm **"Install"** (enabling unknown sources if prompted).

---

## 4. Architecture & Source Directory
```
/app/src/main/
├── AndroidManifest.xml
├── java/com/example/
│   ├── MainActivity.kt
│   ├── data/
│   │   ├── automotive/AutomotiveKnowledgeBase.kt
│   │   ├── local/AppDatabase.kt, DiagnosticDao.kt, DiagnosticRecord.kt, UserPreferences.kt
│   │   ├── model/DiagnosticResult.kt, RepairGuide.kt
│   │   ├── remote/GeminiApiService.kt, GeminiModels.kt
│   │   └── repository/DiagnosticRepository.kt, LibraryRepository.kt
│   └── ui/
│       ├── MechanicViewModel.kt
│       ├── components/AppTopBar.kt, AppBottomNav.kt
│       ├── localization/LocaleStrings.kt
│       ├── screens/
│       │   ├── WelcomeScreen.kt, AuthScreen.kt, DashboardScreen.kt, SearchScreen.kt
│       │   ├── DiagnosticResultScreen.kt, HistoryScreen.kt, LibraryScreen.kt
│       │   ├── GuideDetailScreen.kt, ProfileScreen.kt, InstallationScreen.kt, AboutScreen.kt
│       └── theme/Color.kt, Theme.kt, Type.kt
└── res/
    ├── drawable/ic_launcher_foreground.xml, ic_launcher_background.xml, mechanic_ai_icon_*.jpg
    ├── mipmap-*/ic_launcher.png, ic_launcher_round.png
    └── values/strings.xml, colors.xml
```

---

## 5. Security & Safety Rules
- **No API Keys in Version Control**: Secrets are configured via `.env` / AI Studio Secrets panel.
- **Safety Disclaimer**: Diagnostic results are recommendations based on automotive engineering principles. Technicians must observe 1000V rated safety procedures around High Voltage orange EV cables and high-pressure CRDi diesel fuel circuits (2000 bar).
