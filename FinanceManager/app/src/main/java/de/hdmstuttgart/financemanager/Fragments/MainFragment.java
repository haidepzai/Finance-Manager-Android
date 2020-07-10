package de.hdmstuttgart.financemanager.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Objects;

import de.hdmstuttgart.financemanager.Activity.ItemDetailActivity;
import de.hdmstuttgart.financemanager.Activity.MainActivity;
import de.hdmstuttgart.financemanager.Adapter.RecyclerViewAdapter;
import de.hdmstuttgart.financemanager.Category;
import de.hdmstuttgart.financemanager.Database.Transaction;
import de.hdmstuttgart.financemanager.Helper.CurrencyFormatter;
import de.hdmstuttgart.financemanager.Helper.WrapContentLinearLayoutManager;
import de.hdmstuttgart.financemanager.PaymentMethods;
import de.hdmstuttgart.financemanager.R;

public class MainFragment extends Fragment implements RecyclerViewAdapter.OnNoteListener {
    private RecyclerView mRecyclerView;
    public static RecyclerViewAdapter mAdapter;

    private Dialog myDialog; //Popup Fenster

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    //Textfelder, welche im Dialog angezeigt werden
    private EditText mPurpose;
    private EditText mAmount;
    private EditText mDate;
    private Spinner mCategory;
    private Spinner mMethod;
    private ImageButton calenderBtn;
    private Button addBill;

    private static ArrayAdapter<String> mSpinnerMethodAdapter; //Adapter für Spinner Zahlungsmethode (Dropdown Liste)
    private static ArrayAdapter<String> mSpinnerCategoryAdapter; //Adapter für Spinner Kategorie (Dropdown Liste)

    private String paymentMethod; //Neue Zahlungsmethode wird in dieser Variable gespeichert
    private String formattedCurrency; //Formatierte Währung mit 2 Nachkommastellen

    private String category;
    private int category_logo;

    private Switch incomingPayment;

