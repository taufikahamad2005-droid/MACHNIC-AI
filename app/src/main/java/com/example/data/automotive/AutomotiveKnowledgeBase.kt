package com.example.data.automotive

import com.example.data.model.DiagnosticResult
import com.example.data.model.ExpectedValueItem
import com.example.data.model.RepairItem
import com.example.data.model.SensorCheckItem
import com.example.data.model.SeverityLevel
import com.example.data.model.VehicleInput

object AutomotiveKnowledgeBase {

    fun getDiagnostic(input: VehicleInput): DiagnosticResult {
        val code = input.faultCode.trim().uppercase()
        val vehicleDescriptor = listOf(input.year, input.make, input.model)
            .filter { it.isNotBlank() }
            .joinToString(" ")
            .ifEmpty { "Generic OBD-II Standard Vehicle" }

        val fuelContext = input.fuelType

        return when (code) {
            "P0300" -> createP0300(vehicleDescriptor, fuelContext, input)
            "P0171" -> createP0171(vehicleDescriptor, fuelContext, input)
            "P0420" -> createP0420(vehicleDescriptor, fuelContext, input)
            "P0087" -> createP0087(vehicleDescriptor, fuelContext, input)
            "P0401" -> createP0401(vehicleDescriptor, fuelContext, input)
            "P2002" -> createP2002(vehicleDescriptor, fuelContext, input)
            "P0101" -> createP0101(vehicleDescriptor, fuelContext, input)
            "P0128" -> createP0128(vehicleDescriptor, fuelContext, input)
            "P0700" -> createP0700(vehicleDescriptor, fuelContext, input)
            "U0100" -> createU0100(vehicleDescriptor, fuelContext, input)
            "C0035" -> createC0035(vehicleDescriptor, fuelContext, input)
            else -> createDynamicDiagnostic(code, vehicleDescriptor, fuelContext, input)
        }
    }

    private fun createP0300(vehicle: String, fuel: String, input: VehicleInput): DiagnosticResult {
        return DiagnosticResult(
            faultCode = "P0300",
            title = "Random or Multiple Cylinder Misfire Detected",
            severity = SeverityLevel.HIGH,
            systemInvolved = "Ignition, Fuel Delivery, Air Induction & Mechanical Compression",
            summary = "The Engine Control Module (ECM/PCM) has calculated crankshaft deceleration irregularities across multiple cylinders using the CKP (Crankshaft Position) sensor. Unburned fuel is entering the exhaust stream, creating a high risk of catalytic converter overheating.",
            symptoms = listOf(
                "Flashing or solid Check Engine Light (MIL)",
                "Severe engine shake, hesitation, and stumbling at idle or load",
                "Substantial loss of power during acceleration",
                "Strong unburnt fuel odor from exhaust pipe",
                "P0301-P0308 companion individual cylinder misfire codes"
            ),
            possibleCauses = listOf(
                "Worn or fouled spark plugs (excessive gap > 1.1mm)",
                "Ignition coil pack breakdown or cracked carbon track boots",
                "Unmetered vacuum intake leak (intake manifold gasket, PCV valve hose)",
                "Clogged, leaking, or uneven fuel injectors",
                "Low fuel delivery pressure (weak fuel pump or clogged fuel filter)",
                "Low mechanical compression (valves, head gasket, or piston rings)"
            ),
            diagnosticProcedure = listOf(
                "Step 1: Check live scan tool misfire counter PID to identify if misfires are isolated or jumping cylinders.",
                "Step 2: Inspect spark plugs for electrode erosion, carbon fouling, oil contamination, or porcelain cracks.",
                "Step 3: Swap suspected ignition coil with an adjacent cylinder and re-scan to see if misfire tracks the coil.",
                "Step 4: Perform smoke test across intake ducting, vacuum lines, and intake manifold gaskets.",
                "Step 5: Measure fuel rail pressure under idle and load against OEM service limits.",
                "Step 6: If electrical checks pass, perform relative compression test and manual cylinder leakage test."
            ),
            sensorChecks = listOf(
                SensorCheckItem("Crankshaft Position (CKP)", "Signal & Ground", "Oscilloscope waveform test", "Clean 58x/60-2 missing tooth sine or square pattern"),
                SensorCheckItem("Ignition Coil Primary", "B+ & Trigger Pin", "Multimeter DC Voltage & Resistance", "12.0V - 14.2V B+ supply, 0.4Ω - 1.2Ω primary"),
                SensorCheckItem("Fuel Injector Coils", "2-pin injector harness", "Multimeter Ohms test (Cold & Warm)", "11.5Ω - 14.5Ω per injector (balanced within 0.5Ω)"),
                SensorCheckItem("Mass Air Flow (MAF)", "Signal wire to Ground", "Live PID or Multimeter DC Volts", "0.8V - 1.2V at idle, rising linearly > 3.8V WOT")
            ),
            expectedValues = listOf(
                ExpectedValueItem("Fuel Trim (STFT + LTFT)", "-5% to +5%", "-10% to +10%", "% Combined"),
                ExpectedValueItem("Fuel Rail Pressure", "3.0 - 4.2", "3.8 - 4.5", "Bar (Port Injected)"),
                ExpectedValueItem("Cylinder Compression", "150 - 180", "Variance < 10%", "PSI across all cylinders"),
                ExpectedValueItem("Vacuum (Manifold)", "17 - 21", "Drops then recovers", "inHg at idle")
            ),
            possibleRepairs = listOf(
                RepairItem("Replace spark plugs and ignition coil boots", "High", "Easy"),
                RepairItem("Replace failing ignition coil packs", "High", "Easy"),
                RepairItem("Smoke test and replace cracked PCV hose / intake gasket", "Medium", "Moderate"),
                RepairItem("Ultrasonic cleaning or replacement of clogged fuel injectors", "Medium", "Moderate"),
                RepairItem("Replace fuel pump / fuel filter", "Low", "Moderate")
            ),
            toolsRequired = listOf(
                "Bi-directional OBD-II Diagnostic Scanner",
                "Digital Multimeter (CAT III 600V)",
                "Automotive Smoke Machine (for intake vacuum leaks)",
                "Compression Tester & Spark Tester Tool",
                "Fuel Pressure Test Gauge Kit"
            ),
            safetyPrecautions = listOf(
                "CAUTION: If the Check Engine Light is FLASHING, do NOT drive the vehicle. Flashing indicates catalyst-damaging raw fuel dumping.",
                "Disconnect battery negative terminal prior to testing high-energy ignition primary terminals.",
                "Relieve fuel system residual pressure before disconnecting any fuel lines."
            ),
            isGeneric = true,
            vehicleSpecificNotes = "Configured for $vehicle ($fuel). Note: For variable valve timing engines, check VVT solenoid screens for engine oil sludge before concluding compression fault."
        )
    }

