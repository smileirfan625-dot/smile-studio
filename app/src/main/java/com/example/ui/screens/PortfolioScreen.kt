package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.PortfolioMedia
import com.example.ui.components.SectionHeader
import com.example.ui.components.StudioImage
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkBorderGold
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.StudioViewModel

@Composable
fun PortfolioScreen(
    viewModel: StudioViewModel,
    portfolioList: List<PortfolioMedia>,
    modifier: Modifier = Modifier
) {
    val selectedCategory by viewModel.selectedPortfolioCategory.collectAsState()
    val activePreview by viewModel.activePreviewMedia.collectAsState()
    val isAdminAuthenticated by viewModel.isAdminAuthenticated.collectAsState()

    var showAddMediaDialog by remember { mutableStateOf(false) }
    var showAdminPromptDialog by remember { mutableStateOf(false) }
    var adminPinInput by remember { mutableStateOf("") }
    var adminPinError by remember { mutableStateOf<String?>(null) }

    val onTriggerUpload = {
        if (isAdminAuthenticated) {
            showAddMediaDialog = true
        } else {
            showAdminPromptDialog = true
        }
    }

    val categories = listOf(
        "All",
        "Wedding",
        "Pre-Wedding",
        "Portraits",
        "Family",
        "Birthday",
        "Cinematic Video",
        "Reels",
        "Events"
    )

    val filteredList = if (selectedCategory == "All") {
        portfolioList
    } else {
        portfolioList.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .testTag("portfolio_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        SectionHeader(
                            tagline = "Curated Visuals",
                            title = "Portfolio & Films Gallery",
                            description = "Explore genuine masterworks created for clients across India. Tap any asset for cinematic preview."
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onTriggerUpload,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.height(38.dp).testTag("upload_media_header_btn")
                    ) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Upload", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Horizontal Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    items(categories) { cat ->
                        val isSelected = selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectPortfolioCategory(cat) },
                            label = {
                                Text(
                                    text = cat,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GoldPrimary,
                                selectedLabelColor = Color.Black,
                                containerColor = DarkSurface,
                                labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) GoldPrimary else DarkBorder
                            ),
                            modifier = Modifier.testTag("filter_chip_$cat")
                        )
                    }
                }
            }

            // Gallery Grid
            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = DarkBorderGold,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No media in $selectedCategory yet",
                            color = TextSecondary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Check back soon as our studio uploads new films and photos.",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onTriggerUpload,
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Upload First Item", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 90.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("portfolio_grid")
                ) {
                    items(filteredList, key = { it.id }) { item ->
                        PortfolioItemCard(
                            item = item,
                            onClick = { viewModel.setPreviewMedia(item) }
                        )
                    }
                }
            }
        }

        // Floating Action Button for Upload
        ExtendedFloatingActionButton(
            onClick = onTriggerUpload,
            icon = { Icon(Icons.Default.CloudUpload, contentDescription = "Upload Photos & Videos", tint = Color.Black) },
            text = { Text("Upload Media", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp) },
            containerColor = GoldPrimary,
            contentColor = Color.Black,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp)
                .testTag("fab_upload_media")
        )
    }

    // Admin Verification Dialog for Uploading
    if (showAdminPromptDialog) {
        Dialog(onDismissRequest = {
            showAdminPromptDialog = false
            adminPinInput = ""
            adminPinError = null
        }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = DarkSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderGold),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(GoldContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = GoldLight)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Studio Admin Verification", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
                    Text(
                        text = "Enter administrator password to upload photos and videos to the public gallery",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (adminPinError != null) {
                        Text(adminPinError ?: "", color = ErrorRed, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    OutlinedTextField(
                        value = adminPinInput,
                        onValueChange = { adminPinInput = it },
                        placeholder = { Text("Enter Password", color = TextSecondary) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("gallery_admin_pin_input")
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                showAdminPromptDialog = false
                                adminPinInput = ""
                                adminPinError = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant, contentColor = TextPrimary),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (adminPinInput.trim() == "9741") {
                                    viewModel.authenticateAdmin("9741")
                                    showAdminPromptDialog = false
                                    adminPinInput = ""
                                    adminPinError = null
                                    showAddMediaDialog = true
                                } else {
                                    adminPinError = "Incorrect password. Please try again."
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black),
                            modifier = Modifier.weight(1f).testTag("gallery_verify_pin_btn")
                        ) {
                            Text("Unlock & Upload", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Add Media Dialog
    if (showAddMediaDialog) {
        AddMediaDialog(
            onDismiss = { showAddMediaDialog = false },
            onAdd = { title, cat, type, url, vidUrl, caption, duration, isFeatured ->
                viewModel.addPortfolioMedia(title, cat, type, url, vidUrl, caption, duration, isFeatured)
                showAddMediaDialog = false
            }
        )
    }

    // Full Screen Preview Dialog
    activePreview?.let { media ->
        MediaPreviewDialog(
            media = media,
            onDismiss = { viewModel.setPreviewMedia(null) },
            onBookCategory = {
                viewModel.setPreviewMedia(null)
                viewModel.startBookingForEvent(media.category)
            }
        )
    }
}

@Composable
private fun PortfolioItemCard(
    item: PortfolioMedia,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .clickable(onClick = onClick)
            .testTag("portfolio_card_${item.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            StudioImage(
                url = item.imageUrl,
                contentDescription = item.title,
                modifier = Modifier.fillMaxSize()
            )

            // Dark bottom vignette
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

            // Play Icon Overlay if Video
            if (item.mediaType == "VIDEO") {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.65f))
                        .border(1.dp, GoldLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play Video",
                        tint = GoldLight,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Top tags: Duration or Category
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                shape = RoundedCornerShape(6.dp),
                color = Color.Black.copy(alpha = 0.7f),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, DarkBorderGold)
            ) {
                Text(
                    text = item.duration ?: item.category,
                    fontSize = 10.sp,
                    color = GoldLight,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }

            // Title at bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
            ) {
                Text(
                    text = item.category.uppercase(),
                    fontSize = 9.sp,
                    color = GoldLight,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun MediaPreviewDialog(
    media: PortfolioMedia,
    onDismiss: () -> Unit,
    onBookCategory: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .testTag("media_preview_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = DarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderGold)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top close bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = media.category.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = if (media.mediaType == "VIDEO") "Cinematic Film Preview" else "Master Photography",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceVariant)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Media Display Frame
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black)
                        .border(1.dp, DarkBorder, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    StudioImage(
                        url = media.imageUrl,
                        contentDescription = media.title,
                        modifier = Modifier.fillMaxSize()
                    )

                    if (media.mediaType == "VIDEO") {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.Black.copy(alpha = 0.65f))
                                .padding(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = null,
                                tint = GoldLight,
                                modifier = Modifier.size(54.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "4K Cinematic Playback (${media.duration ?: "HD"})",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Master Audio Track • Dolby Graded",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Metadata & Caption
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = media.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = media.caption.ifBlank { "Original client capture produced by Smile Studio cinematography unit." },
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onBookCategory,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(46.dp)
                    ) {
                        Icon(Icons.Default.BookmarkBorder, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Book This Style", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkSurfaceVariant,
                            contentColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(46.dp)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}
