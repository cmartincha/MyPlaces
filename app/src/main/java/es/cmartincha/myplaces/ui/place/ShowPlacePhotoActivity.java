
package es.cmartincha.myplaces.ui.place;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.ScrollView;
import es.cmartincha.mislugares.R;
import es.cmartincha.myplaces.lib.Place;

public class ShowPlacePhotoActivity extends Activity {
    public static final String EXTRA_PLACE_INFO = "extra_place";

    private Place mPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_place_photo);

        setPlaceInfo();
        setUpViews();
    }

    private void setUpViews() {
        try {
            Bitmap photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(
                    mPlace.getPhotoUri()));
            PhotoImageView photoImageView = new PhotoImageView(this, photo);
            ScrollView scrollView = (ScrollView) findViewById(R.id.svScrollView);

            photoImageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            photoImageView.setScaleType(ScaleType.CENTER);

            scrollView.addView(photoImageView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setPlaceInfo() {
        Intent intent = getIntent();

        mPlace = Place.fromBundle(intent.getBundleExtra(EXTRA_PLACE_INFO));
    }

}
