package com.example.multsentender;

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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "myLogs";
    final List<User> users = new ArrayList<>();
    DataAdapter adapter;
    Spinner spSex, spSmena;
    User currentUser;
    FrameLayout btnAdd, btnRead, btnClear, btnDelete, btnUpdate;
    EditText etName, etSurname, etMiddle;
    final int DBVersion = 2; // версия БД


    DBHelper dbHelper;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new DataAdapter(this, users);
        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etMiddle = findViewById(R.id.etMiddleName);
        spSex = findViewById(R.id.spSex);
        spSmena = findViewById(R.id.spSmena);

        adapter.setSelectElementListener(new DataAdapter.SelectElementListener(){

            @Override
            public void selectElement(User user) {
                currentUser = user;

                List<String> smena = Arrays.asList(getResources().getStringArray(R.array.working_shift));

                spSmena.setSelection(smena.indexOf(user.getSmena()));
                etName.setText(user.getName());

                List<String> sex = Arrays.asList(getResources().getStringArray(R.array.sex));

                spSex.setSelection(sex.indexOf(user.getSex()));
                etSurname.setText(String.valueOf(user.getSurname()));
                etMiddle.setText(String.valueOf(user.getMiddle()));
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
        String name = etName.getText().toString();
        String surname = etSurname.getText().toString();
        String middlename = etMiddle.getText().toString();
        String sex = spSex.getSelectedItem().toString();
        String smena = spSmena.getSelectedItem().toString();

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (v.getId()) {
            case R.id.btnAdd:
                Log.d(LOG_TAG, "--- Insert in mytable: ---");
                // подготовим данные для вставки в виде пар: наименование столбца - значение

                cv.put("name", name);
                cv.put("surname", surname);
                cv.put("middlename", middlename);
                cv.put("sex", sex);
                cv.put("smena", smena);
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
                cv.put("name", name);
                cv.put("surname", surname);
                cv.put("middlename", middlename);
                cv.put("sex", sex);
                cv.put("smena", smena);
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
            int nameColIndex = c.getColumnIndex("name");
            int surnameColIndex = c.getColumnIndex("surname");
            int middlenameColIndex = c.getColumnIndex("middlename");
            int sexColIndex = c.getColumnIndex("sex");
            int smenaColIndex = c.getColumnIndex("smena");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) +
                                ", name = " + c.getString(nameColIndex) +
                                ", surname = " + c.getString(surnameColIndex) +
                                ", middlename = " + c.getString(middlenameColIndex) +
                                ", sex = " + c.getInt(sexColIndex) +
                                ", smena = " + c.getInt(smenaColIndex));
                users.add(new User(c.getString(nameColIndex), c.getString(surnameColIndex), c.getString(middlenameColIndex), c.getString(sexColIndex), c.getString(smenaColIndex), c.getInt(idColIndex)));
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
                    + "name text,"
                    + "surname text,"
                    + "middlename text,"
                    + "sex integer,"
                    + "smena integer"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //если был совершен переход от первой версии ко второй

        }

    }
}