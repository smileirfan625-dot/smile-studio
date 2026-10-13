package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkBorderGold
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.InfoBlue
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber
import com.example.ui.viewmodel.AppScreen

@Composable
fun StudioTopBar(
    currentScreen: AppScreen,
    unreadEnquiriesCount: Int,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("studio_top_bar"),
        color = DarkBackground.copy(alpha = 0.95f),
        tonalElevation = 4.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onNavigate(AppScreen.HOME) }
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(GoldLight, GoldDark)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = "Smile Studio Icon",
                        tint = Color(0xFF141208),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "SMILE STUDIO",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        letterSpacing = 1.2.sp,
                        color = GoldLight
                    )
                    Text(
                        text = "CINEMATIC & PHOTO",
                        fontSize = 9.sp,
                        letterSpacing = 1.5.sp,
                        color = TextSecondary
                    )
                }
            }

            // Quick Actions: Lookup & Admin & Book CTA
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Booking status lookup
                IconButton(
                    onClick = { onNavigate(AppScreen.LOOKUP) },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceVariant)
                        .testTag("top_bar_lookup_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Track Booking Status",
                        tint = if (currentScreen == AppScreen.LOOKUP) GoldPrimary else TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Admin Dashboard button
                IconButton(
                    onClick = { onNavigate(AppScreen.ADMIN) },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceVariant)
                        .testTag("top_bar_admin_button")
                ) {
                    if (unreadEnquiriesCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = GoldPrimary,
                                    contentColor = Color.Black
                                ) {
                                    Text("$unreadEnquiriesCount", fontSize = 9.sp)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = "Admin Portal",
                                tint = if (currentScreen == AppScreen.ADMIN) GoldPrimary else TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin Portal",
                            tint = if (currentScreen == AppScreen.ADMIN) GoldPrimary else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Fast Book Button
                Button(
                    onClick = { onNavigate(AppScreen.BOOKING) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = Color(0xFF141208)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        horizontal = 12.dp,
                        vertical = 6.dp
                    ),
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("top_bar_book_now_button")
                ) {
                    Text(
                        text = "Book Now",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun StudioBottomNavBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .testTag("studio_bottom_nav_bar"),
        containerColor = DarkSurface,
        contentColor = TextPrimary,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple(AppScreen.HOME, "Home", Icons.Default.Home),
            Triple(AppScreen.SERVICES, "Services", Icons.Default.Event),
            Triple(AppScreen.PORTFOLIO, "Gallery", Icons.Default.Collections),
            Triple(AppScreen.PACKAGES, "Packages", Icons.Default.PriceChange),
            Triple(AppScreen.BOOKING, "Book", Icons.Default.BookmarkBorder),
            Triple(AppScreen.CONTACT, "Contact", Icons.Default.Call)
        )

        items.forEach { (screen, label, icon) ->
            val isSelected = currentScreen == screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(screen) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF141208),
                    selectedTextColor = GoldLight,
                    indicatorColor = GoldPrimary,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                ),
                modifier = Modifier.testTag("nav_item_${label.lowercase()}")
            )
        }
    }
}

@Composable
fun StudioImage(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    Box(
        modifier = modifier
            .background(DarkSurfaceVariant)
    ) {
        AsyncImage(
            model = url,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.matchParentSize()
        )
    }
}

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status.uppercase()) {
        "CONFIRMED" -> Triple(SuccessGreen.copy(alpha = 0.2f), SuccessGreen, "Confirmed")
        "PENDING" -> Triple(WarningAmber.copy(alpha = 0.2f), WarningAmber, "Pending Review")
        "RESCHEDULE_REQUESTED", "RESCHEDULED" -> Triple(InfoBlue.copy(alpha = 0.2f), InfoBlue, "Rescheduled")
        "REJECTED" -> Triple(ErrorRed.copy(alpha = 0.2f), ErrorRed, "Rejected")
        "CANCELLED" -> Triple(DarkBorder, TextSecondary, "Cancelled")
        "COMPLETED" -> Triple(GoldContainer, GoldLight, "Completed")
        else -> Triple(DarkSurfaceVariant, TextSecondary, status)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, textColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun PaymentBadge(
    paymentStatus: String,
    amountPaid: Double,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (paymentStatus.uppercase()) {
        "PAID" -> Triple(SuccessGreen.copy(alpha = 0.2f), SuccessGreen, "Paid in Full")
        "PARTIAL" -> Triple(InfoBlue.copy(alpha = 0.2f), InfoBlue, "Deposit Paid: ₹${amountPaid.toInt()}")
        else -> Triple(WarningAmber.copy(alpha = 0.2f), WarningAmber, "Payment Pending")
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, textColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SectionHeader(
    tagline: String,
    title: String,
    description: String? = null,
    modifier: Modifier = Modifier,
    alignCenter: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (alignCenter) Alignment.CenterHorizontally else Alignment.Start
    ) {
        Text(
            text = tagline.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            color = GoldPrimary,
            textAlign = if (alignCenter) TextAlign.Center else TextAlign.Start
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = if (alignCenter) TextAlign.Center else TextAlign.Start
        )
        if (!description.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = TextSecondary,
                lineHeight = 20.sp,
                textAlign = if (alignCenter) TextAlign.Center else TextAlign.Start
            )
        }
    }
}
