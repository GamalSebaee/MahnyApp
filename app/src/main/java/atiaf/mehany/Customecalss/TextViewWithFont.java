package atiaf.mehany.Customecalss;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by TuiyTuy on 16/02/2015.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class TextViewWithFont extends TextView {
    public static Typeface tf;

    public TextViewWithFont(Context context) {
        super(context);
        init();
    }

    public TextViewWithFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewWithFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public TextViewWithFont(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            if (Locale.getDefault().getLanguage().equals("ar")) {
                tf = Typeface.createFromAsset(getResources().getAssets(), "fonts/Cocon.otf");
            }else {
                tf = Typeface.createFromAsset(getResources().getAssets(), "fonts/Cocon.otf");
            }
            setTypeface(tf);
        }
    }



}