    private fun createP0171(vehicle: String, fuel: String, input: VehicleInput): DiagnosticResult {
        return DiagnosticResult(
            faultCode = "P0171",
            title = "System Too Lean (Bank 1)",
            severity = SeverityLevel.MEDIUM,
            systemInvolved = "Fuel Metering & Air Induction System",
            summary = "The upstream Oxygen/Air-Fuel Ratio sensor has detected excess uncombined oxygen in the exhaust. The ECM is applying positive Long-Term Fuel Trim (LTFT > +15% to +25%) to compensate for an air-fuel ratio that is leaner than stoichiometric (14.7:1).",
            symptoms = listOf(
                "Check Engine Light on steady",
                "Hesitation, flat spots, or stumbling during initial acceleration",
                "Surging or erratic idle speed",
                "Decreased fuel economy (due to excessive PCM trim enrichment)",
                "Misfire under load or uphill driving"
            ),
            possibleCauses = listOf(
                "Vacuum leak downstream of Mass Air Flow sensor (cracked intake boot, PCV hose, brake booster)",
                "Dirty, contaminated, or failing Mass Air Flow (MAF) sensor hot wire",
                "Weak fuel pump, restricted fuel filter, or faulty fuel pressure regulator",
                "Clogged or sticking fuel injectors on Bank 1",
                "Exhaust manifold crack before upstream O2 sensor drawing ambient air",
                "Faulty or biased upstream Oxygen (O2) or Air-Fuel Ratio (A/F) sensor"
            ),
            diagnosticProcedure = listOf(
                "Step 1: Check Freeze Frame data: Look at STFT and LTFT at idle versus 2500 RPM. If trim drops at 2500 RPM, suspect a vacuum leak.",
                "Step 2: If fuel trim remains high at 2500 RPM, suspect fuel delivery deficit (weak pump/filter) or dirty MAF sensor.",
                "Step 3: Perform smoke leak test on air intake boot, resonator, PCV tube, and vacuum lines.",
                "Step 4: Clean MAF sensor using dedicated CRC MAF sensor cleaner. Do not touch sensor wire directly.",
                "Step 5: Verify fuel pressure with mechanical gauge connected to fuel test port.",
                "Step 6: Inspect exhaust manifold for soot trails or thermal cracks upstream of O2 sensor."
            ),
            sensorChecks = listOf(
                SensorCheckItem("MAF Sensor Grams/Sec", "Data Stream PID", "Scan Tool Live Data", "2.0 - 4.5 g/s at hot idle (approx 1 g/s per Liter displacement)"),
                SensorCheckItem("Upstream O2 Sensor (B1S1)", "Signal Pin", "Digital Multimeter or Live Graph", "Rapidly switching between 0.1V (lean) and 0.9V (rich)"),
                SensorCheckItem("Fuel Pump Voltage Drop", "Pump connector B+ to Chassis Ground", "Multimeter DC Volts", "Within 0.5V of battery voltage under load"),
                SensorCheckItem("Purge Valve (EVAP)", "Harness & Vacuum Port", "Vacuum pump & 12V test", "Must hold vacuum when de-energized; 0 vacuum leak")
            ),
            expectedValues = listOf(
                ExpectedValueItem("LTFT Bank 1", "-5%", "+5%", "% at idle"),
                ExpectedValueItem("STFT Bank 1", "-5%", "+5%", "% dynamic switching"),
                ExpectedValueItem("MAF at 2500 RPM", "10.0", "15.0", "g/s steady unloaded"),
                ExpectedValueItem("Fuel Pressure (Port)", "40", "55", "PSI steady")
            ),
            possibleRepairs = listOf(
                RepairItem("Replace cracked air intake hose or vacuum lines", "High", "Easy"),
                RepairItem("Clean or replace Mass Air Flow (MAF) sensor", "High", "Easy"),
                RepairItem("Replace PCV valve and breather elbow tube", "Medium", "Easy"),
                RepairItem("Replace fuel filter or in-tank fuel pump assembly", "Medium", "Moderate"),
                RepairItem("Replace upstream Bank 1 Oxygen sensor", "Low", "Moderate")
            ),
            toolsRequired = listOf(
                "OBD-II Scanner with Live Data & Fuel Trim PIDs",
                "Automotive Evaporative / Intake Smoke Machine",
                "Dedicated MAF Cleaner Spray",
                "Digital Multimeter",
                "Fuel Pressure Test Kit"
            ),
            safetyPrecautions = listOf(
                "Do NOT use harsh brake cleaner or carb cleaner on delicate MAF platinum hot-wires; use dedicated MAF cleaner only.",
                "Beware of hot exhaust manifold when inspecting upstream oxygen sensor wiring.",
                "Wear protective safety glasses when pressure testing fuel delivery lines."
            ),
            isGeneric = true,
            vehicleSpecificNotes = "Targeted for $vehicle ($fuel). If equipped with direct injection (GDI), verify high-pressure fuel rail pressure PID (target 30-150 bar depending on RPM)."
        )
    }

