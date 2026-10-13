package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Booking
import com.example.data.model.PackagePlan
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadge
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
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.StudioViewModel

@Composable
fun BookingScreen(
    viewModel: StudioViewModel,
    packages: List<PackagePlan>,
    modifier: Modifier = Modifier
) {
    val step by viewModel.bookingStep.collectAsState()
    val confirmedBooking by viewModel.confirmedBooking.collectAsState()
    val isSubmitting by viewModel.isSubmittingBooking.collectAsState()
    val bookingError by viewModel.bookingError.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .testTag("booking_screen_scroll"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        // Step Tracker Indicator (Steps 1 to 7)
        item {
            BookingProgressHeader(currentStep = step)
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Error message banner if any
        if (bookingError != null) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = ErrorRed.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = ErrorRed)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = bookingError ?: "",
                            color = ErrorRed,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Multi-Step Content
        when (step) {
            1 -> item { Step1SelectEvents(viewModel = viewModel) }
            2 -> item { Step2SelectServices(viewModel = viewModel) }
            3 -> item { Step3ChoosePackage(viewModel = viewModel, packages = packages) }
            4 -> item { Step4DateAndTime(viewModel = viewModel) }
            5 -> item { Step5CustomerDetails(viewModel = viewModel) }
            6 -> item {
                Step6BookingSummary(
                    viewModel = viewModel,
                    packages = packages,
                    isSubmitting = isSubmitting
                )
            }
            7 -> item {
                confirmedBooking?.let { booking ->
                    Step7BookingConfirmation(
                        booking = booking,
                        viewModel = viewModel
                    )
                } ?: run {
                    Text("Loading confirmation details...", color = TextSecondary)
                }
            }
        }

        // Navigation Action Buttons (for steps 1 through 6)
        if (step in 1..6) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (step > 1) {
                        OutlinedButton(
                            onClick = { viewModel.prevBookingStep() },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("booking_prev_step_button")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Previous")
                        }
                    }

                    if (step < 6) {
                        Button(
                            onClick = { viewModel.nextBookingStep() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldPrimary,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(if (step == 1) 1f else 1.2f)
                                .height(48.dp)
                                .testTag("booking_next_step_button")
                        ) {
                            Text(
                                text = if (step == 4) "Verify Date & Continue" else "Continue to Step ${step + 1}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    } else if (step == 6) {
                        Button(
                            onClick = { viewModel.submitBookingRequest() },
                            enabled = !isSubmitting,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldPrimary,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1.5f)
                                .height(48.dp)
                                .testTag("submit_booking_request_button")
                        ) {
                            if (isSubmitting) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Sending Request...", fontWeight = FontWeight.Bold)
                            } else {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Submit Booking Request", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}

@Composable
private fun BookingProgressHeader(currentStep: Int) {
    val stepTitles = listOf(
        "Event",
        "Services",
        "Package",
        "Date/Time",
        "Details",
        "Summary",
        "Confirmed"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "STEP $currentStep OF 7",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary,
                letterSpacing = 1.sp
            )
            Text(
                text = stepTitles.getOrElse(currentStep - 1) { "" },
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Segmented Progress Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (i in 1..7) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            when {
                                i <= currentStep -> GoldPrimary
                                else -> DarkSurfaceVariant
                            }
                        )
                )
            }
        }
    }
}

// STEP 1: Select Event
@Composable
private fun Step1SelectEvents(viewModel: StudioViewModel) {
    val selectedEvents by viewModel.selectedEventTypes.collectAsState()
    val availableEvents = listOf(
        "Wedding Photography",
        "Wedding Videography",
        "Pre-Wedding Shoot",
        "Engagement",
        "Reception",
        "Birthday Party",
        "Baby Shower",
        "Maternity Shoot",
        "Newborn Photography",
        "Family Photography",
        "Couple Photography",
        "Portrait Photography",
        "Graduation",
        "Anniversary",
        "Corporate Events",
        "Cultural Functions",
        "Other Custom Events"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
            tagline = "Step 1: Event Selection",
            title = "Which event are you celebrating?",
            description = "Select one or multiple events (e.g. Pre-Wedding + Wedding + Reception for complete coverage)."
        )

        Spacer(modifier = Modifier.height(14.dp))

        availableEvents.chunked(2).forEach { rowEvents ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowEvents.forEach { eventName ->
                    val isChecked = selectedEvents.contains(eventName)
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 4.dp)
                            .clickable { viewModel.toggleEventType(eventName) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isChecked) GoldContainer else DarkSurface
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isChecked) GoldPrimary else DarkBorder
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { viewModel.toggleEventType(eventName) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = GoldPrimary,
                                    checkmarkColor = Color.Black,
                                    uncheckedColor = TextSecondary
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = eventName,
                                fontSize = 12.sp,
                                fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
                                color = if (isChecked) GoldLight else TextPrimary
                            )
                        }
                    }
                }
                if (rowEvents.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

// STEP 2: Select Services
@Composable
private fun Step2SelectServices(viewModel: StudioViewModel) {
    val selectedServices by viewModel.selectedServices.collectAsState()
    val servicesList = listOf(
        Pair("Photography", "Senior candid and traditional photographers with prime optics"),
        Pair("Videography", "High-definition video coverage with wireless audio capture"),
        Pair("Cinematic Film", "4K storytelling film, emotional vows, master color grading"),
        Pair("Drone Coverage", "Licensed aerial perspectives of venue, couple, and fireworks (+₹12,000)"),
        Pair("Luxury Album & Prints", "Handcrafted leather flush-mount album with art paper (+₹15,000)"),
        Pair("Raw Footage", "Full unedited video files & original RAW photos on SSD drive (+₹8,000)"),
        Pair("Live Streaming", "Multi-cam live broadcast for friends & family globally (+₹10,000)")
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
            tagline = "Step 2: Service Inclusions",
            title = "Choose Your Production Scope",
            description = "Customize photography, videography, cinema reels, drone, and luxury physical deliverables."
        )

        Spacer(modifier = Modifier.height(14.dp))

        servicesList.forEach { (service, desc) ->
            val isChecked = selectedServices.contains(service)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clickable { viewModel.toggleService(service) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isChecked) GoldContainer else DarkSurface
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isChecked) GoldPrimary else DarkBorder
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { viewModel.toggleService(service) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = GoldPrimary,
                            checkmarkColor = Color.Black,
                            uncheckedColor = TextSecondary
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = service,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isChecked) GoldLight else TextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = desc,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

// STEP 3: Choose Package
@Composable
private fun Step3ChoosePackage(
    viewModel: StudioViewModel,
    packages: List<PackagePlan>
) {
    val selectedPkgId by viewModel.selectedPackageId.collectAsState()
    val (estimatedTotal, deposit) = viewModel.calculateEstimatedPrice()

    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
            tagline = "Step 3: Studio Package",
            title = "Select Base Package Plan",
            description = "Each package includes guaranteed master retouching, crew, and delivery timelines."
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Price preview banner
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = DarkSurfaceVariant,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderGold)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("ESTIMATED QUOTATION", fontSize = 10.sp, color = GoldLight, fontWeight = FontWeight.Bold)
                    Text("₹${estimatedTotal.toInt()} INR", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("25% Booking Deposit", fontSize = 10.sp, color = TextSecondary)
                    Text("₹${deposit.toInt()}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GoldLight)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        packages.forEach { pkg ->
            val isSelected = selectedPkgId == pkg.id
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clickable { viewModel.selectedPackageId.value = pkg.id },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFF1E1C14) else DarkSurface
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isSelected) GoldPrimary else DarkBorder
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { viewModel.selectedPackageId.value = pkg.id },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = GoldPrimary,
                            unselectedColor = TextSecondary
                        )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = pkg.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = if (isSelected) GoldLight else TextPrimary
                            )
                            Text(
                                text = "₹${pkg.price.toInt()}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                color = GoldLight
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${pkg.coverageHours}h Coverage • ${pkg.teamSize} Crew • ${pkg.editedPhotos}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = pkg.videoDeliverable,
                            fontSize = 11.sp,
                            color = GoldLight
                        )
                    }
                }
            }
        }
    }
}

