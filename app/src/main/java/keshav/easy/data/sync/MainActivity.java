package keshav.easy.data.sync;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import api.ServiceGenerator;
import api.TestAPI;
import keshav.easy.data.sync.lib.DataSyncAdapter;
import keshav.easy.data.sync.lib.SyncUtil;
import models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SyncStatusObserver {

    private Account mAccount;
    private DataSyncAdapter dataSyncAdapter;
    private String TAG = "MAIN_ACTIVITY";
    private TextView main;

    /**
     * set up
     * @param savedInstanceState
     */
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        main = (TextView) findViewById( R.id.tv_main );
        FloatingActionButton fab = (FloatingActionButton) findViewById( R.id.button );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                Snackbar.make( view, "Button clicked", Snackbar.LENGTH_LONG ).setAction( "Action", null ).show();

                TestAPI api = ServiceGenerator.createService( TestAPI.class );
                Call<User> call = api.getUser( 1 );
                call.enqueue( new Callback<User>() {
                    @Override
                    public void onResponse( Call<User> call, Response<User> response ) {
                        Log.d( "TAG", "successfully got a user" );
                        User user = response.body();
                        main.setText( "" );
                        main.append( user.getName() + "\n" );
                        main.append( user.getWebsite() + "\n" );
                        main.append( user.getCompany().getName() + "\n" );
                        main.append( user.getEmail() + "\n" );

                    }

                    @Override
                    public void onFailure( Call<User> call, Throwable t ) {

                    }
                } );

            }
        } );


        mAccount = SyncUtil.CreateSyncAccount( this );
        if (mAccount != null) {
            dataSyncAdapter = new DataSyncAdapter( this, true );



            ContentResolver.addStatusChangeListener( 1, this );
            ContentResolver.setIsSyncable( mAccount, SyncUtil.AUTHORITY, 1 );
            ContentResolver.setSyncAutomatically( mAccount, SyncUtil.AUTHORITY, true );
            ContentResolver.addPeriodicSync( mAccount, SyncUtil.AUTHORITY, Bundle.EMPTY, 90 );

        } else {
            Log.d(TAG, "Account is null");
        }


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void onClick( View v ) {

    }

    @Override
    public void onStatusChanged( int which ) {
        Log.d( TAG, "Status changed: " + which);
        Log.d( TAG, "is Sync active: " + ContentResolver.isSyncActive( mAccount, SyncUtil.AUTHORITY ));
        Log.d( TAG, "is Sync Pending: " + ContentResolver.isSyncPending( mAccount, SyncUtil.AUTHORITY ) );
    }
}
