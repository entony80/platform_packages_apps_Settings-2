package com.android.settings.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Display;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.settings.R;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class DeveloperPreference extends Preference {

    private static final String TAG = "DeveloperPreference";
    public static final String GRAVATAR_API = "http://www.gravatar.com/avatar/";
    public static int mDefaultAvatarSize = 250;
    private ImageView gplusButton;
    private ImageView githubButton;
    private ImageView photoView;

    private TextView devName;

    private String nameDev;
    private String gplusName;
    private String githubLink;
    private String devEmail;
    private final Display mDisplay;

    public DeveloperPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.DeveloperPreference);
            nameDev = typedArray.getString(R.styleable.DeveloperPreference_nameDev);
            gplusName = typedArray.getString(R.styleable.DeveloperPreference_gplusHandle);
	    githubLink = typedArray.getString(R.styleable.DeveloperPreference_githubLink);
            devEmail = typedArray.getString(R.styleable.DeveloperPreference_emailDev);
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mDisplay = wm.getDefaultDisplay();
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);

        View layout = View.inflate(getContext(), R.layout.dev_card, null);

        gplusButton = (ImageView) layout.findViewById(R.id.gplus_button);
	githubButton = (ImageView) layout.findViewById(R.id.github_button);
        devName = (TextView) layout.findViewById(R.id.name);
        photoView = (ImageView) layout.findViewById(R.id.photo);

        return layout;
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        if (githubLink != null) {
            final OnClickListener openGithub = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri githubURL = Uri.parse(githubLink);
                    final Intent intent = new Intent(Intent.ACTION_VIEW, githubURL);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getContext().startActivity(intent);
                }
            };
            githubButton.setOnClickListener(openGithub);
        } else {
            githubButton.setVisibility(View.GONE);
        }


            final OnPreferenceClickListener openGplus = new OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

			if (gplusName != null) {

	                    Uri gplusURL = Uri.parse("https://plus.google.com/+" + gplusName);
        	            final Intent intent = new Intent(Intent.ACTION_VIEW, gplusURL);
        	            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	            getContext().startActivity(intent);

			}
        
	            return true;
                }
            };

            this.setOnPreferenceClickListener(openGplus);
            UrlImageViewHelper.setUrlDrawable(this.photoView,
                    getGravatarUrl(devEmail),
                    R.drawable.ic_null,
                    UrlImageViewHelper.CACHE_DURATION_ONE_WEEK);

        if (gplusName == null)
		gplusButton.setVisibility(View.INVISIBLE);


        devName.setText(nameDev);

    }

    public String getGravatarUrl(String email) {
        try {
            Point point = new Point();
            mDisplay.getSize(point);
            mDefaultAvatarSize = point.x;
            String emailMd5 = getMd5(email.trim().toLowerCase());
            return String.format("%s%s?s=%d",
                    GRAVATAR_API,
                    emailMd5,
                    mDefaultAvatarSize);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private String getMd5(String devEmail) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(devEmail.getBytes());
        byte byteData[] = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++)
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        return sb.toString();
    }
} 