    private fun createP0420(vehicle: String, fuel: String, input: VehicleInput): DiagnosticResult {
        return DiagnosticResult(
            faultCode = "P0420",
            title = "Catalytic Converter System Efficiency Below Threshold (Bank 1)",
            severity = SeverityLevel.MEDIUM,
            systemInvolved = "Exhaust & Emissions Control System",
            summary = "The ECM compares oxygen storage capacity between upstream (B1S1) and downstream (B1S2) oxygen sensors. A healthy catalytic converter stores oxygen, resulting in a flat downstream signal. If downstream mirrors upstream cycling, catalytic converter washcoat efficiency is degraded.",
            symptoms = listOf(
                "Check Engine Light (MIL) illuminated",
                "Usually no noticeable drivability change unless catalyst is physically melted/restricted",
                "Sulfur or rotten-egg odor from exhaust in severe cases",
                "Failure of official state emissions inspection",
                "Reduced top-end power if substrate is mechanically clogged"
            ),
            possibleCauses = listOf(
                "Degraded catalytic converter substrate (thermal damage, age, or chemical poisoning)",
                "Unaddressed engine misfires (P0300) dumping raw unburnt fuel into the converter",
                "Engine burning oil (valve seals/piston rings) or coolant (head gasket leak) coating catalyst washcoat",
                "Exhaust leak before or near downstream O2 sensor introducing fresh air",
                "Slow or biased downstream heated oxygen sensor (B1S2)"
            ),
            diagnosticProcedure = listOf(
                "Step 1: Check and resolve ALL other engine codes (P0300 misfires, P0171 lean, P0172 rich) BEFORE replacing the catalyst.",
                "Step 2: Warm engine to full operating temperature (minimum 15 minutes drive).",
                "Step 3: Graph B1S1 (upstream) and B1S2 (downstream) O2 sensors side-by-side at 2000 RPM.",
                "Step 4: Upstream should cycle rapidly (0.1V - 0.9V); downstream should remain steady around 0.5V - 0.7V.",
                "Step 5: Inspect exhaust pipe and flange joints between front and rear sensors for pinhole leaks.",
                "Step 6: Measure catalytic converter inlet vs outlet temperature using an infrared pyrometer. Outlet should be 30°C - 50°C hotter than inlet when operating efficiently."
            ),
            sensorChecks = listOf(
                SensorCheckItem("Downstream O2 (B1S2)", "Signal Wire", "Live Scan Tool Graph", "Steady 0.5V - 0.7V at cruise (should NOT mirror upstream)"),
                SensorCheckItem("O2 Sensor Heater Circuit", "Heater pins (2 white wires)", "Multimeter Ohms test", "6Ω - 16Ω across heater element (cold)"),
                SensorCheckItem("Catalyst Temperature Inlet/Outlet", "External housing", "Infrared Thermometer", "Outlet > Inlet by 30°C - 50°C (indicates exothermic conversion)")
            ),
            expectedValues = listOf(
                ExpectedValueItem("B1S2 Downstream Volts", "0.55 - 0.75", "Steady flat line", "Volts DC at 2000 RPM"),
                ExpectedValueItem("Exhaust Backpressure", "< 1.5", "< 2.5", "PSI measured at upstream O2 port")
            ),
            possibleRepairs = listOf(
                RepairItem("Seal exhaust flange or weld exhaust leaks upstream of B1S2", "High", "Moderate"),
                RepairItem("Fix underlying oil/coolant burning or misfire cause", "High", "Moderate"),
                RepairItem("Replace failing downstream O2 sensor", "Medium", "Easy"),
                RepairItem("Replace catalytic converter with EPA/OEM compliant unit", "Medium", "Advanced")
            ),
            toolsRequired = listOf(
                "Live graphing OBD-II diagnostic scanner",
                "Infrared Non-contact Thermometer",
                "Exhaust backpressure gauge",
                "Digital Multimeter"
            ),
            safetyPrecautions = listOf(
                "Catalytic converters operate at 400°C to 800°C (800°F - 1500°F). NEVER touch exhaust components when hot.",
                "Do NOT park on dry grass or combustible materials when testing converter efficiency."
            ),
            isGeneric = true,
            vehicleSpecificNotes = "Targeted for $vehicle. Rule of thumb: Catalysts do not die of old age alone; they are killed by fuel, oil, or coolant contamination. Always cure the root disease first."
        )
    }

    private fun createP0087(vehicle: String, fuel: String, input: VehicleInput): DiagnosticResult {
        return DiagnosticResult(
            faultCode = "P0087",
            title = "Fuel Rail / System Pressure Too Low",
            severity = SeverityLevel.CRITICAL,
            systemInvolved = "High-Pressure Fuel Injection System (Common Rail Diesel / Direct Petrol GDI)",
            summary = "The Fuel Rail Pressure (FRP) sensor reports pressure consistently below the commanded target in the ECM's closed-loop calibration map. Common in CRDi diesel and GDI petrol engines.",
            symptoms = listOf(
                "Engine cuts out under hard acceleration or hill climb",
                "Extended cranking before starting (long crank)",
                "Limp Home Mode (Engine power severely limited to 2000 RPM)",
                "Check Engine Light on accompanied by EPC / Glow Plug light",
                "Engine failure to restart after stall"
            ),
            possibleCauses = listOf(
                "Severely restricted or clogged diesel fuel filter",
                "Excessive injector back-leakage / leak-off returning high-pressure fuel to tank",
                "Failing High-Pressure Fuel Pump (HPFP) or worn cam follower",
                "Faulty Fuel Pressure Regulator / Suction Control Valve (SCV)",
                "Air in diesel fuel lines or low lift-pump supply pressure from fuel tank"
            ),
            diagnosticProcedure = listOf(
                "Step 1: Check low-pressure feed / lift pump delivery (minimum 3.5 to 5.0 bar to HPFP inlet).",
                "Step 2: Inspect fuel filter for water contamination, black sludge, or metal brass shavings.",
                "Step 3: Connect scanner and compare 'Desired Fuel Rail Pressure' vs 'Actual Fuel Rail Pressure'.",
                "Step 4: Perform CRDi injector leak-off test using graduated cylinder tubes. Excessive return volume indicates worn injector needle valve.",
                "Step 5: Test Suction Control Valve (SCV) / Metering unit solenoid resistance and PWM drive signal."
            ),
            sensorChecks = listOf(
                SensorCheckItem("Fuel Rail Pressure Sensor", "Signal wire to Ground", "Live PID or 5V reference check", "0.5V at 0 bar, approx 1.0V - 1.3V at idle (250-300 bar)"),
                SensorCheckItem("SCV / Fuel Metering Unit", "2-pin solenoid plug", "Multimeter Ohms test", "1.5Ω - 4.5Ω (vehicle dependent, inspect harness for oil ingress)"),
                SensorCheckItem("Low Pressure Lift Pump", "In-tank feed harness", "Multimeter Amps / Volts", "4A - 7A current draw, 12V supply")
            ),
            expectedValues = listOf(
                ExpectedValueItem("CRDi Idle Rail Pressure", "250 - 350", "1400 - 2000", "Bar"),
                ExpectedValueItem("GDI Petrol Idle Rail Pressure", "30 - 50", "120 - 200", "Bar"),
                ExpectedValueItem("Low Pressure Feed", "4.0", "5.5", "Bar to HPFP inlet"),
                ExpectedValueItem("Injector Leak-off Rate", "< 20", "Equal across all", "ml / 2 min at idle")
            ),
            possibleRepairs = listOf(
                RepairItem("Replace primary and secondary fuel filters", "High", "Easy"),
                RepairItem("Clean or replace Fuel Suction Control Valve (SCV)", "High", "Moderate"),
                RepairItem("Replace worn injector with excessive leak-off", "Medium", "Advanced"),
                RepairItem("Replace High Pressure Fuel Pump (HPFP)", "Low", "Advanced")
            ),
            toolsRequired = listOf(
                "High-pressure Common Rail Diagnostic Scanner",
                "Graduated Injector Return Leak-Off Test Kit",
                "Low-pressure diesel fuel pressure gauge",
                "Digital Multimeter"
            ),
            safetyPrecautions = listOf(
                "DANGER: Common Rail Diesel pressure exceeds 1800 BAR (26,000 PSI). Fluid injection under skin is lethal.",
                "NEVER loosen high pressure pipes while engine is cranking or running.",
                "Cleanliness is vital: Cover open fuel ports immediately with plastic plugs."
            ),
            isGeneric = false,
            vehicleSpecificNotes = "CRDi & GDI specific architecture for $vehicle. Inspect fuel sample in clean glass jar for metal particles indicating pump internal failure."
        )
    }

