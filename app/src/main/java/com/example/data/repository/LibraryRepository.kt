package com.example.data.repository

import com.example.data.model.LibraryCategory
import com.example.data.model.RepairGuide

object LibraryRepository {

    val categories = listOf(
        LibraryCategory("engine", "Engine Fault Codes", "इंजन फॉल्ट कोड्स", "engineering", 4),
        LibraryCategory("diesel", "Diesel Engine Diagnostics", "डीज़ल इंजन डायग्नोस्टिक्स", "oil_barrel", 3),
        LibraryCategory("petrol", "Petrol Engine Diagnostics", "पेट्रोल इंजन डायग्नोस्टिक्स", "local_gas_station", 3),
        LibraryCategory("fuel_injection", "Fuel Injection (CRDi & GDI)", "फ्यूल इंजेक्शन (CRDi और GDI)", "tune", 3),
        LibraryCategory("turbo", "Turbocharger & Boost Control", "टर्बोचार्जर और बूस्ट कंट्रोल", "speed", 3),
        LibraryCategory("egr_dpf", "EGR & DPF Systems", "EGR और DPF सिस्टम", "filter_alt", 3),
        LibraryCategory("map_maf", "MAP & MAF Sensors", "MAP और MAF सेंसर", "sensors", 3),
        LibraryCategory("abs_brake", "ABS & Braking Systems", "ABS और ब्रेकिंग सिस्टम", "security", 3),
        LibraryCategory("ac_climate", "AC & Climate Control", "AC और क्लाइमेट कंट्रोल", "ac_unit", 3),
        LibraryCategory("battery_charging", "Battery & Charging System", "बैटरी और चार्जिंग सिस्टम", "battery_charging_full", 3),
        LibraryCategory("starter", "Starter Motor Diagnostics", "स्टार्टर मोटर डायग्नोस्टिक्स", "flash_on", 2),
        LibraryCategory("cooling", "Cooling System", "कूलिंग सिस्टम", "thermostat", 3),
        LibraryCategory("electrical", "Electrical Diagnostics & CAN-Bus", "इलेक्ट्रिकल और CAN-Bus", "electrical_services", 3),
        LibraryCategory("transmission", "Transmission Faults", "ट्रांसमिशन फॉल्ट्स", "settings", 3),
        LibraryCategory("hybrid_ev", "Hybrid & EV Safety Basics", "हाइब्रिड और EV सुरक्षा बेसिक्स", "bolt", 3)
    )