// STEP 4: Select Event Date and Time (with real-time conflict check)
@Composable
private fun Step4DateAndTime(viewModel: StudioViewModel) {
    val date by viewModel.eventDate.collectAsState()
    val time by viewModel.eventTime.collectAsState()
    val duration by viewModel.durationHours.collectAsState()
    val venue by viewModel.venueLocation.collectAsState()
    val altDate by viewModel.alternativeDate.collectAsState()
    val availabilityResult by viewModel.availabilityCheckResult.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
            tagline = "Step 4: Schedule & Venue",
            title = "Event Date, Time & Availability Check",
            description = "Smile Studio verifies server availability to prevent overlapping reservations."
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Event Date
        Text("Event Date (YYYY-MM-DD)*", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = date,
            onValueChange = {
                viewModel.eventDate.value = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("booking_input_date"),
            placeholder = { Text("e.g. 2026-11-20", color = TextSecondary) },
            trailingIcon = {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = GoldLight)
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GoldPrimary,
                unfocusedBorderColor = DarkBorder,
                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface
            ),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Check availability button
        Button(
            onClick = { viewModel.checkDateAvailabilityNow() },
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkSurfaceVariant,
                contentColor = GoldLight
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("check_availability_button")
        ) {
            Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Verify Date Availability", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        // Availability Result Display
        if (availabilityResult.first != null) {
            val isAvailable = availabilityResult.first == true
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = if (isAvailable) SuccessGreen.copy(alpha = 0.15f) else ErrorRed.copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isAvailable) SuccessGreen else ErrorRed
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isAvailable) Icons.Default.CheckCircle else Icons.Default.ErrorOutline,
                        contentDescription = null,
                        tint = if (isAvailable) SuccessGreen else ErrorRed
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = availabilityResult.second ?: "",
                        fontSize = 12.sp,
                        color = if (isAvailable) SuccessGreen else ErrorRed,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Time & Duration Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Start Time*", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = time,
                    onValueChange = { viewModel.eventTime.value = it },
                    placeholder = { Text("e.g. 09:00 AM", color = TextSecondary) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = DarkBorder,
                        focusedContainerColor = DarkSurface,
                        unfocusedContainerColor = DarkSurface
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("booking_input_time")
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text("Duration (Hours)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = duration.toString(),
                    onValueChange = { viewModel.durationHours.value = it.toIntOrNull() ?: 4 },
                    placeholder = { Text("e.g. 8", color = TextSecondary) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = DarkBorder,
                        focusedContainerColor = DarkSurface,
                        unfocusedContainerColor = DarkSurface
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("booking_input_duration")
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Venue & Location
        Text("Event Venue & City*", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = venue,
            onValueChange = { viewModel.venueLocation.value = it },
            placeholder = { Text("e.g. ITC Gardenia / Palace Grounds, Bengaluru", color = TextSecondary) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("booking_input_venue"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GoldPrimary,
                unfocusedBorderColor = DarkBorder,
                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface
            ),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Alternative Date (Optional)
        Text("Alternative Date (Optional)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = altDate,
            onValueChange = { viewModel.alternativeDate.value = it },
            placeholder = { Text("Backup date if main date conflicts (YYYY-MM-DD)", color = TextSecondary) },
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
    }
}

// STEP 5: Customer Details
@Composable
private fun Step5CustomerDetails(viewModel: StudioViewModel) {
    val name by viewModel.customerName.collectAsState()
    val phone by viewModel.customerPhone.collectAsState()
    val email by viewModel.customerEmail.collectAsState()
    val specialNotes by viewModel.specialInstructions.collectAsState()
    val preferredContact by viewModel.preferredContactMethod.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
            tagline = "Step 5: Contact Information",
            title = "Who are we crafting memories for?",
            description = "We will send the official booking reference and date confirmation to these details."
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text("Full Name*", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { viewModel.customerName.value = it },
            placeholder = { Text("e.g. Irfan Ahmed / Sneha & Rohit", color = TextSecondary) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("booking_input_name"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GoldPrimary,
                unfocusedBorderColor = DarkBorder,
                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface
            ),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Contact Phone Number*", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = phone,
            onValueChange = { viewModel.customerPhone.value = it },
            placeholder = { Text("e.g. 9113272998", color = TextSecondary) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("booking_input_phone"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GoldPrimary,
                unfocusedBorderColor = DarkBorder,
                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface
            ),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Email Address*", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.customerEmail.value = it },
            placeholder = { Text("e.g. yourname@gmail.com", color = TextSecondary) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("booking_input_email"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GoldPrimary,
                unfocusedBorderColor = DarkBorder,
                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface
            ),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Preferred Contact Channel", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf("WhatsApp", "Phone Call", "Email").forEach { channel ->
                val isSelected = preferredContact == channel
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { viewModel.preferredContactMethod.value = channel },
                    color = if (isSelected) GoldContainer else DarkSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) GoldPrimary else DarkBorder)
                ) {
                    Text(
                        text = channel,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) GoldLight else TextPrimary,
                        modifier = Modifier.padding(vertical = 10.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Special Instructions / Shot List Ideas", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = specialNotes,
            onValueChange = { viewModel.specialInstructions.value = it },
            placeholder = { Text("Tell us about your wedding style, specific rituals, outfit colors, or custom concepts...", color = TextSecondary) },
            minLines = 3,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("booking_input_notes"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GoldPrimary,
                unfocusedBorderColor = DarkBorder,
                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface
            ),
            shape = RoundedCornerShape(10.dp)
        )
    }
}

// STEP 6: Booking Summary & Itemized Breakdown
@Composable
private fun Step6BookingSummary(
    viewModel: StudioViewModel,
    packages: List<PackagePlan>,
    isSubmitting: Boolean
) {
    val name by viewModel.customerName.collectAsState()
    val phone by viewModel.customerPhone.collectAsState()
    val email by viewModel.customerEmail.collectAsState()
    val date by viewModel.eventDate.collectAsState()
    val time by viewModel.eventTime.collectAsState()
    val venue by viewModel.venueLocation.collectAsState()
    val selectedEvents by viewModel.selectedEventTypes.collectAsState()
    val selectedServices by viewModel.selectedServices.collectAsState()
    val selectedPkgId by viewModel.selectedPackageId.collectAsState()

    val pkg = packages.find { it.id == selectedPkgId } ?: packages.firstOrNull()
    val (estimatedTotal, deposit) = viewModel.calculateEstimatedPrice()
    val remainingBalance = estimatedTotal - deposit

    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
            tagline = "Step 6: Review & Confirmation",
            title = "Booking Request Summary",
            description = "Please review your event selections and estimated pricing before sending your request."
        )

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderGold)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("CLIENT DETAILS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldLight, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(name.ifBlank { "Client Name" }, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
                Text("$phone • $email", fontSize = 12.sp, color = TextSecondary)

                Spacer(modifier = Modifier.height(14.dp))
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(DarkBorder))
                Spacer(modifier = Modifier.height(14.dp))

                Text("EVENT SCHEDULE & VENUE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldLight, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Event: ${selectedEvents.joinToString(", ")}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                Text("Date & Time: $date at $time", fontSize = 13.sp, color = GoldLight)
                Text("Venue: $venue", fontSize = 12.sp, color = TextSecondary)

                Spacer(modifier = Modifier.height(14.dp))
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(DarkBorder))
                Spacer(modifier = Modifier.height(14.dp))

                Text("SELECTED SERVICES & PACKAGE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldLight, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Package: ${pkg?.name ?: "Custom Package"}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                Text("Services: ${selectedServices.joinToString(" • ")}", fontSize = 12.sp, color = TextSecondary)

                Spacer(modifier = Modifier.height(16.dp))

                // Itemized Price Breakdown Table
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = DarkSurfaceVariant
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Base Package (${pkg?.name ?: ""})", fontSize = 12.sp, color = TextSecondary)
                            Text("₹${(pkg?.price ?: 35000.0).toInt()}", fontSize = 12.sp, color = TextPrimary)
                        }

                        val addOns = selectedServices.filter { it in listOf("Drone Coverage", "Luxury Album & Prints", "Raw Footage", "Live Streaming") }
                        addOns.forEach { addOn ->
                            val cost = when (addOn) {
                                "Drone Coverage" -> 12000
                                "Luxury Album & Prints" -> 15000
                                "Raw Footage" -> 8000
                                "Live Streaming" -> 10000
                                else -> 0
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("+ $addOn", fontSize = 12.sp, color = TextSecondary)
                                Text("₹$cost", fontSize = 12.sp, color = TextPrimary)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(DarkBorder))
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Estimated Total Price", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                            Text("₹${estimatedTotal.toInt()} INR", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = GoldLight)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Required 25% Deposit Upon Approval", fontSize = 12.sp, color = GoldLight)
                            Text("₹${deposit.toInt()}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = GoldLight)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Remaining Balance (Due on Event)", fontSize = 11.sp, color = TextSecondary)
                            Text("₹${remainingBalance.toInt()}", fontSize = 11.sp, color = TextSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Policy notice
                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = GoldLight, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Booking Policy: Submission of this form is a request. A booking is only officially confirmed once approved by Studio Manager and the 25% deposit is recorded. No payment is charged right now.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

// STEP 7: Booking Confirmation
@Composable
private fun Step7BookingConfirmation(
    booking: Booking,
    viewModel: StudioViewModel
) {
    val context = LocalContext.current
    val studioInfo = viewModel.studioInfo

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("booking_confirmation_step"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Glowing Success Badge
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(GoldContainer)
                .border(2.dp, GoldPrimary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = GoldLight,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Booking Request Submitted!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Your request has been received by Smile Studio. Save your reference number below to track status.",
            fontSize = 13.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Reference Code Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldPrimary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "BOOKING REFERENCE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = booking.id, // e.g. SS-2026-4821
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                StatusBadge(status = booking.status)

                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(DarkBorder))
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Client:", fontSize = 12.sp, color = TextSecondary)
                    Text(booking.customerName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Event Date:", fontSize = 12.sp, color = TextSecondary)
                    Text("${booking.eventDate} (${booking.eventTime})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldLight)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Package:", fontSize = 12.sp, color = TextSecondary)
                    Text(booking.packageName, fontSize = 12.sp, color = TextPrimary)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Estimated Amount:", fontSize = 12.sp, color = TextSecondary)
                    Text("₹${booking.estimatedTotal.toInt()} INR", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // WhatsApp Direct Connect Button
        Button(
            onClick = {
                val text = "Hello Smile Studio! I have submitted a booking request with Reference: ${booking.id} for my event on ${booking.eventDate}."
                val encoded = Uri.encode(text)
                val url = "https://wa.me/919113272998?text=$encoded"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = SuccessGreen,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("whatsapp_confirm_button")
        ) {
            Icon(Icons.Default.Chat, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Chat on WhatsApp with Studio (${studioInfo.phone})", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Track Booking Button
        OutlinedButton(
            onClick = {
                viewModel.lookupSearchQuery.value = booking.id
                viewModel.lookupBooking()
                viewModel.navigateTo(AppScreen.LOOKUP)
            },
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldLight),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(Icons.Default.Search, contentDescription = null, tint = GoldLight)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Track Booking Status", color = GoldLight, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                viewModel.resetBookingForm()
                viewModel.navigateTo(AppScreen.HOME)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkSurfaceVariant,
                contentColor = TextPrimary
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(44.dp)
        ) {
            Text("Return to Home")
        }
    }
}
