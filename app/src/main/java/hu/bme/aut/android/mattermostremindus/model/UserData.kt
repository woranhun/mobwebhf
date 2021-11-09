package hu.bme.aut.android.mattermostremindus.model

data class UserData(
    val auth_data: String,
    val auth_service: String,
    val create_at: Long,
    val delete_at: Int,
    val disable_welcome_email: Boolean,
    val email: String,
    val first_name: String,
    val id: String,
    val last_name: String,
    val last_password_update: Long,
    val last_picture_update: Long,
    val locale: String,
    val nickname: String,
    val notify_props: NotifyProps,
    val position: String,
    val props: Props,
    val roles: String,
    val timezone: Timezone,
    val update_at: Long,
    val username: String
)