    private fun createP0401(vehicle: String, fuel: String, input: VehicleInput): DiagnosticResult {
        return DiagnosticResult(
            faultCode = "P0401",
            title = "Exhaust Gas Recirculation (EGR) Flow Insufficient Detected",
            severity = SeverityLevel.MEDIUM,
            systemInvolved = "Emissions / EGR Exhaust Gas Recirculation",
            summary = "The ECM commands the EGR valve open during cruising to lower combustion temperatures and NOx emissions. During commanded EGR events, the ECM monitors MAP sensor drop, MAF drop, or temperature sensor rise. If the expected manifold response is absent, P0401 is set.",
            symptoms = listOf(
                "Check Engine Light (MIL) on",
                "Engine spark knock / pinging under light acceleration and load",
                "Rough idle if EGR valve is sticking partially open",
                "Increased NOx emissions during technical inspection",
                "Slight hesitation at steady highway cruising speeds"
            ),
            possibleCauses = listOf(
                "Heavy carbon soot buildup clogging the EGR passage or intake manifold port",
                "EGR valve mechanically stuck closed or carbon seized",
                "Faulty EGR stepper motor or position sensor feedback",
                "Clogged DPFE (Differential Pressure Feedback EGR) sensor hoses or sensor failure",
                "Broken or cracked vacuum supply hose to pneumatic EGR actuator"
            ),
            diagnosticProcedure = listOf(
                "Step 1: Use bi-directional scan tool to actuate EGR valve at idle; engine should stumble or stall if passages are clear.",
                "Step 2: Remove EGR valve and inspect valve pintle and port for heavy carbon crust.",
                "Step 3: Clean metal EGR delivery pipe connected to the intake manifold using carbon solvent and wire brush.",
                "Step 4: Check 5V reference, ground, and potentiometer position signal from EGR sensor.",
                "Step 5: For vacuum operated EGR, verify 15+ inHg engine vacuum at solenoid inlet."
            ),
            sensorChecks = listOf(
                SensorCheckItem("EGR Position Sensor", "Signal wire", "Multimeter DC Volts", "0.8V closed, smoothly rising to 4.2V fully open"),
                SensorCheckItem("EGR Solenoid Coil", "2-pin harness", "Multimeter Ohms", "20Ω - 45Ω across solenoid terminals"),
                SensorCheckItem("MAP Sensor Response", "Live scan PID", "Live data graphing", "Pressure increases 10-15 kPa when EGR opens")
            ),
            expectedValues = listOf(
                ExpectedValueItem("EGR Position Desired vs Actual", "0%", "100%", "% Tracking within 5%"),
                ExpectedValueItem("Vacuum to Actuator", "0", "8 - 12", "inHg at cruise")
            ),
            possibleRepairs = listOf(
                RepairItem("Remove and chemically decoke EGR valve and intake manifold port", "High", "Moderate"),
                RepairItem("Replace clogged DPFE sensor hoses or differential sensor", "Medium", "Easy"),
                RepairItem("Replace electronically seized EGR valve assembly", "Medium", "Moderate")
            ),
            toolsRequired = listOf(
                "OBD-II Bi-directional Scan Tool",
                "Hand Vacuum Pump with Gauge (Mityvac)",
                "Carbon Cleaning Solvent & Wire Pick",
                "Digital Multimeter"
            ),
            safetyPrecautions = listOf(
                "Allow EGR pipe to cool before disassembly. Exhaust gases reach 500°C.",
                "Wear nitrile gloves and eye protection when scrubbing carbon soot and chemical cleaner."
            ),
            isGeneric = true,
            vehicleSpecificNotes = "Configured for $vehicle ($fuel). For diesel vehicles, also inspect the EGR cooler for internal coolant bypass leaks."
        )
    }

