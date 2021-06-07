package lilcode.aop.p3.c03.alarm

import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 뷰를 초기화 해주기
        initOnOffButton()
        initChangeAlarmTimeButton()

        // 저장된 데이터 가져오기
        val model = fetchDataFromSharedPreferences()
        // 뷰에 데이터를 그려주기
        renderView(model)


    }

    private fun renderView(model: AlarmDisplayModel) {
        findViewById<TextView>(R.id.ampmTextView).apply{
            text = model.ampmText
        }
        findViewById<TextView>(R.id.timeTextView).apply{
            text = model.timeText
        }
        findViewById<Button>(R.id.onOffButton).apply{
            text = model.onOffText
            tag = model
        }
    }

    private fun fetchDataFromSharedPreferences(): AlarmDisplayModel {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

        val timeDBValue = sharedPreferences.getString(ALARM_KEY, "09:30") ?: "09:30"
        val onOffDBValue = sharedPreferences.getBoolean(ONOFF_KEY, false)
        val alarmData = timeDBValue.split(":")

        val alarmModel = AlarmDisplayModel(alarmData[0].toInt(), alarmData[1].toInt(), onOffDBValue)

        // 보정 조정 예외처리
//        val pendingIntent = PendingIntent.getBroadcast(
//            this,
//            ALARM_REQUEST_CODE,
//            Intent(this, AlarmReceiver::class.java),
//            PendingIntent.FLAG_NO_CREATE) // 있으면 가져오고 없으면 안만든다. (null)
//
//        if ((pendingIntent == null) and alarmModel.onOff){
//            //알람은 꺼져있는데, 데이터는 켜져있는 경우
//            alarmModel.onOff = false
//
//        } else if((pendingIntent != null) and alarmModel.onOff.not()){
//            // 알람은 켜져있는데 데이터는 꺼져있는 경우.
//            // 알람을 취소함
//            pendingIntent.cancel()
//        }
        return alarmModel
    }

    // 시간 재설정 버튼.
    private fun initChangeAlarmTimeButton() {
        val changeAlarmButton = findViewById<Button>(R.id.changeAlarmTimeButton)
        changeAlarmButton.setOnClickListener {
            // 현재 시간을 가져오기 위해 캘린더 인스터늣 사
            val calendar = Calendar.getInstance()
            // TimePickDialog 띄워줘서 시간을 설정을 하게끔 하고, 그 시간을 가져와서
            TimePickerDialog(this, { picker, hour, minute ->


                // 데이터를 저장
                val model = saveAlarmModel(hour, minute, false)
                // 뷰를 업데이트
                renderView(model)

                // 기존에 있던 알람을 삭제한다.

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
                .show()

        }
    }

    // 알람 켜기 끄기 버튼.
    private fun initOnOffButton() {
        val onOffButton = findViewById<Button>(R.id.onOffButton)
        onOffButton.setOnClickListener {
            // 저장한 데이터를 확인한다

            // 온/오프 에 따라 작업을 처리한다

            // 오프 -> 알람을 제거

            // 온 -> 알람을 등록

            // 데이터를 저장한다.
        }
    }

    private fun saveAlarmModel(hour: Int, minute: Int, onOff: Boolean): AlarmDisplayModel {
        val model = AlarmDisplayModel(
            hour = hour,
            minute = minute,
            onOff = onOff
        )

        // time 에 대한 db 파일 생성
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

        // edit 모드로 열어서 작업 (값 저장)
        with(sharedPreferences.edit()) {
            putString(ALARM_KEY, model.makeDataForDB())
            putBoolean(ONOFF_KEY, model.onOff)
            commit()
        }

        return model
    }



    companion object {
        // static 영역 (상수 지정)
        private const val SHARED_PREFERENCE_NAME = "time"
        private const val ALARM_KEY = "alarm"
        private const val ONOFF_KEY = "onOff"
        private val ALARM_REQUEST_CODE = 1000
    }
}