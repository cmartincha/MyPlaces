
package es.cmartincha.myplaces.ui.principal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

import es.cmartincha.mislugares.R;
import es.cmartincha.myplaces.ui.principal.list.PlacesListFragment;
import es.cmartincha.myplaces.ui.principal.map.PlacesMapFragment;

public class PrincipalPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_ITEMS = 2;
    private static final int LIST_FRAGMENT_POSITION = 0;
    private static final int MAP_FRAGMENT_POSITION = 1;

    private PrincipalActivity mPrincipalActivity;
    private PrincipalActivityFragment[] mFragments;

    public PrincipalPagerAdapter(PrincipalActivity principalActivity,
                                 FragmentManager fm) {
        super(fm);

        mPrincipalActivity = principalActivity;
        setFragments();
    }

    private void setFragments() {
        mFragments = new PrincipalActivityFragment[NUM_ITEMS];

        mFragments[LIST_FRAGMENT_POSITION] = new PlacesListFragment();
        mFragments[MAP_FRAGMENT_POSITION] = new PlacesMapFragment();
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
}