    private fun createP2002(vehicle: String, fuel: String, input: VehicleInput): DiagnosticResult {
        return DiagnosticResult(
            faultCode = "P2002",
            title = "Diesel Particulate Filter (DPF) Efficiency Below Threshold (Bank 1)",
            severity = SeverityLevel.HIGH,
            systemInvolved = "Diesel Aftertreatment / DPF System",
            summary = "The Diesel Particulate Filter differential pressure sensor has detected that pressure differential across the ceramic monolith is outside acceptable soot loading parameters, or soot capture efficiency has failed.",
            symptoms = listOf(
                "DPF warning icon and Check Engine Light on instrument cluster",
                "Engine in Derate / Limp mode (loss of turbo boost and acceleration)",
                "Increased frequency of active regeneration attempts",
                "Higher diesel fuel consumption due to post-injection regeneration",
                "Fan running on high speed even after shutting off engine"
            ),
            possibleCauses = listOf(
                "DPF clogged with soot due to frequent short urban trips preventing passive regeneration",
                "Cracked or melted DPF differential pressure silicone sensing tubes",
                "Faulty DPF differential pressure sensor reporting incorrect delta mbar",
                "Leaking turbocharger boost pipe or intercooler causing excessive black smoke soot generation",
                "Stuck open thermostat preventing engine from reaching 80°C required for regen"
            ),
            diagnosticProcedure = listOf(
                "Step 1: Check DPF soot load and ash mass PID in diagnostic scanner.",
                "Step 2: Inspect rubber pressure hoses connecting DPF pressure sensor to the metal exhaust taps.",
                "Step 3: Confirm engine coolant temperature reaches at least 85°C during road test.",
                "Step 4: Check intake tract for boost leaks; any boost leak generates excess soot that overloads DPF.",
                "Step 5: Perform forced stationary DPF regeneration or chemical off-car flush if soot load is between 30g and 45g."
            ),
            sensorChecks = listOf(
                SensorCheckItem("DPF Differential Pressure", "Delta pressure PID", "Scan Tool Live Data", "3 - 8 mbar at idle, < 60 mbar at 2500 RPM (clean DPF)"),
                SensorCheckItem("Exhaust Gas Temp 1 (EGT1)", "Pre-turbo probe", "Scan Tool PID", "180°C - 350°C normal, 550°C - 650°C during active regen"),
                SensorCheckItem("Exhaust Gas Temp 2 (EGT2)", "Pre-DPF probe", "Scan Tool PID", "Must reach 580°C for soot regeneration burn-off")
            ),
            expectedValues = listOf(
                ExpectedValueItem("DPF Soot Mass", "< 15", "> 40 triggers regen", "Grams"),
                ExpectedValueItem("DPF Delta Pressure", "4 - 8", "30 - 55", "mbar at 2500 RPM")
            ),
            possibleRepairs = listOf(
                RepairItem("Replace melted/cracked DPF differential pressure rubber tubes", "High", "Easy"),
                RepairItem("Fix boost leak / intercooler pipe causing rich black soot", "High", "Moderate"),
                RepairItem("Perform professional scanner-commanded Service DPF Regeneration", "Medium", "Moderate"),
                RepairItem("Professional ultrasonic off-vehicle DPF wash or replacement", "Low", "Advanced")
            ),
            toolsRequired = listOf(
                "Professional Diagnostic Scan Tool with DPF Service Functions",
                "Exhaust Backpressure / Manometer Gauge",
                "Automotive Smoke Tester (Intake Boost Leaks)"
            ),
            safetyPrecautions = listOf(
                "FIRE HAZARD: During stationary DPF regeneration, exhaust tip temperature exceeds 500°C. Perform OUTDOORS on non-combustible concrete surface away from buildings and dry grass."
            ),
            isGeneric = false,
            vehicleSpecificNotes = "Diesel CRDi specific for $vehicle. Verify engine oil level: multiple aborted regens cause diesel fuel dilution of engine crankcase oil."
        )
    }

    private fun createP0101(vehicle: String, fuel: String, input: VehicleInput): DiagnosticResult {
        return DiagnosticResult(
            faultCode = "P0101",
            title = "Mass Air Flow (MAF) Circuit Range / Performance Problem",
            severity = SeverityLevel.MEDIUM,
            systemInvolved = "Air Induction & Engine Management",
            summary = "The Mass Air Flow sensor signal is rational but deviates from the expected airflow calculated by the ECM based on Throttle Position, Engine RPM, and Manifold Absolute Pressure (speed-density crosscheck).",
            symptoms = listOf(
                "Check Engine Light on",
                "Engine stalling immediately after start",
                "Black exhaust smoke under acceleration (rich condition)",
                "Hesitation and transmission shifting harshness"
            ),
            possibleCauses = listOf(
                "Contaminated MAF hot-film or wire (dust, oil from aftermarket filter)",
                "Split, torn, or loose air intake duct between MAF and throttle body",
                "Dirty, waterlogged, or warped engine air filter",
                "High resistance or loose pins in MAF 5-pin harness connector"
            ),
            diagnosticProcedure = listOf(
                "Step 1: Visually inspect air intake bellows boot between MAF sensor and throttle body for tears.",
                "Step 2: Inspect air filter box for water, rodent debris, or improper sealing.",
                "Step 3: Remove MAF sensor and examine hot-wire sensing elements under bright magnification.",
                "Step 4: Clean using dedicated MAF sensor cleaner. Reinstall and reset fuel trims.",
                "Step 5: Perform digital multimeter tap test on MAF body while observing frequency/voltage on scope."
            ),
            sensorChecks = listOf(
                SensorCheckItem("MAF 12V Supply Pin", "Pin 2 or 3 to Ground", "Multimeter DC Volts", "Battery Voltage (12.0V - 14.2V)"),
                SensorCheckItem("MAF 5V Reference", "Reference pin to Ground", "Multimeter DC Volts", "4.95V - 5.05V steady"),
                SensorCheckItem("MAF Ground Pin", "Ground pin to Battery Neg", "Voltage Drop Test", "< 0.05V (clean chassis ground)")
            ),
            expectedValues = listOf(
                ExpectedValueItem("Airflow Idle (g/s)", "2.0 - 4.5", "8.0 - 14.0", "g/s at 2000 RPM"),
                ExpectedValueItem("Analog MAF Output", "1.0 - 1.4", "2.0 - 2.8", "Volts at 2500 RPM")
            ),
            possibleRepairs = listOf(
                RepairItem("Clean MAF sensing element with dedicated MAF cleaner", "High", "Easy"),
                RepairItem("Replace engine air filter and clean airbox", "High", "Easy"),
                RepairItem("Replace cracked rubber air intake ducting", "Medium", "Easy"),
                RepairItem("Replace MAF sensor assembly with OEM unit", "Medium", "Moderate")
            ),
            toolsRequired = listOf("OBD-II Scanner", "Digital Multimeter", "MAF Cleaning Spray"),
            safetyPrecautions = listOf("Never touch the platinum wire with fingers, screwdrivers, or cotton swabs."),
            isGeneric = true,
            vehicleSpecificNotes = "Configured for $vehicle. Cheap aftermarket MAF sensors frequently fail calibration curves; use OEM or Tier-1 Bosch/Denso/Hitachi."
        )
    }

