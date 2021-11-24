package hu.bme.aut.android.mattermostremindus.model.post

data class Post(
    val channel_id: String,
    val create_at: Long,
    val delete_at: Long,
    val edit_at: Long,
    val file_ids: List<String>,
    val hashtag: String,
    val id: String,
    val message: String,
    val metadata: Metadata,
    val original_id: String,
    val pending_post_id: String,
    val props: Props,
    val root_id: String,
    val type: String,
    val update_at: Long,
    val user_id: String
)