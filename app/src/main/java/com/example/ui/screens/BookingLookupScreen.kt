package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Booking
import com.example.ui.components.PaymentBadge
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatusBadge
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkBorderGold
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.StudioViewModel

@Composable
fun BookingLookupScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val studioInfo = viewModel.studioInfo

    val searchQuery by viewModel.lookupSearchQuery.collectAsState()
    val lookupResult by viewModel.lookupResult.collectAsState()
    val lookupError by viewModel.lookupError.collectAsState()
    val isSearching by viewModel.isLookingUp.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .testTag("lookup_screen_scroll"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        item {
            SectionHeader(
                tagline = "Order Tracking",
                title = "Check Booking Status",
                description = "Enter the unique booking reference provided when you submitted your event request."
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Search input field & button
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Booking Reference ID",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.lookupSearchQuery.value = it },
                        placeholder = { Text("e.g. SS-2026-4821", color = TextSecondary) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = GoldLight)
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("lookup_search_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = DarkBorder,
                            focusedContainerColor = DarkSurfaceVariant,
                            unfocusedContainerColor = DarkSurfaceVariant
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.lookupBooking() },
                        enabled = !isSearching,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("lookup_search_button")
                    ) {
                        if (isSearching) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Checking Database...", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Lookup Booking", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Error message if not found
        if (lookupError != null) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
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
                            text = lookupError ?: "",
                            color = ErrorRed,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }
        }

        // Result Card
        lookupResult?.let { booking ->
            item {
                BookingResultCard(
                    booking = booking,
                    onChatWhatsApp = {
                        val text = "Hello Smile Studio! I am inquiring about my booking with reference: ${booking.id}."
                        val encoded = Uri.encode(text)
                        val url = "https://wa.me/919113272998?text=$encoded"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    },
                    onCallStudio = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:9113272998"))
                        context.startActivity(intent)
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun BookingResultCard(
    booking: Booking,
    onChatWhatsApp: () -> Unit,
    onCallStudio: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("lookup_result_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldPrimary)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "REFERENCE: ${booking.id}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GoldLight,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = booking.eventName,
                        fontSize = 13.sp,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                StatusBadge(status = booking.status)
            }

            Spacer(modifier = Modifier.height(14.dp))
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(DarkBorder))
            Spacer(modifier = Modifier.height(14.dp))

            // Details
            DetailRow("Client Name", booking.customerName)
            DetailRow("Event Date", "${booking.eventDate} (${booking.eventTime})")
            DetailRow("Duration", "${booking.durationHours} Hours Coverage")
            DetailRow("Venue Location", booking.venueLocation)
            DetailRow("Package", booking.packageName)
            DetailRow("Services", booking.services)

            Spacer(modifier = Modifier.height(12.dp))

            // Financial & Deposit Status Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = DarkSurfaceVariant
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Payment Status", fontSize = 12.sp, color = TextSecondary)
                        PaymentBadge(paymentStatus = booking.paymentStatus, amountPaid = booking.amountPaid)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Estimated Total:", fontSize = 12.sp, color = TextSecondary)
                        Text("₹${booking.estimatedTotal.toInt()} INR", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Recorded Paid Amount:", fontSize = 12.sp, color = TextSecondary)
                        Text("₹${booking.amountPaid.toInt()} INR", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GoldLight)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Remaining Balance:", fontSize = 12.sp, color = TextSecondary)
                        Text("₹${(booking.estimatedTotal - booking.amountPaid).toInt()} INR", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                    }
                }
            }

            // Internal Studio Notes
            if (booking.adminNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF1E1A14),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, DarkBorderGold)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("STUDIO MANAGER NOTE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldLight)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(booking.adminNotes, fontSize = 12.sp, color = TextPrimary, lineHeight = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contact Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onChatWhatsApp,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SuccessGreen,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f).height(44.dp)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("WhatsApp Studio", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onCallStudio,
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldLight),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(44.dp)
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, tint = GoldLight, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Call Studio", color = GoldLight, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, color = TextSecondary)
        Spacer(modifier = Modifier.width(10.dp))
        Text(value, fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
    }
}
