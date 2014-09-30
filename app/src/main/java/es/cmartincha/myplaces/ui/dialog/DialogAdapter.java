
package es.cmartincha.myplaces.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import es.cmartincha.mislugares.R;

/**
 * Adaptador para mostrar un dialogo con un listado de opciones (ej. camara, galeria, ...)
 */
public class DialogAdapter extends ArrayAdapter<DialogItem> {
    /**
     * Array opciones
     */
    private DialogItem[] mDialogItems;

    public DialogAdapter(Context context, DialogItem[] dialogItems) {
        super(context, R.layout.dialog_list_item_source_photo);

        mDialogItems = dialogItems;

        fillDialogOptionsItems();
    }

    /**
     * Metodo para añadir las posibles opciones
     */
    private void fillDialogOptionsItems() {
        //No quiero que se notifiquen los cambios puesto que estoy inicializando las opciones
        setNotifyOnChange(false);

        for (DialogItem imageAppsItem : mDialogItems) {
            add(imageAppsItem);
        }

        setNotifyOnChange(true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DialogItem item = getItem(position);
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
