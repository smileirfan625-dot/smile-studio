package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
import com.example.data.repository.StudioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    SERVICES,
    PORTFOLIO,
    PACKAGES,
    BOOKING,
    LOOKUP,
    CONTACT,
    ADMIN
}

class StudioViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = StudioRepository(SmileStudioDatabase.getDatabase(application))
    val studioInfo: StudioInfo = repository.studioInfo

    // Navigation state
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _screenHistory = mutableListOf<AppScreen>()

    // Data streams from Room
    val allBookings: StateFlow<List<Booking>> = repository.allBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allEnquiries: StateFlow<List<Enquiry>> = repository.allEnquiries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val publishedPortfolio: StateFlow<List<PortfolioMedia>> = repository.publishedPortfolio
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPortfolioAdmin: StateFlow<List<PortfolioMedia>> = repository.allPortfolioAdmin
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activePackages: StateFlow<List<PackagePlan>> = repository.activePackages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPackagesAdmin: StateFlow<List<PackagePlan>> = repository.allPackagesAdmin
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val blockedDates: StateFlow<List<BlockedDate>> = repository.blockedDates
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notificationLogs: StateFlow<List<NotificationLog>> = repository.notificationLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val eventCategories: List<EventCategoryItem> = repository.getEventCategories()
    val testimonials: List<Testimonial> = repository.getTestimonials()
    val faqs: List<Pair<String, String>> = repository.getFaqs()

    // Portfolio Category filter
    private val _selectedPortfolioCategory = MutableStateFlow("All")
    val selectedPortfolioCategory: StateFlow<String> = _selectedPortfolioCategory.asStateFlow()

    private val _activePreviewMedia = MutableStateFlow<PortfolioMedia?>(null)
    val activePreviewMedia: StateFlow<PortfolioMedia?> = _activePreviewMedia.asStateFlow()

    // Booking Wizard State
    private val _bookingStep = MutableStateFlow(1)
    val bookingStep: StateFlow<Int> = _bookingStep.asStateFlow()

    val selectedEventTypes = MutableStateFlow<Set<String>>(setOf("Wedding Photography"))
    val selectedServices = MutableStateFlow<Set<String>>(setOf("Photography", "Videography", "Cinematic Film"))
    val selectedPackageId = MutableStateFlow("pkg_photo_video")
    val eventDate = MutableStateFlow("2026-11-20")
    val eventTime = MutableStateFlow("09:00 AM")
    val durationHours = MutableStateFlow(8)
    val venueLocation = MutableStateFlow("Palace Grounds, Bengaluru")
    val alternativeDate = MutableStateFlow("")
    val customerName = MutableStateFlow("")
    val customerPhone = MutableStateFlow("")
    val customerEmail = MutableStateFlow("")
    val specialInstructions = MutableStateFlow("")
    val preferredContactMethod = MutableStateFlow("WhatsApp")

    private val _availabilityCheckResult = MutableStateFlow<Pair<Boolean?, String?>>(Pair(null, null))
    val availabilityCheckResult: StateFlow<Pair<Boolean?, String?>> = _availabilityCheckResult.asStateFlow()

    private val _isSubmittingBooking = MutableStateFlow(false)
    val isSubmittingBooking: StateFlow<Boolean> = _isSubmittingBooking.asStateFlow()

    private val _confirmedBooking = MutableStateFlow<Booking?>(null)
    val confirmedBooking: StateFlow<Booking?> = _confirmedBooking.asStateFlow()

    private val _bookingError = MutableStateFlow<String?>(null)
    val bookingError: StateFlow<String?> = _bookingError.asStateFlow()

    // Customer Booking Lookup State
    val lookupSearchQuery = MutableStateFlow("")
    private val _lookupResult = MutableStateFlow<Booking?>(null)
    val lookupResult: StateFlow<Booking?> = _lookupResult.asStateFlow()
    private val _lookupError = MutableStateFlow<String?>(null)
    val lookupError: StateFlow<String?> = _lookupError.asStateFlow()
    private val _isLookingUp = MutableStateFlow(false)
    val isLookingUp: StateFlow<Boolean> = _isLookingUp.asStateFlow()

    // Contact Enquiry State
    val enquiryName = MutableStateFlow("")
    val enquiryPhone = MutableStateFlow("")
    val enquiryEmail = MutableStateFlow("")
    val enquiryType = MutableStateFlow("General Enquiry")
    val enquiryMessage = MutableStateFlow("")
    val enquiryBookingRef = MutableStateFlow("")
    private val _enquirySubmitted = MutableStateFlow(false)
    val enquirySubmitted: StateFlow<Boolean> = _enquirySubmitted.asStateFlow()
    private val _enquiryError = MutableStateFlow<String?>(null)
    val enquiryError: StateFlow<String?> = _enquiryError.asStateFlow()

    // Admin State
    private val _isAdminAuthenticated = MutableStateFlow(false)
    val isAdminAuthenticated: StateFlow<Boolean> = _isAdminAuthenticated.asStateFlow()

    val adminPinInput = MutableStateFlow("")
    private val _adminPinError = MutableStateFlow<String?>(null)
    val adminPinError: StateFlow<String?> = _adminPinError.asStateFlow()

    val activeAdminTab = MutableStateFlow(0)
    val bookingFilterStatus = MutableStateFlow("ALL")
    val bookingSearchText = MutableStateFlow("")

    private val _selectedBookingDetail = MutableStateFlow<Booking?>(null)
    val selectedBookingDetail: StateFlow<Booking?> = _selectedBookingDetail.asStateFlow()

    // Navigation functions
    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            _screenHistory.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun handleBack(): Boolean {
        if (_screenHistory.isNotEmpty()) {
            val previous = _screenHistory.removeAt(_screenHistory.size - 1)
            _currentScreen.value = previous
            return true
        }
        if (_currentScreen.value != AppScreen.HOME) {
            _currentScreen.value = AppScreen.HOME
            return true
        }
        return false
    }

    fun selectPortfolioCategory(cat: String) {
        _selectedPortfolioCategory.value = cat
    }

    fun setPreviewMedia(media: PortfolioMedia?) {
        _activePreviewMedia.value = media
    }

    // Booking Wizard actions
    fun startBookingForEvent(eventName: String) {
        selectedEventTypes.value = setOf(eventName)
        _bookingStep.value = 1
        _confirmedBooking.value = null
        navigateTo(AppScreen.BOOKING)
    }

    fun selectPackageFromList(pkgId: String) {
        selectedPackageId.value = pkgId
        _bookingStep.value = 3
        _confirmedBooking.value = null
        navigateTo(AppScreen.BOOKING)
    }

    fun nextBookingStep() {
        if (_bookingStep.value == 4) {
            // Check availability before proceeding to customer details
            checkDateAvailabilityNow { available ->
                if (available) {
                    _bookingStep.value = 5
                }
            }
        } else if (_bookingStep.value < 7) {
            _bookingStep.value += 1
        }
    }

    fun prevBookingStep() {
        if (_bookingStep.value > 1) {
            _bookingStep.value -= 1
        }
    }

    fun setBookingStep(step: Int) {
        _bookingStep.value = step
    }

    fun toggleEventType(eventType: String) {
        val current = selectedEventTypes.value.toMutableSet()
        if (current.contains(eventType)) {
            if (current.size > 1) current.remove(eventType)
        } else {
            current.add(eventType)
        }
        selectedEventTypes.value = current
    }

    fun toggleService(service: String) {
        val current = selectedServices.value.toMutableSet()
        if (current.contains(service)) {
            if (current.size > 1) current.remove(service)
        } else {
            current.add(service)
        }
        selectedServices.value = current
    }

    fun checkDateAvailabilityNow(onComplete: ((Boolean) -> Unit)? = null) {
        val date = eventDate.value.trim()
        if (date.isBlank()) {
            _availabilityCheckResult.value = Pair(false, "Please provide a valid date (YYYY-MM-DD)")
            onComplete?.invoke(false)
            return
        }
        viewModelScope.launch {
            val result = repository.checkDateAvailability(date)
            _availabilityCheckResult.value = Pair(result.first, result.second)
            onComplete?.invoke(result.first)
        }
    }

    fun calculateEstimatedPrice(): Pair<Double, Double> {
        val allPkgs = activePackages.value
        val pkg = allPkgs.find { it.id == selectedPackageId.value } ?: allPkgs.firstOrNull()
        val basePrice = pkg?.price ?: 35000.0

        var addOnsPrice = 0.0
        val services = selectedServices.value
        if (services.contains("Drone Coverage")) addOnsPrice += 12000.0
        if (services.contains("Luxury Album & Prints")) addOnsPrice += 15000.0
        if (services.contains("Raw Footage")) addOnsPrice += 8000.0
        if (services.contains("Live Streaming")) addOnsPrice += 10000.0

        val total = basePrice + addOnsPrice
        val deposit = total * 0.25 // 25% booking deposit
        return Pair(total, deposit)
    }

    fun submitBookingRequest() {
        if (customerName.value.isBlank()) {
            _bookingError.value = "Please enter your full name."
            return
        }
        if (customerPhone.value.trim().length < 8) {
            _bookingError.value = "Please enter a valid contact phone number."
            return
        }
        if (!customerEmail.value.contains("@") || !customerEmail.value.contains(".")) {
            _bookingError.value = "Please enter a valid email address."
            return
        }
        if (venueLocation.value.isBlank()) {
            _bookingError.value = "Please enter the event venue & city."
            return
        }

        _isSubmittingBooking.value = true
        _bookingError.value = null

        viewModelScope.launch {
            try {
                // Final availability validation on server/local repository
                val availability = repository.checkDateAvailability(eventDate.value)
                if (!availability.first) {
                    _bookingError.value = availability.second
                    _isSubmittingBooking.value = false
                    return@launch
                }

                val allPkgs = activePackages.value
                val pkg = allPkgs.find { it.id == selectedPackageId.value } ?: allPkgs.firstOrNull()
                val (total, deposit) = calculateEstimatedPrice()

                val created = repository.submitBooking(
                    customerName = customerName.value.trim(),
                    customerPhone = customerPhone.value.trim(),
                    customerEmail = customerEmail.value.trim(),
                    eventName = "${selectedEventTypes.value.joinToString(", ")} for ${customerName.value.trim()}",
                    eventTypes = selectedEventTypes.value.joinToString(", "),
                    services = selectedServices.value.joinToString(", "),
                    packageName = pkg?.name ?: "Custom Package",
                    packagePrice = pkg?.price ?: total,
                    addOns = selectedServices.value.filter { it in listOf("Drone Coverage", "Luxury Album & Prints", "Raw Footage", "Live Streaming") }.joinToString(", "),
                    estimatedTotal = total,
                    depositAmount = deposit,
                    eventDate = eventDate.value.trim(),
                    eventTime = eventTime.value.trim(),
                    durationHours = durationHours.value,
                    venueLocation = venueLocation.value.trim(),
                    alternativeDate = alternativeDate.value.trim(),
                    specialInstructions = specialInstructions.value.trim(),
                    preferredContactMethod = preferredContactMethod.value
                )

                _confirmedBooking.value = created
                _bookingStep.value = 7
            } catch (e: Exception) {
                _bookingError.value = "Booking submission failed: ${e.localizedMessage}"
            } finally {
                _isSubmittingBooking.value = false
            }
        }
    }

    fun resetBookingForm() {
        _bookingStep.value = 1
        _confirmedBooking.value = null
        _bookingError.value = null
        customerName.value = ""
        customerPhone.value = ""
        customerEmail.value = ""
        specialInstructions.value = ""
        _availabilityCheckResult.value = Pair(null, null)
    }

    // Lookup
    fun lookupBooking() {
        val query = lookupSearchQuery.value.trim()
        if (query.isBlank()) {
            _lookupError.value = "Please enter your Booking Reference (e.g. SS-2026-4821)"
            return
        }
        _isLookingUp.value = true
        _lookupError.value = null
        _lookupResult.value = null

        viewModelScope.launch {
            val booking = repository.getBookingById(query)
            _isLookingUp.value = false
            if (booking != null) {
                _lookupResult.value = booking
            } else {
                _lookupError.value = "No booking found with reference '$query'. Please check and try again."
            }
        }
    }

    // Enquiry
    fun submitCustomerEnquiry() {
        if (enquiryName.value.isBlank()) {
            _enquiryError.value = "Please enter your name."
            return
        }
        if (enquiryPhone.value.trim().length < 8) {
            _enquiryError.value = "Please enter a valid phone number."
            return
        }
        if (!enquiryEmail.value.contains("@")) {
            _enquiryError.value = "Please enter a valid email."
            return
        }
        if (enquiryMessage.value.isBlank()) {
            _enquiryError.value = "Please write your message or enquiry."
            return
        }

        _enquiryError.value = null
        viewModelScope.launch {
            try {
                repository.submitEnquiry(
                    name = enquiryName.value.trim(),
                    phone = enquiryPhone.value.trim(),
                    email = enquiryEmail.value.trim(),
                    type = enquiryType.value,
                    message = enquiryMessage.value.trim(),
                    reference = enquiryBookingRef.value.trim()
                )
                _enquirySubmitted.value = true
                enquiryMessage.value = ""
                enquiryBookingRef.value = ""
            } catch (e: Exception) {
                _enquiryError.value = "Failed to submit enquiry: ${e.localizedMessage}"
            }
        }
    }

    fun resetEnquiryForm() {
        _enquirySubmitted.value = false
        _enquiryError.value = null
    }

    // Admin Auth & Management
    fun authenticateAdmin(pin: String): Boolean {
        // Permanent studio admin password: 9741
        if (pin.trim() == "9741") {
            _isAdminAuthenticated.value = true
            _adminPinError.value = null
            return true
        } else {
            _adminPinError.value = "Invalid Studio Administrator Password. Please try again."
            return false
        }
    }

    fun logoutAdmin() {
        _isAdminAuthenticated.value = false
        adminPinInput.value = ""
        _selectedBookingDetail.value = null
    }

    fun selectBookingForDetail(booking: Booking?) {
        _selectedBookingDetail.value = booking
    }

    fun updateBookingStatus(
        booking: Booking,
        newStatus: String,
        adminNotes: String,
        amountPaid: Double,
        paymentStatus: String
    ) {
        viewModelScope.launch {
            repository.updateBookingStatus(
                booking = booking,
                newStatus = newStatus,
                adminNotes = adminNotes,
                amountPaid = amountPaid,
                paymentStatus = paymentStatus
            )
            // Update local detail if currently open
            if (_selectedBookingDetail.value?.id == booking.id) {
                _selectedBookingDetail.value = booking.copy(
                    status = newStatus,
                    adminNotes = adminNotes,
                    amountPaid = amountPaid,
                    paymentStatus = paymentStatus
                )
            }
        }
    }

    fun deleteBooking(booking: Booking) {
        viewModelScope.launch {
            repository.deleteBooking(booking)
            if (_selectedBookingDetail.value?.id == booking.id) {
                _selectedBookingDetail.value = null
            }
        }
    }

    fun addPortfolioMedia(
        title: String,
        category: String,
        mediaType: String,
        imageUrl: String,
        videoUrl: String?,
        caption: String,
        duration: String?,
        isFeatured: Boolean
    ) {
        viewModelScope.launch {
            val media = PortfolioMedia(
                id = "port_${System.currentTimeMillis()}",
                title = title,
                category = category,
                mediaType = mediaType,
                imageUrl = imageUrl,
                videoUrl = videoUrl,
                duration = duration,
                caption = caption,
                isFeatured = isFeatured,
                isPublished = true
            )
            repository.savePortfolioMedia(media)
        }
    }

    fun toggleMediaPublish(media: PortfolioMedia) {
        viewModelScope.launch {
            repository.updatePortfolioMedia(media.copy(isPublished = !media.isPublished))
        }
    }

    fun toggleMediaFeatured(media: PortfolioMedia) {
        viewModelScope.launch {
            repository.updatePortfolioMedia(media.copy(isFeatured = !media.isFeatured))
        }
    }

    fun deletePortfolioMedia(media: PortfolioMedia) {
        viewModelScope.launch {
            repository.deletePortfolioMedia(media)
        }
    }

    fun togglePackageStatus(pkg: PackagePlan) {
        viewModelScope.launch {
            repository.updatePackage(pkg.copy(isActive = !pkg.isActive))
        }
    }

    fun updatePackagePrice(pkg: PackagePlan, newPrice: Double) {
        viewModelScope.launch {
            repository.updatePackage(pkg.copy(price = newPrice))
        }
    }

    fun blockDate(date: String, reason: String) {
        viewModelScope.launch {
            repository.addBlockedDate(date, reason)
        }
    }

    fun unblockDate(blockedDate: BlockedDate) {
        viewModelScope.launch {
            repository.removeBlockedDate(blockedDate)
        }
    }

    fun markEnquiryAsRead(enquiry: Enquiry) {
        viewModelScope.launch {
            repository.markEnquiryAsRead(enquiry)
        }
    }

    fun deleteEnquiry(enquiry: Enquiry) {
        viewModelScope.launch {
            repository.deleteEnquiry(enquiry)
        }
    }
}
