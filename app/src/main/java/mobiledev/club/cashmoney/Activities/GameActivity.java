package mobiledev.club.cashmoney.Activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.transition.Explode;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;

import mobiledev.club.cashmoney.Models.GameData;
import mobiledev.club.cashmoney.R;


public class GameActivity extends Activity {

    HashMap<String, AnimationRunnable> map;

    private GameData gameData;
    private Handler uiThreadHandler;
    private LooperThread loopThread;
    private RelativeLayout layout;

    private int translationDuration = 10000;

    private int layoutWidth, layoutHeight;

    private ImageView piggyBankLeft, piggyBankRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);
        layout = (RelativeLayout) findViewById(R.id.gameLayout);
        layout.setOnDragListener(new GameLayoutDragListener());

        map = new HashMap<String, AnimationRunnable>();

        //Relative Layout width is 0 when onCreate is called:
        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            //So get the width when it is set up:
            @Override
            public void onGlobalLayout()
            {
                // gets called after layout has been done but before display.
                layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                layoutHeight = layout.getHeight();
                layoutWidth = layout.getWidth();
               // Toast.makeText(GameActivity.this, layoutWidth + " | " + leftImageBound + " | " + rightImageBound, Toast.LENGTH_LONG).show();
            }
        });

        piggyBankLeft = (ImageView) findViewById(R.id.piggyBankLeft);
        piggyBankRight = (ImageView) findViewById(R.id.piggyBankRight);
        piggyBankLeft.setOnDragListener(new PiggyBankDragListener());
        piggyBankRight.setOnDragListener(new PiggyBankDragListener());

        gameData = new GameData();
        uiThreadHandler = new Handler();
        loopThread = new LooperThread();
        loopThread.start();

    }

    public void start(View view){
        ImageView image = new ImageView(this);
        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
        image.setOnTouchListener(new ImageTouchListener());
        Random rand = new Random();
        int width = rand.nextInt(layoutWidth - 40); // ? need some sort of boundary on the right so full image fits

        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.leftMargin = width;

        layout.addView(image, params);

        AnimationRunnable newImage = new AnimationRunnable(image);
        String imageTag = ""+ translationDuration;
        image.setTag(imageTag);
        map.put(imageTag, newImage);
        loopThread.mHandler.post(newImage);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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

    private class PiggyBankDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            View image = (View) event.getLocalState();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    ViewGroup owner = (ViewGroup) image.getParent();
                    owner.removeView(image);
                    map.get(image.getTag()).anim.cancel();
                    Toast.makeText(GameActivity.this, "Caught", Toast.LENGTH_SHORT).show();
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    // v.setBackgroundDrawable(normalShape);
                default:
                    break;
            }
            return true;
        }
    }

    private class GameLayoutDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            View image = (View) event.getLocalState();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    image.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    // v.setBackgroundDrawable(normalShape);
                default:
                    break;
            }
            return true;
        }
    }

    private final class ImageTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }


    class AnimationRunnable implements Runnable {
        private ImageView img;
        ObjectAnimator anim;
        private boolean caught;

        public AnimationRunnable(ImageView image){
            img = image;
        }
        @Override
        public void run() {

            anim = ObjectAnimator.ofFloat(img, "translationY", 0, layoutHeight - img.getHeight());
            translationDuration -= 5;
            anim.setDuration(translationDuration);
            anim.start();
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if(!caught)
                        Toast.makeText(GameActivity.this, "You lost!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    caught = true;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }

    class LooperThread extends Thread {
        public Handler mHandler;
        @Override
        public void run() {
            Looper.prepare();
            mHandler = uiThreadHandler;
            Looper.loop();
        }
    }

}
