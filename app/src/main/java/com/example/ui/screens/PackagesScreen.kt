package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PackagePlan
import com.example.ui.components.SectionHeader
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkBorderGold
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.StudioViewModel

@Composable
fun PackagesScreen(
    viewModel: StudioViewModel,
    packages: List<PackagePlan>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .testTag("packages_screen_scroll"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        item {
            SectionHeader(
                tagline = "Investment & Value",
                title = "Pricing & Packages",
                description = "Transparent event packages in INR (₹). Sample rates can be customized with optional drone, album, and cinematic add-ons."
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Custom Quote Notice Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderGold)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Diamond,
                        contentDescription = null,
                        tint = GoldLight,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Planning a Multi-Day Wedding or Festival?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "We offer bespoke customized rates for destination events.",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                    Button(
                        onClick = { viewModel.navigateTo(AppScreen.CONTACT) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Get Quote", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
        }

        items(packages, key = { it.id }) { pkg ->
            PackageDetailCard(
                pkg = pkg,
                onSelectPackage = {
                    viewModel.selectPackageFromList(pkg.id)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Add-ons list section
        item {
            Spacer(modifier = Modifier.height(10.dp))
            SectionHeader(
                tagline = "Enhance Your Experience",
                title = "Optional Add-Ons",
                description = "Mix and match these premium deliverables with any package."
            )
            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AddOnRow(title = "4K Drone Aerial Cinematography", price = "₹12,000", desc = "Licensed drone pilot, cinematic flyovers & venue establishing shots")
                AddOnRow(title = "Handcrafted Flush-Mount Album (40 Pages)", price = "₹15,000", desc = "Velvet/Leatherette presentation box + metallic art paper")
                AddOnRow(title = "Raw Footage Archive Drive (1TB SSD)", price = "₹8,000", desc = "All unedited 4K video clips & high-res RAW camera files")
                AddOnRow(title = "4K Multi-Camera Live Streaming", price = "₹10,000", desc = "Private YouTube/Zoom link for overseas family and guests")
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun PackageDetailCard(
    pkg: PackagePlan,
    onSelectPackage: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("package_card_${pkg.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (pkg.isPopular) Color(0xFF1C1A14) else DarkSurface
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (pkg.isPopular) GoldPrimary else DarkBorder
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header: Title and Popular Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = pkg.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (pkg.isPopular) GoldLight else TextPrimary
                    )
                    Text(
                        text = if (pkg.isActive) "Available to Book" else "Seasonal Availability",
                        fontSize = 11.sp,
                        color = if (pkg.isActive) SuccessGreen else TextSecondary
                    )
                }

                if (pkg.isPopular) {
                    Surface(
                        color = GoldPrimary,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "POPULAR CHOICE",
                            color = Color.Black,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Pricing
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "₹${pkg.price.toInt()}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldLight
                )
                Text(
                    text = " INR / event",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 3.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = pkg.description,
                fontSize = 13.sp,
                color = TextSecondary,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Spec Highlights Grid
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurfaceVariant)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = GoldLight, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Coverage: ${pkg.coverageHours} Hours on Location", fontSize = 12.sp, color = TextPrimary)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Groups, contentDescription = null, tint = GoldLight, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Crew: ${pkg.teamSize} Master Photographers / Videographers", fontSize = 12.sp, color = TextPrimary)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = GoldLight, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Stills: ${pkg.editedPhotos}", fontSize = 12.sp, color = TextPrimary)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Videocam, contentDescription = null, tint = GoldLight, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Deliverable: ${pkg.videoDeliverable}", fontSize = 12.sp, color = TextPrimary)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.HourglassBottom, contentDescription = null, tint = GoldLight, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Delivery Timeline: ${pkg.deliveryTimelineDays} Days to Master Gallery", fontSize = 12.sp, color = TextPrimary)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Inclusions chips/bullets
            Text(
                text = "PACKAGE INCLUSIONS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))

            pkg.inclusions.split(",").map { it.trim() }.forEach { inclusion ->
                if (inclusion.isNotBlank()) {
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = inclusion,
                            fontSize = 12.sp,
                            color = TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onSelectPackage,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (pkg.isPopular) GoldPrimary else DarkSurfaceVariant,
                    contentColor = if (pkg.isPopular) Color.Black else GoldLight
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("select_package_button_${pkg.id}")
            ) {
                Icon(Icons.Default.BookmarkAdded, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Select & Customize in Booking",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun AddOnRow(
    title: String,
    price: String,
    desc: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                Spacer(modifier = Modifier.height(2.dp))
                Text(desc, fontSize = 11.sp, color = TextSecondary)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(price, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = GoldLight)
        }
    }
}
