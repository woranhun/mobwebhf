package hu.bme.aut.android.mattermostremindus.model

data class Channel(
    val create_at: Int,
    val creator_id: String,
    val delete_at: Int,
    val display_name: String,
    val extra_update_at: Int,
    val header: String,
    val id: String,
    val last_post_at: Int,
    val name: String,
    val policy_id: String,
    val purpose: String,
    val team_display_name: String,
    val team_id: String,
    val team_name: String,
    val team_update_at: Int,
    val total_msg_count: Int,
    val type: String,
    val update_at: Int
)