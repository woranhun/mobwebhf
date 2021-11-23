package hu.bme.aut.android.mattermostremindus.model.login

data class NotifyProps(
    val auto_responder_active: String,
    val auto_responder_message: String,
    val channel: String,
    val comments: String,
    val desktop: String,
    val desktop_notification_sound: String,
    val desktop_sound: String,
    val email: String,
    val first_name: String,
    val mention_keys: String,
    val push: String,
    val push_status: String
)