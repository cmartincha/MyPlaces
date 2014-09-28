
package es.cmartincha.myplaces.ui.place;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.io.FileNotFoundException;

import es.cmartincha.mislugares.R;
import es.cmartincha.myplaces.lib.Place;
import es.cmartincha.myplaces.ui.view.ZoomableImageView;

public class ShowPlacePhotoActivity extends Activity {
    private static final String EXTRA_PLACE_INFO = "extra_place";

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
            ZoomableImageView zoomableImageView = (ZoomableImageView) findViewById(R.id.zivPlacePhoto);

            zoomableImageView.setImage(photo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setPlaceInfo() {
        Intent intent = getIntent();

        mPlace = Place.fromBundle(intent.getBundleExtra(EXTRA_PLACE_INFO));
    }

}
