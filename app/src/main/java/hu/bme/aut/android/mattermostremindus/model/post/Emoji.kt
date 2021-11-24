package hu.bme.aut.android.mattermostremindus.model.post

data class Emoji(
    val create_at: Long,
    val creator_id: String,
    val delete_at: Long,
    val id: String,
    val name: String,
    val update_at: Long
)