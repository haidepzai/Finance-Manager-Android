package de.hdmstuttgart.financemanager;

import android.app.Application;
import de.hdmstuttgart.financemanager.di.AppContainer;

public class FinanceManagerApp extends Application {
    public AppContainer appContainer;

    @Override
    public void onCreate() {
        super.onCreate();
        appContainer = new AppContainer(this);
    }
}
