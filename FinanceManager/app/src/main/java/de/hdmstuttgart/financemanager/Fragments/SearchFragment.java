package de.hdmstuttgart.financemanager.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import de.hdmstuttgart.financemanager.Activity.ItemDetailActivity;
import de.hdmstuttgart.financemanager.Adapter.RecyclerViewAdapter;
import de.hdmstuttgart.financemanager.Category;
import de.hdmstuttgart.financemanager.Database.Transaction;
import de.hdmstuttgart.financemanager.PaymentMethods;
import de.hdmstuttgart.financemanager.R;

public class SearchFragment extends Fragment implements RecyclerViewAdapter.OnNoteListener {
    private RecyclerView mRecyclerView;
    public static RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private DatePickerDialog.OnDateSetListener mDateSetListener1; //Min Datum
    private DatePickerDialog.OnDateSetListener mDateSetListener2; //Max Datum

    private ArrayList<Transaction> resultList;

    private ArrayList<String> searchSpinnerList = new ArrayList<>();
    private String searchItem = "Zweck"; //Standardwert des Spinners;

    private Spinner searchCategorySpinner;
    private String category;

    private Spinner searchMethodSpinner;
    private String paymentMethod;

    private EditText searchPurpose;
    private EditText searchAmount1; //Min Betrag
    private EditText searchAmount2; //Max Betrag
    private TextView searchDate1; //Min Datum
    private TextView searchDate2; //Max Datum

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resultList = new ArrayList<>();
        searchSpinnerList.add("Zweck");
        searchSpinnerList.add("Kategorie");
        searchSpinnerList.add("Zahlungsmethode");
        searchSpinnerList.add("Betrag");
        searchSpinnerList.add("Datum");

        //Min Datum
        mDateSetListener1 = (view, year, month, dayOfMonth) -> {
            month = month + 1;
            String date = dayOfMonth + "." + month + "." + year;
            searchDate1.setText(date);
        };
        //Max Datum
        mDateSetListener2 = (view, year, month, dayOfMonth) -> {
            month = month + 1;
            String date = dayOfMonth + "." + month + "." + year;
            searchDate2.setText(date);
        };

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        Spinner searchSpinner = rootView.findViewById(R.id.searchSpinner);
        Button searchButton = rootView.findViewById(R.id.searchButton);
        searchPurpose = rootView.findViewById(R.id.searchPurpose);
        searchCategorySpinner = rootView.findViewById(R.id.searchCategory);
        searchMethodSpinner = rootView.findViewById(R.id.searchMethod);
        searchAmount1 = rootView.findViewById(R.id.searchAmount1);
        searchAmount2 = rootView.findViewById(R.id.searchAmount2);
        searchDate1 = rootView.findViewById(R.id.searchDate1);
        searchDate2 = rootView.findViewById(R.id.searchDate2);

