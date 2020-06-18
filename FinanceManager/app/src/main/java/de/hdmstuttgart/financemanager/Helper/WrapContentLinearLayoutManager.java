package de.hdmstuttgart.financemanager.Helper;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WrapContentLinearLayoutManager extends LinearLayoutManager {
    /**
     * Problem: App stürzte immer ab, wenn man die App mit dem Zurück Button "schließt" und wieder öffnet
     * FATAL EXCEPTION: RecyclerView and java.lang.IndexOutOfBoundsException: Inconsistency detected.
     * Lösung ist dieses Workaround
     */

    public WrapContentLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("TAG", "meet a IOOBE in RecyclerView");
        }
    }
}