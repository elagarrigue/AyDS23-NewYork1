package ayds.newyork.songinfo.utils.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri

interface NavigationUtils {
    fun openExternalUrl(activity: Context, url: String?)
}

internal class NavigationUtilsImpl: NavigationUtils {
    override fun openExternalUrl(activity: Context, url: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        activity.startActivity(intent)
    }
}