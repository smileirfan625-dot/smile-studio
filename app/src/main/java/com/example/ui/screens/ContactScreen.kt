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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SectionHeader
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkBorderGold
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.StudioViewModel

@Composable
fun ContactScreen(
    viewModel: StudioViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val studioInfo = viewModel.studioInfo

    val name by viewModel.enquiryName.collectAsState()
    val phone by viewModel.enquiryPhone.collectAsState()
    val email by viewModel.enquiryEmail.collectAsState()
    val type by viewModel.enquiryType.collectAsState()
    val message by viewModel.enquiryMessage.collectAsState()
    val ref by viewModel.enquiryBookingRef.collectAsState()
    val isSubmitted by viewModel.enquirySubmitted.collectAsState()
    val errorMsg by viewModel.enquiryError.collectAsState()

    val enquiryTypes = listOf(
        "General Enquiry",
        "Package questions",
        "Price quotation requests",
        "Booking-related questions",
        "Reschedule requests",
        "Cancellation requests",
        "Custom photography requirements"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .testTag("contact_screen_scroll"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        item {
            SectionHeader(
                tagline = "Get in Touch",
                title = "Contact Smile Studio",
                description = "Have questions about pricing, dates, or customized cinematography? Reach out directly via WhatsApp, Phone, or our enquiry desk."
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Direct Contact Cards Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // WhatsApp
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            val url = "https://wa.me/919113272998?text=Hello%20Smile%20Studio!%20I%20would%20like%20to%20get%20in%20touch."
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                        .testTag("contact_whatsapp_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(SuccessGreen.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, tint = SuccessGreen)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("WhatsApp", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                        Text("+91 9113272998", fontSize = 11.sp, color = SuccessGreen)
                    }
                }

                // Phone Call
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:9113272998"))
                            context.startActivity(intent)
                        }
                        .testTag("contact_phone_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(GoldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, tint = GoldLight)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Direct Call", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                        Text("9113272998", fontSize = 11.sp, color = GoldLight)
                    }
                }

                // Email
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:smileirfan625@gmail.com"))
                            context.startActivity(intent)
                        }
                        .testTag("contact_email_card"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(DarkSurfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Email, contentDescription = null, tint = TextPrimary)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Studio Email", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                        Text("Email Studio", fontSize = 11.sp, color = TextSecondary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Studio Location & Hours Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = GoldLight, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(studioInfo.city, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccessTime, contentDescription = null, tint = GoldLight, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(studioInfo.openingHours, fontSize = 12.sp, color = TextSecondary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Contact Enquiry Form
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderGold)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "SEND US A MESSAGE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Customer Enquiry Desk",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (isSubmitted) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = SuccessGreen.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SuccessGreen)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Thank You! Enquiry Received",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = SuccessGreen
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Your message has been stored in our system and delivered to the studio owner (${studioInfo.email}). We will get back to you shortly.",
                                    fontSize = 12.sp,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = { viewModel.resetEnquiryForm() },
                                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant)
                                ) {
                                    Text("Send Another Message", fontSize = 12.sp)
                                }
                            }
                        }
                    } else {
                        if (errorMsg != null) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp),
                                shape = RoundedCornerShape(8.dp),
                                color = ErrorRed.copy(alpha = 0.15f)
                            ) {
                                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = ErrorRed)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(errorMsg ?: "", color = ErrorRed, fontSize = 12.sp)
                                }
                            }
                        }

                        // Form Inputs
                        Text("Your Name*", fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = name,
                            onValueChange = { viewModel.enquiryName.value = it },
                            placeholder = { Text("e.g. Irfan / Priya", color = TextSecondary) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("enquiry_input_name"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = DarkBorder,
                                focusedContainerColor = DarkSurfaceVariant,
                                unfocusedContainerColor = DarkSurfaceVariant
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Phone Number*", fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = phone,
                                    onValueChange = { viewModel.enquiryPhone.value = it },
                                    placeholder = { Text("e.g. 9113272998", color = TextSecondary) },
                                    singleLine = true,
                                    modifier = Modifier.testTag("enquiry_input_phone"),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = GoldPrimary,
                                        unfocusedBorderColor = DarkBorder,
                                        focusedContainerColor = DarkSurfaceVariant,
                                        unfocusedContainerColor = DarkSurfaceVariant
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Email Address*", fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = email,
                                    onValueChange = { viewModel.enquiryEmail.value = it },
                                    placeholder = { Text("e.g. name@mail.com", color = TextSecondary) },
                                    singleLine = true,
                                    modifier = Modifier.testTag("enquiry_input_email"),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = GoldPrimary,
                                        unfocusedBorderColor = DarkBorder,
                                        focusedContainerColor = DarkSurfaceVariant,
                                        unfocusedContainerColor = DarkSurfaceVariant
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Enquiry Subject / Category", fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        // Category selection chips
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            enquiryTypes.chunked(2).forEach { rowTypes ->
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    rowTypes.forEach { itemType ->
                                        val isSel = type == itemType
                                        Surface(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable { viewModel.enquiryType.value = itemType },
                                            color = if (isSel) GoldContainer else DarkSurfaceVariant,
                                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) GoldPrimary else DarkBorder)
                                        ) {
                                            Text(
                                                text = itemType,
                                                fontSize = 11.sp,
                                                color = if (isSel) GoldLight else TextSecondary,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                            )
                                        }
                                    }
                                    if (rowTypes.size == 1) Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Associated Booking Reference (Optional)", fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = ref,
                            onValueChange = { viewModel.enquiryBookingRef.value = it },
                            placeholder = { Text("e.g. SS-2026-4821 if applicable", color = TextSecondary) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("enquiry_input_ref"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = DarkBorder,
                                focusedContainerColor = DarkSurfaceVariant,
                                unfocusedContainerColor = DarkSurfaceVariant
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Message / Requirements*", fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = message,
                            onValueChange = { viewModel.enquiryMessage.value = it },
                            placeholder = { Text("How can Smile Studio help you today?", color = TextSecondary) },
                            minLines = 4,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("enquiry_input_message"),
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
                            onClick = { viewModel.submitCustomerEnquiry() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldPrimary,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("enquiry_submit_button")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Send Message to Studio", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // FAQs Section
        item {
            SectionHeader(
                tagline = "Help & Clarity",
                title = "Frequently Asked Questions",
                description = "Common answers regarding our booking procedure, deliverables, and policies."
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(viewModel.faqs) { (question, answer) ->
            FaqAccordionItem(question = question, answer = answer)
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun FaqAccordionItem(question: String, answer: String) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
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
                Text(
                    text = question,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = GoldLight
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(DarkBorder))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = answer,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
