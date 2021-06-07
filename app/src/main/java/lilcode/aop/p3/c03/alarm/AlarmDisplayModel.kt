package lilcode.aop.p3.c03.alarm

data class AlarmDisplayModel(
    val hour: Int, // 0~23
    val minute: Int,
    var onOff: Boolean
) {

    fun makeDataForDB(): String {
        return "$hour:$minute"
    }

    val timeText: String
        get() {
            val h = "%02d".format(if (hour < 12) hour else hour - 12)
            val m = "%02d".format(minute)

            return "$h:$m"
        }


    val ampmText: String
        get() {
            return if (hour < 12) "AM" else "PM"
        }
}