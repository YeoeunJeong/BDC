package project.yeoeun.bdc.util;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

public class FontHelper extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "aBlackM.otf"))
                .addBold(Typekit.createFromAsset(this, "aBlackB.otf"));
    }
}
