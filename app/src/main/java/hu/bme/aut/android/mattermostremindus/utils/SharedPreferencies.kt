package hu.bme.aut.android.mattermostremindus.utils

import android.content.Context

class SharedPreferencies {
    companion object {
        const val MattermostRemindUs = "MattermostRemindUs"
        const val MmMyUser = "MmMyUser"
        const val MmUrl = "mmUrlKey"
        const val MmApiKey = "mmApiKey"
        fun getToken(context: Context): String? {
            return context.getSharedPreferences(
                SharedPreferencies.MattermostRemindUs,
                Context.MODE_PRIVATE
            ).getString(
                SharedPreferencies.MmApiKey,
                null
            )
        }

        fun getUrl(context: Context): String? {
            return context.getSharedPreferences(
                SharedPreferencies.MattermostRemindUs,
                Context.MODE_PRIVATE
            ).getString(
                SharedPreferencies.MmUrl,
                null
            )
        }

        fun getMyUser(context: Context): String? {
            return context.getSharedPreferences(
                SharedPreferencies.MattermostRemindUs,
                Context.MODE_PRIVATE
            ).getString(
                SharedPreferencies.MmMyUser,
                null
            )
        }

        fun removeToken(context: Context) {
            context.getSharedPreferences(
                SharedPreferencies.MattermostRemindUs,
                Context.MODE_PRIVATE
            ).edit()
                .putString(SharedPreferencies.MmApiKey, null).apply()
        }
    }

}