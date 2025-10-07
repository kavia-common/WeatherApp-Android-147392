package com.aniketjain.weatherapp.toast;

import android.content.Context;

import com.aniketjain.roastedtoast.Toasty;
import com.aniketjain.weatherapp.R;

public class Toaster {
    public static void successToast(Context context, String msg) {
        Toasty.custom(
                context,
                msg,
                R.drawable.ic_baseline_check_24,
                "#454B54",
                14,
                "#EEEEEE");
    }

    public static void errorToast(Context context, String msg) {
        Toasty.custom(
                context,
                msg,
                R.drawable.ic_baseline_error_outline_24,
                "#454B54",
                14,
                "#EEEEEE");
    }

    // PUBLIC_INTERFACE
    /**
     * Shows an informational toast message with neutral styling.
     * This is used as a fallback or to show non-critical info like warm details.
     *
     * @param context Context to show the toast
     * @param msg     Message to display
     */
    public static void infoToast(Context context, String msg) {
        // Using the same custom toast style; in absence of a dedicated info icon, reuse check icon.
        Toasty.custom(
                context,
                msg,
                R.drawable.ic_baseline_check_24,
                "#454B54",
                14,
                "#EEEEEE");
    }
}
