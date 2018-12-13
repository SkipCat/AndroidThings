package hello.supinternet.com.rasberrystuff

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import com.google.android.things.contrib.driver.button.Button
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat
import com.google.android.things.contrib.driver.button.Button.OnButtonEventListener
import com.google.android.things.contrib.driver.ht16k33.Ht16k33
import com.google.android.things.contrib.driver.pwmservo.Servo
import com.google.android.things.contrib.driver.pwmspeaker.Speaker
import java.util.concurrent.TimeUnit
import kotlin.math.min
import com.google.android.things.contrib.driver.apa102.Apa102
import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay
import com.google.android.things.contrib.driver.bmx280.Bmx280






/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * val service = PeripheralManagerService()
 * val mLedGpio = service.openGpio("BCM6")
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * mLedGpio.value = true
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 */
class MainActivity : Activity() {

    private var button :Button? = null
    private var button2 :Button? = null
    private var button3 :Button? = null
    private var servo :Servo? = null
    private var buzzer: Speaker? = null
    private val BAROMETER_RANGE_LOW = 965f
    private val BAROMETER_RANGE_HIGH = 1035f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        button = RainbowHat.openButtonC()
        button?.setOnButtonEventListener { _, _ -> this.finish() }

        button2 = RainbowHat.openButtonB()
        button2?.setOnButtonEventListener { _, _ -> enableMotor() }

        button3 = RainbowHat.openButtonA()
        button3?.setOnButtonEventListener { _, _ -> disableMotor() }

        // AfficherDuTexte1()
        // JouerUnSon1()
        //RubanLed1()
        CapteurMeteos1()
        ServoMotor1()
        CapteurMeteos2()
    }


    override fun onDestroy() {
        servo?.close()
        button?.close()
        button2?.close()
        button3?.close()
        buzzer?.close()
        super.onDestroy()
    }

    private fun enableMotor() {
        servo?.setEnabled(true)
    }

    private fun disableMotor() {
        servo?.setEnabled(false)
    }

    private fun CapteurMeteos1() {
        val sensor = RainbowHat.openSensor()
        sensor.temperatureOversampling = Bmx280.OVERSAMPLING_1X
        val segment = RainbowHat.openDisplay()
        segment.setBrightness(Ht16k33.HT16K33_BRIGHTNESS_MAX)
        segment.display(sensor.readTemperature().toDouble())
        segment.setEnabled(true)
        sensor.close()
        segment.close()

    }
    private fun CapteurMeteos2() {
        val sensor = RainbowHat.openSensor()

        val ledstrip = RainbowHat.openLedStrip()
        ledstrip.brightness = 1
        val colors = getWeatherStripColors(sensor.readPressure())

        for (i in colors.indices) {
            colors[i] = Color.HSVToColor(255, floatArrayOf(i * 360f / colors.size, 1.0f, 1.0f))
        }

        ledstrip.write(colors)
        ledstrip.close()
    }

    fun getWeatherStripColors(pressure: Float): IntArray {
        val t = (pressure - BAROMETER_RANGE_LOW) / (BAROMETER_RANGE_HIGH - BAROMETER_RANGE_LOW)
        val sRainbowColors = IntArray(RainbowHat.LEDSTRIP_LENGTH)
        for (i in sRainbowColors.indices) {
            sRainbowColors[i] = Color.HSVToColor(255, floatArrayOf(i * 360f / sRainbowColors.size, 1.0f, 1.0f))
        }
        var n = Math.ceil((sRainbowColors.size * t).toDouble()).toInt()
        n = Math.max(0, Math.min(n, sRainbowColors.size))

        val colors = IntArray(sRainbowColors.size)
        for (i in 0 until n) {
            val ri = sRainbowColors.size - 1 - i
            colors[ri] = sRainbowColors[ri]
        }

        return colors
    }
    private fun RubanLed1() {
        val ledstrip = RainbowHat.openLedStrip()
        ledstrip.brightness = 1
        val rainbow = IntArray(RainbowHat.LEDSTRIP_LENGTH)
        for (i in rainbow.indices) {
            rainbow[i] = Color.HSVToColor(255, floatArrayOf(i * 360f / rainbow.size, 1.0f, 1.0f))
        }
        ledstrip.write(rainbow)

        ledstrip.close()
    }

    private fun ServoMotor1() {
        servo = RainbowHat.openServo()
        servo?.angle = servo?.maximumAngle!!
        servo?.setPulseDurationRange(servo?.minimumPulseDuration!!, servo?.maximumPulseDuration!!)
    }

    private fun AfficherDuTexte1() {
        val segment = RainbowHat.openDisplay()
        segment.setBrightness(Ht16k33.HT16K33_BRIGHTNESS_MAX)
        segment.display("BOOM")
        segment.setEnabled(true)

        segment.close()
    }

    fun JouerUnSon1() {
        val frequences:IntArray = intArrayOf(261, 294, 329, 349, 392, 440, 493, 523)
        buzzer = RainbowHat.openPiezo()

        frequences.forEach {
            buzzer?.play(it.toDouble())
            TimeUnit.MILLISECONDS.sleep(300)
        }

        buzzer?.stop()
    }

}
