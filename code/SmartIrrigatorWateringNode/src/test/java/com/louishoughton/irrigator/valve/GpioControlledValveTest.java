package com.louishoughton.irrigator.valve;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class GpioControlledValveTest {

    private GpioControlledValve valve;
    
    
    @Before
    public void setup() {
        valve = new GpioControlledValve(new MockGpioPin(null, null, null));
    }
    
    @Test
    public void test_should_start_closed() throws IrrigationValveException {
        assertFalse(valve.isOpen());
    }

    @Test
    public void test_should_open_valve() throws IrrigationValveException {
        valve.open();
        assertTrue(valve.isOpen());
    }
    
    @Test
    public void test_should_close_valve() throws IrrigationValveException {
        valve.open();
        valve.close();
        assertFalse(valve.isOpen());
    }
    
    @Test(expected = IrrigationValveException.class)
    public void test_should_throw_exception_when_valve_already_open() throws Exception {
        valve.open();
        valve.open();
    }
    
    @Test(expected = IrrigationValveException.class)
    public void test_should_throw_exception_when_valve_already_closed() throws Exception {
        valve.close();
    }

    @Test
    public void test_should_open_valve_for_provided_number_of_seconds() throws Exception {
        Date timeStarted = new Date();
        valve.openFor(1);
        long timeTaken = new Date().getTime() - timeStarted.getTime();
        assertThat(timeTaken, greaterThanOrEqualTo(1000L));
    }
    
}
