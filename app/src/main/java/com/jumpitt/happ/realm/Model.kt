package com.jumpitt.happ.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RegisterData(
    var rut: String? = null,
    var names: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var idCompany: String? = null,
    var nameCompany: String? = null,
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
    var passportValidationUrl: String? = null,
    var resultTypeTextTitle: String? = null,
    var resultTypeTextDescription: String? = null
): RealmObject(){
    constructor(): this(null)
}

open class RiskTime(
  var myTcn: String? = null,
  var tcnFound: String? = null,
  var dateFirstContact: String? = null,
  var dateLastContact: String? = null,
  var totalTime: String? = null
): RealmObject(){
    constructor(): this(null)
}

open class TraceProximityNotification(
    @PrimaryKey var id: Int = 1,
    var firstRegisterTrace: String? = null,
    var lastRegisterTrace: String? = null
): RealmObject(){
    constructor(): this(id = 1)
}