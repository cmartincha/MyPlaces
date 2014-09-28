
package es.cmartincha.myplaces.ui.principal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnFocusChangeListener;

import es.cmartincha.mislugares.R;

public class Searcher {
    private Activity mActivity;

    public Searcher(Activity activity) {
        mActivity = activity;
    }

    public void setUp(Menu menu) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setUpSearchWidget(menu);
        } else {
            setUpSearchDialog(menu);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setUpSearchWidget(Menu menu) {
        SearchManager searchManager = (SearchManager) mActivity
                .getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(mActivity.getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    searchView.setQuery("%", true);
                    searchView.setQuery("", false);
                }
            }
        });
    }

    private void setUpSearchDialog(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mActivity.onSearchRequested();

                return true;
            }
        });
    }
}
