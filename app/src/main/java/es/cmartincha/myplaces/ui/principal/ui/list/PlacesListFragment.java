
package es.cmartincha.myplaces.ui.principal.ui.list;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import es.cmartincha.mislugares.R;
import es.cmartincha.myplaces.lib.Place;
import es.cmartincha.myplaces.ui.place.ShowPlaceActivity;
import es.cmartincha.myplaces.ui.principal.PrincipalActivityFragment;

public class PlacesListFragment extends ListFragment implements PrincipalActivityFragment {
    private CursorAdapter mCursorAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_places_list,
                container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCursorAdapter = new PlacesListAdapter(getActivity());

        setListAdapter(mCursorAdapter);
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
    public void onDataChanged(Cursor data) {
        mCursorAdapter.swapCursor(data);
    }
}
