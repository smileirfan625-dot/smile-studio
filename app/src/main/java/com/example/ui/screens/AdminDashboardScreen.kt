package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.BlockedDate
import com.example.data.model.Booking
import com.example.data.model.Enquiry
import com.example.data.model.NotificationLog
import com.example.data.model.PackagePlan
import com.example.data.model.PortfolioMedia
import com.example.ui.components.PaymentBadge
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadge
import com.example.ui.components.StudioImage
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
import com.example.ui.viewmodel.StudioViewModel

@Composable
fun AdminDashboardScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val isAuthenticated by viewModel.isAdminAuthenticated.collectAsState()

    if (!isAuthenticated) {
        AdminLoginGate(viewModel = viewModel, modifier = modifier)
    } else {
        AdminDashboardContent(viewModel = viewModel, modifier = modifier)
    }
}

// 1. PIN Authentication Gate
@Composable
private fun AdminLoginGate(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val pin by viewModel.adminPinInput.collectAsState()
    val pinError by viewModel.adminPinError.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp)
            .testTag("admin_login_screen"),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderGold)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(GoldContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = GoldLight,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Studio Management Portal",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Secure Admin Access for Smile Studio",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (pinError != null) {
                    Text(
                        text = pinError ?: "",
                        color = ErrorRed,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                OutlinedTextField(
                    value = pin,
                    onValueChange = { viewModel.adminPinInput.value = it },
                    placeholder = { Text("Enter Admin Password", color = TextSecondary) },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_pin_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = DarkBorder,
                        focusedContainerColor = DarkSurfaceVariant,
                        unfocusedContainerColor = DarkSurfaceVariant
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.authenticateAdmin(pin) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("admin_login_button")
                ) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Unlock Dashboard", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

// 2. Full Admin Dashboard Tabs
@Composable
private fun AdminDashboardContent(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val activeTab by viewModel.activeAdminTab.collectAsState()
    val bookings by viewModel.allBookings.collectAsState()
    val enquiries by viewModel.allEnquiries.collectAsState()
    val portfolioList by viewModel.allPortfolioAdmin.collectAsState()
    val packagesList by viewModel.allPackagesAdmin.collectAsState()
    val blockedDatesList by viewModel.blockedDates.collectAsState()
    val notificationsList by viewModel.notificationLogs.collectAsState()

    val unreadCount = enquiries.count { !it.isRead }
    val tabs = listOf(
        "Overview",
        "Bookings (${bookings.size})",
        "Gallery (${portfolioList.size})",
        "Packages",
        "Calendar",
        if (unreadCount > 0) "Inbox ($unreadCount)" else "Inbox",
        "Notifications"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .testTag("admin_dashboard_content")
    ) {
        // Admin Header with Logout
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = DarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = GoldLight)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("SMILE STUDIO ADMIN", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("Logged in as Studio Director", fontSize = 10.sp, color = TextSecondary)
                    }
                }

                Button(
                    onClick = { viewModel.logoutAdmin() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkSurfaceVariant,
                        contentColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("Lock Portal", fontSize = 11.sp)
                }
            }
        }

        // Horizontal Scrollable Tabs
        ScrollableTabRow(
            selectedTabIndex = activeTab,
            containerColor = DarkSurface,
            contentColor = GoldLight,
            edgePadding = 12.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                    color = GoldPrimary,
                    height = 2.dp
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = activeTab == index,
                    onClick = { viewModel.activeAdminTab.value = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (activeTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (activeTab == index) GoldLight else TextSecondary
                        )
                    }
                )
            }
        }

        // Tab Content
        Box(modifier = Modifier.fillMaxSize()) {
            when (activeTab) {
                0 -> AdminOverviewTab(
                    bookings = bookings,
                    enquiries = enquiries,
                    portfolio = portfolioList,
                    onSwitchTab = { viewModel.activeAdminTab.value = it }
                )
                1 -> AdminBookingsTab(viewModel = viewModel, bookings = bookings)
                2 -> AdminMediaTab(viewModel = viewModel, portfolio = portfolioList)
                3 -> AdminPackagesTab(viewModel = viewModel, packages = packagesList)
                4 -> AdminCalendarTab(viewModel = viewModel, blockedDates = blockedDatesList, bookings = bookings)
                5 -> AdminInboxTab(viewModel = viewModel, enquiries = enquiries)
                6 -> AdminNotificationsTab(notifications = notificationsList)
            }
        }
    }
}

