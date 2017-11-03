package com.vnshine.learnjapanese.Activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.GridView;

import com.vnshine.learnjapanese.Adapters.GridViewAdapter;
import com.vnshine.learnjapanese.DataBase.DatabaseHelper;
import com.vnshine.learnjapanese.Models.Category;
import com.vnshine.learnjapanese.Models.GridViewItem;
import com.vnshine.learnjapanese.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    GridView gridView;
    GridViewAdapter gridViewAdapter;
    ArrayList<Category> categories = new ArrayList<>();
    ArrayList<GridViewItem> items = new ArrayList<>();

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
        setSupportActionBar(toolbar);
        setNavigationView(toolbar);
        setGridView();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds sentences to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
    }

    private void readDB() {
        DatabaseHelper.handleCopyingDataBase(this);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        categories = databaseHelper.getAllCategories();
        GridViewItem item;
        for (int i = 0; i < categories.size(); i++) {
            item = new GridViewItem(imageId[i], categories.get(i));
            items.add(item);
        }
        databaseHelper.close();
    }

    public void setGridView() {
        readDB();
        gridView = findViewById(R.id.category);
        gridViewAdapter = new GridViewAdapter(this, R.layout.item_category, items);
        gridView.setAdapter(gridViewAdapter);

    }
}
