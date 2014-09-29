
package es.cmartincha.myplaces.ui.principal.list;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import es.cmartincha.mislugares.R;
import es.cmartincha.myplaces.lib.Place;

public class PlacesListAdapter extends CursorAdapter {
    private Context mContext;

    public PlacesListAdapter(Context context) {
        super(context, null, 0);

        mContext = context;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = getViewHolder(view);
        Place place = Place.fromCursor(cursor);

        setUpListItemViews(view, viewHolder, place);
    }

    private void setUpListItemViews(View view, ViewHolder viewHolder, Place place) {
        viewHolder.tvPlaceName.setText(place.getName());
        viewHolder.tvPlaceDescription.setText(place.getDescription());

        setUpPhoto(viewHolder, place);

        view.setTag(R.id.tag_lugar, place);
    }

    private void setUpPhoto(ViewHolder viewHolder, Place place) {
        if (place.hasPhoto()) {
            ImageLoader imageLoader = new ImageLoader(place.getPhotoUri(),
                    viewHolder.ivPlacePhoto, mContext);

            imageLoader.loadBitmap();
        }
    }

    private ViewHolder getViewHolder(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.tag_holder);

        if (viewHolder == null) {
            viewHolder = new ViewHolder();

            viewHolder.ivPlacePhoto = (ImageView) view
                    .findViewById(R.id.ivListPlacePhoto);
            viewHolder.tvPlaceName = (TextView) view
                    .findViewById(R.id.tvListPlaceName);
            viewHolder.tvPlaceDescription = (TextView) view
                    .findViewById(R.id.tvListPlaceDescription);

            view.setTag(R.id.tag_holder, viewHolder);
        }

        return viewHolder;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return inflater.inflate(R.layout.list_item_place, viewGroup, false);
    }

    static class ViewHolder {
        ImageView ivPlacePhoto;
        TextView tvPlaceName;
        TextView tvPlaceDescription;
    }
}