    private String billType; //Eingang + / Ausgang -

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Dialog (Popup Fenster) zum Hinzufügen weitere Elemente
        myDialog = new Dialog(Objects.requireNonNull(getContext()));
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //Change Date
        mDateSetListener = (view, year, month, dayOfMonth) -> {
            mDate = myDialog.findViewById(R.id.inputDate);
            month = month + 1;
            String date = dayOfMonth + "." + month + "." + year;
            mDate.setText(date);
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Floating Action Button unten rechts
        FloatingActionButton fab = rootView.findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> {
            Button btnClose;
            myDialog.setContentView(R.layout.custompopup);

            btnClose = myDialog.findViewById(R.id.btnClose);
            btnClose.setOnClickListener(v12 -> myDialog.dismiss());
            myDialog.show();

            initializeText(); //Initialisierung der TextViews in dem Dialog

            billType = "-"; //Standardwert
            //Switch (Zahlungseingang / Zahlungsausgang)
            incomingPayment.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    billType = "+";
                    mSpinnerCategoryAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.spinner_item, Category.incomingTypeSpinnerMain);
                } else {
                    billType = "-";
                    mSpinnerCategoryAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.spinner_item, Category.categorySpinnerMain);
                }
                mCategory.setAdapter(mSpinnerCategoryAdapter);
            });

            //Öffnet Datum-Feld
            calenderBtn.setOnClickListener(v13 -> {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Objects.requireNonNull(getContext()),
                        android.R.style.Theme_DeviceDefault_Light_Dialog, //Calender Style
                        mDateSetListener,
                        year, month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            });

            mSpinnerMethodAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.spinner_item, PaymentMethods.methodSpinnerMain);
            mMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                //Verhalten des Spinners
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //Weist die Variable paymentMethod das ausgewählt Item zu
                    paymentMethod = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            mMethod.setAdapter(mSpinnerMethodAdapter);

            mSpinnerCategoryAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.spinner_item, Category.categorySpinnerMain);
            mCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                //Verhalten des Spinners
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //Weist die Variable paymentMethod das ausgewählt Item zu
                    category = parent.getItemAtPosition(position).toString();
                    switch (category) {
                        case "Einkauf":
                            category_logo = R.drawable.logo_shopping;
                            break;
                        case "Essen":
                            category_logo = R.drawable.logo_restaurant;
                            break;
                        case "Studium/Beruf":
                            category_logo = R.drawable.logo_education;
                            break;
                        case "Freizeit":
                            category_logo = R.drawable.logo_star;
                            break;
                        case "Gebühren":
                            category_logo = R.drawable.gebuehr_logo;
                            break;
                        case "Geschenk":
                            category_logo = R.drawable.logo_gift;
                            break;
                        case "Lohn/Gehalt":
                            category_logo = R.drawable.ic_euro_black;
                            break;
                        case "Sonstige":
                            category_logo = R.drawable.logo_misc;
                            break;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            mCategory.setAdapter(mSpinnerCategoryAdapter);

            //Button im Dialog um Rechnung hinzuzufügen
            addBill = myDialog.findViewById(R.id.addBill);
            addBill.setOnClickListener(v1 -> {
                setGreenFieldBorder();
                checkEmptyField();

                if (mPurpose.getText().toString().equals("") || mAmount.getText().toString().equals("") ||
                        mDate.getText().toString().equals("") || mSpinnerMethodAdapter.getPosition(paymentMethod) == 0 ||
                        mSpinnerMethodAdapter.getPosition(paymentMethod) == 1 || mSpinnerCategoryAdapter.getPosition(paymentMethod) == 0 ||
                        mSpinnerCategoryAdapter.getPosition(paymentMethod) == 1) {
                    Toast.makeText(getContext(), "Bitte die rot markierten Felder ausfüllen",
                            Toast.LENGTH_LONG).show();
                } else {
                    String number = mAmount.getText().toString();
                    if (!number.equals("")) {
                        formattedCurrency = CurrencyFormatter.formatNumberCurrency(number);
                    }
                    Transaction.addEntry(new Transaction(category_logo,
                            mPurpose.getText().toString(),
                            billType + formattedCurrency + "€",
                            mDate.getText().toString(),
                            category,
                            paymentMethod
                    )); //Add into Database
                    addEntry(new Transaction(category_logo,
                            mPurpose.getText().toString(),
                            billType + formattedCurrency + "€",
                            mDate.getText().toString(),
                            category,
                            paymentMethod));
                    MainActivity.setTotalSaldo();
                    mAdapter.notifyDataSetChanged();
                    myDialog.dismiss();
                }
            });
        });

        //Initialize RecyclerView
        mRecyclerView = rootView.findViewById(R.id.itemRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new WrapContentLinearLayoutManager(getContext());
        mAdapter = new RecyclerViewAdapter(Transaction.itemList, this);

        final Transaction[] deletedTransaction = {null}; //Variable zum Zwischenspeichern des gelöschten Elements
        //Swiper
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) { //Linker Swipe
            //Andere Swipe Richtungen (0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP)

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            //Swipe löschen (mit Möglichkeit Rückgänig zu machen)
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                final int position = viewHolder.getAdapterPosition();
                int id_Position = Transaction.itemList.get(position).uid; //ID Database
                deletedTransaction[0] = Transaction.itemList.get(position); //Zwischenspeichern des gelöschten Elements
                deleteEntry(Transaction.itemList.get(position)); //Delete Entry in Database
                Transaction.itemList.remove(position);
                mAdapter.notifyDataSetChanged();
                Snackbar.make(mRecyclerView, deletedTransaction[0].getmPurpose() + " gelöscht", Snackbar.LENGTH_LONG)
                        .setAction("undo", v -> {
                            Transaction.itemList.add(position, deletedTransaction[0]);
                            MainActivity.db.transactionDetailDao().insertItem(id_Position, deletedTransaction[0].mImageResource, deletedTransaction[0].mPurpose,
                                    deletedTransaction[0].mAmount, deletedTransaction[0].mDate, deletedTransaction[0].mCategory, deletedTransaction[0].mMethod);
                            mAdapter.notifyItemInserted(position);
                        }).show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    private void initializeText() {
        mPurpose = myDialog.findViewById(R.id.inputPurpose);
        mAmount = myDialog.findViewById(R.id.inputAmount);
        mMethod = myDialog.findViewById(R.id.inputMethod);
        mCategory = myDialog.findViewById((R.id.inputCategory));
        mDate = myDialog.findViewById(R.id.inputDate);
        calenderBtn = myDialog.findViewById(R.id.calendar_btn);
        incomingPayment = myDialog.findViewById(R.id.switch1);
    }

    //Wechselt in ItemDetailActivity und zeigt ausführliche Infos an
    @Override
    public void onNoteClick(int position) {
        //Übergibt die Informationen mit putExtra
        Intent intent = new Intent(getContext(), ItemDetailActivity.class);
        intent.putExtra("Image", Transaction.itemList.get(position).getmImageResource());
        intent.putExtra("Purpose", Transaction.itemList.get(position).getmPurpose());
        intent.putExtra("Amount", Transaction.itemList.get(position).getmAmount());
        intent.putExtra("Date", Transaction.itemList.get(position).getmDate());
        intent.putExtra("Category", Transaction.itemList.get(position).getmCategory());
        intent.putExtra("Method", Transaction.itemList.get(position).getmMethod());
        intent.putExtra("Position", position);
        intent.putExtra("ID", Transaction.itemList.get(position).uid);
        startActivity(intent);
    }

    private void checkEmptyField() {
        if (mPurpose.getText().toString().equals("")) {
            mPurpose.setBackgroundResource(R.drawable.edit_border_red);
        }
        if (mAmount.getText().toString().equals("")) {
            mAmount.setBackgroundResource(R.drawable.edit_border_red);
        }
        if (mDate.getText().toString().equals("")) {
            mDate.setBackgroundResource(R.drawable.input_left_rounded_red);
            calenderBtn.setBackgroundResource(R.drawable.button_right_rounded_red);
        }
        if (mSpinnerCategoryAdapter.getPosition(category) == 0 || mSpinnerCategoryAdapter.getPosition(category) == 1) {
            mCategory.setBackgroundResource(R.drawable.edit_border_red);
        }
        if (mSpinnerMethodAdapter.getPosition(paymentMethod) == 0 || mSpinnerMethodAdapter.getPosition(paymentMethod) == 1) {
            mMethod.setBackgroundResource(R.drawable.edit_border_red);
        }
    }

    private void setGreenFieldBorder() {
        mPurpose.setBackgroundResource(R.drawable.edit_border);
        mAmount.setBackgroundResource(R.drawable.edit_border);
        mDate.setBackgroundResource(R.drawable.input_left_rounded_green);
        calenderBtn.setBackgroundResource(R.drawable.button_right_rounded_green);
        mCategory.setBackgroundResource(R.drawable.edit_border);
        mMethod.setBackgroundResource(R.drawable.edit_border);
    }

    public void addEntry(Transaction transactionDetail) {
        new Thread(() -> MainActivity.db.transactionDetailDao().insert(transactionDetail)).start();
    }

    public void deleteEntry(Transaction transactionDetail) {
        new Thread(() -> MainActivity.db.transactionDetailDao().delete(transactionDetail)).start();
    }
}