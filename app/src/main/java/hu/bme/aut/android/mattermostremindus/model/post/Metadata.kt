package hu.bme.aut.android.mattermostremindus.model.post

data class Metadata(
    val embeds: List<Embed>,
    val emojis: List<Emoji>,
    val files: List<File>,
    val images: Images,
    val reactions: List<Reaction>
)