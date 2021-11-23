package hu.bme.aut.android.mattermostremindus.model.user

data class User(
    val auth_service: String,
    val create_at: Int,
    val delete_at: Int,
    val email: String,
    val email_verified: Boolean,
    val failed_attempts: Int,
    val first_name: String,
    val id: String,
    val last_name: String,
    val last_password_update: Int,
    val last_picture_update: Int,
    val locale: String,
    val mfa_active: Boolean,
    val nickname: String,
    val notify_props: NotifyProps,
    val props: Props,
    val roles: String,
    val terms_of_service_create_at: Int,
    val terms_of_service_id: String,
    val timezone: Timezone,
    val update_at: Int,
    val username: String
)