    private fun createP0128(vehicle: String, fuel: String, input: VehicleInput): DiagnosticResult {
        return DiagnosticResult(
            faultCode = "P0128",
            title = "Coolant Thermostat (Coolant Temp Below Thermostat Regulating Temperature)",
            severity = SeverityLevel.LOW,
            systemInvolved = "Engine Thermal Management & Cooling System",
            summary = "The ECM calculates thermal warm-up curves from cold start. If the Engine Coolant Temperature (ECT) sensor does not reach closed-loop operating temperature (approx 80°C-88°C) within the calibrated time/mileage window, P0128 is flagged.",
            symptoms = listOf(
                "Check Engine Light on",
                "Temperature gauge stays low or drops when driving on highway",
                "Cabin heater blows lukewarm or cold air in winter",
                "Increased fuel consumption (engine stays in open-loop enrichment)",
                "Radiator cooling fan running continuously in failsafe mode"
            ),
            possibleCauses = listOf(
                "Thermostat stuck mechanically open or soft spring opening prematurely",
                "Degraded rubber thermostat seal allowing coolant bypass",
                "Engine Coolant Temperature (ECT) sensor biased high or low resistance",
                "Low coolant level with air pocket at ECT sensor location"
            ),
            diagnosticProcedure = listOf(
                "Step 1: Check coolant level in expansion reservoir and radiator when cold.",
                "Step 2: Start cold engine. Feel upper and lower radiator hoses. Lower hose should stay cool until thermostat reaches 88°C.",
                "Step 3: If lower hose warms immediately from cold start, thermostat is stuck open.",
                "Step 4: Verify ECT sensor reading on scan tool matches physical temperature with infrared thermometer."
            ),
            sensorChecks = listOf(
                SensorCheckItem("ECT Sensor Resistance", "2-pin sensor terminals", "Multimeter Ohms test", "2000Ω - 3000Ω at 20°C, drops to 200Ω - 300Ω at 90°C (NTC thermistor)"),
                SensorCheckItem("ECT 5V Reference", "Harness plug to Ground", "Multimeter DC Volts", "4.95V - 5.05V with key ON")
            ),
            expectedValues = listOf(
                ExpectedValueItem("Operating Temp", "88 - 95", "88 - 102", "°C fully warm")
            ),
            possibleRepairs = listOf(
                RepairItem("Replace thermostat assembly and fresh coolant bleed", "High", "Moderate"),
                RepairItem("Replace Engine Coolant Temperature (ECT) sensor", "Medium", "Easy")
            ),
            toolsRequired = listOf("Digital Multimeter", "Infrared Thermometer", "Cooling system vacuum refill kit"),
            safetyPrecautions = listOf("NEVER remove radiator cap or expansion cap when engine is HOT. Severe boiling scald hazard."),
            isGeneric = true,
            vehicleSpecificNotes = "Configured for $vehicle. Many modern engines use electronically heated map-controlled thermostats; check electrical heater connector for corrosion."
        )
    }

    private fun createP0700(vehicle: String, fuel: String, input: VehicleInput): DiagnosticResult {
        return DiagnosticResult(
            faultCode = "P0700",
            title = "Transmission Control System (MIL Request)",
            severity = SeverityLevel.HIGH,
            systemInvolved = "Automatic / Dual Clutch / CVT Transmission Control",
            summary = "P0700 is an informational gateway code set in the ECM indicating that the Transmission Control Module (TCM) has detected a transmission fault and requested Check Engine Light illumination. You MUST scan the TCM module directly for sub-codes (e.g. P0730, P0750, P0840).",
            symptoms = listOf(
                "Check Engine Light on",
                "Transmission stuck in 'Limp Home' mode (typically locked in 3rd gear)",
                "Harsh engagement when shifting into Drive or Reverse",
                "Slipping gears, delayed upshifts, or RPM flare between shifts"
            ),
            possibleCauses = listOf(
                "Low, burnt, or contaminated automatic transmission fluid (ATF)",
                "Faulty shift solenoid or line pressure control solenoid inside valve body",
                "Transmission input / output speed sensor failure",
                "TCM wiring harness connector contaminated with oil or moisture",
                "Internal clutch pack wear or torque converter clutch lockup failure"
            ),
            diagnosticProcedure = listOf(
                "Step 1: Connect scan tool and select Transmission Control Module (TCM) to retrieve specific transmission DTCs.",
                "Step 2: Check transmission fluid level and condition (pink/red is healthy; dark brown/burnt smelling indicates clutch wear).",
                "Step 3: Inspect transmission 16-pin harness plug for transmission fluid wicking into wiring.",
                "Step 4: Monitor turbine speed sensor vs output shaft speed sensor PIDs during road test."
            ),
            sensorChecks = listOf(
                SensorCheckItem("Shift Solenoid A/B", "Valve body harness pins", "Multimeter Ohms", "10Ω - 25Ω per solenoid coil"),
                SensorCheckItem("TCM Power & Grounds", "TCM harness connectors", "Multimeter Volts & Drop", "Battery voltage on 12V B+, < 0.1V ground drop")
            ),
            expectedValues = listOf(
                ExpectedValueItem("ATF Operating Temp", "75 - 90", "Max 110", "°C"),
                ExpectedValueItem("Slip Speed (Locked)", "0 - 20", "0 - 40", "RPM at steady cruise")
            ),
            possibleRepairs = listOf(
                RepairItem("Scan TCM to pinpoint exact sub-code", "High", "Easy"),
                RepairItem("Transmission fluid and internal filter service", "High", "Moderate"),
                RepairItem("Replace shift solenoid pack or valve body lead frame", "Medium", "Advanced")
            ),
            toolsRequired = listOf("Bi-directional Scanner supporting Transmission TCU/TCM", "ATF Dipstick tool / Level check adapter", "Digital Multimeter"),
            safetyPrecautions = listOf("Ensure vehicle is safely raised on certified jack stands or hoist before working under transmission."),
            isGeneric = true,
            vehicleSpecificNotes = "Vehicle: $vehicle. Scan TCM directly; clearing P0700 in the engine module will not fix transmission faults."
        )
    }

