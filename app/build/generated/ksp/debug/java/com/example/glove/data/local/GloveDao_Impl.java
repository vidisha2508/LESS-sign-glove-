package com.example.glove.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class GloveDao_Impl implements GloveDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<LayoutEntity> __insertionAdapterOfLayoutEntity;

  private final EntityInsertionAdapter<MappingEntity> __insertionAdapterOfMappingEntity;

  private final EntityDeletionOrUpdateAdapter<LayoutEntity> __updateAdapterOfLayoutEntity;

  private final EntityDeletionOrUpdateAdapter<MappingEntity> __updateAdapterOfMappingEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearSelectedLayout;

  private final SharedSQLiteStatement __preparedStmtOfSetSelectedLayout;

  private final SharedSQLiteStatement __preparedStmtOfDeleteLayout;

  public GloveDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLayoutEntity = new EntityInsertionAdapter<LayoutEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `layouts` (`id`,`name`,`isSelected`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LayoutEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        final int _tmp = entity.isSelected() ? 1 : 0;
        statement.bindLong(3, _tmp);
      }
    };
    this.__insertionAdapterOfMappingEntity = new EntityInsertionAdapter<MappingEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `mappings` (`id`,`layoutId`,`gestureIndex`,`mappedWord`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MappingEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getLayoutId());
        statement.bindLong(3, entity.getGestureIndex());
        statement.bindString(4, entity.getMappedWord());
      }
    };
    this.__updateAdapterOfLayoutEntity = new EntityDeletionOrUpdateAdapter<LayoutEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `layouts` SET `id` = ?,`name` = ?,`isSelected` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LayoutEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        final int _tmp = entity.isSelected() ? 1 : 0;
        statement.bindLong(3, _tmp);
        statement.bindLong(4, entity.getId());
      }
    };
    this.__updateAdapterOfMappingEntity = new EntityDeletionOrUpdateAdapter<MappingEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `mappings` SET `id` = ?,`layoutId` = ?,`gestureIndex` = ?,`mappedWord` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MappingEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getLayoutId());
        statement.bindLong(3, entity.getGestureIndex());
        statement.bindString(4, entity.getMappedWord());
        statement.bindLong(5, entity.getId());
      }
    };
    this.__preparedStmtOfClearSelectedLayout = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE layouts SET isSelected = 0 WHERE isSelected = 1";
        return _query;
      }
    };
    this.__preparedStmtOfSetSelectedLayout = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE layouts SET isSelected = 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteLayout = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM layouts WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertLayout(final LayoutEntity layout,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfLayoutEntity.insertAndReturnId(layout);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMappings(final List<MappingEntity> mappings,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMappingEntity.insert(mappings);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLayout(final LayoutEntity layout,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfLayoutEntity.handle(layout);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMapping(final MappingEntity mapping,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMappingEntity.handle(mapping);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clearSelectedLayout(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearSelectedLayout.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearSelectedLayout.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setSelectedLayout(final int layoutId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetSelectedLayout.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, layoutId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetSelectedLayout.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteLayout(final int layoutId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteLayout.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, layoutId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteLayout.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<LayoutEntity>> getAllLayouts() {
    final String _sql = "SELECT * FROM layouts";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"layouts"}, new Callable<List<LayoutEntity>>() {
      @Override
      @NonNull
      public List<LayoutEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIsSelected = CursorUtil.getColumnIndexOrThrow(_cursor, "isSelected");
          final List<LayoutEntity> _result = new ArrayList<LayoutEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final LayoutEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final boolean _tmpIsSelected;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSelected);
            _tmpIsSelected = _tmp != 0;
            _item = new LayoutEntity(_tmpId,_tmpName,_tmpIsSelected);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<LayoutEntity> getSelectedLayout() {
    final String _sql = "SELECT * FROM layouts WHERE isSelected = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"layouts"}, new Callable<LayoutEntity>() {
      @Override
      @Nullable
      public LayoutEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIsSelected = CursorUtil.getColumnIndexOrThrow(_cursor, "isSelected");
          final LayoutEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final boolean _tmpIsSelected;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSelected);
            _tmpIsSelected = _tmp != 0;
            _result = new LayoutEntity(_tmpId,_tmpName,_tmpIsSelected);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<MappingEntity>> getMappingsForLayout(final int layoutId) {
    final String _sql = "SELECT * FROM mappings WHERE layoutId = ? ORDER BY gestureIndex ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, layoutId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"mappings"}, new Callable<List<MappingEntity>>() {
      @Override
      @NonNull
      public List<MappingEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLayoutId = CursorUtil.getColumnIndexOrThrow(_cursor, "layoutId");
          final int _cursorIndexOfGestureIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "gestureIndex");
          final int _cursorIndexOfMappedWord = CursorUtil.getColumnIndexOrThrow(_cursor, "mappedWord");
          final List<MappingEntity> _result = new ArrayList<MappingEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MappingEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpLayoutId;
            _tmpLayoutId = _cursor.getInt(_cursorIndexOfLayoutId);
            final int _tmpGestureIndex;
            _tmpGestureIndex = _cursor.getInt(_cursorIndexOfGestureIndex);
            final String _tmpMappedWord;
            _tmpMappedWord = _cursor.getString(_cursorIndexOfMappedWord);
            _item = new MappingEntity(_tmpId,_tmpLayoutId,_tmpGestureIndex,_tmpMappedWord);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getMappingSync(final int layoutId, final int gestureIndex,
      final Continuation<? super MappingEntity> $completion) {
    final String _sql = "SELECT * FROM mappings WHERE layoutId = ? AND gestureIndex = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, layoutId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, gestureIndex);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MappingEntity>() {
      @Override
      @Nullable
      public MappingEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLayoutId = CursorUtil.getColumnIndexOrThrow(_cursor, "layoutId");
          final int _cursorIndexOfGestureIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "gestureIndex");
          final int _cursorIndexOfMappedWord = CursorUtil.getColumnIndexOrThrow(_cursor, "mappedWord");
          final MappingEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpLayoutId;
            _tmpLayoutId = _cursor.getInt(_cursorIndexOfLayoutId);
            final int _tmpGestureIndex;
            _tmpGestureIndex = _cursor.getInt(_cursorIndexOfGestureIndex);
            final String _tmpMappedWord;
            _tmpMappedWord = _cursor.getString(_cursorIndexOfMappedWord);
            _result = new MappingEntity(_tmpId,_tmpLayoutId,_tmpGestureIndex,_tmpMappedWord);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
