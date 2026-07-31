package com.hdmstuttgart.financemanager.presentation.ui;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Objects;

import com.hdmstuttgart.financemanager.presentation.ui.ItemDetailActivity;
import com.hdmstuttgart.financemanager.Category;
import com.hdmstuttgart.financemanager.FinanceManagerApp;
import com.hdmstuttgart.financemanager.Helper.CurrencyFormatter;
import com.hdmstuttgart.financemanager.Helper.WrapContentLinearLayoutManager;
import com.hdmstuttgart.financemanager.PaymentMethods;
import com.hdmstuttgart.financemanager.R;
import com.hdmstuttgart.financemanager.domain.model.TransactionDetail;
import com.hdmstuttgart.financemanager.presentation.viewmodel.MainViewModel;
import com.hdmstuttgart.financemanager.presentation.viewmodel.MainViewModelFactory;

public class MainFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TransactionAdapter mAdapter;
    private MainViewModel viewModel;

    private Dialog myDialog;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private EditText mPurpose, mAmount, mDate;
    private Spinner mCategory, mMethod;
    private ImageButton calenderBtn;
    private Button addBill;
    private Switch incomingPayment;

    private ArrayAdapter<String> mSpinnerMethodAdapter;
    private ArrayAdapter<String> mSpinnerCategoryAdapter;

    private String paymentMethod, category, billType, formattedCurrency;
    private int category_logo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FinanceManagerApp app = (FinanceManagerApp) requireActivity().getApplication();
        MainViewModelFactory factory = new MainViewModelFactory(
                app.appContainer.getTransactionsUseCase,
                app.appContainer.addTransactionUseCase,
                app.appContainer.deleteTransactionUseCase
        );
        viewModel = new ViewModelProvider(requireActivity(), factory).get(MainViewModel.class);

        myDialog = new Dialog(requireContext());
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mDateSetListener = (view, year, month, dayOfMonth) -> {
            mDate = myDialog.findViewById(R.id.inputDate);
            String date = dayOfMonth + "." + (month + 1) + "." + year;
            mDate.setText(date);
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        FloatingActionButton fab = rootView.findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> showAddDialog());

        mRecyclerView = rootView.findViewById(R.id.itemRecyclerView);
        mAdapter = new TransactionAdapter(this::onTransactionClick);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        viewModel.getTransactions().observe(getViewLifecycleOwner(), transactions -> {
            mAdapter.setTransactions(transactions);
        });

        setupSwipeToDelete();

        return rootView;
    }

    private void showAddDialog() {
        myDialog.setContentView(R.layout.custompopup);
        myDialog.findViewById(R.id.btnClose).setOnClickListener(v -> myDialog.dismiss());
        myDialog.show();

        initializeDialogViews();
        billType = "-";

        incomingPayment.setOnCheckedChangeListener((buttonView, isChecked) -> {
            billType = isChecked ? "+" : "-";
            mSpinnerCategoryAdapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, 
                    isChecked ? Category.incomingTypeSpinnerMain : Category.categorySpinnerMain);
            mCategory.setAdapter(mSpinnerCategoryAdapter);
        });

        calenderBtn.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(requireContext(), android.R.style.Theme_DeviceDefault_Light_Dialog, 
                    mDateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        mSpinnerMethodAdapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, PaymentMethods.methodSpinnerMain);
        mMethod.setAdapter(mSpinnerMethodAdapter);
        mMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paymentMethod = parent.getItemAtPosition(position).toString();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        mSpinnerCategoryAdapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, Category.categorySpinnerMain);
        mCategory.setAdapter(mSpinnerCategoryAdapter);
        mCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
                category_logo = getCategoryLogo(category);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        addBill = myDialog.findViewById(R.id.addBill);
        addBill.setOnClickListener(v -> {
            if (validateFields()) {
                String number = mAmount.getText().toString();
                formattedCurrency = CurrencyFormatter.formatNumberCurrency(number);
                
                TransactionDetail newTransaction = new TransactionDetail(
                        0, category_logo, mPurpose.getText().toString(),
                        billType + formattedCurrency + "€", mDate.getText().toString(),
                        category, paymentMethod
                );
                viewModel.addTransaction(newTransaction);
                myDialog.dismiss();
            }
        });
    }

    private int getCategoryLogo(String category) {
        switch (category) {
            case "Einkauf": return R.drawable.logo_shopping;
            case "Essen": return R.drawable.logo_restaurant;
            case "Studium/Beruf": return R.drawable.logo_education;
            case "Freizeit": return R.drawable.logo_freetime;
            case "Gebühren": return R.drawable.gebuehr_logo;
            case "Geschenk": return R.drawable.logo_gift;
            case "Lohn/Gehalt": return R.drawable.logo_money;
            default: return R.drawable.logo_other;
        }
    }

    private void initializeDialogViews() {
        mPurpose = myDialog.findViewById(R.id.inputPurpose);
        mAmount = myDialog.findViewById(R.id.inputAmount);
        mMethod = myDialog.findViewById(R.id.inputMethod);
        mCategory = myDialog.findViewById((R.id.inputCategory));
        mDate = myDialog.findViewById(R.id.inputDate);
        calenderBtn = myDialog.findViewById(R.id.calendar_btn);
        incomingPayment = myDialog.findViewById(R.id.switch1);
    }

    private boolean validateFields() {
        boolean isValid = true;
        setGreenFieldBorder();
        if (mPurpose.getText().toString().isEmpty()) { mPurpose.setBackgroundResource(R.drawable.edit_border_red); isValid = false; }
        if (mAmount.getText().toString().isEmpty()) { mAmount.setBackgroundResource(R.drawable.edit_border_red); isValid = false; }
        if (mDate.getText().toString().isEmpty()) { mDate.setBackgroundResource(R.drawable.input_left_rounded_red); calenderBtn.setBackgroundResource(R.drawable.button_right_rounded_red); isValid = false; }
        if (mSpinnerCategoryAdapter.getPosition(category) < 2) { mCategory.setBackgroundResource(R.drawable.edit_border_red); isValid = false; }
        if (mSpinnerMethodAdapter.getPosition(paymentMethod) < 2) { mMethod.setBackgroundResource(R.drawable.edit_border_red); isValid = false; }
        
        if (!isValid) Toast.makeText(getContext(), "Bitte alle Felder korrekt ausfüllen", Toast.LENGTH_LONG).show();
        return isValid;
    }

    private void setGreenFieldBorder() {
        mPurpose.setBackgroundResource(R.drawable.edit_border);
        mAmount.setBackgroundResource(R.drawable.edit_border);
        mDate.setBackgroundResource(R.drawable.input_left_rounded_green);
        calenderBtn.setBackgroundResource(R.drawable.button_right_rounded_green);
        mCategory.setBackgroundResource(R.drawable.edit_border);
        mMethod.setBackgroundResource(R.drawable.edit_border);
    }

    private void onTransactionClick(TransactionDetail transaction) {
        Intent intent = new Intent(getContext(), ItemDetailActivity.class);
        intent.putExtra("Image", transaction.getImageResource());
        intent.putExtra("Purpose", transaction.getPurpose());
        intent.putExtra("Amount", transaction.getAmount());
        intent.putExtra("Date", transaction.getDate());
        intent.putExtra("Category", transaction.getCategory());
        intent.putExtra("Method", transaction.getMethod());
        intent.putExtra("ID", transaction.getId());
        startActivity(intent);
    }

    private void setupSwipeToDelete() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override public boolean onMove(@NonNull RecyclerView r, @NonNull RecyclerView.ViewHolder v, @NonNull RecyclerView.ViewHolder t) { return false; }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                TransactionDetail deleted = mAdapter.getCurrentList().get(position);
                viewModel.deleteTransaction(deleted);
                Snackbar.make(mRecyclerView, deleted.getPurpose() + " gelöscht", Snackbar.LENGTH_LONG)
                        .setAction("undo", v -> viewModel.addTransaction(deleted)).show();
            }
        }).attachToRecyclerView(mRecyclerView);
    }
}
