package pers.wengzc.snailhunterrt;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import pers.wengzc.snailhunterrt.ISnailHunterService;
import pers.wengzc.snailhunterrt.ISnailWatcher;
import pers.wengzc.snailhunterrt.R;
import pers.wengzc.snailhunterrt.Snail;
import pers.wengzc.snailhunterrt.SnailHunterService;
import pers.wengzc.snailhunterrt.ui.DisplayAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author wengzc
 */
public class CanaryDisplayActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DisplayAdapter mAdapter;

    private ISnailHunterService mSnailHunterService = null;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            synchronized (CanaryDisplayActivity.this){
                mSnailHunterService = ISnailHunterService.Stub.asInterface(service);
                try {
                    mSnailHunterService.registerNewSnailWatcher(mSnailWatcher);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                fetchDataAndUpdateView();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            synchronized (CanaryDisplayActivity.this){
                mSnailHunterService = null;
            }
        }
    };

    private final ISnailWatcher mSnailWatcher = new ISnailWatcher.Stub() {
        @Override
        public void onCatchNewSnail(Snail snail) throws RemoteException {
            fetchDataAndUpdateView();
        }
    };

    private void fetchDataAndUpdateView (){
        if (mSnailHunterService != null){
            try {
                List<Snail> newData = mSnailHunterService.getAllSnail();
                mAdapter.updateData(newData);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_canary_display);
        mRecyclerView = findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DisplayAdapter(Collections.<Snail>emptyList());
        mRecyclerView.setAdapter(mAdapter);

        Intent intent = new Intent(this, SnailHunterService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private synchronized ISnailHunterService getSnailHunterService (){
        return mSnailHunterService;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ISnailHunterService snailHunterService = getSnailHunterService();
        if (snailHunterService != null){
            try {
                snailHunterService.unregisterNewSnailWatcher(mSnailWatcher);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mServiceConnection);
    }
}
