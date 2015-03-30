package com.example.jephillips.sql_lite_test;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.example.jephillips.sql_lite_test.R.id;


public class MainActivity extends ActionBarActivity {

    SQLiteDatabase contactsDB;

    @InjectView(id.createDatabaseButton) Button createDBButton;
    @InjectView(id.addContactButton) Button addContactButton;
    @InjectView(id.getContactButton) Button getContactButton;
    @InjectView(id.deleteContactButton) Button deleteContactButton;
    @InjectView(id.deleteDatabaseButton) Button deleteDBButton;

    @InjectView(id.nameInput) EditText nameEditText;
    @InjectView(id.emailInput) EditText emailEditText;
    @InjectView(id.deleteIDInput) EditText idEditText;
    @InjectView(id.contactList) EditText contactListEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

    }

    public void addContact(View view) {
        String contactName = nameEditText.getText().toString();
        String contactEmail = emailEditText.getText().toString();
        contactsDB.execSQL("INSERT INTO contacts (name, email) VALUES ('"+
        contactName + "', '" + contactEmail + "');");
    }

    public void createDatabase(View view) {

        try{
            contactsDB = this.openOrCreateDatabase("MyContacts", MODE_PRIVATE, null);
            contactsDB.execSQL("CREATE TABLE IF NOT EXISTS contacts " + "(id integer primary key," +
                    " name VARCHAR, email VARCHAR);");
        } catch(Exception e) {
            Log.e("Contacts Error", "Error creating DB");
        }

        addContactButton.setClickable(true);
        deleteContactButton.setClickable(true);
        getContactButton.setClickable(true);
        deleteDBButton.setClickable(true);
    }

    public void getContacts(View view) {

        Cursor cursor = contactsDB.rawQuery("SELECT * FROM contacts", null);
        int idColumn = cursor.getColumnIndex("id");
        int nameColumn = cursor.getColumnIndex("name");
        int emailColumn = cursor.getColumnIndex("email");
        cursor.moveToFirst();
        String contactList = "";

        if(cursor != null && (cursor.getCount() > 0)){

            do{
                String id = cursor.getString(idColumn);
                String name = cursor.getString(nameColumn);
                String email = cursor.getString(emailColumn);
                contactList = contactList + id + ":" + name + ":" + email + "\n";

            } while(cursor.moveToNext());

            contactListEditText.setText(contactList);
        } else {
            Toast.makeText(this, "No results to show", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteContact(View view) {
        String id = idEditText.getText().toString();
        contactsDB.execSQL("DELETE FROM contacts WHERE id = " + id + ";");
    }

    public void deleteDatabase(View view) {
        this.deleteDatabase("MyContacts");
    }

    @Override
    protected void onDestroy() {
        contactsDB.close();
        super.onDestroy();
    }
}
