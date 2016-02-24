package project.yeoeun.bdc.flipanimation;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

public class ApplyRotation {
    private ImageView image1;
    private ImageView image2;
    private boolean isFirstImage;

    public static final int ROTATION_FROM_FRONT = 0;
    public static final int ROTATION_FROM_BACK = 1;

    public ApplyRotation(ImageView image1, ImageView image2) {
        this.image1 = image1;
        this.image2 = image2;
        this.isFirstImage = true;
    }

    public void apply(int type) {
        if (type == ROTATION_FROM_FRONT) {
            apply(0, 90);
            isFirstImage = false;
        } else {
            apply(0, -90);
            isFirstImage = true;
        }
    }

    private void apply(float start, float end) {
        // Find the center of image
        final float centerX = image1.getWidth() / 2.0f;
        final float centerY = image1.getHeight() / 2.0f;

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final Flip3dAnimation rotation =
                new Flip3dAnimation(start, end, centerX, centerY);
        rotation.setDuration(300);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView(isFirstImage, image1, image2));

        if (isFirstImage) {
            image1.startAnimation(rotation);
        } else {
            image2.startAnimation(rotation);
        }
    }

    public boolean getIsFirstImage() {
        return this.isFirstImage;
    }
}
