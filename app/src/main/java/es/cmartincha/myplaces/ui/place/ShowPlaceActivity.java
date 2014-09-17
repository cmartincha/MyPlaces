
package es.cmartincha.myplaces.ui.place;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import es.cmartincha.mislugares.R;
import es.cmartincha.myplaces.lib.Place;

public class ShowPlaceActivity extends ActionBarActivity implements OnClickListener {
    public static final String EXTRA_PLACE_INFO = "extra_place";
    public static final int EDIT_PLACE_REQUEST = 1;

    private ImageView ivPlacePhoto;
    private TextView tvPlaceName;
    private TextView tvPlaceDescription;
    private Place mPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_place);

        setPlaceInfo(getIntent().getBundleExtra(EXTRA_PLACE_INFO));
        setUpViews();
        fillPlaceInfo();
    }

    private void setPlaceInfo(Bundle bundle) {
        mPlace = Place.fromBundle(bundle);
    }

    private void setUpViews() {
        ivPlacePhoto = (ImageView) findViewById(R.id.ivShowPlacePhoto);
        tvPlaceName = (TextView) findViewById(R.id.tvShowPlaceName);
        tvPlaceDescription = (TextView) findViewById(R.id.tvShowPlaceDescription);

        ivPlacePhoto.setOnClickListener(this);
    }

    private void fillPlaceInfo() {
        if (mPlace.hasPhoto()) {
            ivPlacePhoto.setScaleType(ScaleType.CENTER_CROP);
            ivPlacePhoto.setImageURI(Uri.parse(mPlace.getPhoto()));
        } else {
            ivPlacePhoto.setScaleType(ScaleType.CENTER_INSIDE);
        }

        tvPlaceName.setText(mPlace.getName());
        tvPlaceDescription.setText(mPlace.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_place, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                navigateToEditPlaceActivity();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void navigateToEditPlaceActivity() {
        Intent intent = new Intent(this, EditPlaceActivity.class);

        intent.putExtra(EditPlaceActivity.EXTRA_PLACE_INFO, mPlace.toBundle());

        startActivityForResult(intent, EDIT_PLACE_REQUEST);
    }

    @Override
    protected void onActivityResult(int request, int resultCode, Intent data) {
        super.onActivityResult(request, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (request == EDIT_PLACE_REQUEST) {
                setPlaceInfo(data.getBundleExtra(EXTRA_PLACE_INFO));

                fillPlaceInfo();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivShowPlacePhoto) {
            if (mPlace.hasPhoto()) {
                navigateToShowPhotoPlaceActivity();
            }
        }
    }

    private void navigateToShowPhotoPlaceActivity() {
        Intent intent = new Intent(this, ShowPlacePhotoActivity.class);

        intent.putExtra(EXTRA_PLACE_INFO, mPlace.toBundle());

        startActivity(intent);
    }
}
