package de.hdmstuttgart.financemanager.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdmstuttgart.financemanager.Activity.ItemDetailActivity;
import de.hdmstuttgart.financemanager.Adapter.RecyclerViewAdapter;
import de.hdmstuttgart.financemanager.Category;
import de.hdmstuttgart.financemanager.PaymentMethods;
import de.hdmstuttgart.financemanager.R;
import de.hdmstuttgart.financemanager.TransactionItem;

public class SearchFragment extends Fragment implements RecyclerViewAdapter.OnNoteListener {
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<TransactionItem> resultList;

    private Spinner searchSpinner;
    private static ArrayAdapter<String> mSpinnerSearchAdapter;
    private ArrayList<String> searchSpinnerList = new ArrayList<>();
    private String searchItem  = "Zweck"; //Standardwert des Spinners;

    private Spinner searchCategorySpinner;
    private static ArrayAdapter<String> mSpinnerSearchCategoryAdapter;
    private String category;

    private Spinner searchMethodSpinner;
    private static ArrayAdapter<String> mSpinnerSearchMethodAdapter;
    private String paymentMethod;

    private EditText searchPurpose;
    //TODO: Evtl noch nach Datum suchen
    private EditText searchDate;
    private EditText searchAmount1;
    private EditText searchAmount2;

    private Button searchButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resultList = new ArrayList<>();
        searchSpinnerList.add("Zweck");
        searchSpinnerList.add("Kategorie");
        searchSpinnerList.add("Zahlungsmethode");
        searchSpinnerList.add("Betrag");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        searchSpinner = rootView.findViewById(R.id.searchSpinner);
        searchButton = rootView.findViewById(R.id.searchButton);
        searchPurpose = rootView.findViewById(R.id.searchPurpose);
        searchCategorySpinner = rootView.findViewById(R.id.searchCategory);
        searchMethodSpinner = rootView.findViewById(R.id.searchMethod);
        searchAmount1 = rootView.findViewById(R.id.searchAmount1);
        searchAmount2 = rootView.findViewById(R.id.searchAmount2);

        //Spinner für Suchbegriff
        mSpinnerSearchAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, searchSpinnerList);
        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //Verhalten des Spinners
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Weist die Variable paymentMethod das ausgewählt Item zu
                searchItem = parent.getItemAtPosition(position).toString();

                switch(searchItem) {
                    case "Zweck":
                        searchPurpose.setVisibility(View.VISIBLE);
                        searchCategorySpinner.setVisibility(View.GONE);
                        searchMethodSpinner.setVisibility(View.GONE);
                        searchAmount1.setVisibility(View.GONE);
                        searchAmount2.setVisibility(View.GONE);
                        break;
                    case "Kategorie":
                        searchPurpose.setVisibility(View.GONE);
                        searchCategorySpinner.setVisibility(View.VISIBLE);
                        searchMethodSpinner.setVisibility(View.GONE);
                        searchAmount1.setVisibility(View.GONE);
                        searchAmount2.setVisibility(View.GONE);
                        break;
                    case "Zahlungsmethode":
                        searchPurpose.setVisibility(View.GONE);
                        searchCategorySpinner.setVisibility(View.GONE);
                        searchMethodSpinner.setVisibility(View.VISIBLE);
                        searchAmount1.setVisibility(View.GONE);
                        searchAmount2.setVisibility(View.GONE);
                        break;
                    case "Betrag":
                        searchPurpose.setVisibility(View.GONE);
                        searchCategorySpinner.setVisibility(View.GONE);
                        searchMethodSpinner.setVisibility(View.GONE);
                        searchAmount1.setVisibility(View.VISIBLE);
                        searchAmount2.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        searchSpinner.setAdapter(mSpinnerSearchAdapter);

        //Spinner für Kategorie
        mSpinnerSearchCategoryAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, Category.categorySpinnerDetail);
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
        mSpinnerSearchMethodAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, PaymentMethods.methodSpinnerDetail);
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

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterItems();
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
            }
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
        //Übergibt die Informationen mit putExtra
        Intent intent = new Intent(getContext(), ItemDetailActivity.class);
        intent.putExtra("Purpose", resultList.get(position).getmPurpose());
        intent.putExtra("Amount", resultList.get(position).getmAmount());
        intent.putExtra("Date", resultList.get(position).getmDate());
        intent.putExtra("Category", resultList.get(position).getmCategory());
        intent.putExtra("Method", resultList.get(position).getmMethod());
        intent.putExtra("Position", position);
        startActivity(intent);
    }

    private void filterItems() {

        if (searchItem.equals("Zweck")) {
            int counter = 0;
            resultList.clear();
            if (searchPurpose.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Bitte einen Suchbegriff angeben", Toast.LENGTH_LONG).show();
            } else {
                for (TransactionItem item : TransactionItem.itemList) {
                    if (item.getmPurpose().toLowerCase().contains(searchPurpose.getText().toString().toLowerCase())) {
                        resultList.add(item);
                        counter++;
                    }
                }
                if (counter == 0) {
                    Toast.makeText(getContext(), "Kein Eintrag gefunden", Toast.LENGTH_LONG).show();
                }
            }
        } else if (searchItem.equals("Kategorie")) {
            int counter = 0;
            resultList.clear();
            for (TransactionItem item : TransactionItem.itemList) {
                if (item.getmCategory().matches(category)) {
                    resultList.add(item);
                    counter++;
                }
            }
            if (counter == 0) {
                Toast.makeText(getContext(), "Kein Eintrag gefunden", Toast.LENGTH_LONG).show();
            }
        } else if (searchItem.equals("Zahlungsmethode")) {
            int counter = 0;
            resultList.clear();
            for (TransactionItem item : TransactionItem.itemList) {
                if (item.getmMethod().matches(paymentMethod)) {
                    resultList.add(item);
                    counter++;
                }
            }
            if (counter == 0) {
                Toast.makeText(getContext(), "Kein Eintrag gefunden", Toast.LENGTH_LONG).show();
            }
        } else if (searchItem.equals("Betrag")) {
            int counter = 0;
            resultList.clear();

            double amount1 = Double.parseDouble(searchAmount1.getText().toString());
            double amount2 = Double.parseDouble(searchAmount2.getText().toString());

            for (TransactionItem item : TransactionItem.itemList) {
                double currentAmount = Double.parseDouble(item.getmAmount().replaceAll("[-€]", ""));
                if (currentAmount >= amount1 && currentAmount <= amount2) {
                    resultList.add(item);
                    counter++;
                }
            }
            if (counter == 0) {
                Toast.makeText(getContext(), "Kein Eintrag gefunden", Toast.LENGTH_LONG).show();
            }
        }
    }
}