    private fun createU0100(vehicle: String, fuel: String, input: VehicleInput): DiagnosticResult {
        return DiagnosticResult(
            faultCode = "U0100",
            title = "Lost Communication with Engine Control Module (ECM / PCM 'A')",
            severity = SeverityLevel.CRITICAL,
            systemInvolved = "Controller Area Network (CAN-Bus) High Speed Network",
            summary = "A secondary module (such as TCM, ABS, BCM, or Instrument Cluster) failed to receive the periodic heartbeat CAN broadcast frames from the primary Engine Control Module across CAN High and CAN Low lines.",
            symptoms = listOf(
                "Engine cranks but will not start (No Start)",
                "Multiple warning lights on dashboard (ABS, Brake, Check Engine, Airbag)",
                "Instrument cluster gauges (Speedometer, Tachometer) dead or frozen",
                "Scan tool unable to establish communication with Engine Module (No Response from ECM)"
            ),
            possibleCauses = listOf(
                "Blown ECM main power fuse or corroded main ECM power relay",
                "Damaged CAN-Bus twisted pair wires (CAN-High or CAN-Low shorted to ground or open)",
                "Corroded or broken ECM chassis ground straps",
                "Water ingress into ECM enclosure or wiring bulkhead connector",
                "Internal hardware failure of the Engine Control Module"
            ),
            diagnosticProcedure = listOf(
                "Step 1: Test CAN bus resistance at OBD-II port between Pin 6 (CAN-H) and Pin 14 (CAN-L) with battery DISCONNECTED. Normal is exactly 60 Ohms (two 120Ω terminating resistors in parallel).",
                "Step 2: If reading is 120 Ohms, one terminating resistor (usually inside ECM or Instrument Cluster) is disconnected/open.",
                "Step 3: If reading is near 0 Ohms, CAN-H and CAN-L are shorted together.",
                "Step 4: Check ECM main relay clicking and output 12V power at the engine fuse box.",
                "Step 5: Verify ECM ground wires have less than 0.05V drop under load."
            ),
            sensorChecks = listOf(
                SensorCheckItem("OBD-II Pin 6 to Pin 14 Resistance", "Key OFF, Battery Disconnected", "Multimeter Ohms", "58Ω - 62Ω (nominal 60Ω)"),
                SensorCheckItem("CAN-High Voltage (Pin 6 to Ground)", "Key ON", "Multimeter DC Volts", "2.5V - 3.5V (recessive 2.5V, dominant 3.5V)"),
                SensorCheckItem("CAN-Low Voltage (Pin 14 to Ground)", "Key ON", "Multimeter DC Volts", "1.5V - 2.5V (recessive 2.5V, dominant 1.5V)"),
                SensorCheckItem("ECM Main Power Feed", "ECM Power Pins", "Multimeter DC Volts", "12.0V - 14.2V")
            ),
            expectedValues = listOf(
                ExpectedValueItem("CAN Bus Total Resistance", "60", "60", "Ohms at OBD port"),
                ExpectedValueItem("CAN-H + CAN-L Sum", "5.0", "5.0", "Volts (Differential symmetry)")
            ),
            possibleRepairs = listOf(
                RepairItem("Replace blown ECM power fuse or main system relay", "High", "Easy"),
                RepairItem("Repair corroded ECM ground terminal wire", "High", "Easy"),
                RepairItem("Locate and repair pinched CAN-Bus twisted pair harness", "Medium", "Advanced"),
                RepairItem("Replace or clone damaged Engine Control Module", "Low", "Advanced")
            ),
            toolsRequired = listOf("Digital Multimeter", "Breakout Box for OBD-II Port", "2-Channel Automotive Oscilloscope", "Factory Wiring Diagram"),
            safetyPrecautions = listOf("Disconnect battery before checking bus resistance. Ohmmeter current on powered CAN lines damages meter."),
            isGeneric = true,
            vehicleSpecificNotes = "Targeted for $vehicle. Check wiring harness behind battery tray and engine bay fuse box for battery acid corrosion."
        )
    }

    private fun createC0035(vehicle: String, fuel: String, input: VehicleInput): DiagnosticResult {
        return DiagnosticResult(
            faultCode = "C0035",
            title = "Left Front Wheel Speed Sensor Circuit Fault",
            severity = SeverityLevel.MEDIUM,
            systemInvolved = "Anti-Lock Braking (ABS), Traction Control & Stability (ESC)",
            summary = "The ABS Control Module has detected an open, short, or erratic pulse signal from the Left Front Wheel Speed Sensor circuit during vehicle motion or self-check.",
            symptoms = listOf(
                "ABS warning lamp and Traction Control (TCS) light illuminated",
                "Pulse vibration in brake pedal at low speeds (false ABS activation)",
                "Hill start assist and cruise control disabled",
                "Speedometer inaccuracy on models using wheel speed sensor for speed display"
            ),
            possibleCauses = listOf(
                "Damage or road debris cutting the flexible wheel arch sensor wire",
                "Cracked, rusted, or missing magnetic tone ring on wheel hub / CV axle",
                "Faulty Hall-effect or variable reluctance wheel speed sensor",
                "Corrosion inside 2-pin connector behind wheel splash shield",
                "Excessive wheel bearing play moving tone ring out of sensor airgap"
            ),
            diagnosticProcedure = listOf(
                "Step 1: Check live scan data for all 4 wheel speed sensors while driving slowly in a straight line.",
                "Step 2: Inspect Left Front sensor wiring along suspension strut for chafing against tire/wheel.",
                "Step 3: Unplug 2-pin sensor connector and inspect pins for green corrosion or water ingress.",
                "Step 4: Check sensor supply voltage from ABS module (typically 12V or 5V on active Hall sensors).",
                "Step 5: Inspect magnetic encoder ring on wheel bearing with magnetic viewer card for missing segments."
            ),
            sensorChecks = listOf(
                SensorCheckItem("Wheel Speed Sensor Supply", "Harness plug pin to Ground", "Multimeter DC Volts", "11.0V - 12.5V (active sensor reference)"),
                SensorCheckItem("Passive VR Sensor Coil", "Sensor pins (unplugged)", "Multimeter Ohms", "800Ω - 1600Ω (if passive magnetic inductive type)"),
                SensorCheckItem("Wheel Bearing Play", "Wheel hub rock test", "Dial Indicator", "< 0.05 mm axial play")
            ),
            expectedValues = listOf(
                ExpectedValueItem("4-Wheel Speed Matching", "Identical", "± 1", "km/h across all wheels at 40 km/h")
            ),
            possibleRepairs = listOf(
                RepairItem("Replace damaged Left Front wheel speed sensor and harness lead", "High", "Moderate"),
                RepairItem("Clean tone ring and clear debris from sensor tip", "Medium", "Easy"),
                RepairItem("Replace wheel bearing hub with integrated magnetic encoder ring", "Medium", "Moderate")
            ),
            toolsRequired = listOf("ABS Diagnostic Scanner", "Digital Multimeter", "Magnetic Tone Ring Viewer Card", "Jack & Safety Stands"),
            safetyPrecautions = listOf("Normal friction hydraulic braking remains functional, but ABS and Electronic Stability Control are disabled. Drive with extra caution in wet or slippery conditions."),
            isGeneric = true,
            vehicleSpecificNotes = "Targeted for $vehicle. Do NOT use magnetic tools near magnetic encoder wheel bearings as they demagnetize the encoder sectors."
        )
    }

