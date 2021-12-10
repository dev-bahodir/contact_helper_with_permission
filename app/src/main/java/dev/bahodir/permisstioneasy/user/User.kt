package dev.bahodir.permisstioneasy.user

import java.io.Serializable

data class User(
    var name: String? = null,
    var number: String? = null
): Serializable