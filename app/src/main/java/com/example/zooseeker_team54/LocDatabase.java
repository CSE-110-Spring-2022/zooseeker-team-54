package com.example.zooseeker_team54;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Abstract database class for use with out Location Items and Edges
 */
@Database(entities = {LocItem.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class LocDatabase extends RoomDatabase {
    private static LocDatabase singleton = null;

    public abstract LocItemDao LocItemDao();

    public synchronized static LocDatabase getSingleton(Context context) {
        if (singleton == null) {
            singleton = LocDatabase.makeDatabase(context);
        }
        return singleton;
    }

    private static LocDatabase makeDatabase(Context context) {
        return Room.databaseBuilder(context, LocDatabase.class, "zoo_app.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                            List<LocItem> locs = LocItem
                                    .loadJSON(context, "sample_node_info.json");
                            getSingleton(context).LocItemDao().insertAll(locs);
                        });
                    }
                })
                .build();
    }

    @VisibleForTesting
    public static void injectTestDatabase(LocDatabase testDatabase) {
        if (singleton != null) {
            singleton.close();
        }
        singleton = testDatabase;
    }
}
