package com.hlinero.featureDiscovery;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Display;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();
    private TapTargetSequence tapTargetSequence;
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the toolbar
        initToolbar();

        // Initialize the target sequence
        initTapTargetSequence();

        // Initialize the target view
        initTapTargetView();
    }

    private void initToolbar(){
        // Initializing the toolbar
        toolbar = findViewById(R.id.toolbar);

        // Inflating the toolbar menu
        toolbar.inflateMenu(R.menu.menu_main);

        // Setting the back arrow in the toolbar
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp));
    }

    private void initTapTargetSequence(){
        // Creating a target sequence
        tapTargetSequence = new TapTargetSequence(this)
                .targets(

                        // Target #1: Back Arrow --> We need to pass the toolbar
                        TapTarget
                                .forToolbarNavigationIcon(toolbar, "This is the back button", "It allows you to go back")
                                .cancelable(false),

                        // Target #2: Search Menu Item --> We have to pass the toolbar and the id of the menu item to target.
                        TapTarget
                                .forToolbarMenuItem(toolbar, R.id.search, "This is a search icon", "It let's you search for things")
                                .cancelable(false),

                        // Target #3: Overflow Menu Item --> We have to pass the toolbar and the id of the menu item to target.
                        TapTarget
                                .forToolbarOverflow(toolbar, "This shows you more options", "These are not that important")
                                .cancelable(false)
                )
                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {

                        // Notify the user the feature discovery sequence is done
                        Toast.makeText(MainActivity.this,"Congrats! You're all set.",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                        // Logging the id of the target clicked
                        Log.d(TAG, "Clicked on " + lastTarget.id());
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {

                        // Notify the user he has canceled the sequence
                        // This only happens if .cancelable(false) is not set
                        Toast.makeText(MainActivity.this,"You canceled the sequence :(",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initTapTargetView(){
        // Instead of a sequence, we ca have a single time tap target
        TapTargetView
                .showFor(this, TapTarget.forView(findViewById(R.id.fab), "Hi there!", "This is a tutorial for feature discovery. ")
                        .cancelable(false)
                        .tintTarget(false), new TapTargetView.Listener() {
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);

                        // Start the sequence of item
                        tapTargetSequence.start();
                    }

                    @Override
                    public void onOuterCircleClick(TapTargetView view) {
                        super.onOuterCircleClick(view);

                        // Listening to clicks in the outer circle
                        Toast.makeText(view.getContext(), "This is the outer circle", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                        Log.d(TAG, "You dismissed me");
                    }
                });
    }
}
