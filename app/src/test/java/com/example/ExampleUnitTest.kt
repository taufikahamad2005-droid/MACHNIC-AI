package com.example

import com.example.data.automotive.AutomotiveKnowledgeBase
import com.example.data.model.SeverityLevel
import com.example.data.model.VehicleInput
import com.example.data.repository.LibraryRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testP0300DiagnosticStructure() {
        val input = VehicleInput(
            faultCode = "P0300",
            make = "Toyota",
            model = "Corolla",
            year = "2020",
            fuelType = "Petrol"
        )
        val result = AutomotiveKnowledgeBase.getDiagnostic(input)

        assertEquals("P0300", result.faultCode)
        assertEquals(SeverityLevel.HIGH, result.severity)
        assertTrue(result.symptoms.isNotEmpty())
        assertTrue(result.possibleCauses.isNotEmpty())
        assertTrue(result.diagnosticProcedure.isNotEmpty())
        assertTrue(result.sensorChecks.isNotEmpty())
        assertTrue(result.expectedValues.isNotEmpty())
        assertTrue(result.possibleRepairs.isNotEmpty())
        assertTrue(result.toolsRequired.isNotEmpty())
        assertTrue(result.safetyPrecautions.isNotEmpty())
    }

    @Test
    fun testCRDiP0087Diagnostic() {
        val input = VehicleInput(
            faultCode = "P0087",
            make = "Hyundai",
            model = "Creta CRDi",
            year = "2021",
            fuelType = "Diesel"
        )
        val result = AutomotiveKnowledgeBase.getDiagnostic(input)

        assertEquals("P0087", result.faultCode)
        assertEquals(SeverityLevel.CRITICAL, result.severity)
        assertTrue(result.systemInvolved.contains("Common Rail") || result.systemInvolved.contains("High-Pressure"))
    }

    @Test
    fun testDynamicCodeHandling() {
        val input = VehicleInput(
            faultCode = "P0234",
            make = "Ford",
            model = "Ranger",
            year = "2019"
        )
        val result = AutomotiveKnowledgeBase.getDiagnostic(input)

        assertEquals("P0234", result.faultCode)
        assertNotNull(result.summary)
        assertTrue(result.diagnosticProcedure.isNotEmpty())
    }

    @Test
    fun testLibraryCategoriesAndGuides() {
        assertEquals(15, LibraryRepository.categories.size)
        assertTrue(LibraryRepository.guides.isNotEmpty())

        val turboGuides = LibraryRepository.getGuidesByCategory("turbo")
        assertTrue(turboGuides.isNotEmpty())

        val searchResult = LibraryRepository.searchGuides("multimeter")
        assertTrue(searchResult.isNotEmpty())
    }
}
