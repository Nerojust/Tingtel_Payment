package tingtel.payment.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.util.concurrent.Executors;

import tingtel.payment.dao.SimCardsDao;
import tingtel.payment.models.SimCards;

@Database(entities = {SimCards.class},version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase mInstance;

    private static final String DATABASE_NAME = "production";


    //livedata to monitor when database is being populated
    private final MutableLiveData<String> mIsDatabaseCreated = new MutableLiveData<>();



    public abstract SimCardsDao simCardsDao();


    public synchronized static AppDatabase getInstance(Context context) {
        if (mInstance == null) {
            mInstance = getDatabaseInstance(context);
        }
        return mInstance;
    }



    public static AppDatabase getDatabaseInstance(final Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                            //   getInstance(context).databaseInterface().insertAll(BanksCode.populateBanksCodes());

                            AppDatabase database = AppDatabase.getInstance(context);

                            database.mIsDatabaseCreated.postValue("populated");
                        });
                    }
                })
                .build();

    }

    public static void destroyInstance() {
        mInstance = null;

    }

    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

}

