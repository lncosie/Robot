package com.lncosie.robot.orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lncosie.robot.log.BookSendUriList;
import com.lncosie.robot.log.FetchList;
import com.lncosie.robot.log.RedoUserList;
import com.lncosie.robot.log.Robot;
import com.lncosie.robot.log.SuccessList;
import com.lncosie.robot.log.TodoList;

import java.lang.reflect.Field;
import java.util.Iterator;

/**
 * Created by Administrator on 2016/4/21.
 */
public class Orm {
    static SqliteHelper db;

    public synchronized static void OrmInit(Context context) {
        db = new SqliteHelper(context, "log.db");
        Class<?> orm[] = new Class[]{
                BookSendUriList.class,
                FetchList.class,
                SuccessList.class,
                RedoUserList.class,
                Robot.class
        };
        for (Class<?> cls : orm) {
            execSql(getTableCreateSchma(cls));
        }
        createTodoView(TodoList.class);
    }

    private static void createTodoView(Class<?> view) {
        View meta=view.getAnnotation(View.class);
        String name=meta.value();
        String select=meta.select();
        StringBuilder builder=new StringBuilder("create view if not exists ");
        builder.append(name);
        builder.append(" as ");
        builder.append(select);
        execSql(builder.toString());
    }

    public static boolean delete(Object remove) {
        Class<?> cls = remove.getClass();
        for (Field field : cls.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                try {
                    String[] args = new String[]{field.get(remove).toString()};
                    return db.getWritableDatabase().delete(getTableName(cls), "id=?", args)>0;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
        return false;
    }
    public static boolean delete(Class<?> table,String whereClause, String[] whereArgs) {
        return db.getWritableDatabase().delete(getTableName(table), whereClause, whereArgs)>0;

    }
    public static boolean save(Object save) {
        ContentValues values = new ContentValues();
        try {
            Field key = null;
            for (Field field : save.getClass().getFields()) {
                Object value = field.get(save);
                if (field.getAnnotation(Id.class) != null) {
                    key = field;
                    if (value != null)
                        values.put(field.getName(), (long) value);
                } else if (value != null)
                    values.put(field.getName(), value.toString());
            }
            Object id=key.get(save);
            if (id==null) {
                long index= getWriter().insert(getTableName(save.getClass()), null, values);
                key.set(save, index);
            } else {
                getWriter().replace(getTableName(save.getClass()), null, values);
            }
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static <T> Iterator<T> load(final Class<T> cls,String where, String[] whereArgs) {
        StringBuilder select=new StringBuilder();
        select.append("Select * from ");
        select.append(getTableName(cls));
        if(where!=null){
            select.append(" where ");
            select.append(where);
        }
        select.append(";");
        final Cursor cursor = getReader().rawQuery(select.toString(), whereArgs);
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                boolean hasMore = cursor.moveToNext();
                if (hasMore == false) {
                    cursor.close();
                }
                return hasMore;
            }

            @Override
            public T next() {
                try {
                    T value = cls.newInstance();
                    for (Field field : cls.getFields()) {
                        int idx = cursor.getColumnIndex(field.getName());
                        if (idx >= 0) {
                            if (field.getAnnotation(Id.class) != null) {
                                long key = cursor.getLong(idx);
                                field.set(value, key);
                            } else {
                                String col = cursor.getString(idx);
                                field.set(value, col);
                            }

                        }

                    }
                    return value;
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void remove() {

            }
        };
    }
    public static void clear(Class<?> cls) {
        String clear="delete from "+getTableName(cls)+";";
        execSql(clear);
    }
    private static String getTableName(Class<?> cls) {
        Table table=cls.getAnnotation(Table.class);
        if(table!=null)
            return table.value();
        else{
            View view=cls.getAnnotation(View.class);
            return view.value();
        }
    }

    private static String getTableCreateSchma(Class<?> cls) {
        String keyname = null;
        StringBuilder builder = new StringBuilder("create table if not exists ");
        builder.append(getTableName(cls));
        builder.append("(");
        for (Field field : cls.getFields()) {
            if (field.getName().charAt(0) == '$')
                continue;
            if (field.getAnnotation(Id.class) != null) {
                keyname = field.getName();
            } else {
                builder.append(field.getName());
                builder.append(" Text,");
            }
        }
        if (keyname == null)
            keyname = "id";
        builder.append(keyname);
        builder.append(" integer primary key);");

        return builder.toString();
    }

    private static void execSql(String sql) {
        try {
            getWriter().execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SQLiteDatabase getWriter() {
        return db.getWritableDatabase();
    }

    private static SQLiteDatabase getReader() {
        return db.getReadableDatabase();
    }
}
