package com.xxhhxhh.mainthing.login_and_register.timer;

import android.os.CountDownTimer;
import android.widget.Button;

public class CountDownReceviceTime extends CountDownTimer {
    /**
     * @param millisInFuture  时间   The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval 间隔 The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */

    private Button button;
    private String text;

    public CountDownReceviceTime(long millisInFuture, long countDownInterval, Button button) {
        super(millisInFuture, countDownInterval);
        this.button = button;
        text = button.getText().toString();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        button.setEnabled(false);
        int now = (int)millisUntilFinished / 1000;
        String now1 = String.valueOf(now);
        button.setText(now1);
    }

    @Override
    public void onFinish() {
        button.setText(text);
        button.setEnabled(true);
    }
}
