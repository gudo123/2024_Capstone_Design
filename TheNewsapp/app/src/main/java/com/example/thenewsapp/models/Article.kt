package com.example.thenewsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val article_id: String = "",  // 기본값 설정
    val title: String = "No Title",
    val description: String? = null,
    val link: String = "",
    val image_url: String? = null,
    val pubDate: String? = null,
    val source_id: String? = null
) : Serializable {
    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (article_id?.hashCode() ?: 0) // article_id 추가
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (link?.hashCode() ?: 0)
        result = 31 * result + (image_url?.hashCode() ?: 0)
        result = 31 * result + (pubDate?.hashCode() ?: 0)
        result = 31 * result + (source_id?.hashCode() ?: 0)
        return result
    }
}
