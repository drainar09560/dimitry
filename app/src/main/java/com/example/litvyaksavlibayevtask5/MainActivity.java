package com.example.litvyaksavlibayevtask5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Spinner;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "myLogs";
    final List<User> users = new ArrayList<>();
    DataAdapter adapter;
    Spinner spMark, spType;
    User currentUser;
    FrameLayout btnAdd, btnRead, btnClear, btnDelete, btnUpdate;
    EditText etModel, etVintage, etCounter, etReg;
    final int DBVersion = 2; // версия БД


    DBHelper dbHelper;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new DataAdapter(this, users);
        etModel = findViewById(R.id.etModel);
        etVintage = findViewById(R.id.etVintage);
        etCounter = findViewById(R.id.etCounter);
        etReg = findViewById(R.id.etReg);
        spMark = findViewById(R.id.spMark);
        spType = findViewById(R.id.spType);

        adapter.setSelectElementListener(new DataAdapter.SelectElementListener(){

            @Override
            public void selectElement(User user) {
                currentUser = user;

                List<String> car = Arrays.asList(getResources().getStringArray(R.array.mark_arr));

                spMark.setSelection(car.indexOf(user.getMark()));
                etModel.setText(user.getModel());

                List<String> type = Arrays.asList(getResources().getStringArray(R.array.type_arr));

                spType.setSelection(type.indexOf(user.getType()));
                etVintage.setText(String.valueOf(user.getVintage()));
                etCounter.setText(String.valueOf(user.getCounter()));
                etReg.setText(String.valueOf(user.getReg()));
            }
        });
        RecyclerView recyler = findViewById(R.id.listItem);
        recyler.setAdapter(adapter);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);

    }

    public void onClick(View v) {

        // создаем объект для данных
        ContentValues cv = new ContentValues();

        // получаем данные из полей ввода
        String mark = spMark.getSelectedItem().toString();
        String model = etModel.getText().toString();
        String type = spType.getSelectedItem().toString();
        String vintage = etVintage.getText().toString();
        String counter = etCounter.getText().toString();
        String reg = etReg.getText().toString();

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (v.getId()) {
            case R.id.btnAdd:
                Log.d(LOG_TAG, "--- Insert in mytable: ---");
                // подготовим данные для вставки в виде пар: наименование столбца - значение

                cv.put("mark", mark);
                cv.put("model", model);
                cv.put("type", type);
                cv.put("vintage", vintage);
                cv.put("counter", counter);
                cv.put("reg", reg);
                // вставляем запись и получаем ее ID
                long rowID = db.insert("mytable", null, cv);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                readBd(db);
                break;
            case R.id.btnRead:
                readBd(db);
                break;
            case R.id.btnClear:
                Log.d(LOG_TAG, "--- Clear mytable: ---");
                // удаляем все записи
                int clearCount = db.delete("mytable", null, null);
//                db.endTransaction();
                Log.d(LOG_TAG, "deleted rows count = " + clearCount);
                users.clear();
                adapter.notifyDataSetChanged();

//                dbHelper = new DBHelper(this);
//                   readBd(dbHelper.getWritableDatabase());
                break;
            case R.id.btnDelete:
                if (currentUser == null) {
                    break;
                }
                Log.d(LOG_TAG, "--- Delete from mytable: ---");
                // удаляем по id
                int delCount = db.delete("mytable", "id = " + currentUser.getId(), null);
                Log.d(LOG_TAG, "deleted rows count = " + delCount);
                readBd(db);
                break;

            case R.id.btnUpdate:
                if (currentUser == null) {
                    break;
                }
                Log.d(LOG_TAG, "--- Update mytable: ---");
                // подготовим значения для обновления
                cv.put("mark", mark);
                cv.put("model", model);
                cv.put("type", type);
                cv.put("vintage", vintage);
                cv.put("counter", counter);
                cv.put("reg", reg);
                // обновляем по id
                int updCount = db.update("mytable", cv, "id = ?",
                        new String[] {String.valueOf(currentUser.getId())});
                Log.d(LOG_TAG, "updated rows count = " + updCount);
                readBd(db);
                break;

        }
        // закрываем подключение к БД
        dbHelper.close();
    }

    private void readBd(SQLiteDatabase db) {
        users.clear();
        Log.d(LOG_TAG, "--- Rows in mytable: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        String orderBy ="";
        String NAME_COLUMN = "name";
        String EMAIL_COLUMN = "email";
        String AGE_COLUMN = "age";
        String DESC = " DESC";
        String selection = null;
        String[] selectionArgs = null;


// делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("mytable", null, selection, selectionArgs, null, null, orderBy);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int markColIndex = c.getColumnIndex("mark");
            int modelColIndex = c.getColumnIndex("model");
            int typeColIndex = c.getColumnIndex("type");
            int vintageColIndex = c.getColumnIndex("vintage");
            int counterColIndex = c.getColumnIndex("counter");
            int regColIndex = c.getColumnIndex("reg");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) +
                                ", mark = " + c.getString(markColIndex) +
                                ", model = " + c.getString(modelColIndex) +
                                ", type = " + c.getString(typeColIndex) +
                                ", vintage = " + c.getInt(vintageColIndex) +
                                ", counter = " + c.getInt(counterColIndex) +
                                ", reg = " + c.getInt(regColIndex));
                users.add(new User(c.getString(markColIndex), c.getString(modelColIndex), c.getString(typeColIndex), c.getInt(vintageColIndex), c.getInt(counterColIndex), c.getInt(regColIndex), c.getInt(idColIndex)));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
            adapter.notifyDataSetChanged();
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "myDB", null, DBVersion);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "mark text,"
                    + "model text,"
                    + "type text,"
                    + "vintage integer,"
                    + "counter integer,"
                    + "reg integer"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //если был совершен переход от первой версии ко второй

        }

    }
}