// TAB 0: Overview & Metrics
@Composable
private fun AdminOverviewTab(
    bookings: List<Booking>,
    enquiries: List<Enquiry>,
    portfolio: List<PortfolioMedia>,
    onSwitchTab: (Int) -> Unit
) {
    val pendingCount = bookings.count { it.status == "PENDING" }
    val confirmedCount = bookings.count { it.status == "CONFIRMED" }
    val completedCount = bookings.count { it.status == "COMPLETED" }
    val unreadCount = enquiries.count { !it.isRead }
    val totalRevenue = bookings.sumOf { it.amountPaid }
    val totalEstimated = bookings.sumOf { it.estimatedTotal }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Business Dashboard & Performance", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("Real-time telemetry based on verified customer bookings and communications.", fontSize = 12.sp, color = TextSecondary)
        }

        // Metrics Grid
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard(
                    title = "Pending Bookings",
                    value = "$pendingCount",
                    color = WarningAmber,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Confirmed Events",
                    value = "$confirmedCount",
                    color = SuccessGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard(
                    title = "Recorded Deposits",
                    value = "₹${totalRevenue.toInt()}",
                    color = GoldLight,
                    subtitle = "Est pipeline: ₹${totalEstimated.toInt()}",
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Unread Enquiries",
                    value = "$unreadCount",
                    color = if (unreadCount > 0) GoldPrimary else TextSecondary,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Quick action shortcuts
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("QUICK WORKFLOW ACTIONS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldLight, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onSwitchTab(1) },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Manage Bookings", fontSize = 11.sp, color = TextPrimary)
                        }
                        Button(
                            onClick = { onSwitchTab(2) },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Add Portfolio", fontSize = 11.sp, color = TextPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onSwitchTab(4) },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Block Calendar Dates", fontSize = 11.sp, color = TextPrimary)
                        }
                        Button(
                            onClick = { onSwitchTab(5) },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Read Enquiries", fontSize = 11.sp, color = TextPrimary)
                        }
                    }
                }
            }
        }

        // Recent Bookings Snippet
        item {
            Text("Recent Booking Requests", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }

        items(bookings.take(3)) { b ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(b.id, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = GoldLight)
                        Text("${b.customerName} • ${b.eventDate}", fontSize = 12.sp, color = TextPrimary)
                        Text(b.eventName, fontSize = 11.sp, color = TextSecondary)
                    }
                    StatusBadge(status = b.status)
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, fontSize = 11.sp, color = TextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = color)
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(subtitle, fontSize = 10.sp, color = TextSecondary)
            }
        }
    }
}

