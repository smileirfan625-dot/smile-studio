package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PackagePlan
import com.example.data.model.PortfolioMedia
import com.example.ui.components.SectionHeader
import com.example.ui.components.StudioImage
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkBorderGold
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.StudioViewModel

@Composable
fun HomeScreen(
    viewModel: StudioViewModel,
    packages: List<PackagePlan>,
    portfolio: List<PortfolioMedia>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val studioInfo = viewModel.studioInfo

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .testTag("home_screen_scroll"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. Cinematic Full-Screen Hero Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(440.dp)
            ) {
                // Background image with dark gradient overlay
                StudioImage(
                    url = "https://images.unsplash.com/photo-1519741497674-611481863552?w=1200&auto=format&fit=crop",
                    contentDescription = "Smile Studio Hero Background",
                    modifier = Modifier.fillMaxSize()
                )

                // Cinematic Dark Radial & Linear Scrim
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.4f),
                                    Color.Black.copy(alpha = 0.75f),
                                    DarkBackground
                                )
                            )
                        )
                )

                // Hero Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.Start
                ) {
                    // Studio pill badge
                    Surface(
                        color = GoldContainer.copy(alpha = 0.9f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Diamond,
                                contentDescription = null,
                                tint = GoldLight,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "PREMIUM CINEMATOGRAPHY & PHOTO",
                                color = GoldLight,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = studioInfo.tagline, // "Every Smile Tells a Story."
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary,
                        lineHeight = 38.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = studioInfo.mission, // "Capturing your most beautiful moments with creativity, emotion, and cinematic storytelling."
                        fontSize = 14.sp,
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // 3 Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.navigateTo(AppScreen.BOOKING) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldPrimary,
                                contentColor = Color(0xFF141208)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1.2f)
                                .height(46.dp)
                                .testTag("hero_book_event_button")
                        ) {
                            Text(
                                text = "Book Your Event",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        OutlinedButton(
                            onClick = { viewModel.navigateTo(AppScreen.PORTFOLIO) },
                            border = androidx.compose.foundation.BorderStroke(1.dp, GoldLight),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldLight),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("hero_explore_work_button")
                        ) {
                            Text(
                                text = "Explore Work",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        OutlinedButton(
                            onClick = { viewModel.navigateTo(AppScreen.PACKAGES) },
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("hero_view_packages_button")
                        ) {
                            Text(
                                text = "Packages",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Quick Stats / Highlights Strip
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
                    .padding(vertical = 14.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("500+", color = GoldLight, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Weddings & Shoots", color = TextSecondary, fontSize = 11.sp)
                }
                Box(modifier = Modifier.width(1.dp).height(32.dp).background(DarkBorder))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("4K HDR", color = GoldLight, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Cinema Standards", color = TextSecondary, fontSize = 11.sp)
                }
                Box(modifier = Modifier.width(1.dp).height(32.dp).background(DarkBorder))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("4.9 ★", color = GoldLight, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Verified Reviews", color = TextSecondary, fontSize = 11.sp)
                }
            }
        }

        // 2. Featured Photography & Video Highlights
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionHeader(
                        tagline = "Selected Portfolio",
                        title = "Featured Highlights"
                    )
                    Text(
                        text = "View All →",
                        color = GoldPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable { viewModel.navigateTo(AppScreen.PORTFOLIO) }
                            .padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    val featured = portfolio.filter { it.isFeatured }.ifEmpty { portfolio.take(5) }
                    items(featured) { item ->
                        Card(
                            modifier = Modifier
                                .width(240.dp)
                                .height(260.dp)
                                .clickable {
                                    viewModel.setPreviewMedia(item)
                                    viewModel.navigateTo(AppScreen.PORTFOLIO)
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                StudioImage(
                                    url = item.imageUrl,
                                    contentDescription = item.title,
                                    modifier = Modifier.fillMaxSize()
                                )
                                // Gradient
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(
                                                    Color.Transparent,
                                                    Color.Black.copy(alpha = 0.85f)
                                                )
                                            )
                                        )
                                )

                                // Video tag or photo badge
                                Surface(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(10.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.Black.copy(alpha = 0.7f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderGold)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = if (item.mediaType == "VIDEO") Icons.Default.PlayCircle else Icons.Default.CameraAlt,
                                            contentDescription = null,
                                            tint = GoldLight,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = item.duration ?: item.category,
                                            color = TextPrimary,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                                // Title & Category at bottom
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = item.category.uppercase(),
                                        fontSize = 10.sp,
                                        color = GoldLight,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = item.title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Popular Event Categories
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                SectionHeader(
                    tagline = "Our Expertise",
                    title = "Popular Event Coverage",
                    description = "Choose from 17 customized event categories tailored for unforgettable captures."
                )

                Spacer(modifier = Modifier.height(14.dp))

                val popularCategories = viewModel.eventCategories.take(4)
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    popularCategories.forEach { category ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.startBookingForEvent(category.name)
                                },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                ) {
                                    StudioImage(
                                        url = category.imageUrl,
                                        contentDescription = category.name,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = category.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = TextPrimary
                                        )
                                        if (category.popularTag != null) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                color = GoldContainer,
                                                shape = RoundedCornerShape(6.dp),
                                                border = androidx.compose.foundation.BorderStroke(0.5.dp, GoldPrimary)
                                            ) {
                                                Text(
                                                    text = category.popularTag,
                                                    color = GoldLight,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = category.description,
                                        fontSize = 12.sp,
                                        color = TextSecondary,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Starts at ₹${category.startingPrice.toInt()}",
                                        fontSize = 12.sp,
                                        color = GoldLight,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Book",
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = { viewModel.navigateTo(AppScreen.SERVICES) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderGold)
                ) {
                    Text(
                        text = "Browse All 17 Event Categories",
                        color = GoldLight,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // 4. Featured Packages Preview
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                SectionHeader(
                    tagline = "Transparent Pricing",
                    title = "Featured Studio Packages",
                    description = "Comprehensive photography and videography plans with zero hidden fees."
                )

                Spacer(modifier = Modifier.height(14.dp))

                val featuredPackages = packages.take(3)
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    featuredPackages.forEach { pkg ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (pkg.isPopular) Color(0xFF1E1C16) else DarkSurface
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (pkg.isPopular) GoldPrimary else DarkBorder
                            )
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = pkg.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = if (pkg.isPopular) GoldLight else TextPrimary
                                    )
                                    if (pkg.isPopular) {
                                        Surface(
                                            color = GoldPrimary,
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = "MOST POPULAR",
                                                color = Color.Black,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = "₹${pkg.price.toInt()}",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = GoldLight
                                    )
                                    Text(
                                        text = " / event",
                                        fontSize = 13.sp,
                                        color = TextSecondary,
                                        modifier = Modifier.padding(bottom = 2.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = pkg.description,
                                    fontSize = 13.sp,
                                    color = TextSecondary
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Inclusions snippet
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = SuccessGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${pkg.coverageHours}h Coverage • ${pkg.teamSize} Crew • ${pkg.editedPhotos}",
                                        fontSize = 12.sp,
                                        color = TextPrimary
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = { viewModel.selectPackageFromList(pkg.id) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (pkg.isPopular) GoldPrimary else DarkSurfaceVariant,
                                        contentColor = if (pkg.isPopular) Color.Black else GoldLight
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Select Package & Book", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. About Smile Studio Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderGold)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    SectionHeader(
                        tagline = "Our Philosophy",
                        title = "About Smile Studio"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Founded with the conviction that authentic moments hold infinite value, Smile Studio blends documentary candids with high-fashion lighting and cinematic video grading. Whether an intimate engagement or a grand wedding, we capture genuine laughter, unscripted tears, and radiant celebrations.",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            color = DarkSurfaceVariant
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Icon(Icons.Default.CameraAlt, contentDescription = null, tint = GoldLight)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Sony FX & A1 Cinema", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                                Text("Full-frame master optics", fontSize = 11.sp, color = TextSecondary)
                            }
                        }

                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            color = DarkSurfaceVariant
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Icon(Icons.Default.Movie, contentDescription = null, tint = GoldLight)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Drone Aerial 4K", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                                Text("Licensed cinematographers", fontSize = 11.sp, color = TextSecondary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Contact: ${studioInfo.formattedPhone} • ${studioInfo.email}",
                        fontSize = 12.sp,
                        color = GoldLight,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // 6. Testimonials
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 20.dp)
            ) {
                SectionHeader(
                    tagline = "Client Love",
                    title = "What Couples Say",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(viewModel.testimonials) { t ->
                        Card(
                            modifier = Modifier
                                .width(280.dp)
                                .height(190.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row {
                                    repeat(t.rating) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = GoldPrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "“${t.review}”",
                                    fontSize = 12.sp,
                                    color = TextPrimary,
                                    maxLines = 4,
                                    overflow = TextOverflow.Ellipsis,
                                    lineHeight = 18.sp
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(t.clientName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = GoldLight)
                                        Text(t.eventType, fontSize = 10.sp, color = TextSecondary)
                                    }
                                    Text(t.dateText, fontSize = 10.sp, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 7. Final Booking Call to Action Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF2E2412),
                                Color(0xFF14141A)
                            )
                        )
                    )
                    .border(1.dp, GoldPrimary, RoundedCornerShape(20.dp))
                    .padding(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "READY TO CAPTURE YOUR STORY?",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Dates for 2026 Season Are Filling Fast",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Check our live availability calendar, select your custom package, and get your booking reference in under 2 minutes.",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.navigateTo(AppScreen.BOOKING) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldPrimary,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).height(48.dp)
                        ) {
                            Text("Book Event Online", fontWeight = FontWeight.Bold)
                        }

                        // WhatsApp connect
                        Button(
                            onClick = {
                                val url = "https://wa.me/919113272998?text=Hello%20Smile%20Studio,%20I%20would%20like%20to%20enquire%20about%20event%20booking"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkSurfaceVariant,
                                contentColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = "WhatsApp", tint = SuccessGreen)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("WhatsApp")
                        }
                    }
                }
            }
        }
    }
}
