package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BlockedDate
import com.example.data.model.Booking
import com.example.data.model.Enquiry
import com.example.data.model.NotificationLog
import com.example.data.model.PackagePlan
import com.example.data.model.PortfolioMedia
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY createdAt DESC")
    fun getAllBookings(): Flow<List<Booking>>

    @Query("SELECT * FROM bookings WHERE id = :id LIMIT 1")
    suspend fun getBookingById(id: String): Booking?

    @Query("SELECT * FROM bookings WHERE eventDate = :date AND status IN ('CONFIRMED', 'PENDING')")
    suspend fun getBookingsForDate(date: String): List<Booking>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: Booking)

    @Update
    suspend fun updateBooking(booking: Booking)

    @Delete
    suspend fun deleteBooking(booking: Booking)
}

@Dao
interface EnquiryDao {
    @Query("SELECT * FROM enquiries ORDER BY createdAt DESC")
    fun getAllEnquiries(): Flow<List<Enquiry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnquiry(enquiry: Enquiry)

    @Update
    suspend fun updateEnquiry(enquiry: Enquiry)

    @Delete
    suspend fun deleteEnquiry(enquiry: Enquiry)
}

@Dao
interface PortfolioDao {
    @Query("SELECT * FROM portfolio_media WHERE isPublished = 1 ORDER BY createdAt DESC")
    fun getPublishedPortfolio(): Flow<List<PortfolioMedia>>

    @Query("SELECT * FROM portfolio_media ORDER BY createdAt DESC")
    fun getAllPortfolioAdmin(): Flow<List<PortfolioMedia>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPortfolio(media: PortfolioMedia)

    @Update
    suspend fun updatePortfolio(media: PortfolioMedia)

    @Delete
    suspend fun deletePortfolio(media: PortfolioMedia)
}

@Dao
interface PackageDao {
    @Query("SELECT * FROM packages WHERE isActive = 1")
    fun getActivePackages(): Flow<List<PackagePlan>>

    @Query("SELECT * FROM packages")
    fun getAllPackagesAdmin(): Flow<List<PackagePlan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPackage(plan: PackagePlan)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPackages(plans: List<PackagePlan>)

    @Update
    suspend fun updatePackage(plan: PackagePlan)

    @Delete
    suspend fun deletePackage(plan: PackagePlan)
}

@Dao
interface BlockedDateDao {
    @Query("SELECT * FROM blocked_dates ORDER BY date ASC")
    fun getAllBlockedDates(): Flow<List<BlockedDate>>

    @Query("SELECT * FROM blocked_dates WHERE date = :date LIMIT 1")
    suspend fun getBlockedDate(date: String): BlockedDate?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlockedDate(blockedDate: BlockedDate)

    @Delete
    suspend fun deleteBlockedDate(blockedDate: BlockedDate)
}

@Dao
interface NotificationLogDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(log: NotificationLog)

    @Update
    suspend fun updateNotification(log: NotificationLog)
}
