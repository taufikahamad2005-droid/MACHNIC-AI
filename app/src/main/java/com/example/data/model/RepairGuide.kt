package com.example.data.model

data class RepairGuide(
    val id: String,
    val categoryId: String,
    val categoryName: String,
    val title: String,
    val titleHi: String,
    val summary: String,
    val summaryHi: String,
    val symptoms: List<String>,
    val testPoints: List<String>,
    val multimeterSpecs: List<String>,
    val stepByStep: List<String>,
    val safetyTips: List<String>,
    val isVerifiedOEM: Boolean = false,
    val tags: List<String> = emptyList()
)

data class LibraryCategory(
    val id: String,
    val name: String,
    val nameHi: String,
    val iconName: String,
    val guideCount: Int
)