// TAB 1: Booking Management
@Composable
private fun AdminBookingsTab(
    viewModel: StudioViewModel,
    bookings: List<Booking>
) {
    val context = LocalContext.current
    var searchText by remember { mutableStateOf("") }
    var selectedStatusFilter by remember { mutableStateOf("ALL") }
    var editingBooking by remember { mutableStateOf<Booking?>(null) }

    val filtered = bookings.filter { b ->
        val matchesSearch = searchText.isBlank() ||
                b.id.contains(searchText, ignoreCase = true) ||
                b.customerName.contains(searchText, ignoreCase = true) ||
                b.eventName.contains(searchText, ignoreCase = true) ||
                b.eventDate.contains(searchText, ignoreCase = true)
        val matchesStatus = selectedStatusFilter == "ALL" || b.status == selectedStatusFilter
        matchesSearch && matchesStatus
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            // Search & Filter controls
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Search by ID, name, date...", color = TextSecondary) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GoldLight) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoldPrimary,
                    unfocusedBorderColor = DarkBorder,
                    focusedContainerColor = DarkSurface,
                    unfocusedContainerColor = DarkSurface
                ),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Status Filter Chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(listOf("ALL", "PENDING", "CONFIRMED", "COMPLETED", "REJECTED", "CANCELLED")) { st ->
                    val isSel = selectedStatusFilter == st
                    FilterChip(
                        selected = isSel,
                        onClick = { selectedStatusFilter = st },
                        label = { Text(st, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldPrimary,
                            selectedLabelColor = Color.Black,
                            containerColor = DarkSurface,
                            labelColor = TextSecondary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Export CSV action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${filtered.size} Bookings Found", fontSize = 12.sp, color = TextSecondary)

                OutlinedButton(
                    onClick = {
                        val csvContent = buildString {
                            append("Reference,Client,Phone,Email,Date,Package,Total,Paid,Status\n")
                            filtered.forEach { b ->
                                append("${b.id},\"${b.customerName}\",${b.customerPhone},${b.customerEmail},${b.eventDate},\"${b.packageName}\",${b.estimatedTotal},${b.amountPaid},${b.status}\n")
                            }
                        }
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, "Smile Studio Bookings Export")
                            putExtra(Intent.EXTRA_TEXT, csvContent)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Export Bookings CSV"))
                    },
                    modifier = Modifier.height(32.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp), tint = GoldLight)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Export CSV", fontSize = 11.sp, color = GoldLight)
                }
            }
        }

        items(filtered, key = { it.id }) { booking ->
            AdminBookingCard(
                booking = booking,
                onEditStatus = { editingBooking = booking },
                onChatWhatsApp = {
                    val msg = "Hello ${booking.customerName}! Updating you regarding your Smile Studio booking ${booking.id} (${booking.eventDate}). Status: ${booking.status}."
                    val url = "https://wa.me/91${booking.customerPhone.filter { it.isDigit() }}?text=${Uri.encode(msg)}"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                },
                onCall = {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${booking.customerPhone}"))
                    context.startActivity(intent)
                },
                onDelete = { viewModel.deleteBooking(booking) }
            )
        }
    }

    // Edit Booking Dialog
    editingBooking?.let { b ->
        EditBookingStatusDialog(
            booking = b,
            onDismiss = { editingBooking = null },
            onSave = { newStatus, notes, paidAmt, payStatus ->
                viewModel.updateBookingStatus(
                    booking = b,
                    newStatus = newStatus,
                    adminNotes = notes,
                    amountPaid = paidAmt,
                    paymentStatus = payStatus
                )
                editingBooking = null
            }
        )
    }
}

