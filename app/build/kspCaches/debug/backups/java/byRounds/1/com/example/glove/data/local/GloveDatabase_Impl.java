package com.example.glove.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class GloveDatabase_Impl extends GloveDatabase {
  private volatile GloveDao _gloveDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `layouts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `isSelected` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `mappings` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `layoutId` INTEGER NOT NULL, `gestureIndex` INTEGER NOT NULL, `mappedWord` TEXT NOT NULL, FOREIGN KEY(`layoutId`) REFERENCES `layouts`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_mappings_layoutId` ON `mappings` (`layoutId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3037ae7a96c42a51968df1749d616389')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `layouts`");
        db.execSQL("DROP TABLE IF EXISTS `mappings`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsLayouts = new HashMap<String, TableInfo.Column>(3);
        _columnsLayouts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLayouts.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLayouts.put("isSelected", new TableInfo.Column("isSelected", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLayouts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLayouts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLayouts = new TableInfo("layouts", _columnsLayouts, _foreignKeysLayouts, _indicesLayouts);
        final TableInfo _existingLayouts = TableInfo.read(db, "layouts");
        if (!_infoLayouts.equals(_existingLayouts)) {
          return new RoomOpenHelper.ValidationResult(false, "layouts(com.example.glove.data.local.LayoutEntity).\n"
                  + " Expected:\n" + _infoLayouts + "\n"
                  + " Found:\n" + _existingLayouts);
        }
        final HashMap<String, TableInfo.Column> _columnsMappings = new HashMap<String, TableInfo.Column>(4);
        _columnsMappings.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMappings.put("layoutId", new TableInfo.Column("layoutId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMappings.put("gestureIndex", new TableInfo.Column("gestureIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMappings.put("mappedWord", new TableInfo.Column("mappedWord", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMappings = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysMappings.add(new TableInfo.ForeignKey("layouts", "CASCADE", "NO ACTION", Arrays.asList("layoutId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMappings = new HashSet<TableInfo.Index>(1);
        _indicesMappings.add(new TableInfo.Index("index_mappings_layoutId", false, Arrays.asList("layoutId"), Arrays.asList("ASC")));
        final TableInfo _infoMappings = new TableInfo("mappings", _columnsMappings, _foreignKeysMappings, _indicesMappings);
        final TableInfo _existingMappings = TableInfo.read(db, "mappings");
        if (!_infoMappings.equals(_existingMappings)) {
          return new RoomOpenHelper.ValidationResult(false, "mappings(com.example.glove.data.local.MappingEntity).\n"
                  + " Expected:\n" + _infoMappings + "\n"
                  + " Found:\n" + _existingMappings);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "3037ae7a96c42a51968df1749d616389", "db81d854300b6b0cfc50226fa49f9733");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "layouts","mappings");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `layouts`");
      _db.execSQL("DELETE FROM `mappings`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(GloveDao.class, GloveDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public GloveDao gloveDao() {
    if (_gloveDao != null) {
      return _gloveDao;
    } else {
      synchronized(this) {
        if(_gloveDao == null) {
          _gloveDao = new GloveDao_Impl(this);
        }
        return _gloveDao;
      }
    }
  }
}
