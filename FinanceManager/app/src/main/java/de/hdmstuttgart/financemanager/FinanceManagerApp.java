package com.hdmstuttgart.financemanager;

import android.app.Application;
import com.hdmstuttgart.financemanager.di.AppContainer;

public class FinanceManagerApp extends Application {
    public AppContainer appContainer;

    @Override
    public void onCreate() {
        super.onCreate();
        appContainer = new AppContainer(this);
    }
}
