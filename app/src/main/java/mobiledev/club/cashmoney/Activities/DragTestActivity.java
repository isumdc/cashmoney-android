package mobiledev.club.cashmoney.Activities;

import android.content.ClipData;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import mobiledev.club.cashmoney.R;

public class DragTestActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_test);

        ImageView image1 = (ImageView)findViewById(R.id.test_imageview_1);
        image1.setOnTouchListener(new MyTouchListener());

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativelayout);
        relativeLayout.setOnDragListener(new MyDragListener());
/*
        ViewAnimator animator = new ViewAnimator(this);
        animator.setTranslationY(1000);

        Animation animation =  AnimationUtils.loadAnimation(this, android.R.anim.linear_interpolator);
        animation.setDuration(1000);

        image1.setAnimation(animation);
*/
        image1.animate().translationY(1000).setDuration(10000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drag_test, menu);
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


    // This defines your touch listener
    private final class MyTouchListener implements View.OnTouchListener {
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

    private class MyDragListener implements View.OnDragListener {
//        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
//        Drawable normalShape = getResources().getDrawable(R.drawable.shape);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            View image = (View) event.getLocalState();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    image.clearAnimation();
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    image.clearAnimation();
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    //v.setBackgroundDrawable(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    ViewGroup owner = (ViewGroup) image.getParent();
                    owner.removeView(image);
                    RelativeLayout container = (RelativeLayout) v;
                    container.addView(image);
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
}
