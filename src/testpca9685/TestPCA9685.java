/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testpca9685;

import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import java.math.BigDecimal;
import java.util.Scanner;


/**
 *
 * @author darwin
 */
public class TestPCA9685 {
    
    private static final int SERVO_DURATION_MIN = 900; 
    private static final int SERVO_DURATION_NEUTRAL = 1500; 
    private static final int SERVO_DURATION_MAX = 2100; 
 
    @SuppressWarnings("resource") 
    public static void main(String args[]) throws Exception { 
        System.out.println("<--Pi4J--> PCA9685 PWM Example ... started."); 
        // This would theoretically lead into a resolution of 5 microseconds per step: 
        // 4096 Steps (12 Bit) 
        // T = 4096 * 0.000005s = 0.02048s 
        // f = 1 / T = 48.828125 
        BigDecimal frequency = new BigDecimal("1000"); 
        // Correction factor: actualFreq / targetFreq 
        // e.g. measured actual frequency is: 51.69 Hz 
        // Calculate correction factor: 51.65 / 48.828 = 1.0578 
        // --> To measure actual frequency set frequency without correction factor(or set to 1) 
        BigDecimal frequencyCorrectionFactor = new BigDecimal("1.0578"); 
        // Create custom PCA9685 GPIO provider 
        I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1); 
        System.out.println("set stage 1");
        final PCA9685GpioProvider gpioProvider = new PCA9685GpioProvider(bus, 0x40, frequency, frequencyCorrectionFactor); 
        System.out.println("2");
        // Define outputs in use for this example 
        GpioPinPwmOutput[] myOutputs = provisionPwmOutputs(gpioProvider); 
        System.out.println("3");
        // Reset outputs 
        //gpioProvider.reset(); 
        System.out.println("gpio Reset");
        // 
        // Set full ON 
       //gpioProvider.setAlwaysOn(PCA9685Pin.PWM_00); 
        // Set full OFF 
        gpioProvider.setAlwaysOff(PCA9685Pin.PWM_11); 
        // Set 0.9ms pulse (R/C Servo minimum position)  
        double frequency_double = Double.parseDouble(frequency.toString());
        
        double steps = (1/frequency_double)*1000000;
        
        System.out.println("VALUE OF STEPS IS: " + steps);
        int step_int = (int)steps;
        for(int x = 0 ; x <= 653;x++){
            gpioProvider.setPwm(PCA9685Pin.PWM_02 , 100+x);
            gpioProvider.setPwm(PCA9685Pin.PWM_03 , 100+x);
            System.out.println(500+x);
            Thread.sleep(1000);
        }
        
        System.out.println("done");
    /*
        // 
        // Show PWM values for outputs 0..14 
        for (GpioPinPwmOutput output : myOutputs) { 
            int[] onOffValues = gpioProvider.getPwmOnOffValues(output.getPin()); 
            System.out.println(output.getPin().getName() + " (" + output.getName() + "): ON value [" + onOffValues[0] + "], OFF value [" + onOffValues[1] + "]"); 
        } */
        System.out.println("Press <Enter> to terminate..."); 
        new Scanner(System.in).nextLine(); 
        gpioProvider.shutdown();
    } 
 
    private static int checkForOverflow(int position) { 
        int result = position; 
        if (position > PCA9685GpioProvider.PWM_STEPS - 1) { 
            result = position - PCA9685GpioProvider.PWM_STEPS - 1; 
        } 
        return result; 
    } 
 
    private static GpioPinPwmOutput[] provisionPwmOutputs(final PCA9685GpioProvider gpioProvider) { 
        GpioController gpio = GpioFactory.getInstance(); 
        GpioPinPwmOutput myOutputs[] = { 
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_00, "Pulse 00"), 
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_01, "Pulse 01"), 
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_02, "Pulse 02"), 
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_03, "Pulse 03"), 
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_04, "Pulse 04"), 
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_05, "Pulse 05"), 
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_06, "Pulse 06"), 
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_07, "Pulse 07"), 
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_08, "Pulse 08"), 
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_09, "Pulse 09"), 
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_10, "Always ON"), 
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_11, "Always OFF"), 
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_12, "Servo pulse MIN"), 
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_13, "Servo pulse NEUTRAL"), 
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_14, "Servo pulse MAX"), 
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_15, "not used")}; 
        return myOutputs; 
    } 
}

