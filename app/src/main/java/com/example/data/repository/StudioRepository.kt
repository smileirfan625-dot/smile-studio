package com.example.data.repository

import com.example.data.local.SmileStudioDatabase
import com.example.data.model.BlockedDate
import com.example.data.model.Booking
import com.example.data.model.Enquiry
import com.example.data.model.EventCategoryItem
import com.example.data.model.NotificationLog
import com.example.data.model.PackagePlan
import com.example.data.model.PortfolioMedia
import com.example.data.model.StudioInfo
import com.example.data.model.Testimonial
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.random.Random

class StudioRepository(private val db: SmileStudioDatabase) {
    val studioInfo = StudioInfo()

    val allBookings: Flow<List<Booking>> = db.bookingDao().getAllBookings()
    val allEnquiries: Flow<List<Enquiry>> = db.enquiryDao().getAllEnquiries()
    val publishedPortfolio: Flow<List<PortfolioMedia>> = db.portfolioDao().getPublishedPortfolio()
    val allPortfolioAdmin: Flow<List<PortfolioMedia>> = db.portfolioDao().getAllPortfolioAdmin()
    val activePackages: Flow<List<PackagePlan>> = db.packageDao().getActivePackages()
    val allPackagesAdmin: Flow<List<PackagePlan>> = db.packageDao().getAllPackagesAdmin()
    val blockedDates: Flow<List<BlockedDate>> = db.blockedDateDao().getAllBlockedDates()
    val notificationLogs: Flow<List<NotificationLog>> = db.notificationLogDao().getAllNotifications()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedInitialDataIfNeeded()
        }
    }

    private suspend fun seedInitialDataIfNeeded() {
        val existingPackages = db.packageDao().getAllPackagesAdmin().first()
        if (existingPackages.isEmpty()) {
            db.packageDao().insertAllPackages(getDefaultPackages())
        }

        val existingPortfolio = db.portfolioDao().getAllPortfolioAdmin().first()
        if (existingPortfolio.isEmpty()) {
            getDefaultPortfolio().forEach {
                db.portfolioDao().insertPortfolio(it)
            }
        }

        val existingBlocked = db.blockedDateDao().getAllBlockedDates().first()
        if (existingBlocked.isEmpty()) {
            db.blockedDateDao().insertBlockedDate(
                BlockedDate(
                    id = "block_sample_1",
                    date = "2026-10-25",
                    reason = "Studio Diwali Holiday & Equipment Maintenance"
                )
            )
        }
    }

    // Booking actions
    suspend fun checkDateAvailability(date: String): Pair<Boolean, String> {
        val blocked = db.blockedDateDao().getBlockedDate(date)
        if (blocked != null) {
            return Pair(false, "Date is unavailable: ${blocked.reason}")
        }
        val existingBookings = db.bookingDao().getBookingsForDate(date)
        val confirmedCount = existingBookings.count { it.status == "CONFIRMED" }
        if (confirmedCount >= 2) {
            return Pair(false, "Date is fully booked (maximum slots reached). Please pick an alternative date.")
        }
        return Pair(true, "Date is currently available for booking!")
    }

    suspend fun submitBooking(
        customerName: String,
        customerPhone: String,
        customerEmail: String,
        eventName: String,
        eventTypes: String,
        services: String,
        packageName: String,
        packagePrice: Double,
        addOns: String,
        estimatedTotal: Double,
        depositAmount: Double,
        eventDate: String,
        eventTime: String,
        durationHours: Int,
        venueLocation: String,
        alternativeDate: String,
        specialInstructions: String,
        preferredContactMethod: String
    ): Booking {
        val referenceId = "SS-${Random.nextInt(1000, 9999)}"
        val booking = Booking(
            id = referenceId,
            customerName = customerName,
            customerPhone = customerPhone,
            customerEmail = customerEmail,
            eventName = eventName,
            eventTypes = eventTypes,
            services = services,
            packageName = packageName,
            packagePrice = packagePrice,
            addOns = addOns,
            estimatedTotal = estimatedTotal,
            depositAmount = depositAmount,
            amountPaid = 0.0,
            paymentStatus = "PENDING",
            eventDate = eventDate,
            eventTime = eventTime,
            durationHours = durationHours,
            venueLocation = venueLocation,
            alternativeDate = alternativeDate,
            specialInstructions = specialInstructions,
            preferredContactMethod = preferredContactMethod,
            status = "PENDING",
            adminNotes = "New booking request submitted via online portal. Pending studio manager review."
        )

        db.bookingDao().insertBooking(booking)

        // Generate studio admin notification log
        val adminNotification = NotificationLog(
            id = "notif_admin_${System.currentTimeMillis()}",
            type = "ADMIN_NEW_BOOKING",
            recipient = studioInfo.email,
            title = "New Booking Request: $referenceId ($eventName)",
            message = "Customer $customerName ($customerPhone) requested $eventTypes on $eventDate. Package: $packageName (Est: ₹${estimatedTotal.toInt()})",
            channel = "EMAIL",
            status = "DELIVERED"
        )
        db.notificationLogDao().insertNotification(adminNotification)

        // Customer acknowledgement notification log
        val customerNotification = NotificationLog(
            id = "notif_cust_${System.currentTimeMillis() + 1}",
            type = "CUSTOMER_ACK",
            recipient = customerEmail,
            title = "Smile Studio Booking Request Received: $referenceId",
            message = "Dear $customerName, we received your booking request for $eventDate ($eventName). Our studio team will review and contact you shortly.",
            channel = "EMAIL",
            status = "DELIVERED"
        )
        db.notificationLogDao().insertNotification(customerNotification)

        return booking
    }

    suspend fun getBookingById(id: String): Booking? {
        return db.bookingDao().getBookingById(id.trim().uppercase())
    }

    suspend fun updateBookingStatus(
        booking: Booking,
        newStatus: String,
        adminNotes: String,
        amountPaid: Double = booking.amountPaid,
        paymentStatus: String = booking.paymentStatus
    ) {
        val updated = booking.copy(
            status = newStatus,
            adminNotes = adminNotes,
            amountPaid = amountPaid,
            paymentStatus = paymentStatus,
            updatedAt = System.currentTimeMillis()
        )
        db.bookingDao().updateBooking(updated)

        // Notification log for customer status change
        val statusLog = NotificationLog(
            id = "notif_status_${System.currentTimeMillis()}",
            type = "STATUS_UPDATE",
            recipient = booking.customerEmail,
            title = "Smile Studio Booking Update: ${booking.id} is now $newStatus",
            message = "Your booking for ${booking.eventDate} has been updated to $newStatus. Admin note: $adminNotes",
            channel = if (booking.preferredContactMethod == "WhatsApp") "WHATSAPP" else "EMAIL",
            status = "DELIVERED"
        )
        db.notificationLogDao().insertNotification(statusLog)
    }

    suspend fun deleteBooking(booking: Booking) {
        db.bookingDao().deleteBooking(booking)
    }

    // Enquiry actions
    suspend fun submitEnquiry(
        name: String,
        phone: String,
        email: String,
        type: String,
        message: String,
        reference: String?
    ): Enquiry {
        val enquiry = Enquiry(
            id = "enq_${System.currentTimeMillis()}",
            customerName = name,
            customerPhone = phone,
            customerEmail = email,
            enquiryType = type,
            message = message,
            bookingReference = reference?.takeIf { it.isNotBlank() },
            isRead = false
        )
        db.enquiryDao().insertEnquiry(enquiry)

        val log = NotificationLog(
            id = "notif_enq_${System.currentTimeMillis()}",
            type = "ADMIN_ENQUIRY",
            recipient = studioInfo.email,
            title = "New Customer Enquiry: $type from $name",
            message = "Message: $message (Contact: $phone)",
            channel = "EMAIL",
            status = "DELIVERED"
        )
        db.notificationLogDao().insertNotification(log)
        return enquiry
    }

    suspend fun markEnquiryAsRead(enquiry: Enquiry) {
        db.enquiryDao().updateEnquiry(enquiry.copy(isRead = true))
    }

    suspend fun deleteEnquiry(enquiry: Enquiry) {
        db.enquiryDao().deleteEnquiry(enquiry)
    }

    // Portfolio actions
    suspend fun savePortfolioMedia(media: PortfolioMedia) {
        db.portfolioDao().insertPortfolio(media)
    }

    suspend fun updatePortfolioMedia(media: PortfolioMedia) {
        db.portfolioDao().updatePortfolio(media)
    }

    suspend fun deletePortfolioMedia(media: PortfolioMedia) {
        db.portfolioDao().deletePortfolio(media)
    }

    // Package actions
    suspend fun savePackage(pkg: PackagePlan) {
        db.packageDao().insertPackage(pkg)
    }

    suspend fun updatePackage(pkg: PackagePlan) {
        db.packageDao().updatePackage(pkg)
    }

    suspend fun deletePackage(pkg: PackagePlan) {
        db.packageDao().deletePackage(pkg)
    }

    // Blocked dates actions
    suspend fun addBlockedDate(date: String, reason: String) {
        val entity = BlockedDate(
            id = "block_${System.currentTimeMillis()}",
            date = date,
            reason = reason
        )
        db.blockedDateDao().insertBlockedDate(entity)
    }

    suspend fun removeBlockedDate(blockedDate: BlockedDate) {
        db.blockedDateDao().deleteBlockedDate(blockedDate)
    }

    // Static Category metadata
    fun getEventCategories(): List<EventCategoryItem> = listOf(
        EventCategoryItem(
            id = "wedding_photo",
            name = "Wedding Photography",
            description = "Emotional candids, rituals, bridal portraits, and timeless ceremony captures with dual-angle coverage.",
            startingPrice = 45000.0,
            imageUrl = "https://images.unsplash.com/photo-1519741497674-611481863552?w=800&auto=format&fit=crop",
            popularTag = "Most Popular"
        ),
        EventCategoryItem(
            id = "wedding_video",
            name = "Wedding Videography",
            description = "4K cinematic wedding films, drone aerials, heartfelt audio vows, and Instagram teaser reels.",
            startingPrice = 55000.0,
            imageUrl = "https://images.unsplash.com/photo-1511285560929-80b456fea0bc?w=800&auto=format&fit=crop",
            popularTag = "Trending"
        ),
        EventCategoryItem(
            id = "pre_wedding",
            name = "Pre-Wedding Shoot",
            description = "Dreamy outdoor destinations, romantic storytelling, bespoke concepts, and song teaser reels.",
            startingPrice = 30000.0,
            imageUrl = "https://images.unsplash.com/photo-1583939003579-730e3918a45a?w=800&auto=format&fit=crop",
            popularTag = "Signature"
        ),
        EventCategoryItem(
            id = "engagement",
            name = "Engagement",
            description = "Ring exchange, intimate family blessings, couple portraits, and joyful reception candid photos.",
            startingPrice = 25000.0,
            imageUrl = "https://images.unsplash.com/photo-1515934751635-c81c6bc9a2d8?w=800&auto=format&fit=crop"
        ),
        EventCategoryItem(
            id = "reception",
            name = "Reception",
            description = "Grand entrance, dynamic stage lighting, dance performances, and vibrant guest portraits.",
            startingPrice = 35000.0,
            imageUrl = "https://images.unsplash.com/photo-1469371670807-013ccf25f16a?w=800&auto=format&fit=crop"
        ),
        EventCategoryItem(
            id = "birthday",
            name = "Birthday Party",
            description = "Thematic cake-cutting, child laughs, family games, balloon decor, and cheerful event candids.",
            startingPrice = 18000.0,
            imageUrl = "https://images.unsplash.com/photo-1530103862676-de8c9debad1d?w=800&auto=format&fit=crop"
        ),
        EventCategoryItem(
            id = "baby_shower",
            name = "Baby Shower",
            description = "Warm maternal blessings, traditional rituals, decor details, and family happiness.",
            startingPrice = 20000.0,
            imageUrl = "https://images.unsplash.com/photo-1516627145497-ae6968895b74?w=800&auto=format&fit=crop"
        ),
        EventCategoryItem(
            id = "maternity",
            name = "Maternity Shoot",
            description = "Graceful indoor studio or golden-hour outdoor portraits celebrating new life.",
            startingPrice = 22000.0,
            imageUrl = "https://images.unsplash.com/photo-1520854221256-17451cc331bf?w=800&auto=format&fit=crop"
        ),
        EventCategoryItem(
            id = "newborn",
            name = "Newborn Photography",
            description = "Gentle, temperature-regulated cozy setups with safety-first posing props and swaddles.",
            startingPrice = 20000.0,
            imageUrl = "https://images.unsplash.com/photo-1555252333-9f8e92e65df9?w=800&auto=format&fit=crop"
        ),
        EventCategoryItem(
            id = "family",
            name = "Family Photography",
            description = "Multi-generational portraits, candid interactions, and heirloom legacy memories.",
            startingPrice = 18000.0,
            imageUrl = "https://images.unsplash.com/photo-1609220136736-443140cffec6?w=800&auto=format&fit=crop"
        ),
        EventCategoryItem(
            id = "couple",
            name = "Couple Photography",
            description = "Intimate natural expressions, anniversary celebrations, and candid casual outdoor moments.",
            startingPrice = 20000.0,
            imageUrl = "https://images.unsplash.com/photo-1522673607200-164d1b6ce486?w=800&auto=format&fit=crop"
        ),
        EventCategoryItem(
            id = "portrait",
            name = "Portrait Photography",
            description = "High-end editorial, fashion portfolio, model casting, and artistic headshots.",
            startingPrice = 15000.0,
            imageUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&auto=format&fit=crop"
        ),
        EventCategoryItem(
            id = "graduation",
            name = "Graduation",
            description = "Convocation day, cap toss celebration, degree moments, and proud family group photos.",
            startingPrice = 15000.0,
            imageUrl = "https://images.unsplash.com/photo-1523050854058-8df90110c9f1?w=800&auto=format&fit=crop"
        ),
        EventCategoryItem(
            id = "anniversary",
            name = "Anniversary",
            description = "Silver, golden or first milestone anniversaries captured with grace and nostalgic joy.",
            startingPrice = 22000.0,
            imageUrl = "https://images.unsplash.com/photo-1511795409834-ef04bbd61622?w=800&auto=format&fit=crop"
        ),
        EventCategoryItem(
            id = "corporate",
            name = "Corporate Events",
            description = "Keynotes, annual conferences, leadership headshots, product launches, and brand galas.",
            startingPrice = 30000.0,
            imageUrl = "https://images.unsplash.com/photo-1511578314322-379afb476865?w=800&auto=format&fit=crop"
        ),
        EventCategoryItem(
            id = "cultural",
            name = "Cultural Functions",
            description = "Traditional dances, festive community gatherings, temple functions, and classical arts.",
            startingPrice = 25000.0,
            imageUrl = "https://images.unsplash.com/photo-1567157577867-05ccb1388e66?w=800&auto=format&fit=crop"
        ),
        EventCategoryItem(
            id = "custom_event",
            name = "Other Custom Events",
            description = "Housewarming, private dinners, product showcases, or custom multi-day festivals.",
            startingPrice = 20000.0,
            imageUrl = "https://images.unsplash.com/photo-1465847899084-d164df4dedc6?w=800&auto=format&fit=crop"
        )
    )

    fun getTestimonials(): List<Testimonial> = listOf(
        Testimonial(
            clientName = "Rohit & Priya Sharma",
            eventType = "Grand Royal Wedding",
            review = "Smile Studio made our wedding look like a royal fairy tale film. The team was unobtrusive yet captured every tender emotion and laugh. The album quality is breathtaking!",
            rating = 5,
            dateText = "September 2026"
        ),
        Testimonial(
            clientName = "Ananya Hegde",
            eventType = "Pre-Wedding & Engagement",
            review = "From location styling to drone aerials, Irfan and the Smile Studio crew went beyond our wildest expectations. Their creative angles and warm approach made us feel so comfortable.",
            rating = 5,
            dateText = "August 2026"
        ),
        Testimonial(
            clientName = "Vikram Menon",
            eventType = "Corporate Brand Gala",
            review = "Top tier professionalism! Delivered 600 edited executive photographs and an engaging 4K teaser reel within 4 days. Absolutely reliable studio partners.",
            rating = 5,
            dateText = "July 2026"
        )
    )

    fun getFaqs(): List<Pair<String, String>> = listOf(
        Pair(
            "How do I confirm my booking?",
            "After submitting your booking request, our studio reviews date availability and sends you confirmation details. A 25% deposit secures your date on our calendar."
        ),
        Pair(
            "When will we receive our photos and videos?",
            "Teaser highlights are delivered within 48-72 hours. Complete retouched galleries and cinematic films are typically delivered in 15-25 days depending on the selected package."
        ),
        Pair(
            "Do you travel for destination shoots and weddings?",
            "Yes! Smile Studio covers events across India and internationally. Travel and accommodation are arranged or billed transparently based on destination."
        ),
        Pair(
            "Can we customize packages and add drone or extra hours?",
            "Absolutely! Every package can be tailored with optional add-ons like drone footage, luxury parent albums, live streaming, and extra coverage hours."
        )
    )

    private fun getDefaultPackages(): List<PackagePlan> = listOf(
        PackagePlan(
            id = "pkg_basic_photo",
            name = "Basic Photography",
            description = "Ideal for intimate celebrations, birthdays, graduations, and small family ceremonies.",
            price = 25000.0,
            coverageHours = 4,
            teamSize = 1,
            editedPhotos = "150+ Retouched High-Res Images",
            videoDeliverable = "None (Photo Only)",
            deliveryTimelineDays = 12,
            inclusions = "1 Senior Candid Photographer, Color-graded high-res gallery, Online private download link, Print release",
            isActive = true,
            isPopular = false
        ),
        PackagePlan(
            id = "pkg_premium_photo",
            name = "Premium Photography",
            description = "Comprehensive traditional and candid photography for engagements, receptions, and family milestones.",
            price = 45000.0,
            coverageHours = 8,
            teamSize = 2,
            editedPhotos = "350+ Retouched Images",
            videoDeliverable = "60s Highlight Reel",
            deliveryTimelineDays = 15,
            inclusions = "2 Senior Photographers (1 Candid + 1 Traditional), 1 Hardbound Luxury Album (30 pages), Online Cloud Gallery, High-res print rights",
            isActive = true,
            isPopular = false
        ),
        PackagePlan(
            id = "pkg_photo_video",
            name = "Photography + Videography",
            description = "Our most requested dual-coverage package combining breathtaking candid stills and cinematic video.",
            price = 75000.0,
            coverageHours = 8,
            teamSize = 4,
            editedPhotos = "500+ Retouched Photos",
            videoDeliverable = "4K Cinematic Film (4-6 mins) + 60s Instagram Reel",
            deliveryTimelineDays = 20,
            inclusions = "2 Photographers + 2 Videographers, Drone aerial coverage (where permitted), 1 Premium Leather Album (40 pages), Raw footage backup",
            isActive = true,
            isPopular = true
        ),
        PackagePlan(
            id = "pkg_wedding_royal",
            name = "Wedding Cinematic Royal",
            description = "The ultimate luxury cinematic experience for grand Indian weddings, sangeet, and reception.",
            price = 120000.0,
            coverageHours = 12,
            teamSize = 6,
            editedPhotos = "800+ Mastered Masterpieces",
            videoDeliverable = "4K Cinematic Feature Film (15-20 mins) + 2 Reels + Teaser",
            deliveryTimelineDays = 25,
            inclusions = "3 Photographers + 3 Cinematographers + Drone Operator, 2 Luxury Acrylic Cover Albums + 2 Parent Albums, Wooden Keepsake USB Box",
            isActive = true,
            isPopular = true
        ),
        PackagePlan(
            id = "pkg_pre_wedding",
            name = "Pre-Wedding Cinematic Story",
            description = "Romantic cinematic storytelling in breathtaking locations with creative concept styling.",
            price = 35000.0,
            coverageHours = 6,
            teamSize = 2,
            editedPhotos = "80+ Glamour Retouched Stills",
            videoDeliverable = "2-3 mins 4K Romantic Song Teaser + 1 Vertical Reel",
            deliveryTimelineDays = 10,
            inclusions = "1 Candid Photographer + 1 Cinematographer, Drone cinematic perspectives, Location styling guidance, Online cloud link",
            isActive = true,
            isPopular = false
        ),
        PackagePlan(
            id = "pkg_custom_event",
            name = "Custom Event Package",
            description = "Tailored coverage designed specifically around your venue, schedule, and unique vision.",
            price = 20000.0,
            coverageHours = 4,
            teamSize = 2,
            editedPhotos = "120+ Curated Stills",
            videoDeliverable = "Customizable (Select in Add-ons)",
            deliveryTimelineDays = 10,
            inclusions = "Tailored photography team, Flexible schedule adjustments, Digital proofing gallery, Direct studio manager coordination",
            isActive = true,
            isPopular = false
        )
    )

    private fun getDefaultPortfolio(): List<PortfolioMedia> = listOf(
        PortfolioMedia(
            id = "port_1",
            title = "Royal Bengaluru Palace Wedding",
            category = "Wedding",
            mediaType = "PHOTO",
            imageUrl = "https://images.unsplash.com/photo-1519741497674-611481863552?w=900&auto=format&fit=crop",
            caption = "Timeless royal varmala exchange under floral chandeliers.",
            isFeatured = true,
            isPublished = true
        ),
        PortfolioMedia(
            id = "port_2",
            title = "Golden Hour Pre-Wedding Film",
            category = "Cinematic Video",
            mediaType = "VIDEO",
            imageUrl = "https://images.unsplash.com/photo-1583939003579-730e3918a45a?w=900&auto=format&fit=crop",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            duration = "03:45",
            caption = "Romantic 4K hills destination couple shoot.",
            isFeatured = true,
            isPublished = true
        ),
        PortfolioMedia(
            id = "port_3",
            title = "Bridal Elegance & Mehndi Jewels",
            category = "Portraits",
            mediaType = "PHOTO",
            imageUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=900&auto=format&fit=crop",
            caption = "Intricate jewelry and candid emotional bridal gaze.",
            isFeatured = true,
            isPublished = true
        ),
        PortfolioMedia(
            id = "port_4",
            title = "Sangeet Dance Celebration",
            category = "Events",
            mediaType = "PHOTO",
            imageUrl = "https://images.unsplash.com/photo-1469371670807-013ccf25f16a?w=900&auto=format&fit=crop",
            caption = "High-energy stage performances and confetti splashes.",
            isFeatured = false,
            isPublished = true
        ),
        PortfolioMedia(
            id = "port_5",
            title = "Teaser Reel: Love in Nandi Hills",
            category = "Reels",
            mediaType = "VIDEO",
            imageUrl = "https://images.unsplash.com/photo-1522673607200-164d1b6ce486?w=900&auto=format&fit=crop",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            duration = "00:59",
            caption = "Viral cinematic reel capturing pure sunset warmth.",
            isFeatured = true,
            isPublished = true
        ),
        PortfolioMedia(
            id = "port_6",
            title = "Little Joy: 1st Birthday Wonder",
            category = "Birthday",
            mediaType = "PHOTO",
            imageUrl = "https://images.unsplash.com/photo-1530103862676-de8c9debad1d?w=900&auto=format&fit=crop",
            caption = "Pure innocent smiles and colorful balloon paradise.",
            isFeatured = false,
            isPublished = true
        ),
        PortfolioMedia(
            id = "port_7",
            title = "Three Generations of Love",
            category = "Family",
            mediaType = "PHOTO",
            imageUrl = "https://images.unsplash.com/photo-1609220136736-443140cffec6?w=900&auto=format&fit=crop",
            caption = "Cherished grandfather, parents, and grandchild moments.",
            isFeatured = false,
            isPublished = true
        ),
        PortfolioMedia(
            id = "port_8",
            title = "Lakeside Engagement Vows",
            category = "Pre-Wedding",
            mediaType = "PHOTO",
            imageUrl = "https://images.unsplash.com/photo-1515934751635-c81c6bc9a2d8?w=900&auto=format&fit=crop",
            caption = "Intimate ring promise amidst misty water reflections.",
            isFeatured = true,
            isPublished = true
        )
    )
}
