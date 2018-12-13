package hello.supinternet.com.rasberrystuff

import android.app.Activity
import android.os.Bundle
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat







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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        val led = RainbowHat.openLedRed()
        led.value = true
        led.close()

    }

    override fun onDestroy() {
        super.onDestroy()
    }

/*
    fun displayTxt() {
        val segment = RainbowHat.openDisplay()
        segment.setBrightness(Ht16k33.HT16K33_BRIGHTNESS_MAX)
        segment.display("ABCD")
        segment.setEnabled(true)

        segment.close()
    }
*/
}