    val guides = listOf(
        // 1. Engine
        RepairGuide(
            id = "eng_misfire_01",
            categoryId = "engine",
            categoryName = "Engine Fault Codes",
            title = "Systematic Cylinder Misfire Isolation (P0300 - P0308)",
            titleHi = "सिलेंडर मिसफायर की सटीक जांच प्रक्रिया (P0300 - P0308)",
            summary = "Step-by-step isolation protocol for random and cylinder-specific misfires using scan tool live counters, coil swapping, and compression testing.",
            summaryHi = "स्कैन टूल, कॉइल स्वैपिंग और कम्प्रेशन टेस्टिंग के ज़रिए मिसफायर पकड़ने का व्यावहारिक तरीका।",
            symptoms = listOf("Rough idle", "MIL flashing under load", "Loss of power", "Strong exhaust fuel smell"),
            testPoints = listOf("Coil secondary spark output", "Injector pulse signal (Noid light)", "Cylinder relative compression", "Intake manifold vacuum leak"),
            multimeterSpecs = listOf(
                "Coil Primary Resistance: 0.5Ω - 1.2Ω",
                "Coil B+ Power Pin: 12.0V - 14.2V (Key ON)",
                "Injector Resistance: 12Ω - 14.5Ω (balanced within 0.4Ω)",
                "Cylinder Compression: Minimum 150 PSI, max 10% cylinder-to-cylinder variance"
            ),
            stepByStep = listOf(
                "1. Connect OBD-II scanner and view misfire count PID per cylinder.",
                "2. Swap suspected cylinder's ignition coil with known good adjacent cylinder.",
                "3. Clear codes and retest: If misfire migrates, coil is bad. If misfire stays, check plug, injector, or compression.",
                "4. Check for unmetered vacuum intake leaks with smoke machine.",
                "5. Perform dry and wet mechanical compression test if electrical checks pass."
            ),
            safetyTips = listOf("Do NOT drive when MIL is flashing. Catalyst meltdown occurs within minutes."),
            isVerifiedOEM = true
        ),

        // 2. Diesel
        RepairGuide(
            id = "dsl_rail_pressure_01",
            categoryId = "diesel",
            categoryName = "Diesel Engine Diagnostics",
            title = "Common Rail Diesel (CRDi) High Pressure Diagnosis",
            titleHi = "कॉमन रेल डीज़ल (CRDi) हाई प्रेशर डायग्नोसिस",
            summary = "Troubleshooting hard starting, rail pressure drop, and limp mode in Bosch, Delphi, and Denso CRDi diesel engines.",
            summaryHi = "बॉश, डेल्फी और डेंसो CRDi इंजनों में स्टार्टिंग और हाई प्रेशर ड्रॉप की जांच।",
            symptoms = listOf("Long crank before start", "Cut-off under hard acceleration", "Glow plug / check engine lamp on"),
            testPoints = listOf("Low pressure tank pump delivery (3.5 - 5.0 bar)", "Suction Control Valve (SCV)", "High pressure fuel rail sensor", "Injector back-leak return"),
            multimeterSpecs = listOf(
                "SCV Solenoid Resistance: 1.8Ω - 4.5Ω (cold)",
                "Fuel Rail Pressure Sensor 5V Reference: 4.95V - 5.05V",
                "Fuel Rail Pressure Sensor Signal: 0.5V at 0 bar, 1.0V - 1.3V at idle (250-300 bar)",
                "Low Pressure In-tank Pump: 4A - 7A current draw"
            ),
            stepByStep = listOf(
                "1. Verify clean diesel fuel supply with no air bubbles in clear feed line.",
                "2. Measure low pressure delivery to high pressure pump (minimum 3.5 bar).",
                "3. Run graduated cylinder injector leak-off test. Any injector returning >20ml in 2 mins at idle causes rail pressure collapse.",
                "4. Inspect fuel filter bowl for magnetic metal brass flakes (indicates pump internal disintegration).",
                "5. Perform scan tool rail pressure relief valve leakage test."
            ),
            safetyTips = listOf("CRDi pressures reach 2000 BAR. Never loosen high pressure fittings while cranking; skin puncture risk!"),
            isVerifiedOEM = true
        ),

        // 3. Petrol
        RepairGuide(
            id = "pet_gdi_01",
            categoryId = "petrol",
            categoryName = "Petrol Engine Diagnostics",
            title = "Gasoline Direct Injection (GDI) Carbon Buildup & High Pressure Test",
            titleHi = "GDI पेट्रोल इंजन: इनटेक वाल्व कार्बन और हाई प्रेशर टेस्टिंग",
            summary = "Diagnosing hesitation, rough cold start, and low fuel pressure on direct-injected petrol vehicles.",
            summaryHi = "GDI पेट्रोल गाड़ियों में इनटेक वाल्व पर कार्बन जमने और हाई प्रेशर पंप की जांच।",
            symptoms = listOf("Rough cold idle", "Hesitation on throttle tip-in", "Reduced fuel economy", "P0087 / P0171 codes"),
            testPoints = listOf("High pressure fuel pump cam follower wear", "Intake valve tulip carbon deposit", "Low pressure lift pump", "GDI injector leakage"),
            multimeterSpecs = listOf(
                "GDI Fuel Pressure Sensor: 0.5V at 0 bar, 1.2V - 1.5V at idle (40 - 50 bar)",
                "GDI Injector Peak & Hold Resistance: 1.2Ω - 2.5Ω (low impedance, driven by 65V boost circuit)",
                "Low pressure in-tank feed: 4.5 - 6.0 bar steady"
            ),
            stepByStep = listOf(
                "1. Check mechanical HPFP camshaft follower bucket for wear or puncture.",
                "2. Borescope inspect intake valves for heavy carbon crust due to lack of port fuel wash.",
                "3. Perform walnut shell blast decoking if valves are heavily restricted.",
                "4. Compare commanded vs actual high fuel rail pressure PID (40-150 bar).",
                "5. Check engine oil for petrol smell (leaking HPFP shaft seal dilutes crankcase oil)."
            ),
            safetyTips = listOf("Relieve residual 50+ bar fuel pressure via scanner or fuel pump fuse before opening GDI rail."),
            isVerifiedOEM = true
        ),

        // 4. Fuel Injection
        RepairGuide(
            id = "inj_trim_01",
            categoryId = "fuel_injection",
            categoryName = "Fuel Injection (CRDi & GDI)",
            title = "Mastering Fuel Trims (STFT & LTFT) for Root-Cause Diagnostics",
            titleHi = "फ्यूल ट्रिम (STFT और LTFT) से इंजन फॉल्ट पकड़ने का तरीका",
            summary = "How to read Short-Term and Long-Term fuel trims to instantly separate vacuum leaks, fuel pump deficits, and MAF faults.",
            summaryHi = "फ्यूल ट्रिम्स के ज़रिए वैक्यूम लीक, कमजोर फ्यूल पंप और खराब MAF सेंसर में फर्क पहचानें।",
            symptoms = listOf("P0171 / P0174 Lean", "P0172 / P0175 Rich", "Hesitation", "Failed emissions test"),
            testPoints = listOf("STFT & LTFT at Idle (650 RPM)", "STFT & LTFT at 2500 RPM (Cruise)", "O2 / A/F sensor response curve"),
            multimeterSpecs = listOf(
                "Healthy Combined Fuel Trim: -5% to +5%",
                "Vacuum Leak Signature: LTFT > +18% at idle, drops towards normal (+4%) at 2500 RPM",
                "Weak Fuel Pump / MAF Signature: LTFT normal at idle, climbs > +20% under 2500 RPM load",
                "Leaking Injector / High Pressure: LTFT negative (-15% to -25%) at idle"
            ),
            stepByStep = listOf(
                "1. Warm engine to closed-loop operating temperature (85°C+).",
                "2. Record STFT + LTFT total at idle speed.",
                "3. Hold engine speed steady at 2500 RPM for 30 seconds and record new total.",
                "4. If total fuel trim improves at higher RPM = VACUUM LEAK.",
                "5. If total fuel trim worsens at higher RPM = FUEL DELIVERY / MAF DEFICIT."
            ),
            safetyTips = listOf("Always confirm fuel trim readings in both open-loop warm-up and closed-loop cruise."),
            isVerifiedOEM = true
        ),

        // 5. Turbocharger
        RepairGuide(
            id = "trb_boost_01",
            categoryId = "turbo",
            categoryName = "Turbocharger & Boost Control",
            title = "Turbo Underboost (P0299) & Wastegate / VGT Diagnosis",
            titleHi = "टर्बो अंडरबूस्ट (P0299) और VGT वैक्यूम / वेस्टगेट जांच",
            summary = "Comprehensive troubleshooting for turbo lag, lack of boost, electronic actuators, and boost pipe leaks.",
            summaryHi = "टर्बो लैग, कम बूस्ट, इलेक्ट्रॉनिक एक्चुएटर और इंटरकूलर पाइप लीकेज की पूरी जांच।",
            symptoms = listOf("P0299 Boost Pressure Under Regulation", "Black smoke under acceleration", "Hissing or whooshing noise", "No top speed"),
            testPoints = listOf("Intake boost pipes & intercooler core", "VGT vacuum actuator rod stroke", "Boost pressure control solenoid (N75)", "Turbo compressor impeller play"),
            multimeterSpecs = listOf(
                "N75 Boost Solenoid Resistance: 14Ω - 22Ω",
                "VGT Position Sensor 5V Ref: 5.0V ± 0.05V",
                "VGT Position Sensor Output: 0.8V (vanes open) to 4.2V (vanes closed)",
                "Vacuum Supply to Solenoid: Minimum -22 inHg engine vacuum"
            ),
            stepByStep = listOf(
                "1. Pressurize intake tract with smoke/pressure tester to 15 PSI to find split boost hoses.",
                "2. Connect hand vacuum pump to VGT actuator: rod must move smoothly 10-14mm between -5 inHg and -18 inHg with NO binding.",
                "3. Check turbocharger compressor shaft for radial play (must NOT touch housing) and axial end-play (<0.1mm).",
                "4. Inspect exhaust side for stuck variable geometry vanes (carbon seized).",
                "5. Verify MAP sensor accuracy with key-on engine-off (must match ambient barometric pressure)."
            ),
            safetyTips = listOf("Allow turbo to cool completely before touching. Exhaust turbine reaches 900°C."),
            isVerifiedOEM = true
        ),

        // 6. EGR & DPF
        RepairGuide(
            id = "egr_dpf_01",
            categoryId = "egr_dpf",
            categoryName = "EGR & DPF Systems",
            title = "Diesel Particulate Filter (DPF) Regeneration & Differential Sensor Testing",
            titleHi = "DPF रीजेनरेशन और डिफरेंशियल प्रेशर सेंसर टेस्टिंग",
            summary = "Resolving DPF warning light, high soot load, pressure sensor faults, and forced service regeneration procedures.",
            summaryHi = "DPF वार्निंग लाइट, अधिक कालिख (Soot), प्रेशर सेंसर और रीजेनरेशन प्रक्रिया।",
            symptoms = listOf("DPF warning light on", "Engine in limp derate mode", "Exhaust fan running continuously"),
            testPoints = listOf("DPF differential pressure silicone hoses", "Pre-DPF & Post-DPF Exhaust Gas Temperature (EGT) sensors", "Thermostat operating temp (must exceed 80°C)"),
            multimeterSpecs = listOf(
                "DPF Differential Sensor (Idle): 3 - 8 mbar (0.5V - 0.7V)",
                "DPF Differential Sensor (2500 RPM): < 50 mbar (clean), > 100 mbar (severely restricted)",
                "EGT Sensors: Pt200 type resistance ~ 200Ω at 0°C, 350Ω at 200°C"
            ),
            stepByStep = listOf(
                "1. Inspect rubber pressure hoses between DPF metal pipes and sensor for melting or cracks.",
                "2. Check scan tool DPF soot load: If <40g, proceed to forced regeneration. If >50g, remove DPF for physical wash.",
                "3. Verify all prerequisites: Coolant > 80°C, fuel tank > 25%, zero active glow plug/EGR codes.",
                "4. Perform stationary forced regeneration or 20-minute highway cycle at 2500 RPM.",
                "5. Check engine oil level: change oil immediately if diesel fuel dilution is detected."
            ),
            safetyTips = listOf("Exhaust reaches 650°C during DPF regeneration. Perform outdoors away from flammable grass/fuels."),
            isVerifiedOEM = true
        ),

        // 7. MAP & MAF
        RepairGuide(
            id = "map_maf_01",
            categoryId = "map_maf",
            categoryName = "MAP & MAF Sensors",
            title = "Mass Air Flow (MAF) & Manifold Absolute Pressure (MAP) Multimeter Testing",
            titleHi = "MAF और MAP सेंसर की मल्टीमीटर से पिन-टू-पिन टेस्टिंग",
            summary = "Pinout test procedures for analog 0-5V and digital frequency MAF sensors, along with MAP pressure calibration curves.",
            summaryHi = "एनालॉग और डिजिटल फ्रीक्वेंसी MAF सेंसर और MAP सेंसर को मल्टीमीटर से जांचने की गाइड।",
            symptoms = listOf("Stalling on acceleration", "Black exhaust smoke", "P0101 / P0106 codes", "Surging idle"),
            testPoints = listOf("Sensor 12V B+ supply", "5V reference from ECM", "Signal ground (< 50mV)", "Signal voltage / frequency curve"),
            multimeterSpecs = listOf(
                "MAF Analog Voltage: 0.8V - 1.2V at idle, rises smoothly to 3.8V - 4.5V at WOT",
                "MAF Digital Frequency: 2.0 kHz at idle, rises to 8.0 kHz+ under full acceleration",
                "MAP Voltage Key ON Engine OFF: ~ 4.5V (100 kPa ambient barometric pressure)",
                "MAP Voltage at Idle: 0.9V - 1.4V (25 - 35 kPa engine vacuum)"
            ),
            stepByStep = listOf(
                "1. Connect digital multimeter black lead to battery negative terminal.",
                "2. Backprobe 5V reference pin: must read 4.95V to 5.05V with key ON.",
                "3. Backprobe signal ground pin: must read less than 0.05V (50 mV). High drop indicates bad harness ground.",
                "4. Backprobe signal pin: Snap throttle open and observe voltage response on fast min/max or scope.",
                "5. Spray MAF elements with dedicated cleaner if reading is lazy or sluggish."
            ),
            safetyTips = listOf("Never insert multimeter probes into front of female connector pins; this spreads the terminal and causes intermittent contact."),
            isVerifiedOEM = true
        ),

        // 8. ABS & Brakes
        RepairGuide(
            id = "abs_sensor_01",
            categoryId = "abs_brake",
            categoryName = "ABS & Braking Systems",
            title = "Active vs Passive Wheel Speed Sensor & Magnetic Tone Ring Diagnosis",
            titleHi = "एक्टिव और पैसिव ABS व्हील स्पीड सेंसर और मैग्नेटिक रिंग की जांच",
            summary = "Differentiating 2-wire Active Hall-effect sensors from Passive Inductive VR sensors, tone ring inspection, and ABS pump bleeding.",
            summaryHi = "एक्टिव और पैसिव ABS सेंसर में अंतर, मैग्नेटिक रिंग और ABS मॉड्यूल की टेस्टिंग।",
            symptoms = listOf("ABS & TCS warning lights on", "Pedal kickback at low speeds", "C0035 / C0040 / C0050 codes"),
            testPoints = listOf("Wheel speed sensor supply voltage", "Sensor mA current pulse (Active sensor)", "Magnetic encoder ring integrity", "ABS harness continuity"),
            multimeterSpecs = listOf(
                "Active Hall Sensor Supply: 11.5V - 12.5V from ABS module",
                "Active Hall Current Toggle: Switches between 7mA (low) and 14mA (high) as wheel rotates",
                "Passive VR Sensor Coil: 800Ω - 1600Ω across sensor pins",
                "Wheel Bearing Play: < 0.05mm axial movement"
            ),
            stepByStep = listOf(
                "1. Disconnect sensor plug and measure voltage from ABS module with key ON (Active sensors receive 12V).",
                "2. Inspect flexible wiring along suspension strut for fatigue breaks caused by steering movement.",
                "3. Place magnetic viewing card against wheel bearing seal to inspect magnetic encoder poles.",
                "4. If all 4 wheel speeds match in scan tool live data except one that drops to 0 at low speed, replace that sensor or hub.",
                "5. Always use scan tool automated bleed function when replacing ABS hydraulic modulator unit."
            ),
            safetyTips = listOf("Do not use magnetic pick-up tools or grinders near magnetic encoder wheel bearings."),
            isVerifiedOEM = true
        ),

        // 9. AC & Climate
        RepairGuide(
            id = "ac_diag_01",
            categoryId = "ac_climate",
            categoryName = "AC & Climate Control",
            title = "AC Manifold Gauge Pressure Diagnostics (R134a & R1234yf)",
            titleHi = "कार एसी मैनिफोल्ड गेज प्रेशर और कूलिंग समस्या की पहचान",
            summary = "Diagnosing high and low side pressure readings to pinpoint expansion valve blocks, compressor failure, and refrigerant undercharge.",
            summaryHi = "हाई और लो साइड प्रेशर गेज से कंप्रेसर, एक्सपेंशन वाल्व और गैस लीकेज की जांच।",
            symptoms = listOf("Warm air from vents", "AC compressor clutch not engaging", "Hissing noise from dash", "AC cycles rapidly"),
            testPoints = listOf("Low Side Gauge Pressure", "High Side Gauge Pressure", "Compressor clutch coil resistance", "Condenser fan high speed operation"),
            multimeterSpecs = listOf(
                "Low Side Normal: 28 - 35 PSI (at 2000 RPM, 30°C ambient)",
                "High Side Normal: 175 - 225 PSI (approx 2.5x to 3x ambient temperature in °F)",
                "Compressor Clutch Coil: 3.0Ω - 4.5Ω resistance",
                "Clutch Air Gap: 0.35mm - 0.65mm feeler gauge specification"
            ),
            stepByStep = listOf(
                "1. Connect manifold gauges to high and low service ports with engine OFF: Equalized static pressure should be 70-90 PSI at 25°C.",
                "2. Start engine, set AC to max cold and blower on high.",
                "3. If Low Side is very low (<15 PSI) and High Side is low (<100 PSI) = UNDERCHARGED / LEAK.",
                "4. If Low Side is in a vacuum and High Side is low = RESTRICTED EXPANSION VALVE / DRIER.",
                "5. If Low Side is high (>55 PSI) and High Side is low (<120 PSI) = DEFECTIVE COMPRESSOR VALVES."
            ),
            safetyTips = listOf("Always wear safety goggles. Liquid R134a boils at -26°C and causes instant blindness on eye contact."),
            isVerifiedOEM = true
        ),

        // 10. Battery & Charging
        RepairGuide(
            id = "bat_alt_01",
            categoryId = "battery_charging",
            categoryName = "Battery & Charging System",
            title = "Alternator Diode AC Ripple & Parasitic Drain Testing",
            titleHi = "अल्टरनेटर एसी रिपल, बैटरी हेल्थ और करंट ड्रेन (Parasitic Drain) टेस्ट",
            summary = "Detecting bad alternator rectifier diodes, battery State of Health (SOH), and locating battery drain using fuse voltage drops.",
            summaryHi = "खराब अल्टरनेटर डायोड और रातभर में बैटरी उतरने (Parasitic Drain) की सटीक जांच।",
            symptoms = listOf("Battery dead after overnight parking", "Flickering lights", "Whining noise from alternator", "Battery warning lamp"),
            testPoints = listOf("Alternator output voltage under load", "Alternator AC ripple voltage", "Key-off parasitic battery drain current", "Chassis ground drop"),
            multimeterSpecs = listOf(
                "Resting Battery Voltage: 12.6V = 100%, 12.4V = 75%, 12.0V = Discharged",
                "Charging Voltage: 13.8V - 14.4V with headlights and AC blower running",
                "Alternator AC Ripple: < 0.05V (50 mV AC). Ripple > 0.1V AC indicates blown rectifier diode!",
                "Normal Parasitic Drain: < 0.035A (< 35 milliamps) after vehicle modules sleep"
            ),
            stepByStep = listOf(
                "1. Measure resting voltage across battery posts before starting.",
                "2. Start engine, turn on high-beams and rear defogger, measure DC voltage at battery posts.",
                "3. Switch multimeter to AC VOLTS and measure across battery posts: excessive AC ripple (>0.1V) ruins batteries and ECMs.",
                "4. For parasitic drain: Connect DC amp clamp to battery negative cable. Wait 30 minutes for CAN-bus modules to enter sleep mode.",
                "5. If drain > 50mA, measure millivolt drop across individual fuses in fuse box to pinpoint live circuit without waking modules."
            ),
            safetyTips = listOf("Disconnect battery ground cable first when servicing electrical systems to prevent short circuit sparks."),
            isVerifiedOEM = true
        ),

        // 11. Starter Motor
        RepairGuide(
            id = "str_drop_01",
            categoryId = "starter",
            categoryName = "Starter Motor Diagnostics",
            title = "Starter Circuit Voltage Drop & Solenoid Testing",
            titleHi = "स्टार्टर मोटर वोल्टेज ड्रॉप और सोलेनोइड टेस्टिंग",
            summary = "How to test starter B+ feed, solenoid trigger signal, and ground circuit voltage drop during cranking.",
            summaryHi = "क्रैंकिंग के दौरान स्टार्टर मोटर, सोलेनोइड और ग्राउंड वायर की वोल्टेज ड्रॉप टेस्टिंग।",
            symptoms = listOf("Single click with no crank", "Slow sluggish crank", "Intermittent start failure"),
            testPoints = listOf("Starter B+ main battery lug", "Terminal 50 solenoid trigger wire", "Engine block to battery negative ground"),
            multimeterSpecs = listOf(
                "B+ Positive Cable Voltage Drop: < 0.5V DC while cranking",
                "Ground Circuit Voltage Drop: < 0.2V DC while cranking",
                "Terminal 50 Trigger Voltage: > 10.5V DC while key held in Start position",
                "Starter Cranking Current: 120A - 180A (4-cyl petrol), 200A - 350A (diesel)"
            ),
            stepByStep = listOf(
                "1. Connect red multimeter lead to battery positive post; black lead to starter main B+ terminal.",
                "2. Crank engine and record voltage: A reading > 0.5V indicates high resistance in positive cable or connections.",
                "3. Connect red lead to starter metal body; black lead to battery negative post. Crank engine: reading must be < 0.2V.",
                "4. Check Terminal 50 solenoid trigger signal: If 12V arrives but starter only clicks, solenoid internal contacts are burnt.",
                "5. Inspect starter ring gear on flywheel through starter hole for stripped teeth."
            ),
            safetyTips = listOf("Ensure transmission is in Park/Neutral and handbrake engaged before testing starter trigger terminals."),
            isVerifiedOEM = true
        ),

        // 12. Cooling System
        RepairGuide(
            id = "clg_overheat_01",
            categoryId = "cooling",
            categoryName = "Cooling System",
            title = "Blown Head Gasket Combustion Gas & Cooling System Test",
            titleHi = "हेड गैस्केट लीकेज (कमबशन गैस) और कूलिंग सिस्टम की जांच",
            summary = "Definitively confirming blown cylinder head gasket using chemical combustion gas test block, radiator pressure decay, and cooling fan speeds.",
            summaryHi = "केमिकल ब्लॉक टेस्ट और प्रेशर टेस्टिंग से हेड गैस्केट लीक और ओवरहीटिंग की पुष्टि।",
            symptoms = listOf("Engine overheating", "Coolant bubbling violently in reservoir", "White sweet-smelling exhaust smoke", "Loss of coolant without visible external leak"),
            testPoints = listOf("Cooling system pressure test (15-18 PSI)", "Chemical combustion gas test fluid (blue to yellow)", "Radiator fan low and high speed relays", "Thermostat opening temperature"),
            multimeterSpecs = listOf(
                "Radiator Cap Relief Pressure: 0.9 - 1.1 bar (13 - 16 PSI stamped on cap)",
                "Fan Low Speed Relay Trigger: 12V supply, relay coil 70Ω - 90Ω",
                "Coolant Temp Sensor (ECT): 2000Ω cold (20°C), drops to 250Ω warm (90°C)"
            ),
            stepByStep = listOf(
                "1. Pressurize cold cooling system to radiator cap rating (16 PSI). Watch gauge for 15 minutes for pressure decay.",
                "2. Install chemical combustion leak tester (block tester) with blue fluid on radiator neck.",
                "3. Run engine: If blue fluid turns YELLOW (petrol) or GREEN (diesel), exhaust combustion gas is blowing past head gasket into coolant.",
                "4. Remove spark plugs and borescope cylinders for steam-cleaned piston crowns.",
                "5. Verify radiator electric cooling fan activates on high speed when AC is switched on."
            ),
            safetyTips = listOf("DANGER: Never open radiator pressure cap on a hot engine. Hot pressurized coolant sprays violently."),
            isVerifiedOEM = true
        ),

        // 13. Electrical & CAN-Bus
        RepairGuide(
            id = "elec_can_01",
            categoryId = "electrical",
            categoryName = "Electrical Diagnostics & CAN-Bus",
            title = "CAN-Bus 60-Ohm Termination & Scope Waveform Troubleshooting",
            titleHi = "CAN-Bus 60-ओम टर्मिनेशन रेजिस्टेंस और वेवफॉर्म टेस्टिंग",
            summary = "Locating network communication failures, bus-off conditions, and shorted twisted-pair lines using a multimeter and oscilloscope.",
            summaryHi = "OBD पोर्ट पर 60-ओम रेजिस्टेंस टेस्ट और CAN-High/CAN-Low की मल्टीमीटर से जांच।",
            symptoms = listOf("U0100 / U0121 / U0140 Network codes", "Multiple dashboard warning lights", "Gauges falling to zero", "Scan tool no communication"),
            testPoints = listOf("OBD-II Pin 6 (CAN-H) to Pin 14 (CAN-L) Resistance", "Pin 6 to Chassis Ground Voltage", "Pin 14 to Chassis Ground Voltage", "Gateway module heartbeat"),
            multimeterSpecs = listOf(
                "CAN Bus Termination Resistance: 58Ω - 62Ω (Key OFF, battery disconnected)",
                "If reading is 120Ω: One 120Ω terminating resistor module is disconnected or open",
                "If reading is near 0Ω: CAN-H and CAN-L are shorted together",
                "CAN-High Voltage (Key ON): 2.5V recessive, pulses to 3.5V dominant",
                "CAN-Low Voltage (Key ON): 2.5V recessive, pulses to 1.5V dominant",
                "Sum Rule: CAN-H + CAN-L voltage equals ~ 5.0V at all times"
            ),
            stepByStep = listOf(
                "1. Disconnect battery negative terminal and wait 5 minutes for capacitors to discharge.",
                "2. Set multimeter to Ohms. Measure resistance between Pin 6 and Pin 14 of OBD diagnostic port.",
                "3. If resistance is 60Ω, physical termination network is intact.",
                "4. Reconnect battery, switch ignition ON, measure DC volts from Pin 6 to Ground (approx 2.7V) and Pin 14 to Ground (approx 2.3V).",
                "5. If CAN-H is 12V, wire is shorted to battery power. If CAN-H is 0V, wire is shorted to ground.",
                "6. Unplug modules one by one while monitoring resistance to find node bringing down the bus."
            ),
            safetyTips = listOf("Disconnect battery before ohmmeter testing. Applying ohmmeter to powered circuits destroys meter fuses."),
            isVerifiedOEM = true
        ),

        // 14. Transmission
        RepairGuide(
            id = "trn_shudder_01",
            categoryId = "transmission",
            categoryName = "Transmission Faults",
            title = "Automatic Transmission Torque Converter Clutch (TCC) Shudder",
            titleHi = "ऑटोमैटिक ट्रांसमिशन टॉर्क कन्वर्टर क्लच शडर और सोलेनोइड जांच",
            summary = "Diagnosing torque converter clutch slip, vibration between 40-70 km/h, shift solenoid resistance, and ATF degradation.",
            summaryHi = "40 से 70 km/h पर गाड़ी कांपने (TCC Shudder) और गियर शिफ्ट सोलेनोइड की टेस्टिंग।",
            symptoms = listOf("Vibration like driving over rumble strips between 40-70 km/h", "RPM needle fluctuating at steady cruising speed", "P0741 / P0744 TCC Slip codes"),
            testPoints = listOf("TCC Slip RPM live data PID (should be 0-20 RPM when locked)", "TCC PWM solenoid resistance", "Transmission fluid color and odor", "Brake switch cancel input"),
            multimeterSpecs = listOf(
                "TCC PWM Solenoid Resistance: 10Ω - 16Ω (valve body harness)",
                "Shift Solenoids A, B, C: 12Ω - 25Ω per coil",
                "TCC Slip Speed: 0 - 20 RPM (fully locked), > 100 RPM indicates clutch slip"
            ),
            stepByStep = listOf(
                "1. Connect scanner and monitor 'TCC Slip Speed' and 'TCC Commanded Duty Cycle' during highway cruise.",
                "2. When shudder occurs, lightly tap brake pedal with left foot while maintaining throttle: If shudder instantly disappears, TCC clutch is slipping.",
                "3. Inspect automatic transmission fluid (ATF): If black and smelling burnt, clutch friction material has burned off.",
                "4. Perform transmission fluid and internal filter flush using OEM friction-modified fluid.",
                "5. Test TCC solenoid electrical resistance at transmission case bulkhead connector."
            ),
            safetyTips = listOf("Ensure vehicle is safely supported on certified hoists before inspecting under-body transmission plugs."),
            isVerifiedOEM = true
        ),

        // 15. Hybrid & EV Safety
        RepairGuide(
            id = "ev_safety_01",
            categoryId = "hybrid_ev",
            categoryName = "Hybrid & EV Safety Basics",
            title = "High Voltage (400V - 800V) De-Energizing & Safety Isolation",
            titleHi = "इलेक्ट्रिक व हाइब्रिड (EV/HEV) 400V-800V हाई वोल्टेज सेफ्टी डी-एनर्जाइजिंग",
            summary = "Mandatory safety protocol for de-energizing high voltage traction battery, Manual Service Disconnect (MSD), and 0V verification.",
            summaryHi = "इलेक्ट्रिक गाड़ियों में 400V-800V ऑरेंज केबल पर काम करने से पहले करंट बंद करने की अनिवार्य प्रक्रिया।",
            symptoms = listOf("High Voltage Isolation Fault code", "Master warning triangle", "Vehicle will not enter 'READY' mode"),
            testPoints = listOf("Manual Service Disconnect (MSD) / Service Plug", "12V Auxiliary battery negative terminal", "Inverter capacitor discharge test", "CAT III 1000V rated insulated voltmeter check"),
            multimeterSpecs = listOf(
                "Traction Battery Voltage: 350V - 800V DC (LETHAL)",
                "Zero Voltage Proof: < 1.0V DC across inverter high voltage DC bus terminals after discharge",
                "Insulation Resistance Test: > 100 Megohms at 500V or 1000V DC test voltage",
                "12V Auxiliary Battery: 12.6V DC"
            ),
            stepByStep = listOf(
                "1. Wear Class 0 1000V-rated rubber insulating gloves with protective leather outer shell (tested within 6 months).",
                "2. Turn OFF ignition and move smart key fob at least 10 meters away from vehicle.",
                "3. Disconnect 12V auxiliary battery negative cable and secure it away from post.",
                "4. Remove High Voltage Service Plug / Manual Service Disconnect (MSD) and place in personal lock box.",
                "5. Wait 10 full minutes for high voltage inverter capacitors to discharge through bleeder resistors.",
                "6. Test meter on a known live 12V battery first, verify < 1V DC across HV inverter terminals, then re-test meter on 12V battery to prove meter functionality (Test-Verify-Test)."
            ),
            safetyTips = listOf(
                "DANGER: ORANGE HIGH-VOLTAGE HARNESSES CARRY LETHAL CURRENT (400V - 800V).",
                "NEVER cut or probe orange cables.",
                "Never work alone on High Voltage systems; ensure a trained safety observer is present."
            ),
            isVerifiedOEM = true
        )
    )

    fun getGuidesByCategory(categoryId: String): List<RepairGuide> {
        return guides.filter { it.categoryId == categoryId }
    }

    fun searchGuides(query: String): List<RepairGuide> {
        if (query.isBlank()) return guides
        val q = query.trim().lowercase()
        return guides.filter {
            it.title.lowercase().contains(q) ||
            it.titleHi.lowercase().contains(q) ||
            it.summary.lowercase().contains(q) ||
            it.categoryName.lowercase().contains(q) ||
            it.symptoms.any { s -> s.lowercase().contains(q) }
        }
    }

    fun getGuideById(id: String): RepairGuide? {
        return guides.firstOrNull { it.id == id }
    }
}
