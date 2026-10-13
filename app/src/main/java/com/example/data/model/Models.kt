package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey val id: String,
    val customerName: String,
    val customerPhone: String,
    val customerEmail: String,
    val eventName: String,
    val eventTypes: String,
    val services: String,
    val packageName: String,
    val packagePrice: Double,
    val addOns: String,
    val estimatedTotal: Double,
    val depositAmount: Double,
    val amountPaid: Double = 0.0,
    val paymentStatus: String = "PENDING", // PENDING, PARTIAL, PAID
    val eventDate: String, // YYYY-MM-DD
    val eventTime: String,
    val durationHours: Int = 4,
    val venueLocation: String,
    val alternativeDate: String = "",
    val specialInstructions: String = "",
    val preferredContactMethod: String = "WhatsApp",
    val status: String = "PENDING", // PENDING, CONFIRMED, RESCHEDULE_REQUESTED, REJECTED, CANCELLED, COMPLETED
    val adminNotes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "enquiries")
data class Enquiry(
    @PrimaryKey val id: String,
    val customerName: String,
    val customerPhone: String,
    val customerEmail: String,
    val enquiryType: String,
    val message: String,
    val bookingReference: String? = null,
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "portfolio_media")
data class PortfolioMedia(
    @PrimaryKey val id: String,
    val title: String,
    val category: String, // Wedding, Pre-Wedding, Portraits, Family, Birthday, Cinematic Video, Reels, Events
    val mediaType: String, // PHOTO, VIDEO
    val imageUrl: String,
    val videoUrl: String? = null,
    val duration: String? = null,
    val caption: String = "",
    val isFeatured: Boolean = false,
    val isPublished: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "packages")
data class PackagePlan(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val coverageHours: Int,
    val teamSize: Int,
    val editedPhotos: String,
    val videoDeliverable: String,
    val deliveryTimelineDays: Int,
    val inclusions: String,
    val isActive: Boolean = true,
    val isPopular: Boolean = false
)

@Entity(tableName = "blocked_dates")
data class BlockedDate(
    @PrimaryKey val id: String,
    val date: String, // YYYY-MM-DD
    val reason: String,
    val isAllDay: Boolean = true
)

@Entity(tableName = "notifications")
data class NotificationLog(
    @PrimaryKey val id: String,
    val type: String, // ADMIN_NEW_BOOKING, CUSTOMER_ACK, ADMIN_ENQUIRY, STATUS_UPDATE
    val recipient: String,
    val title: String,
    val message: String,
    val channel: String, // EMAIL, WHATSAPP, IN_APP
    val status: String, // DELIVERED, FAILED, QUEUED
    val timestamp: Long = System.currentTimeMillis()
)

data class EventCategoryItem(
    val id: String,
    val name: String,
    val description: String,
    val startingPrice: Double,
    val imageUrl: String,
    val popularTag: String? = null
)

data class Testimonial(
    val clientName: String,
    val eventType: String,
    val review: String,
    val rating: Int = 5,
    val dateText: String
)

data class StudioInfo(
    val name: String = "Smile Studio",
    val phone: String = "9113272998",
    val formattedPhone: String = "+91 9113272998",
    val email: String = "smileirfan625@gmail.com",
    val tagline: String = "Every Smile Tells a Story.",
    val mission: String = "Capturing your most beautiful moments with creativity, emotion, and cinematic storytelling.",
    val businessType: String = "Photography, videography, cinematic films, and event coverage",
    val city: String = "Bengaluru & Worldwide Coverage",
    val openingHours: String = "Mon - Sun: 9:00 AM - 9:00 PM (By Appointment)"
)
