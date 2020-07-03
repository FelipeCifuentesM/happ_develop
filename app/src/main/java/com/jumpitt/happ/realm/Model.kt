package com.jumpitt.happ.realm

import io.realm.RealmObject

open class RegisterData(
    var rut: String? = null,
    var names: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var homeCommuneId: String? = null,
    var workCommuneId: String? = null,
    var accessToken: String? = null,
    var refreshToken: String? = null
): RealmObject(){
    constructor(): this(null)
}

open class TriageReturnValue(
    var score: Int? = null,
    var riskTitle: String? = null,
    var riskLevel: String? = null,
    var riskDescription: String? = null,
    var riskMessage: String? = null,
    var lastReview: String? = null,
    var passportTimeRemainingVerbose: String? = null,
    var passportValidationUrl: String? = null
): RealmObject(){
    constructor(): this(null)
}
