package com.aniketjain.weatherapp.mock;

/**
 * Provides mock weather-related values for UI display without depending on live network data.
 */
public class MockWeatherProvider {

    // PUBLIC_INTERFACE
    /**
     * Returns a mock "warm" index used for demonstration purposes.
     * @return an integer warm index
     */
    public static int getWarm() {
        return 3; // arbitrary warm index for mock display
    }
}