        //Spinner für Suchbegriff
        ArrayAdapter<String> mSpinnerSearchAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.spinner_item, searchSpinnerList);
        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //Verhalten des Spinners
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Weist die Variable paymentMethod das ausgewählt Item zu
                searchItem = parent.getItemAtPosition(position).toString();

                switch (searchItem) {
                    case "Zweck":
                        searchPurpose.setVisibility(View.VISIBLE);
                        searchCategorySpinner.setVisibility(View.GONE);
                        searchMethodSpinner.setVisibility(View.GONE);
                        searchAmount1.setVisibility(View.GONE);
                        searchAmount2.setVisibility(View.GONE);
                        searchDate1.setVisibility(View.GONE);
                        searchDate2.setVisibility(View.GONE);
                        break;
                    case "Kategorie":
                        searchPurpose.setVisibility(View.GONE);
                        searchCategorySpinner.setVisibility(View.VISIBLE);
                        searchMethodSpinner.setVisibility(View.GONE);
                        searchAmount1.setVisibility(View.GONE);
                        searchAmount2.setVisibility(View.GONE);
                        searchDate1.setVisibility(View.GONE);
                        searchDate2.setVisibility(View.GONE);
                        break;
                    case "Zahlungsmethode":
                        searchPurpose.setVisibility(View.GONE);
                        searchCategorySpinner.setVisibility(View.GONE);
                        searchMethodSpinner.setVisibility(View.VISIBLE);
                        searchAmount1.setVisibility(View.GONE);
                        searchAmount2.setVisibility(View.GONE);
                        searchDate1.setVisibility(View.GONE);
                        searchDate2.setVisibility(View.GONE);
                        break;
                    case "Betrag":
                        searchPurpose.setVisibility(View.GONE);
                        searchCategorySpinner.setVisibility(View.GONE);
                        searchMethodSpinner.setVisibility(View.GONE);
                        searchAmount1.setVisibility(View.VISIBLE);
                        searchAmount2.setVisibility(View.VISIBLE);
                        searchDate1.setVisibility(View.GONE);
                        searchDate2.setVisibility(View.GONE);
                        break;
                    case "Datum":
                        searchPurpose.setVisibility(View.GONE);
                        searchCategorySpinner.setVisibility(View.GONE);
                        searchMethodSpinner.setVisibility(View.GONE);
                        searchAmount1.setVisibility(View.GONE);
                        searchAmount2.setVisibility(View.GONE);
                        searchDate1.setVisibility(View.VISIBLE);
                        searchDate2.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        searchSpinner.setAdapter(mSpinnerSearchAdapter);

        //Spinner für Kategorie
        ArrayAdapter<String> mSpinnerSearchCategoryAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, Category.categorySpinnerDetail);
        searchCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //Verhalten des Spinners
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        searchCategorySpinner.setAdapter(mSpinnerSearchCategoryAdapter);

        //Spinner für Zahlungsmethode
        ArrayAdapter<String> mSpinnerSearchMethodAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, PaymentMethods.methodSpinnerDetail);
        searchMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //Verhalten des Spinners
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paymentMethod = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        searchMethodSpinner.setAdapter(mSpinnerSearchMethodAdapter);

        //Min Datum
        searchDate1.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getContext(),
                    android.R.style.Theme_DeviceDefault_Light_Dialog, //Calender Style
                    mDateSetListener1,
                    year, month, day);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.show();
        });
        //Max Datum
        searchDate2.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getContext(),
                    android.R.style.Theme_DeviceDefault_Light_Dialog, //Calender Style
                    mDateSetListener2,
                    year, month, day);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.show();
        });

        searchButton.setOnClickListener(v -> {
            filterItems();
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
        });

        mRecyclerView = rootView.findViewById(R.id.recyclerViewSearch);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerViewAdapter(resultList, this);

        return rootView;
    }

    //Click Listener der Items in der RecyclerView
    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(getContext(), ItemDetailActivity.class);

        //Übergibt die Informationen mit putExtra

        /*
         * Mittels For Loop die richtige ID finden in der Hauptliste
         * Da die Position und ID des Item in dieser Activity nicht mit dem in der Hauptliste übereinstimmen,
         * führt es zum Fehler, falls man später in der ItemDetailActivity den Eintrag ändert
         * Lösung: ID und Position des gesuchten Eintrages in der Hauptliste finden:
         *
         * Falls Item ID (in dieser Activity) mit dem in der Hauptliste übereinstimmt
         * -> Dessen ID und Listenindex als intent übergeben
         * Ist notwendig, da man sonst den falschen Eintrag in der ItemDetailActivity ändert!!
         */

        for (Transaction item : Transaction.itemList) {
            if (resultList.get(position).uid == item.uid) {
                intent.putExtra("ID", item.uid);
                int index = Transaction.itemList.indexOf(item);
                intent.putExtra("Position", index);
            }
        }
        intent.putExtra("Image", resultList.get(position).getmImageResource());
        intent.putExtra("Purpose", resultList.get(position).getmPurpose());
        intent.putExtra("Amount", resultList.get(position).getmAmount());
        intent.putExtra("Date", resultList.get(position).getmDate());
        intent.putExtra("Category", resultList.get(position).getmCategory());
        intent.putExtra("Method", resultList.get(position).getmMethod());
        startActivity(intent);
    }

    private void filterItems() {

        int counter = 0; //if counter = 0 -> No entries

        searchAmount1.setBackgroundResource(R.drawable.edit_border);
        searchAmount2.setBackgroundResource(R.drawable.edit_border);
        searchPurpose.setBackgroundResource(R.drawable.edit_border);
        searchDate1.setBackgroundResource(R.drawable.edit_border);
        searchDate2.setBackgroundResource(R.drawable.edit_border);

        switch (searchItem) {
            case "Zweck":
                resultList.clear();
                if (searchPurpose.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Bitte einen Suchbegriff angeben", Toast.LENGTH_LONG).show();
                    searchPurpose.setBackgroundResource(R.drawable.edit_border_red);
                    counter++;
                } else {
                    for (Transaction item : Transaction.itemList) {
                        if (item.getmPurpose().toLowerCase().contains(searchPurpose.getText().toString().toLowerCase())) {
                            resultList.add(item);
                            counter++;
                        }
                    }
                }
                break;
            case "Kategorie":
                resultList.clear();
                for (Transaction item : Transaction.itemList) {
                    if (item.getmCategory().matches(category)) {
                        resultList.add(item);
                        counter++;
                    }
                }
                break;
            case "Zahlungsmethode":
                resultList.clear();
                for (Transaction item : Transaction.itemList) {
                    if (item.getmMethod().matches(paymentMethod)) {
                        resultList.add(item);
                        counter++;
                    }
                }
                break;
            case "Betrag":
                resultList.clear();

                if(searchAmount1.getText().toString().equals("") && searchAmount2.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Bitte Beträge angeben", Toast.LENGTH_LONG).show();
                    searchAmount1.setBackgroundResource(R.drawable.edit_border_red);
                    searchAmount2.setBackgroundResource(R.drawable.edit_border_red);
                    counter++;
                } else if (searchAmount1.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Bitte min. Betrag angeben", Toast.LENGTH_LONG).show();
                    searchAmount1.setBackgroundResource(R.drawable.edit_border_red);
                    counter++;
                } else if (searchAmount2.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Bitte max. Betrag angeben", Toast.LENGTH_LONG).show();
                    searchAmount2.setBackgroundResource(R.drawable.edit_border_red);
                    counter++;
                }

                else {
                    double amount1 = Double.parseDouble(searchAmount1.getText().toString());
                    double amount2 = Double.parseDouble(searchAmount2.getText().toString());

                    for (Transaction item : Transaction.itemList) {
                        double currentAmount = Double.parseDouble(item.getmAmount().replaceAll("[-€+,]", ""));
                        if (currentAmount >= amount1 && currentAmount <= amount2) {
                            resultList.add(item);
                            counter++;
                        }
                    }
                }
                break;
            case "Datum":
                resultList.clear();

                if(searchDate1.getText().toString().equals("Min. Datum") && searchDate2.getText().toString().equals("Max. Datum")){
                    Toast.makeText(getContext(), "Bitte Datum angeben", Toast.LENGTH_LONG).show();
                    searchDate1.setBackgroundResource(R.drawable.edit_border_red);
                    searchDate2.setBackgroundResource(R.drawable.edit_border_red);
                    counter++;
                } else if (searchDate1.getText().toString().equals("Min. Datum")){
                    Toast.makeText(getContext(), "Bitte min. Datum angeben", Toast.LENGTH_LONG).show();
                    searchDate1.setBackgroundResource(R.drawable.edit_border_red);
                    counter++;
                } else if (searchDate2.getText().toString().equals("Max. Datum")){
                    Toast.makeText(getContext(), "Bitte max. Datum angeben", Toast.LENGTH_LONG).show();
                    searchDate2.setBackgroundResource(R.drawable.edit_border_red);
                    counter++;
                } else {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);

                        Date date1 = sdf.parse(searchDate1.getText().toString());
                        Date date2 = sdf.parse(searchDate2.getText().toString());

                        for (Transaction item : Transaction.itemList) {
                            Date itemDate = sdf.parse(item.getmDate());
                            assert itemDate != null;
                            if (!(itemDate.before(date1) || itemDate.after(date2))) {
                                resultList.add(item);
                                counter++;
                            }
                        }
                    } catch (ParseException e) {
                        Toast.makeText(getContext(), "Fehler bei der Suche", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        Log.d("Error: ", e.toString());
                    }
                }
                break;
        }
        if (counter == 0) {
            Toast.makeText(getContext(), "Kein Eintrag gefunden", Toast.LENGTH_LONG).show();
        }
    }
    //Hide search
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.action_search);
        if(item!=null)
            item.setVisible(false);
    }
}
