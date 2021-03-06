package net.dicesoft.colorpicker_android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import net.dicesoft.colorpicker_android.Listeners.SeekBarOnChangeListener;


public class MainColorPickerActivity extends Activity {

    View colorView;
    SeekBar redSeekBar, greenSeekBar, blueSeekBar;
    TextView redToolTip, greenToolTip, blueToolTip;
    Button buttonSelector;
    Window window;
    Display display;
    MusicThread musicThread;
    int red, green, blue, seekBarLeft;

    Intent anotherAppIntent;

    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setContentView(R.layout.layout_color_picker);
        } else {
            setContentView(R.layout.layout_16);
        }


        display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        SharedPreferences settings = getSharedPreferences("COLOR_SETTINGS", 0);
        red = settings.getInt("RED_COLOR", 0);
        green = settings.getInt("GREEN_COLOR", 0);
        blue = settings.getInt("BLUE_COLOR", 0);

        colorView = findViewById(R.id.colorView);
        window = getWindow();

        redSeekBar = (SeekBar)findViewById(R.id.redSeekBar);
        greenSeekBar = (SeekBar)findViewById(R.id.greenSeekBar);
        blueSeekBar = (SeekBar)findViewById(R.id.blueSeekBar);

        seekBarLeft = redSeekBar.getPaddingLeft();

        redToolTip = (TextView)findViewById(R.id.redToolTip);
        greenToolTip = (TextView)findViewById(R.id.greenToolTip);
        blueToolTip = (TextView)findViewById(R.id.blueToolTip);

        buttonSelector = (Button)findViewById(R.id.buttonSelector);


        musicThread = new MusicThread(this);
        SeekBarOnChangeListener seekBarOnChangeListener = new SeekBarOnChangeListener(display,
                colorView,
                window,
                buttonSelector,
                redSeekBar,
                greenSeekBar,
                blueSeekBar,
                redToolTip,
                greenToolTip,
                blueToolTip,
                musicThread);
        redSeekBar.setOnSeekBarChangeListener(seekBarOnChangeListener);
        greenSeekBar.setOnSeekBarChangeListener(seekBarOnChangeListener);
        blueSeekBar.setOnSeekBarChangeListener(seekBarOnChangeListener);

        redSeekBar.setProgress(red);
        greenSeekBar.setProgress(green);
        blueSeekBar.setProgress(blue);


        anotherAppIntent = getIntent();
        if (anotherAppIntent == null) {
            buttonSelector.setEnabled(false);
            buttonSelector.setText("Button disabled!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_color_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void colorSelect(View view) {


        Toast.makeText(this, "Color " + buttonSelector.getText() + " copied to the color blender app! ", Toast.LENGTH_SHORT).show();


        Intent resultColorIntent = new Intent();
        resultColorIntent.putExtra("red", redSeekBar.getProgress());
        resultColorIntent.putExtra("green", greenSeekBar.getProgress());
        resultColorIntent.putExtra("blue", blueSeekBar.getProgress());

        setResult(RESULT_OK, resultColorIntent);
        finish();

    }

    public void showDetails(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            View dialogLayout = View.inflate(this, R.layout.dialog, null);

            alertDialog = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.app_name))

                    .setView(dialogLayout)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();

            alertDialog.findViewById(R.id.website).setOnClickListener(new View.OnClickListener(){
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse("https://dicesoft.net")));
                }
            });

            alertDialog.findViewById(R.id.github).setOnClickListener(new View.OnClickListener(){
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse("https://github.com/redragonx")));
                }
            });

        }
        else {

            MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(this);
            View dialogLayout = View.inflate(this, R.layout.dialog_16, null);


            materialDialog.title(R.string.app_name);
            materialDialog.customView(dialogLayout, false);
            materialDialog.positiveText("OK");
            materialDialog.show();

            dialogLayout.findViewById(R.id.website).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse("https://dicesoft.net")));
                }
            });

            dialogLayout.findViewById(R.id.github).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse("https://github.com/redragonx")));
                }
            });
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        musicThread.interrupt();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        //Storing values of red, green & blue in SharedPreferences
        SharedPreferences settings = getSharedPreferences("COLOR_SETTINGS", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("RED_COLOR", redSeekBar.getProgress());
        editor.putInt("GREEN_COLOR", greenSeekBar.getProgress());
        editor.putInt("BLUE_COLOR", blueSeekBar.getProgress());
        editor.apply();

        //Properly dismissing dialog to prevent errors while changing orientation
        try {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
        catch (NullPointerException e) {
            //do nothing
        }

        musicThread.interrupt();

    }
}
