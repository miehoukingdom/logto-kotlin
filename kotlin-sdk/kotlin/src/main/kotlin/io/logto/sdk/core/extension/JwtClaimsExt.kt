package io.logto.sdk.core.extension

import io.logto.sdk.core.constant.ClaimName
import io.logto.sdk.core.type.IdTokenClaims
import org.jose4j.jwt.JwtClaims

fun JwtClaims.toIdTokenClaims(): IdTokenClaims = IdTokenClaims(
    iss = this.issuer,
    sub = this.subject,
    aud = this.audience[0],
    exp = this.expirationTime.value,
    iat = this.issuedAt.value,
    atHash = this.getClaimValueAsString(ClaimName.AT_HASH),
    name = this.getClaimValueAsString(ClaimName.NAME),
    username = this.getClaimValueAsString(ClaimName.USERNAME),
    picture = this.getClaimValueAsString(ClaimName.PICTURE),
    roleNames = this.getStringListClaimValue(ClaimName.ROLE_NAMES),
    email = this.getClaimValueAsString(ClaimName.EMAIL),
    emailVerified = this.getClaimValue(ClaimName.EMAIL_VERIFIED) as Boolean?,
    phoneNumber = this.getClaimValueAsString(ClaimName.PHONE_NUMBER),
    phoneNumberVerified = this.getClaimValue(ClaimName.PHONE_NUMBER_VERIFIED) as Boolean?,
)
