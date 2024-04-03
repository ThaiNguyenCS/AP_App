package custom_textview;

import android.content.Context;
import android.graphics.Typeface;

public class Utils {

    private static Typeface alexBrushTypeface;
    private static Typeface greatVibesTypeface;
    private static Typeface PacificoTypeface;

    public static Typeface getAlexBrushTypeface(Context context) {
        if (alexBrushTypeface == null) {
            alexBrushTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/AlexBrush-Regular.ttf");
        }
        return alexBrushTypeface;
    }

    public static Typeface getGreatVibesTypeface(Context context) {
        if (greatVibesTypeface == null) {
            greatVibesTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/GreatVibes-Regular.otf");
        }
        return greatVibesTypeface;
    }

    public static Typeface getPacificoTypeface(Context context) {
        if (PacificoTypeface == null) {
            PacificoTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Pacifico.ttf");
        }
        return PacificoTypeface;
    }
}
