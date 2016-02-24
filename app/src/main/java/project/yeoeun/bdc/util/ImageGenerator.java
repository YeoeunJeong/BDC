package project.yeoeun.bdc.util;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import project.yeoeun.bdc.R;

public class ImageGenerator {
    private static ImageGenerator instance;

    public ImageGenerator() {
    }

    public static ImageGenerator getInstance() {
        if (instance == null) {
            instance = new ImageGenerator();
        }
        return instance;
    }

    public void createImageService(String url, ImageView view) {
        Picasso.with(view.getContext())
                .load(url)
                .placeholder(R.drawable.game_card_apple)
                .error(R.drawable.game_card_apple)
                .fit().centerCrop()
                .into(view);
    }
}
