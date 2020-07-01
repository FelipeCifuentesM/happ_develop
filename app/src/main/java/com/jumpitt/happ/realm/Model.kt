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

//class TriageReturnValue: RealmObject(){
//
//}