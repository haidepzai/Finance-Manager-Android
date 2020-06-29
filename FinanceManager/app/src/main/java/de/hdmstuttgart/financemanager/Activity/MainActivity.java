package de.hdmstuttgart.financemanager.Activity;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
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
import androidx.room.Room;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import de.hdmstuttgart.financemanager.Database.AppDatabase;
import de.hdmstuttgart.financemanager.Database.Transaction;
import de.hdmstuttgart.financemanager.Fragments.ImpressumFragment;
import de.hdmstuttgart.financemanager.Fragments.MainFragment;
import de.hdmstuttgart.financemanager.Fragments.SearchFragment;
import de.hdmstuttgart.financemanager.Fragments.StatisticFragment;
import de.hdmstuttgart.financemanager.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer; //DrawerLayout für Navigation

    private NavigationView navigationView;
    private static Double total_saldo = 0.00;
    public static AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "transaction").allowMainThreadQueries().build();

        /*
        //TODO: Dummy items, später löschen
        Transaction.addEntry(new Transaction(R.drawable.ic_euro_black, "Mensa Aufladung", "-10.00 €", "8.6.2020", "Studium/Beruf", "PayPal"));
        Transaction.addEntry(new Transaction(R.drawable.ic_euro_black, "Google Pay Aufladung", "-20.00 €", "1.6.2020", "Freizeit", "Kreditkarte"));
        Transaction.addEntry(new Transaction(R.drawable.ic_euro_black, "Vapiano SE", "-9.00 €", "28.5.2020", "Essen", "EC"));
        Transaction.addEntry(new Transaction(R.drawable.ic_euro_black, "Bosch Gehalt", "+1,500.00 €", "15.6.2020", "Lohn/Gehalt", "Überweisung"));
*/

        new Thread(() -> {
            Transaction.itemList.addAll(db.transactionDetailDao().getList());
            Transaction.outcomingBills.clear();
            Transaction.incomingBills.clear();
            for (Transaction item : Transaction.itemList) {
                if (item.getmAmount().contains("-")) {
                    Transaction.outcomingBills.add(item);
                } else {
                    Transaction.incomingBills.add(item);
                }
            }
        }).start();

        //Initialisierung DrawerLayout
        drawer = findViewById(R.id.drawer_layout);
        initializeMenu();

        //Hamburger Menü
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            //Set animated total saldo in navigation menu
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d("-AG-", "Beginning of onDrawerOpened: " + total_saldo);
                setTotalSaldo();
                Log.d("-AG-", "After setTotalSaldo of onDrawerOpened: " + total_saldo);

                TextView total_money_view = navigationView.getHeaderView(0).findViewById(R.id.kontostandView);

                if (total_saldo == 0) {
                    total_money_view.setText(R.string.value_zero); //0.00€
                } else {
                    ValueAnimator animator = new ValueAnimator();
                    animator.setObjectValues(0.00, total_saldo); //double value
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator animation) {
                            total_money_view.setText(String.format("%s€", animation.getAnimatedValue()));
                        }
                    });
                    animator.setEvaluator(new TypeEvaluator<Double>() {
                        @Override
                        public Double evaluate(float fraction, Double startValue, Double endValue) {
                            return Math.round((startValue + ((endValue - startValue) * fraction)) * 100.00) / 100.00;
                        }
                    });
                    animator.setDuration(1000);
                    animator.start();

                    //Set total saldo back to 0, otherwise it would constantly count up
                    total_saldo = 0.00;
                    Log.d("-AG-", "End of onDrawerOpened: " + total_saldo);
                }
            }


        }; //Verhalten beim öffnen/schließen
        drawer.addDrawerListener(toggle);
        toggle.syncState(); //Rotiert Hamburger Menü

        //Start Fragment (MainFragment)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MainFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }


    }

    //Wechselt Fragmente, je nachdem welches Menu Item man in der Nav auswählt
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d("-AG-", "In onNavigationItemSelected");
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MainFragment()).commit();
                break;
            case R.id.nav_chart:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new StatisticFragment()).commit();
                break;
            case R.id.nav_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchFragment()).commit();
                break;
            case R.id.nav_impressum:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ImpressumFragment()).commit();
                break;
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

    //Suche (Filter)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                MainFragment.mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    private void initializeMenu() {
        navigationView = findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.action_search).setVisible(false); //Suche nicht im Hamburger Menü anzeigen!
        navigationView.setNavigationItemSelectedListener(this);
        setTotalSaldo();
    }

    //Das Menü (3-dots Menü oben rechts) ausblenden
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem home_item = menu.findItem(R.id.nav_home);
        home_item.setVisible(false);
        MenuItem chart_item = menu.findItem(R.id.nav_chart);
        chart_item.setVisible(false);
        MenuItem extended_search = menu.findItem(R.id.nav_search);
        extended_search.setVisible(false);
        MenuItem impressum_item = menu.findItem(R.id.nav_impressum);
        impressum_item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    //Set total saldo in navigation menu
    public static void setTotalSaldo() {
        total_saldo = 0.00;

        List<Transaction> temp_transactions = db.transactionDetailDao().getList();
        for (Transaction t : Transaction.itemList) {
            String amount = t.getmAmount();
            total_saldo += Double.parseDouble(amount.replace("€", "").replace(",", "").replace("+", ""));
        }
    }
}
