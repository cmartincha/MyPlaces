
package es.cmartincha.myplaces.ui.place;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.cmartincha.mislugares.R;
import es.cmartincha.myplaces.lib.Place;
import es.cmartincha.myplaces.ui.dialog.DialogAdapter;
import es.cmartincha.myplaces.ui.dialog.DialogItem;
import es.cmartincha.myplaces.ui.principal.PrincipalActivity;

public class EditPlaceActivity extends ActionBarActivity implements OnClickListener,
        android.content.DialogInterface.OnClickListener {
    public static final String EXTRA_PLACE_INFO = "extra_place";
    public static final int DIALOG_CAMERA_OPTION = 0;
    public static final int DIALOG_GALLERY_OPTION = 1;
    public static final int DIALOG_NO_PHOTO_OPTION = 2;

    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_REQUEST = 1;

    private Button bSave;
    private Button bCancel;
    private EditText etPlaceName;
    private EditText etPlaceDescription;
    private ImageView ivPlacePhoto;

    private Place mPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_place);

        setPlaceInfo();
        setUpViews();
        fillPlaceInfo();
    }

    private void setUpViews() {
        bSave = (Button) findViewById(R.id.bEditPlaceSave);
        bCancel = (Button) findViewById(R.id.bEditPlaceCancel);
        etPlaceName = (EditText) findViewById(R.id.etEditPlaceName);
        etPlaceDescription = (EditText) findViewById(R.id.etEditPlaceDescription);
        ivPlacePhoto = (ImageView) findViewById(R.id.ivEditPlacePhoto);

        if (newPlace()) {
            bSave.setText(R.string.text_button_create);
        }

        bCancel.setOnClickListener(this);
        bSave.setOnClickListener(this);
        ivPlacePhoto.setOnClickListener(this);
    }

    private boolean newPlace() {
        return !mPlace.has_id();
    }

    private void setPlaceInfo() {
        mPlace = Place.fromBundle(getIntent().getBundleExtra(EXTRA_PLACE_INFO));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!newPlace()) {
            getMenuInflater().inflate(R.menu.edit_place, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_discard:
                showConfirmDeleteDialog();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showConfirmDeleteDialog() {
        final Context context = this;
        AlertDialog.Builder builder = new Builder(context);

        builder.setMessage(R.string.text_confirm_discard);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPlace.delete(context);

                        navigateToPrincipalActivity();
                    }

                    private void navigateToPrincipalActivity() {
                        Intent intent = new Intent(context, PrincipalActivity.class);

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

        builder.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    private void fillPlaceInfo() {
        if (mPlace.hasPhoto()) {
            ivPlacePhoto.setImageURI(Uri.parse(mPlace.getPhoto()));
        } else {
            ivPlacePhoto.setScaleType(ScaleType.CENTER_INSIDE);
        }

        etPlaceName.setText(mPlace.getName());
        etPlaceDescription.setText(mPlace.getDescription());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bEditPlaceCancel:
                goBack(Activity.RESULT_CANCELED);

                break;
            case R.id.bEditPlaceSave:
                if (!emptyName()) {
                    savePlace();
                    goBack(Activity.RESULT_OK);
                } else {
                    Toast.makeText(this, R.string.error_text_empty_name, Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.ivEditPlacePhoto:
                showPhotoSourcePickerDialog();

                break;
        }
    }

    private void showPhotoSourcePickerDialog() {
        Builder builder = new Builder(this);
        DialogItem[] dialogItems = {
                new DialogItem(R.string.text_camera, R.drawable.ic_action_camera),
                new DialogItem(R.string.text_gallery, R.drawable.ic_action_picture),
                new DialogItem(R.string.text_no_photo, R.drawable.ic_action_cancel)
        };
        DialogAdapter adapter = new DialogAdapter(this, dialogItems);

        builder.setTitle(R.string.text_title_image_chooser);
        builder.setAdapter(adapter, this);

        builder.create().show();
    }

    private Intent createResultActivityData() {
        Intent resultData = new Intent();

        resultData.putExtra(EXTRA_PLACE_INFO, mPlace.toBundle());
        return resultData;
    }

    private void savePlace() {
        mPlace.setName(etPlaceName.getText().toString());
        mPlace.setDescription(etPlaceDescription.getText().toString());

        if (mPlace.save(this) == -1) {
            Toast.makeText(this, R.string.error_place_save, Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(this, R.string.text_place_saved, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void goBack(int result) {
        setResult(result, createResultActivityData());

        finish();
    }

    private boolean emptyName() {
        return etPlaceName.getText().length() == 0;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DIALOG_CAMERA_OPTION:
                showCameraApp();
                break;
            case DIALOG_GALLERY_OPTION:
                showGalleryApp();
                break;
            case DIALOG_NO_PHOTO_OPTION:
                removePlacePhoto();
                break;
        }
    }

    private void removePlacePhoto() {
        mPlace.setPhoto(null);
        ivPlacePhoto.setScaleType(ScaleType.CENTER_INSIDE);
        ivPlacePhoto.setImageDrawable(getResources().getDrawable(R.drawable.default_place_background));
    }

    private void showGalleryApp() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, GALLERY_REQUEST);
    }

    private void showCameraApp() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File photoFile = createImageFile();

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            } catch (IOException ex) {
                Toast.makeText(this, R.string.error_text_take_photo, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mPlace.setPhoto(Uri.fromFile(image).toString());

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    ivPlacePhoto.setImageURI(Uri.parse(mPlace.getPhoto()));
                    ivPlacePhoto.setScaleType(ScaleType.CENTER_CROP);
                    break;
                case GALLERY_REQUEST:
                    ivPlacePhoto.setImageURI(data.getData());
                    ivPlacePhoto.setScaleType(ScaleType.CENTER_CROP);
                    mPlace.setPhoto(data.getDataString());
                    break;

            }
        } else {
            Toast.makeText(this, R.string.error_text_get_photo, Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
