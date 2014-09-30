
package es.cmartincha.myplaces.ui.principal;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Locale;

import es.cmartincha.mislugares.R;
import es.cmartincha.myplaces.ui.principal.list.PlacesListFragment;
import es.cmartincha.myplaces.ui.principal.map.PlacesMapFragment;

/**
 * Clase adaptador para el deslizador horizontal de la actividad principal
 */
public class PrincipalPagerAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_ITEMS = 2;
    private static final int LIST_FRAGMENT_POSITION = 0;
    private static final int MAP_FRAGMENT_POSITION = 1;

    private PrincipalActivity mPrincipalActivity;
    private PrincipalActivityFragment[] mFragments;

    /**
     * Informacion de los lugares
     */
    private Cursor mData;

    public PrincipalPagerAdapter(PrincipalActivity principalActivity, Cursor data, FragmentManager fm) {
        super(fm);

        mData = data;
        mPrincipalActivity = principalActivity;
        setFragments();
    }

    private void setFragments() {
        mFragments = new PrincipalActivityFragment[NUM_ITEMS];

        //Listado de lugares
        mFragments[LIST_FRAGMENT_POSITION] = new PlacesListFragment();
        //Mapa de lugares
        mFragments[MAP_FRAGMENT_POSITION] = new PlacesMapFragment();
    }

    public void setData(Cursor data) {
        mData = data;
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) mFragments[position];
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();


        switch (position) {
            case LIST_FRAGMENT_POSITION:
                return mPrincipalActivity.getString(R.string.title_section_lista)
                        .toUpperCase(l);
            case MAP_FRAGMENT_POSITION:
                return mPrincipalActivity.getString(R.string.title_section_mapa)
                        .toUpperCase(l);
        }

        return null;
    }

    @Override
    /**
     * Este metodo se llama cada vez que hay que pintar (como cuando hay que actualizar la informacion)
     */
    public int getItemPosition(Object object) {
        PrincipalActivityFragment fragment = (PrincipalActivityFragment) object;

        //Notifico la informacion a los fragments para que se pinten adecuadamente
        fragment.notififyDataChanged(mData);

        return super.getItemPosition(object);
    }
}
