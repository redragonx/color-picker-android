package net.dicesoft.colorpicker_android.Listeners;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import net.dicesoft.colorpicker_android.MusicThread;
import net.dicesoft.colorpicker_android.R;

/**
 * Created by stephen on 8/21/15.
 */
public class SeekBarOnChangeListener implements SeekBar.OnSeekBarChangeListener {


    private MusicThread musicTimeThread;
    private Button buttonSelector;


    SeekBar redSeekBar, greenSeekBar, blueSeekBar;
    private Window window;
    private View colorView;
    private Display display;
    int red, green, blue, seekBarLeft;

    TextView redToolTip, greenToolTip, blueToolTip;
    Rect thumbRect;

    public SeekBarOnChangeListener(Display display,
                                   View colorView,
                                   Window window,
                                   Button buttonSelector,
                                   SeekBar redSeekBar,
                                   SeekBar greenSeekBar,
                                   SeekBar blueSeekBar,
                                   TextView redToolTip,
                                   TextView greenToolTip,
                                   TextView blueToolTip,
                                   MusicThread musicThread
    ) {
        this.display = display;
        this.colorView = colorView;
        this.window = window;
        this.buttonSelector = buttonSelector;


        this.redSeekBar = redSeekBar;
        this.greenSeekBar = greenSeekBar;
        this.blueSeekBar = blueSeekBar;

        seekBarLeft = redSeekBar.getPaddingLeft();

        this.redToolTip = redToolTip;
        this.greenToolTip = greenToolTip;
        this.blueToolTip = blueToolTip;

        this.musicTimeThread = musicThread;

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.getId() == R.id.redSeekBar) {

            red = progress;
            thumbRect = seekBar.getThumb().getBounds();

            redToolTip.setX(seekBarLeft + thumbRect.left);

            if (progress < 10)
                redToolTip.setText("  " + red);
            else if (progress < 100)
                redToolTip.setText(" " + red);
            else
                redToolTip.setText(red + "");

        }
        else if (seekBar.getId() == R.id.greenSeekBar) {

            green = progress;
            thumbRect = seekBar.getThumb().getBounds();

            greenToolTip.setX(seekBar.getPaddingLeft() + thumbRect.left);
            if (progress<10)
                greenToolTip.setText("  " + green);
            else if (progress<100)
                greenToolTip.setText(" " + green);
            else
                greenToolTip.setText(green + "");

        }
        else if (seekBar.getId() == R.id.blueSeekBar) {

            blue = progress;
            thumbRect = seekBar.getThumb().getBounds();

            blueToolTip.setX(seekBarLeft + thumbRect.left);
            if (progress<10)
                blueToolTip.setText("  " + blue);
            else if (progress<100)
                blueToolTip.setText(" " + blue);
            else
                blueToolTip.setText(blue + "");

        }

        colorView.setBackgroundColor(Color.rgb(red, green, blue));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (display.getRotation() == Surface.ROTATION_0)
                window.setStatusBarColor(Color.rgb(red, green, blue));

        }

        if (red == 246 && green == 9 && blue == 19 )
        {
            if (!musicTimeThread.isAlive()) {
                musicTimeThread.start();
            }
        }

        //Setting the button hex color
        buttonSelector.setText(String.format("#%02x%02x%02x", red, green, blue));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
