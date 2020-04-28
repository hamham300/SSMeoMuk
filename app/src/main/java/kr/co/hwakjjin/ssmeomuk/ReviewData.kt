package kr.co.hwakjjin.ssmeomuk

import java.util.*

class ReviewData(
    code : String,
    ID : String,
    Review: String,
    Rate: Float,
    Up: Int,
    Date: Int
) {
    private var code = ""
    private var ID = ""
    private var Review = ""
    private var Rate = 0f
    private var Up = 0
    private var Date = 0

    init {
        this.code = code
        this.ID = ID
        this.Review = Review
        this.Rate = Rate
        this.Up = Up
        this.Date = Date
    }

    fun getCode(): String?{
        return code
    }

    fun getID(): String?{
        return ID
    }
    fun getReview(): String?{
        return Review
    }

    fun getRate(): Float?{
        return Rate
    }

    fun getUp(): Int?{
        return Up
    }

    fun getDate(): Int?{
        return Date
    }


}