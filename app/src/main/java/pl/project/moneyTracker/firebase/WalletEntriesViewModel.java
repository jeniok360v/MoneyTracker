package pl.project.moneyTracker.firebase;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import pl.project.moneyTracker.firebase.models.WalletEntry;

public class WalletEntriesViewModel extends ViewModel {

    private final FirebaseQueryLiveData liveData;

    public WalletEntriesViewModel(String uid) {
        liveData = new FirebaseQueryLiveData(uid);
    }

    @NonNull
    public LiveData<ListDataSet<WalletEntry>> getDataSnapshotLiveData() {
        return liveData;
    }
}