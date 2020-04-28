package kr.co.hwakjjin.ssmeomuk

class MenuData(
    code : String,
    foodPic: Int,
    txt_menu: String,
    txt_diner: String,
    rate: Float,
    price: Int,
    bestReviewer: String,
    bestReview: String,
    bestReviewUp: Int
) {
    private var code = ""
    private var foodPic = 0
    private var txt_menu: String = ""
    private var txt_diner: String = ""
    private var rate = 0f
    private var price = 0
    private var bestReviewer: String = ""
    private var bestReview: String = ""
    private var bestReviewUp = 0

    init{
        this.code = code
        this.foodPic = foodPic
        this.txt_menu = txt_menu
        this.txt_diner = txt_diner
        this.rate = rate
        this.price = price
        this.bestReviewer = bestReviewer
        this.bestReview = bestReview
        this.bestReviewUp = bestReviewUp
    }


    fun getCode(): String? {
        return code
    }

    fun getFoodPic(): Int {
        return foodPic
    }

    fun getMenuTxt(): String? {
        return txt_menu
    }

    fun getDiner(): String?{
        return txt_diner
    }

    fun getRate(): Float {
        return rate
    }

    fun getPrice(): Int {
        return price
    }

    fun getBestReviewer(): String? {
        return bestReviewer
    }

    fun getBestReview(): String? {
        return bestReview
    }

    fun getBestReviewUp(): Int {
        return bestReviewUp
    }
}