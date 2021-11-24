package hu.bme.aut.android.mattermostremindus.model.channels

data class Channel(
    val create_at: Long,
    val creator_id: String,
    val delete_at: Long,
    val display_name: String,
    val extra_update_at: Long,
    val header: String,
    val id: String,
    val last_post_at: Long,
    val name: String,
    val policy_id: String,
    val purpose: String,
    val team_display_name: String,
    val team_id: String,
    val team_name: String,
    val team_update_at: Long,
    val total_msg_count: Long,
    val type: String,
    val update_at: Long
)