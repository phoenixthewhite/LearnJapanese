package com.vnshine.learnjapanese.Activities;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.vnshine.learnjapanese.Adapters.ExpandableListAdapter;
import com.vnshine.learnjapanese.Adapters.GridViewAdapter;
import com.vnshine.learnjapanese.DataBase.DatabaseHelper;
import com.vnshine.learnjapanese.Dialog.CloseDialog;
import com.vnshine.learnjapanese.Models.Category;
import com.vnshine.learnjapanese.Models.GridViewItem;
import com.vnshine.learnjapanese.Models.JapaneseSentence;
import com.vnshine.learnjapanese.Models.Meaning;
import com.vnshine.learnjapanese.R;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;
    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<GridViewItem> items = new ArrayList<>();
    private ArrayList<Meaning> listMeaning = new ArrayList<>();
    private ArrayList<JapaneseSentence> listJapaneseSentences = new ArrayList<>();
    private ExpandableListAdapter eAdapter;
    private ExpandableListView listResult;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private DatabaseHelper databaseHelper;
    private int lastExpandedPosition = -1;


    int[] imageId = new int[]{
            R.drawable.favorite, R.drawable.greeting, R.drawable.general, R.drawable.number
            , R.drawable.time, R.drawable.direction, R.drawable.transportation, R.drawable.accommodation
            , R.drawable.eating, R.drawable.shopping, R.drawable.color, R.drawable.city
            , R.drawable.country, R.drawable.travel, R.drawable.family, R.drawable.dating1
            , R.drawable.emergency, R.drawable.sick, R.drawable.homonym, R.drawable.store
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        DatabaseHelper.handleCopyingDataBase(this);
        databaseHelper = new DatabaseHelper(this);
        setSupportActionBar(toolbar);
        setNavigationView(toolbar);
//        setSearchFunction();
        setGridView();
        setPermission();

    }

    private void setPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
//                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use pronounce function\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.RECORD_AUDIO)
                .check();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setSearchFunction();
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }

    private void setSearchFunction() {
        listResult = findViewById(R.id.search_result);
        eAdapter = new ExpandableListAdapter(this, listJapaneseSentences, listMeaning);
        listResult.setAdapter(eAdapter);
        listResult.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    listResult.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
        listResult.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (searchView.getQuery().length() != 0) {
            searchView.setQuery("", false);
        } else {
            CloseDialog closeDialog = new CloseDialog(this);
            closeDialog.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                    if (Objects.equals(newText, "")) {
                        eAdapter.setFilter(new ArrayList<Meaning>(), new ArrayList<JapaneseSentence>());
                    } else {
                        newText = newText.toLowerCase();
                        newText = unAccent(newText);
                        databaseHelper.getFilteredSentences("%" + newText + "%");
                        Log.i("Array size:  ", databaseHelper.getListMeanings().size() + "");
                        eAdapter.setFilter(databaseHelper.getListMeanings(),
                                databaseHelper.getListJapansesSentences());
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_translate) {
            // Handle the camera action
            Intent intent = new Intent(this,TranslateActivity.class);
            MainActivity.this.startActivity(intent);
        } else if (id == R.id.nav_support) {
            this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=7261021571514228233")));
        } else if (id == R.id.nav_about) {
            this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://vnshineteam.tech/")));
//        } else if (id == R.id.nav_manage) {

//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setNavigationView(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }

    private void readDB() {
        categories = databaseHelper.getAllCategories();
        GridViewItem item;
        for (int i = 0; i < categories.size(); i++) {
            item = new GridViewItem(imageId[i], categories.get(i));
            items.add(item);
        }
    }

    public void setGridView() {
        readDB();
        gridView = findViewById(R.id.category);
        gridViewAdapter = new GridViewAdapter(this, R.layout.item_category, items);
        gridView.setAdapter(gridViewAdapter);

    }

    public static String unAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("")
                .replaceAll("Đ", "D")
                .replace("đ", "");
    }

}
