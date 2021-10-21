package io.logto.android.auth.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION

class AuthorizationActivity : AppCompatActivity() {

    private val customTabsAvailable: Boolean
        get() {
            val activityIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.fromParts("http", "", null)
                addCategory(Intent.CATEGORY_BROWSABLE)
            }

            val resolvedActivityList = packageManager.queryIntentActivities(activityIntent, 0)
            val packagesSupportingCustomTabs = mutableListOf<ResolveInfo>()
            for (info in resolvedActivityList) {
                val serviceIntent = Intent().apply {
                    action = ACTION_CUSTOM_TABS_CONNECTION
                    setPackage(info.activityInfo.packageName)
                }
                packageManager.resolveService(serviceIntent, 0)?.let {
                    packagesSupportingCustomTabs.add(info)
                }
            }

            return packagesSupportingCustomTabs.isNotEmpty()
        }

    private var flowStarted: Boolean = false

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        handleFlow()
    }

    private fun handleFlow() {
        if (flowStarted) {
            handleFlowEnd()
            return
        }
        val endpoint = intent.getStringExtra(EXTRA_FLOW_ENDPOINT)
        if (endpoint == null) {
            handleFlowEnd()
            return
        }
        handleFlowStart(endpoint)
    }

    private fun handleFlowStart(endpoint: String) {
        if (customTabsAvailable) {
            startFlowWithCustomTabs(endpoint)
        } else {
            startFlowWithBrowser(endpoint)
        }
        flowStarted = true
    }

    private fun handleFlowEnd() {
        finish()
        flowStarted = false
    }

    private fun startFlowWithCustomTabs(endpoint: String) {
        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        customTabsIntent.launchUrl(this, Uri.parse(endpoint))
    }

    private fun startFlowWithBrowser(endpoint: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(endpoint)))
    }

    companion object {
        private const val EXTRA_FLOW_ENDPOINT = "EXTRA_FLOW_ENDPOINT"

        fun createHandleStartIntent(
            context: Context,
            endpoint: String,
        ): Intent {
            return Intent(context, AuthorizationActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                putExtra(EXTRA_FLOW_ENDPOINT, endpoint)
            }
        }

        fun createHandleCompleteIntent(
            context: Context,
        ): Intent {
            return Intent(context, AuthorizationActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
        }
    }
}
