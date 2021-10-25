package io.logto.android.config

data class LogtoConfig(
    val logtoUrl: String,
    val clientId: String,
    val scopes: List<String>,
    val redirectUri: String,
    val postLogoutRedirectUri: String,
) {
    val encodedScopes: String
        get() = scopes.joinToString(" ")
}