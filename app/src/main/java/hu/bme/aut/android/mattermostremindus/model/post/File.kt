package hu.bme.aut.android.mattermostremindus.model.post

data class File(
    val create_at: Long,
    val delete_at: Long,
    val extension: String,
    val has_preview_image: Boolean,
    val height: Int,
    val id: String,
    val mime_type: String,
    val name: String,
    val post_id: String,
    val size: Long,
    val update_at: Long,
    val user_id: String,
    val width: Int
)