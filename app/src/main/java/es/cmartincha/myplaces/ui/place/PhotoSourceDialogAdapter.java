
package es.cmartincha.myplaces.ui.place;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import es.cmartincha.mislugares.R;

public class PhotoSourceDialogAdapter extends ArrayAdapter<PhotoSourceDialogItem> {
    private static final PhotoSourceDialogItem[] photoSourceItems = {
            new PhotoSourceDialogItem(R.string.text_camera, R.drawable.ic_action_camera),
            new PhotoSourceDialogItem(R.string.text_gallery, R.drawable.ic_action_picture)
    };

    public PhotoSourceDialogAdapter(Context context) {
        super(context, R.layout.dialog_list_item_source_photo);

        fillDialogOptionsItems();
    }

    private void fillDialogOptionsItems() {
        setNotifyOnChange(false);

        for (PhotoSourceDialogItem imageAppsItem : photoSourceItems) {
            add(imageAppsItem);
        }

        setNotifyOnChange(true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoSourceDialogItem item = getItem(position);
        PhotoSourceDialogItemHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.dialog_list_item_source_photo, parent, false);
            holder = new PhotoSourceDialogItemHolder();

            holder.tvAppImageName = (TextView) convertView.findViewById(R.id.tvSourcePhotoName);

            convertView.setTag(holder);
        } else {
            holder = (PhotoSourceDialogItemHolder) convertView.getTag();
        }

        holder.tvAppImageName.setText(getContext().getResources().getString(item.mTextResource));
        holder.tvAppImageName.setCompoundDrawablesWithIntrinsicBounds(
                getContext().getResources().getDrawable(item.mIconResource), null, null, null);

        return convertView;
    }

    private static class PhotoSourceDialogItemHolder {
        TextView tvAppImageName;
    }
}
