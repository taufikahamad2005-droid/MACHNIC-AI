package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LibraryCategory
import com.example.data.model.RepairGuide
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.AppStrings
import com.example.ui.theme.DiagnosticGreen
import com.example.ui.theme.ElectricBlueLight
import com.example.ui.theme.ElectricBluePrimary
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCardBackground
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyDarkBackground
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun LibraryScreen(
    categories: List<LibraryCategory>,
    selectedCategory: String?,
    onSelectCategory: (String?) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    guides: List<RepairGuide>,
    strings: AppStrings,
    currentLanguage: AppLanguage,
    onOpenGuide: (RepairGuide) -> Unit,
    modifier: Modifier = Modifier
) {
    val isHindi = currentLanguage == AppLanguage.HINDI

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDarkBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Search repair guides, systems, or sensors...", color = TextTertiary, fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = ElectricBlueLight) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("library_search_input"),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ElectricBluePrimary,
                    unfocusedBorderColor = NavyBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = NavyCardBackground,
                    unfocusedContainerColor = NavyCardBackground
                )
            )
        }

        // Category Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { onSelectCategory(null) },
                        label = { Text("All (15 Categories)", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricBluePrimary,
                            selectedLabelColor = TextPrimary,
                            containerColor = NavyCardBackground,
                            labelColor = TextSecondary
                        ),
                        modifier = Modifier.testTag("library_cat_all")
                    )
                }

                items(categories) { cat ->
                    val isSelected = selectedCategory == cat.id
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectCategory(if (isSelected) null else cat.id) },
                        label = { Text(if (isHindi) cat.nameHi else cat.name, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricBluePrimary,
                            selectedLabelColor = TextPrimary,
                            containerColor = NavyCardBackground,
                            labelColor = TextSecondary
                        ),
                        modifier = Modifier.testTag("library_cat_${cat.id}")
                    )
                }
            }
        }

        // Guide Cards List
        if (guides.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.MenuBook, contentDescription = null, tint = TextTertiary, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No repair guides found matching your search.", color = TextSecondary, fontSize = 13.sp)
                    }
                }
            }
        } else {
            items(guides, key = { it.id }) { guide ->
                GuideCard(
                    guide = guide,
                    isHindi = isHindi,
                    onClick = { onOpenGuide(guide) }
                )
            }
        }
    }
}

@Composable
private fun GuideCard(
    guide: RepairGuide,
    isHindi: Boolean,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = NavyCardBackground),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("guide_card_${guide.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = NavyCardElevated,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = guide.categoryName,
                        color = ElectricBlueLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                if (guide.isVerifiedOEM) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = DiagnosticGreen, modifier = Modifier.size(14.dp))
                        Text("Verified OEM Specs", color = DiagnosticGreen, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Text(
                text = if (isHindi) guide.titleHi else guide.title,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (isHindi) guide.summaryHi else guide.summary,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${guide.testPoints.size} Test Points • ${guide.multimeterSpecs.size} Multimeter Specs",
                    color = TextTertiary,
                    fontSize = 10.sp
                )

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = ElectricBlueLight,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
