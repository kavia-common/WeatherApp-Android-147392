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

    // PUBLIC_INTERFACE
    /**
     * Returns mock details about the "warm" metric to be displayed in an info dialog.
     * If no information is available, this may return null and callers should handle it safely.
     *
     * @return WarmInfo object containing index, description and tip; or null if unavailable.
     */
    public static WarmInfo getWarmInfo() {
        // In a real implementation, this might compute or fetch meaningful data.
        // Here, we provide static mock data. Return non-null normally, but callers will still guard for null.
        return new WarmInfo(getWarm(), "Mild", "Stay hydrated");
    }

    /**
     * Simple data holder for warm-related information.
     */
    public static class WarmInfo {
        public final int index;
        public final String description;
        public final String tip;

        public WarmInfo(int index, String description, String tip) {
            this.index = index;
            this.description = description;
            this.tip = tip;
        }
    }
}
