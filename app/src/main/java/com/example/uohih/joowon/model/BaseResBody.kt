package com.example.uohih.joowon.model

import java.sql.Timestamp

open class BaseResBody(
        var timestamp: String? = null,
        var txid: String? = null,
        var result: String? = null,
        var errCode: String? =null,
        var msg:String? = null
)