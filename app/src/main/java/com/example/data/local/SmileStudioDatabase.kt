package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.BlockedDateDao
import com.example.data.local.dao.BookingDao
import com.example.data.local.dao.EnquiryDao
import com.example.data.local.dao.NotificationLogDao
import com.example.data.local.dao.PackageDao
import com.example.data.local.dao.PortfolioDao
import com.example.data.model.BlockedDate
import com.example.data.model.Booking
import com.example.data.model.Enquiry
import com.example.data.model.NotificationLog
import com.example.data.model.PackagePlan
import com.example.data.model.PortfolioMedia

@Database(
    entities = [
        Booking::class,
        Enquiry::class,
        PortfolioMedia::class,
        PackagePlan::class,
        BlockedDate::class,
        NotificationLog::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SmileStudioDatabase : RoomDatabase() {
    abstract fun bookingDao(): BookingDao
    abstract fun enquiryDao(): EnquiryDao
    abstract fun portfolioDao(): PortfolioDao
    abstract fun packageDao(): PackageDao
    abstract fun blockedDateDao(): BlockedDateDao
    abstract fun notificationLogDao(): NotificationLogDao

    companion object {
        @Volatile
        private var INSTANCE: SmileStudioDatabase? = null

        fun getDatabase(context: Context): SmileStudioDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SmileStudioDatabase::class.java,
                    "smile_studio_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
