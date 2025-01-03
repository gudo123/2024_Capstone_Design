package com.example.thenewsapp.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            if (!isColumnExists(db, "articles", "source")) {
                db.execSQL("ALTER TABLE articles ADD COLUMN source TEXT")  // nullable로 추가
            }
            if (!isColumnExists(db, "articles", "content")) {
                db.execSQL("ALTER TABLE articles ADD COLUMN content TEXT")  // nullable로 추가
            }
        }
    }

    private fun isColumnExists(database: SupportSQLiteDatabase, tableName: String, columnName: String): Boolean {
        val cursor = database.query("PRAGMA table_info($tableName)")
        cursor.use {
            while (cursor.moveToNext()) {
                val nameIndex = cursor.getColumnIndex("name")
                if (nameIndex >= 0 && cursor.getString(nameIndex) == columnName) {
                    return true
                }
            }
        }
        return false
    }
}
