package pl.project.moneyTracker.firebase;

import android.arch.lifecycle.LiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class FirebaseQueryLiveDataSet<T> extends LiveData<ListDataSet<T>> {
    private final Class<T> genericTypeClass;
    private Query query;
    private ValueEventListener listener;
    ListDataSet<T> walletEntriesLiveDataSet;
    private List<T> walletEntries;
    private ArrayList<String> walletEntriesIds;

    public FirebaseQueryLiveDataSet(Class<T> genericTypeClass, Query query) {
        listener = new ValueEventListener();
        walletEntriesLiveDataSet = new ListDataSet<>();
        walletEntries = walletEntriesLiveDataSet.list;
        walletEntriesIds = walletEntriesLiveDataSet.getIDList();
        setValue(walletEntriesLiveDataSet);
        this.genericTypeClass = genericTypeClass;
        this.query = query;
    }


    @Override
    protected void onActive() {
        query.addChildEventListener(listener);
    }

    @Override
    protected void onInactive() {
        query.removeEventListener(listener);
    }

    private class ValueEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            T item = dataSnapshot.getValue(genericTypeClass);

            String key = dataSnapshot.getKey();

            if (!walletEntriesIds.contains(key)) {
                int insertedPosition;
                if (previousChildName == null) {
                    walletEntries.add(0, item);
                    walletEntriesIds.add(0, key);
                    insertedPosition = 0;
                } else {
                    int previousIndex = walletEntriesIds.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == walletEntries.size()) {
                        walletEntries.add(item);
                        walletEntriesIds.add(key);
                    } else {
                        walletEntries.add(nextIndex, item);
                        walletEntriesIds.add(nextIndex, key);
                    }
                    insertedPosition = nextIndex;
                }

                //notifyItemInserted(insertedPosition);
                walletEntriesLiveDataSet.setItemInserted(insertedPosition);
                setValue(walletEntriesLiveDataSet);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            T item = dataSnapshot.getValue(genericTypeClass);
            String key = dataSnapshot.getKey();

            if (walletEntriesIds.contains(key)) {
                int index = walletEntriesIds.indexOf(key);
                T oldItem = walletEntries.get(index);
                walletEntries.set(index, item);
                //notifyItemChanged(index);
                walletEntriesLiveDataSet.setItemChanged(index);
                setValue(walletEntriesLiveDataSet);

            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();

            if (walletEntriesIds.contains(key)) {
                int index = walletEntriesIds.indexOf(key);
                if (index == -1) return;
                T item = walletEntries.get(index);

                walletEntriesIds.remove(index);
                walletEntries.remove(index);

                //notifyItemRemoved(index);
                walletEntriesLiveDataSet.setItemRemoved(index);
                setValue(walletEntriesLiveDataSet);
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            T item = dataSnapshot.getValue(genericTypeClass);
            String key = dataSnapshot.getKey();

            int index = walletEntriesIds.indexOf(key);
            walletEntries.remove(index);
            walletEntriesIds.remove(index);
            int newPosition;
            if (previousChildName == null) {
                walletEntries.add(0, item);
                walletEntriesIds.add(0, key);
                newPosition = 0;
            } else {
                int previousIndex = walletEntriesIds.indexOf(previousChildName);
                int nextIndex = previousIndex + 1;
                if (nextIndex == walletEntries.size()) {
                    walletEntries.add(item);
                    walletEntriesIds.add(key);
                } else {
                    walletEntries.add(nextIndex, item);
                    walletEntriesIds.add(nextIndex, key);
                }
                newPosition = nextIndex;
            }
            //notifyItemMoved(index, newPosition);
            walletEntriesLiveDataSet.setItemMoved(index, newPosition);
            setValue(walletEntriesLiveDataSet);


        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}