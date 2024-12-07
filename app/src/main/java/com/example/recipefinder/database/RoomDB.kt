package com.example.recipefinder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

import com.example.recipefinder.entities.Recipe
import com.example.recipefinder.entities.RecipeDAO
import com.example.recipefinder.entities.TypeConverter

@Database(entities = [Recipe::class], version = 2, exportSchema = false)
@TypeConverters(TypeConverter::class)  // Register the Converters
abstract class RoomDB : RoomDatabase() {
    abstract fun recipeDao(): RecipeDAO

    companion object {
        @Volatile
        private var INSTANCE: RoomDB? = null

        fun getDatabase(context: Context): RoomDB {
            return INSTANCE ?: synchronized(this) {
                val Migration_1_2 = object : Migration(1, 2) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        // Add a new column to the table
                        database.execSQL("ALTER TABLE recipes ADD COLUMN new_column TEXT DEFAULT ''")
                    }
                }
                val Migration_2_3 = object : Migration(2, 3) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        // Create a new table
                        database.execSQL("""
            CREATE TABLE new_table (
                id INTEGER PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                value TEXT
            )
        """.trimIndent())
                    }
                }
                val Migration_3_4 = object : Migration(3, 4) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        // Create the new table
                        database.execSQL("""
            CREATE TABLE new_table (
                id INTEGER PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                value TEXT
            )
        """.trimIndent())

                        // Copy data from old table to new table
                        database.execSQL("""
            INSERT INTO new_table (id, name, value)
            SELECT id, name, value FROM old_table
        """.trimIndent())

                        // Drop the old table
                        database.execSQL("DROP TABLE old_table")

                        // Rename the new table to the old table's name
                        database.execSQL("ALTER TABLE new_table RENAME TO old_table")
                    }
                }
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDB::class.java,
                    "recipes"
                ).addMigrations(Migration_1_2, Migration_2_3, Migration_3_4)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    val Migration_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Add a new column to the table
            database.execSQL("ALTER TABLE recipes ADD COLUMN new_column TEXT DEFAULT ''")
        }
    }

    val Migration_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create a new table
            database.execSQL("""
            CREATE TABLE new_table (
                id INTEGER PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                value TEXT
            )
        """.trimIndent())
        }
    }

    val Migration_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create the new table
            database.execSQL("""
            CREATE TABLE new_table (
                id INTEGER PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                value TEXT
            )
        """.trimIndent())

            // Copy data from old table to new table
            database.execSQL("""
            INSERT INTO new_table (id, name, value)
            SELECT id, name, value FROM old_table
        """.trimIndent())

            // Drop the old table
            database.execSQL("DROP TABLE old_table")

            // Rename the new table to the old table's name
            database.execSQL("ALTER TABLE new_table RENAME TO old_table")
        }
    }
}