    private fun createDynamicDiagnostic(
        code: String,
        vehicle: String,
        fuel: String,
        input: VehicleInput
    ): DiagnosticResult {
        val prefix = code.firstOrNull()?.uppercaseChar() ?: 'P'
        val systemCategory = when (prefix) {
            'P' -> "Powertrain (Engine, Fuel, Emissions, Transmission)"
            'B' -> "Body (Airbags, Central Locking, Climate, Lighting, BCM)"
            'C' -> "Chassis (ABS Brakes, Steering, Suspension, ESC)"
            'U' -> "Network & Communication (CAN-Bus, LIN, FlexRay, Gateway)"
            else -> "Automotive Electronic Control System"
        }

        val severity = when (prefix) {
            'P', 'U' -> SeverityLevel.HIGH
            'C' -> SeverityLevel.HIGH
            'B' -> SeverityLevel.MEDIUM
            else -> SeverityLevel.MEDIUM
        }

        return DiagnosticResult(
            faultCode = code,
            title = "Automotive Fault Code $code - Diagnostic Specification",
            severity = severity,
            systemInvolved = systemCategory,
            summary = "OBD-II DTC $code registered by electronic control unit for $vehicle ($fuel). System detected telemetry outside factory calibrated parameters. Cross-referencing technical service documentation.",
            symptoms = if (input.selectedSymptoms.isNotEmpty()) {
                input.selectedSymptoms
            } else {
                listOf(
                    "Warning light illuminated on instrument cluster",
                    "Stored freeze-frame data present in ECU memory",
                    "Performance limitation or fallback mode active",
                    "Emission test or system self-readiness check incomplete"
                )
            },
            possibleCauses = listOf(
                "Sensor circuit open, high resistance, or short to ground",
                "Loose, corroded, or bent connector pins at component harness",
                "Component mechanical wear, contamination, or thermal failure",
                "Blown circuit fuse or faulty system relay in auxiliary fuse box",
                "Unmetered air, vacuum leak, or fuel/hydraulic pressure deviation"
            ),
            diagnosticProcedure = listOf(
                "Step 1: Record Freeze Frame parameters (RPM, coolant temperature, vehicle speed, load) when the code occurred.",
                "Step 2: Perform visual inspection of wiring harnesses, connectors, and ground points connected to $code.",
                "Step 3: Measure supply voltage (12V B+ or 5V reference) and ground circuit integrity using digital multimeter.",
                "Step 4: Check signal line for proper analog voltage or frequency duty cycle with scanner live data or oscilloscope.",
                "Step 5: Perform bidirectional actuation test if supported by scanner before replacing parts."
            ),
            sensorChecks = listOf(
                SensorCheckItem("Circuit Supply Voltage", "Harness B+ pin to Ground", "Multimeter DC Volts", "12.0V - 14.4V (or 5.0V ± 0.1V for reference sensors)"),
                SensorCheckItem("Circuit Ground Drop", "Ground pin to Battery Negative", "Multimeter Voltage Drop", "< 0.05V (< 50 millivolts)"),
                SensorCheckItem("Component Coil / Resistance", "Component pins (unplugged)", "Multimeter Ohms", "Compare against manufacturer workshop manual specifications")
            ),
            expectedValues = listOf(
                ExpectedValueItem("System Voltage", "12.4 - 12.6", "13.8 - 14.4", "Volts (Key ON vs Engine Running)"),
                ExpectedValueItem("Sensor Reference", "4.95 - 5.05", "4.95 - 5.05", "Volts DC steady")
            ),
            possibleRepairs = listOf(
                RepairItem("Inspect, clean, and reseat harness connector terminals", "High", "Easy"),
                RepairItem("Verify and repair damaged wiring loom or poor chassis ground", "High", "Moderate"),
                RepairItem("Test and replace defective sensor or actuator component", "Medium", "Moderate"),
                RepairItem("Clear DTC and perform drive cycle readiness verification", "Medium", "Easy")
            ),
            toolsRequired = listOf(
                "Digital Multimeter (CAT III 600V)",
                "Bi-directional Diagnostic Scan Tool",
                "Backprobe Test Lead Kit",
                "Vehicle Workshop Electrical Wiring Schematics"
            ),
            safetyPrecautions = listOf(
                "Always adhere to safety rules: block wheels, engage handbrake, and ensure vehicle cannot move during engine testing.",
                "Do not puncture wiring insulation directly with sharp test probes; use backprobes at connector seals to avoid green wire corrosion.",
                "If testing high voltage EV/Hybrid circuits, never touch orange harnesses without high-voltage isolation equipment."
            ),
            isGeneric = true,
            vehicleSpecificNotes = "Configured for $vehicle ($fuel). Verify vehicle-specific Technical Service Bulletins (TSBs) for known software updates related to $code."
        )
    }
}