@Composable
private fun AdminBookingCard(
    booking: Booking,
    onEditStatus: () -> Unit,
    onChatWhatsApp: () -> Unit,
    onCall: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(booking.id, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = GoldLight)
                    Text(booking.customerName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                }
                StatusBadge(status = booking.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Event: ${booking.eventName}", fontSize = 12.sp, color = TextPrimary)
            Text("Date: ${booking.eventDate} at ${booking.eventTime} (${booking.durationHours}h)", fontSize = 12.sp, color = TextSecondary)
            Text("Venue: ${booking.venueLocation}", fontSize = 12.sp, color = TextSecondary)
            Text("Package: ${booking.packageName} • Est: ₹${booking.estimatedTotal.toInt()}", fontSize = 12.sp, color = GoldLight)

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                PaymentBadge(paymentStatus = booking.paymentStatus, amountPaid = booking.amountPaid)
                Spacer(modifier = Modifier.width(8.dp))
                if (booking.adminNotes.isNotBlank()) {
                    Text("Note: ${booking.adminNotes}", fontSize = 11.sp, color = TextSecondary, maxLines = 1)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onEditStatus,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(36.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Manage Status", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                IconButton(
                    onClick = onChatWhatsApp,
                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(SuccessGreen.copy(alpha = 0.2f))
                ) {
                    Icon(Icons.Default.Chat, contentDescription = "WhatsApp", tint = SuccessGreen, modifier = Modifier.size(16.dp))
                }

                IconButton(
                    onClick = onCall,
                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(DarkSurfaceVariant)
                ) {
                    Icon(Icons.Default.Call, contentDescription = "Call", tint = GoldLight, modifier = Modifier.size(16.dp))
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(DarkSurfaceVariant)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ErrorRed, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

// Dialog: Update Booking Status & Payments
@Composable
private fun EditBookingStatusDialog(
    booking: Booking,
    onDismiss: () -> Unit,
    onSave: (newStatus: String, notes: String, amountPaid: Double, paymentStatus: String) -> Unit
) {
    var selectedStatus by remember { mutableStateOf(booking.status) }
    var notes by remember { mutableStateOf(booking.adminNotes) }
    var amountPaidText by remember { mutableStateOf(booking.amountPaid.toInt().toString()) }
    var selectedPaymentStatus by remember { mutableStateOf(booking.paymentStatus) }

    val statusOptions = listOf("PENDING", "CONFIRMED", "RESCHEDULED", "REJECTED", "CANCELLED", "COMPLETED")
    val paymentOptions = listOf("PENDING", "PARTIAL", "PAID")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderGold),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Manage Booking: ${booking.id}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
                Text("Client: ${booking.customerName} • Date: ${booking.eventDate}", fontSize = 12.sp, color = TextSecondary)

                Spacer(modifier = Modifier.height(14.dp))

                Text("Update Status*", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldLight)
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(statusOptions) { st ->
                        val isSel = selectedStatus == st
                        FilterChip(
                            selected = isSel,
                            onClick = { selectedStatus = st },
                            label = { Text(st, fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GoldPrimary,
                                selectedLabelColor = Color.Black,
                                containerColor = DarkSurfaceVariant,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text("Payment Status*", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldLight)
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    paymentOptions.forEach { p ->
                        val isSel = selectedPaymentStatus == p
                        FilterChip(
                            selected = isSel,
                            onClick = { selectedPaymentStatus = p },
                            label = { Text(p, fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GoldPrimary,
                                selectedLabelColor = Color.Black,
                                containerColor = DarkSurfaceVariant,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text("Recorded Payment Amount (INR)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldLight)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = amountPaidText,
                    onValueChange = { amountPaidText = it },
                    placeholder = { Text("e.g. 25000", color = TextSecondary) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = DarkBorder,
                        focusedContainerColor = DarkSurfaceVariant,
                        unfocusedContainerColor = DarkSurfaceVariant
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text("Internal Admin Notes", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldLight)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = { Text("e.g. Assigned lead photographer + drone operator. Advance received.", color = TextSecondary) },
                    minLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = DarkBorder,
                        focusedContainerColor = DarkSurfaceVariant,
                        unfocusedContainerColor = DarkSurfaceVariant
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            val paid = amountPaidText.toDoubleOrNull() ?: 0.0
                            onSave(selectedStatus, notes, paid, selectedPaymentStatus)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save Updates", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// TAB 2: Media Management
@Composable
private fun AdminMediaTab(
    viewModel: StudioViewModel,
    portfolio: List<PortfolioMedia>
) {
    var showAddDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Portfolio & Films Manager", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text("Only authorized studio admins can manage public gallery content.", fontSize = 11.sp, color = TextSecondary)
                }

                Button(
                    onClick = { showAddDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Media", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        items(portfolio, key = { it.id }) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp))) {
                        StudioImage(url = item.imageUrl, contentDescription = item.title, modifier = Modifier.fillMaxSize())
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                        Text("${item.category} • ${item.mediaType} ${item.duration?.let { "($it)" } ?: ""}", fontSize = 11.sp, color = TextSecondary)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(if (item.isPublished) "Published" else "Hidden", fontSize = 10.sp, color = if (item.isPublished) SuccessGreen else TextSecondary)
                            if (item.isFeatured) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("• Featured", fontSize = 10.sp, color = GoldLight)
                            }
                        }
                    }

                    // Toggles
                    IconButton(onClick = { viewModel.toggleMediaPublish(item) }) {
                        Icon(
                            imageVector = if (item.isPublished) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle publish",
                            tint = if (item.isPublished) SuccessGreen else TextSecondary
                        )
                    }

                    IconButton(onClick = { viewModel.toggleMediaFeatured(item) }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Toggle featured",
                            tint = if (item.isFeatured) GoldPrimary else TextSecondary
                        )
                    }

                    IconButton(onClick = { viewModel.deletePortfolioMedia(item) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ErrorRed)
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddMediaDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { title, cat, type, url, vidUrl, caption, dur, featured ->
                viewModel.addPortfolioMedia(title, cat, type, url, vidUrl, caption, dur, featured)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun AddMediaDialog(
    onDismiss: () -> Unit,
    onAdd: (title: String, category: String, type: String, url: String, vidUrl: String?, caption: String, duration: String?, isFeatured: Boolean) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Wedding") }
    var type by remember { mutableStateOf("PHOTO") }
    var imageUrl by remember { mutableStateOf("https://images.unsplash.com/photo-1519741497674-611481863552?w=800&auto=format&fit=crop") }
    var videoUrl by remember { mutableStateOf("") }
    var caption by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("03:30") }
    var isFeatured by remember { mutableStateOf(false) }

    // Android Photo & Video Picker (ActivityResultContracts.PickVisualMedia)
    val mediaPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val uriString = uri.toString()
            imageUrl = uriString
            if (type == "VIDEO") {
                videoUrl = uriString
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderGold),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Upload Photo or Video", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
                Text("Add media directly to public client gallery", fontSize = 11.sp, color = TextSecondary)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Title (e.g. Royal Wedding)", color = TextSecondary) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    placeholder = { Text("Category (Wedding, Pre-Wedding, etc.)", color = TextSecondary) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = type == "PHOTO",
                        onClick = { type = "PHOTO" },
                        label = { Text("PHOTO") }
                    )
                    FilterChip(
                        selected = type == "VIDEO",
                        onClick = { type = "VIDEO" },
                        label = { Text("VIDEO") }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Upload button from device storage
                Button(
                    onClick = {
                        val request = if (type == "VIDEO") {
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                        } else {
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        }
                        mediaPickerLauncher.launch(request)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldContainer, contentColor = GoldLight),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary),
                    modifier = Modifier.fillMaxWidth().height(42.dp)
                ) {
                    Icon(
                        imageVector = if (type == "VIDEO") Icons.Default.Videocam else Icons.Default.CloudUpload,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Choose ${if (type == "VIDEO") "Video" else "Photo"} from Device",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Live Preview Thumbnail
                if (imageUrl.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(95.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black)
                            .border(1.dp, DarkBorderGold, RoundedCornerShape(8.dp))
                    ) {
                        StudioImage(
                            url = imageUrl,
                            contentDescription = "Selected media preview",
                            modifier = Modifier.fillMaxSize()
                        )
                        Surface(
                            modifier = Modifier.align(Alignment.TopEnd).padding(4.dp),
                            color = Color.Black.copy(alpha = 0.75f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "Ready to Publish",
                                fontSize = 9.sp,
                                color = SuccessGreen,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    placeholder = { Text("Or paste image/thumbnail URL", color = TextSecondary) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = caption,
                    onValueChange = { caption = it },
                    placeholder = { Text("Caption description", color = TextSecondary) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text("Cancel") }
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onAdd(title, category, type, imageUrl, videoUrl.takeIf { it.isNotBlank() }, caption, duration.takeIf { type == "VIDEO" }, isFeatured)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Add to Gallery", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// TAB 3: Packages Management
@Composable
private fun AdminPackagesTab(
    viewModel: StudioViewModel,
    packages: List<PackagePlan>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Package & Pricing Configurations", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("Enable/disable packages or modify INR pricing in real-time.", fontSize = 11.sp, color = TextSecondary)
        }

        items(packages, key = { it.id }) { pkg ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(pkg.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                            Text("₹${pkg.price.toInt()} INR • ${pkg.coverageHours}h Coverage", fontSize = 12.sp, color = GoldLight)
                        }

                        Button(
                            onClick = { viewModel.togglePackageStatus(pkg) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (pkg.isActive) SuccessGreen.copy(alpha = 0.2f) else DarkSurfaceVariant,
                                contentColor = if (pkg.isActive) SuccessGreen else TextSecondary
                            ),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text(if (pkg.isActive) "Active" else "Disabled", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(pkg.description, fontSize = 11.sp, color = TextSecondary)
                }
            }
        }
    }
}

// TAB 4: Availability Calendar & Blocked Dates
@Composable
private fun AdminCalendarTab(
    viewModel: StudioViewModel,
    blockedDates: List<BlockedDate>,
    bookings: List<Booking>
) {
    var newBlockDate by remember { mutableStateOf("2026-11-15") }
    var newBlockReason by remember { mutableStateOf("Private studio maintenance") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Availability Rules & Date Blockout", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("Dates blocked here will be immediately rejected on the customer booking form.", fontSize = 11.sp, color = TextSecondary)
        }

        // Add blocked date card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderGold)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Block A Date", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = GoldLight)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = newBlockDate,
                            onValueChange = { newBlockDate = it },
                            placeholder = { Text("YYYY-MM-DD", color = TextSecondary) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = newBlockReason,
                            onValueChange = { newBlockReason = it },
                            placeholder = { Text("Reason for block", color = TextSecondary) },
                            singleLine = true,
                            modifier = Modifier.weight(1.5f),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            if (newBlockDate.isNotBlank()) {
                                viewModel.blockDate(newBlockDate.trim(), newBlockReason.trim())
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(40.dp)
                    ) {
                        Icon(Icons.Default.Block, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Block Date in Calendar", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Text("Currently Blocked Dates (${blockedDates.size})", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }

        items(blockedDates, key = { it.id }) { bDate ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(bDate.date, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = ErrorRed)
                        Text(bDate.reason, fontSize = 11.sp, color = TextSecondary)
                    }
                    IconButton(onClick = { viewModel.unblockDate(bDate) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Unblock", tint = TextSecondary)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text("Confirmed Booked Dates", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }

        val confirmedBookings = bookings.filter { it.status == "CONFIRMED" }
        if (confirmedBookings.isEmpty()) {
            item {
                Text("No confirmed events on calendar yet.", fontSize = 12.sp, color = TextSecondary)
            }
        } else {
            items(confirmedBookings, key = { it.id }) { b ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("${b.eventDate} (${b.eventTime})", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SuccessGreen)
                            Text("${b.eventName} for ${b.customerName}", fontSize = 11.sp, color = TextPrimary)
                            Text("Venue: ${b.venueLocation}", fontSize = 10.sp, color = TextSecondary)
                        }
                        StatusBadge(status = b.status)
                    }
                }
            }
        }
    }
}

// TAB 5: Enquiries Inbox
@Composable
private fun AdminInboxTab(
    viewModel: StudioViewModel,
    enquiries: List<Enquiry>
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Customer Messages & Inquiries (${enquiries.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("Inquiries submitted by customers online are stored here securely.", fontSize = 11.sp, color = TextSecondary)
        }

        if (enquiries.isEmpty()) {
            item {
                Text("No enquiries received yet.", color = TextSecondary, fontSize = 13.sp)
            }
        }

        items(enquiries, key = { it.id }) { enquiry ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (!enquiry.isRead) Color(0xFF1F1D16) else DarkSurface
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (!enquiry.isRead) GoldPrimary else DarkBorder
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(enquiry.customerName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                            if (!enquiry.isRead) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(color = GoldPrimary, shape = RoundedCornerShape(4.dp)) {
                                    Text("NEW", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                                }
                            }
                        }
                        Text(enquiry.enquiryType, fontSize = 11.sp, color = GoldLight)
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("${enquiry.customerPhone} • ${enquiry.customerEmail}", fontSize = 11.sp, color = TextSecondary)

                    if (enquiry.bookingReference != null) {
                        Text("Ref: ${enquiry.bookingReference}", fontSize = 11.sp, color = GoldLight, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(enquiry.message, fontSize = 13.sp, color = TextPrimary, lineHeight = 18.sp)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // WhatsApp Reply
                        Button(
                            onClick = {
                                val text = "Hello ${enquiry.customerName}! Replying to your enquiry (${enquiry.enquiryType}) with Smile Studio."
                                val url = "https://wa.me/91${enquiry.customerPhone.filter { it.isDigit() }}?text=${Uri.encode(text)}"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                                viewModel.markEnquiryAsRead(enquiry)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen, contentColor = Color.Black),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.height(32.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reply WhatsApp", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        // Mark Read
                        if (!enquiry.isRead) {
                            OutlinedButton(
                                onClick = { viewModel.markEnquiryAsRead(enquiry) },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.height(32.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("Mark Read", fontSize = 11.sp, color = TextPrimary)
                            }
                        }

                        IconButton(
                            onClick = { viewModel.deleteEnquiry(enquiry) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

// TAB 6: Notifications Delivery Audit
@Composable
private fun AdminNotificationsTab(
    notifications: List<NotificationLog>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Notification Delivery Audit Logs (${notifications.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("Monitors delivery of booking alerts to smileirfan625@gmail.com and customer receipts.", fontSize = 11.sp, color = TextSecondary)
        }

        if (notifications.isEmpty()) {
            item {
                Text("No notifications recorded yet.", color = TextSecondary, fontSize = 13.sp)
            }
        }

        items(notifications, key = { it.id }) { log ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(log.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                        Surface(
                            color = if (log.status == "DELIVERED") SuccessGreen.copy(alpha = 0.2f) else ErrorRed.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = log.status,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (log.status == "DELIVERED") SuccessGreen else ErrorRed,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Recipient: ${log.recipient} • Channel: ${log.channel}", fontSize = 11.sp, color = GoldLight)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(log.message, fontSize = 11.sp, color = TextSecondary)
                }
            }
        }
    }
}
