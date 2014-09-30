
package es.cmartincha.myplaces.ui.principal;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;

import es.cmartincha.mislugares.R;
import es.cmartincha.myplaces.db.PlacesDB;
import es.cmartincha.myplaces.db.PlacesProvider;

public class PrincipalActivity extends ActionBarActivity implements
        ActionBar.TabListener, OnPageChangeListener, LoaderCallbacks<Cursor> {

    private PrincipalPagerAdapter mSectionsPagerAdapter;
    private ViewPager vpMyPlaces;
    private ActionBar mActionBar;

    private Searcher mSearcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        setUpActionBar();
        setUpViewPagerAndTabs();

        getSupportLoaderManager().initLoader(0, null, this);
    }

    private void setUpActionBar() {
        mActionBar = getSupportActionBar();
        //Configuro el actionBar para que muestre pestañas
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }

    @Override
    /**
     * Este metodo se llama cuando se va a realizar una busqueda
     */
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (searchIntent(intent)) {
            performSearch(intent);
        }
    }

    private void performSearch(Intent intent) {
        Bundle extra = new Bundle();
        String query = intent.getStringExtra(SearchManager.QUERY);

        extra.putString(SearchManager.QUERY, query);
        //Reinicio el loader con el texto de la busqueda de la busqueda
        getSupportLoaderManager().restartLoader(0, extra, this);
    }

    private boolean searchIntent(Intent intent) {
        return Intent.ACTION_SEARCH.equals(intent.getAction());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.principal, menu);

        //Configura el buscador del actionBar
        mSearcher = new Searcher(this);

        mSearcher.setUp(menu);

        return true;
    }

    private void setUpViewPagerAndTabs() {
        setUpViewPager();
        addTabs();
    }

    /**
     * Añade las pestañas al actionBar
     */
    private void addTabs() {
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            mActionBar.addTab(mActionBar.newTab()
                    .setText(mSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }
    }

    /**
     * Configura el deslizador horizontal para moverse entre las pestañas
     */
    private void setUpViewPager() {
        mSectionsPagerAdapter = new PrincipalPagerAdapter(this,
                null,
                getSupportFragmentManager());
        vpMyPlaces = (ViewPager) findViewById(R.id.vpMisLugares);

        vpMyPlaces.setAdapter(mSectionsPagerAdapter);
        vpMyPlaces.requestTransparentRegion(vpMyPlaces);
        vpMyPlaces.setOnPageChangeListener(this);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab,
                              FragmentTransaction fragmentTransaction) {
        vpMyPlaces.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    /**
     * Cuando te deslizas a una pagina actualiza la pestaña activa
     */
    public void onPageSelected(int position) {
        mActionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        notifyDataChangeToFragments(data);
    }

    /**
     * Notifica un cambio en los lugares a todas las paginas para que se refresquen con la informacion actualizada
     *
     * @param data
     */
    private void notifyDataChangeToFragments(Cursor data) {
        mSectionsPagerAdapter.setData(data);
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        notifyDataChangeToFragments(null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle extra) {
        Uri baseUri = Uri.parse("content://" + PlacesProvider.AUTHORITY + "/"
                + PlacesDB.PlaceTable.TABLE_NAME);
        String selection = null;
        if (extra != null) { //Se esta realizando una busqueda
            String query = extra.getString(SearchManager.QUERY);

            if (query != null) {
                selection = PlacesDB.PlaceTable.COLUMN_NAME + " LIKE '" + query + "%'";
            }
        }

        return new CursorLoader(this, baseUri, null, selection, null,
                PlacesDB.PlaceTable.COLUMN_NAME
                        + " ASC");
    }
}
