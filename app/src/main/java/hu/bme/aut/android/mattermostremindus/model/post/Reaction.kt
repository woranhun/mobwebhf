package hu.bme.aut.android.mattermostremindus.model.post

data class Reaction(
    val create_at: Long,
    val emoji_name: String,
    val post_id: String,
    val user_id: String
)