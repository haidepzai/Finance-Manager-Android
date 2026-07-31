package com.hdmstuttgart.financemanager.presentation.ui;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;

import com.hdmstuttgart.financemanager.FinanceManagerApp;
import com.hdmstuttgart.financemanager.R;
import com.hdmstuttgart.financemanager.presentation.viewmodel.MainViewModel;
import com.hdmstuttgart.financemanager.presentation.viewmodel.MainViewModelFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FinanceManagerApp app = (FinanceManagerApp) getApplication();
        MainViewModelFactory factory = new MainViewModelFactory(
                app.appContainer.getTransactionsUseCase,
                app.appContainer.addTransactionUseCase,
                app.appContainer.deleteTransactionUseCase
        );
        viewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);

        viewModel.getTotalBalance().observe(this, balance -> {
            updateNavigationHeader();
        });

        drawer = findViewById(R.id.drawer_layout);
        initializeMenu();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                updateNavigationHeader();
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MainFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    private void updateNavigationHeader() {
        TextView total_money_view = navigationView.getHeaderView(0).findViewById(R.id.kontostandView);
        Double total_saldo = viewModel.getTotalBalance().getValue();
        
        if (total_saldo == null || total_saldo == 0) {
            total_money_view.setText(R.string.value_zero);
        } else {
            ValueAnimator animator = new ValueAnimator();
            animator.setObjectValues(0.00, total_saldo);
            animator.addUpdateListener(animation -> total_money_view.setText(String.format("%s€", animation.getAnimatedValue())));
            animator.setEvaluator((TypeEvaluator<Double>) (fraction, startValue, endValue) -> 
                    Math.round((startValue + ((endValue - startValue) * fraction)) * 100.00) / 100.00);
            animator.setDuration(1000);
            animator.start();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();
        } else if (id == R.id.nav_chart) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StatisticFragment()).commit();
        } else if (id == R.id.nav_search) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).commit();
        } else if (id == R.id.nav_impressum) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ImpressumFragment()).commit();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.setSearchQuery(newText);
                return true;
            }
        });
        return true;
    }

    private void initializeMenu() {
        navigationView = findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.action_search).setVisible(false);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem home_item = menu.findItem(R.id.nav_home);
        if (home_item != null) home_item.setVisible(false);
        MenuItem chart_item = menu.findItem(R.id.nav_chart);
        if (chart_item != null) chart_item.setVisible(false);
        MenuItem extended_search = menu.findItem(R.id.nav_search);
        if (extended_search != null) extended_search.setVisible(false);
        MenuItem impressum_item = menu.findItem(R.id.nav_impressum);
        if (impressum_item != null) impressum_item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
        return true;
    }
}
