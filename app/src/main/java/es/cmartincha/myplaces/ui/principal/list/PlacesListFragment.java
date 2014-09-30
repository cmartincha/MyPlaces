
package es.cmartincha.myplaces.ui.principal.list;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import es.cmartincha.mislugares.R;
import es.cmartincha.myplaces.lib.Place;
import es.cmartincha.myplaces.ui.dialog.DialogAdapter;
import es.cmartincha.myplaces.ui.dialog.DialogItem;
import es.cmartincha.myplaces.ui.place.EditPlaceActivity;
import es.cmartincha.myplaces.ui.place.ShowPlaceActivity;
import es.cmartincha.myplaces.ui.principal.PrincipalActivityFragment;

/**
 * Fragment con el listado de lugares
 */
public class PlacesListFragment extends ListFragment implements PrincipalActivityFragment, AdapterView.OnItemLongClickListener, DialogInterface.OnClickListener {
    private static final String EXTRA_PLACE_INFO = "extra_place";
    private static final int DIALOG_EDIT_OPTION = 0;
    private static final int DIALOG_DELETE_OPTION = 1;
    private CursorAdapter mCursorAdapter;
    private Place mPlaceListLongClick;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Evita que se destruya el fragment al volver a crear la actividad
        setRetainInstance(true);

        return inflater.inflate(R.layout.fragment_places_list,
                container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCursorAdapter = new PlacesListAdapter(getActivity());
        setListAdapter(mCursorAdapter);

        getListView().setLongClickable(true);
        getListView().setOnItemLongClickListener(this);
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

        navigateToShowPlaceActivity(view);
    }

    private void navigateToShowPlaceActivity(View view) {
        Intent intent = new Intent(getActivity(), ShowPlaceActivity.class);
        Place lugar = (Place) view.getTag(R.id.tag_lugar);

        intent.putExtra(ShowPlaceActivity.EXTRA_PLACE_INFO, lugar.toBundle());

        startActivity(intent);
    }

    @Override
    public void notififyDataChanged(Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    /**
     * Si mantengo pulsado en un elemento de la lista aparece un dialogo con opciones (editar y eliminar)
     */
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        DialogItem[] dialogItems = {
                new DialogItem(R.string.text_edit, R.drawable.ic_action_edit_gray),
                new DialogItem(R.string.text_delete, R.drawable.ic_action_discard_gray)
        };
        DialogAdapter adapter = new DialogAdapter(getActivity(), dialogItems);

        builder.setTitle(R.string.text_title_image_chooser);
        builder.setAdapter(adapter, this);

        builder.create().show();

        mPlaceListLongClick = (Place) view.getTag(R.id.tag_lugar);

        return true;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        switch (which) {
            case DIALOG_EDIT_OPTION:
                navigateToEditActivity();
                break;
            case DIALOG_DELETE_OPTION:
                showConfirmDeleteDialog();
                break;
        }
    }

    private void showConfirmDeleteDialog() {
        final Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(R.string.text_confirm_discard);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPlaceListLongClick.delete(context);
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

    private void navigateToEditActivity() {
        Intent intent = new Intent(getActivity(), EditPlaceActivity.class);

        intent.putExtra(EXTRA_PLACE_INFO, mPlaceListLongClick.toBundle());

        startActivity(intent);
    }
}
