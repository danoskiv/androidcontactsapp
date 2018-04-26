package com.example.vladimir.croappwebapi.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.vladimir.croappwebapi.models.Award;
import com.example.vladimir.croappwebapi.models.Contact;
import com.example.vladimir.croappwebapi.models.Question;
import com.example.vladimir.croappwebapi.models.User;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Vladimir on 3/17/2018.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String MyPREFERENCES = "MyPrefs" ;
    private Context appContext;
    private SharedPreferences sharedPreferences;
    private int DATABASE_VERSION;
    private static final String DATABASE_NAME = "CroApp.db";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_QUESTIONS = "questions";
    private static final String TABLE_CONTACTS = "contacts";
    private static final String TABLE_AWARDS = "awards";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_CREATED = "created_at";
    private static final String COLUMN_PID = "pid";
    private static final String COLUMN_QID = "qid";
    private static final String COLUMN_AID = "aid";
    private static final String COLUMN_CORRECT = "correct";
    private static final String COLUMN_BODY = "body";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE_NUMBER = "number";
    private static final String COLUMN_AWARD_NAME = "name";
    private static final String COLUMN_AWARD_COUNT = "value";
    private static final String COLUMN_CONTACT_NAME = "name";
    private static final String COLUMN_FLAG = "flag";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_USER_NAME + " VARCHAR(250) NOT NULL, "
            + COLUMN_USERNAME + " VARCHAR(250) NOT NULL, "
            + COLUMN_USER_PASSWORD + " TEXT NOT NULL, "
            + COLUMN_STATUS + " INTEGER DEFAULT 1, "
            + COLUMN_CREATED + " DATETIME);";

    private static final String CREATE_TABLE_QUESTIONS = "CREATE TABLE " + TABLE_QUESTIONS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_PID + " INTEGER, "
            + COLUMN_QID + " INTEGER, "
            + COLUMN_AID + " INTEGER, "
            + COLUMN_CORRECT + " INTEGER DEFAULT 1, "
            + COLUMN_BODY + " VARCHAR(250) NOT NULL, "
            + COLUMN_STATUS + " INTEGER DEFAULT 1, "
            + COLUMN_CREATED + " DATETIME, "
            + COLUMN_USER_ID + " INTEGER, "
            + "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ")" +
            ");";

    private static final String CREATE_TABLE_CONTACTS = "CREATE TABLE " + TABLE_CONTACTS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_CONTACT_NAME + " VARCHAR(256) NOT NULL, "
            + COLUMN_EMAIL + " VARCHAR(256), "
            + COLUMN_PHONE_NUMBER + " VARCHAR(20), "
            + COLUMN_FLAG + " INTEGER DEFAULT 0, "
            + COLUMN_USER_ID + " INTEGER NOT NULL, "
            + "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ")" +
            ");";

    private static final String CREATE_TABLE_AWARDS = "CREATE TABLE " + TABLE_AWARDS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL, "
            + COLUMN_AWARD_NAME + " VARCHAR(250) NOT NULL, "
            + "pid INTEGER NOT NULL, "
            + COLUMN_AWARD_COUNT + " INTEGER NOT NULL);";

    private static final String DROP_TABLES = "DROP TABLE " + TABLE_USERS + ";" +
            "DROP TABLE " + TABLE_QUESTIONS + ";" +
            "DROP TABLE " + TABLE_CONTACTS + ";" +
            "DROP TABLE " + TABLE_AWARDS + ";";

    private static final String TAG = "DBHELPER";

    public DbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        DATABASE_VERSION = sharedPreferences.getInt("count", 1);
        appContext = context;
    }

    public DbHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        DATABASE_VERSION = sharedPreferences.getInt("count", 1);
        appContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sharedPreferences = appContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        DATABASE_VERSION = sharedPreferences.getInt("count", 1);
        sqLiteDatabase.execSQL(CREATE_TABLE_USERS);
        sqLiteDatabase.execSQL(CREATE_TABLE_QUESTIONS);
        sqLiteDatabase.execSQL(CREATE_TABLE_CONTACTS);
        sqLiteDatabase.execSQL(CREATE_TABLE_AWARDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(newVersion > oldVersion)
        {
            sharedPreferences = appContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            DATABASE_VERSION = sharedPreferences.getInt("count", 1);
            sqLiteDatabase.execSQL("DROP TABLE users");
            sqLiteDatabase.execSQL("DROP TABLE questions");
            sqLiteDatabase.execSQL("DROP TABLE awards");
            sqLiteDatabase.execSQL(CREATE_TABLE_USERS);
            sqLiteDatabase.execSQL(CREATE_TABLE_QUESTIONS);
            sqLiteDatabase.execSQL(CREATE_TABLE_AWARDS);
        }
    }

    public boolean addUser(User u) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, u.getId());
            values.put(COLUMN_USER_NAME, u.getName());
            values.put(COLUMN_USERNAME, u.getUsername());
            values.put(COLUMN_USER_PASSWORD, u.getPassword());
            values.put(COLUMN_STATUS, u.getStatus());
            values.put(COLUMN_CREATED, u.getCreated());

            long rowInserted = db.insert(TABLE_USERS, null, values);
        boolean result;
        if (rowInserted != -1)
            {
                result = true;
            } else {
                result = false;
            }
            db.close();
        return result;
    }

    public boolean addQuestion(Question q)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, q.getId());
        values.put(COLUMN_PID, q.getPid());
        values.put(COLUMN_QID, q.getQid());
        values.put(COLUMN_AID, q.getAid());
        values.put(COLUMN_CORRECT, q.getCorrect());
        values.put(COLUMN_BODY, q.getBody());
        values.put(COLUMN_STATUS, q.getStatus());
        values.put(COLUMN_CREATED, q.getCreated());
        values.put(COLUMN_USER_ID, q.getUserId());

        long rowInserted = db.insert(TABLE_QUESTIONS, null, values);
        boolean result;
        if(rowInserted != -1) {
            result = true;
        } else {
            result = false;
        }
        db.close();
        return result;
    }

    public boolean addContact(Contact c)
    {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_NAME, c.getName());
        values.put(COLUMN_EMAIL, c.getEmail());
        values.put(COLUMN_PHONE_NUMBER, c.getNumber());
        values.put(COLUMN_USER_ID, c.getUserId());

        long rowInserted = db.insert(TABLE_CONTACTS, null, values);
        if(rowInserted != -1)
        {
            result = true;
        } else {
            result = false;
        }
        db.close();

        return result;
    }

    public boolean addAward(Award a)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, a.getId());
        values.put(COLUMN_AWARD_NAME, a.getName());
        values.put(COLUMN_AWARD_COUNT, a.getValue());
        values.put(COLUMN_PID, a.getPid());

        long rowInserted = db.insert(TABLE_AWARDS, null, values);
        boolean result;
        if(rowInserted!=-1)
        {
            result = true;
        } else {
            result = false;
        }
        db.close();
        return result;
    }

    public int getLatestContactId()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        int number = 0;
        String statement = "SELECT id FROM contacts ORDER BY id DESC LIMIT 1";
        Cursor cursor = db.rawQuery(statement, null);
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                 number = cursor.getInt(0);
            }

            cursor.close();
        }
        return number;
    }


    public User getUser(String username)
    {
        String[] columns = {
                COLUMN_ID,
                COLUMN_USER_NAME,
                COLUMN_USERNAME,
                COLUMN_USER_PASSWORD
        };

        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        SQLiteDatabase db = this.getReadableDatabase();
        User user = new User();

        Cursor cursor = db.query(TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
                );
        if(cursor!=null)
        {
            if(cursor.moveToFirst())
            {
                do {
                    user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
                    user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                    user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
                    user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        db.close();
        return user;
    }

    public Question getRandomQuestion(int pid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Question question = new Question();

        Integer random;
        int number = 0;

        //get random question id
        String selectionArgs[] = {Integer.toString(pid)};
        String selection = "SELECT COUNT(DISTINCT " + COLUMN_QID + ") FROM " + TABLE_QUESTIONS + " WHERE pid = ?";
        Cursor cursor  = db.rawQuery(selection, selectionArgs);
        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                number = cursor.getInt(0);
                Log.d(TAG, "Number of questions: " + number);
            }
            cursor.close();
        }

        random = new Random().nextInt(number) + 1;

        Log.d(TAG, "Random number: " + random);

        String selection2 = "SELECT pid, qid, body FROM questions WHERE qid = ? AND pid = ? LIMIT 1";
        Log.d(TAG, "toStringMethod: " + Integer.toString(random));
        Cursor cursor1 = db.rawQuery(selection2, new String[]{Integer.toString(random), Integer.toString(pid)});
        //get question
        if(cursor1.moveToFirst())
        {
                question.setPid(Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(COLUMN_PID))));
                question.setQid(Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(COLUMN_QID))));
                question.setBody(cursor1.getString(cursor1.getColumnIndex(COLUMN_BODY)));
        }

        Log.d(TAG, "Question ID: " + question.getQid());
        Log.d(TAG, "Question body: " + question.getBody());
        cursor1.close();
        db.close();
        return question;

    }

    public List<Question> getAllAnswers(int qid, int pid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Question> answers = new ArrayList<>();

        String[] selectionArgs = {Integer.toString(qid), Integer.toString(pid)};
        String selection = "SELECT qid, aid, body, correct FROM questions WHERE aid != 0 AND qid=? AND pid=?";
        Cursor cursor = db.rawQuery(selection, selectionArgs);

        if (cursor!=null)
        {
            if(cursor.moveToFirst())
            {
                do {
                    Question answer = new Question();
                    Log.d(TAG, "qidFromCursor: " + cursor.getString(cursor.getColumnIndex(COLUMN_QID)));
                    Log.d(TAG, "aidFromCursor: " + cursor.getString(cursor.getColumnIndex(COLUMN_AID)));
                    Log.d(TAG, "bodyFromCursor: " + cursor.getString(cursor.getColumnIndex(COLUMN_BODY)));
                    answer.setQid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_QID))));
                    answer.setAid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_AID))));
                    answer.setBody(cursor.getString(cursor.getColumnIndex(COLUMN_BODY)));
                    answer.setCorrect(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CORRECT))));
                    Log.d(TAG, "answerQid: " + answer.getQid());
                    Log.d(TAG, "answerAid: " + answer.getAid());
                    Log.d(TAG, "answerBody: " + answer.getBody());
                    answers.add(answer);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        db.close();

        return answers;
    }

    public boolean checkAward(int pid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String statement = "SELECT COUNT(DISTINCT id), value FROM awards WHERE pid = ?";
        int number = 0;
        int value = 0;

        Cursor cursor = db.rawQuery(statement, new String[]{Integer.toString(pid)});
        if(cursor!= null)
        {
            if(cursor.moveToFirst())
            {
                number = cursor.getInt(0);
                value = cursor.getInt(1);
            }
        }
        cursor.close();
        db.close();
        if(number > 0 && value > 0)
        {
            return true;
        }
        else
            return false;
    }

    public Award getRandomAward(int pid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Award award = new Award();
        int id = 0;
        Integer random;


        String statement = "SELECT COUNT(DISTINCT id) FROM awards WHERE value != 0 AND pid = ?";
        Cursor cursor = db.rawQuery(statement, new String[]{Integer.toString(pid)});

        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                id = cursor.getInt(0);
                Log.d(TAG, "idValue: " + id);
            }
            cursor.close();
        }
        int i = 0;
        String[] niza = new String[id];
        String statement3 = "SELECT id FROM awards WHERE pid = ?";
        Cursor cursor2 = db.rawQuery(statement3, new String[] {Integer.toString(pid)});
        if(cursor2!=null)
        {
            if(cursor2.moveToFirst())
            {
                do {
                    niza[i++] = cursor2.getString(cursor2.getColumnIndex(COLUMN_ID));
                } while (cursor2.moveToNext());
            }
        }

        random = new Random().nextInt(id);
        if(random == 0)
        {
            random = 1;
        }
        Log.d(TAG, "Random: " + random);

        String statement2 = "SELECT * FROM awards WHERE id = ? AND pid = ? AND value != 0";
        Cursor cursor1 = db.rawQuery(statement2, new String[]{niza[random - 1], Integer.toString(pid)});
        if (cursor1 != null)
        {
            if(cursor1.moveToFirst())
            {
                award.setId(Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(COLUMN_ID))));
                award.setName(cursor1.getString(cursor1.getColumnIndex(COLUMN_AWARD_NAME)));
                award.setValue(Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(COLUMN_AWARD_COUNT))));
                award.setPid(Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(COLUMN_PID))));
            }
        }
        cursor1.close();
        db.close();

        return award;
    }

    public long getAwardCount()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        long number = 0;
        String statement = "SELECT COUNT(DISTINCT id) FROM awards";

        Cursor cursor = db.rawQuery(statement, null);
        if(cursor!=null)
        {
            if(cursor.moveToFirst())
            {
                number = cursor.getInt(0);
            }
        }
        cursor.close();
        db.close();

        return number;
    }

    public long getContactsCount()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        long number = 0;
        String statement = "SELECT COUNT(DISTINCT id) FROM contacts";

        Cursor cursor = db.rawQuery(statement, null);
        if(cursor!=null)
        {
            if(cursor.moveToFirst())
            {
                number = cursor.getInt(0);
            }
        }
        cursor.close();
        db.close();

        return number;
    }

    public Contact getContactById(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String statement = "SELECT * FROM contacts WHERE id = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Contact contact = new Contact();

        Cursor cursor = db.rawQuery(statement, selectionArgs);
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                do {
                    contact = new Contact();
                    contact.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
                    contact.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)));
                    contact.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                    contact.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER)));
                    contact.setUserId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                    contact.setFlag2(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_FLAG))));
                } while(cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();

        return contact;
    }

    public Award getAwardById(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String statement = "SELECT * FROM awards WHERE id = ?";
        String[] selectionArgs = {Integer.toString(id)};
        Award award = new Award();

        Cursor cursor = db.rawQuery(statement, selectionArgs);
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                do {
                    award.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
                    award.setName(cursor.getString(cursor.getColumnIndex(COLUMN_AWARD_NAME)));
                    award.setValue(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_AWARD_COUNT))));
                    award.setPid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PID))));
                } while(cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();

        return award;
    }

    public Cursor queryData(String sql)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    public List<String> getAllCategories()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT body FROM questions WHERE qid = 0 AND aid = 0";

        List<String> categories = new ArrayList<String>();
        Cursor cursor = db.rawQuery(sql, null);
        Log.d(TAG, "COLUMN COUNT: " + cursor.getColumnCount());

        if(cursor.moveToFirst())
        {
            do {
                Log.d(TAG, "text" + cursor.getString(0));
                categories.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return categories;
    }

    public void decrementValue(Award award)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = award.getId();
        int value = award.getValue();
        int newValue = value - 1;
        String name = award.getName();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_AWARD_NAME, name);
        values.put(COLUMN_AWARD_COUNT, newValue);

        db.update(TABLE_AWARDS, values, COLUMN_ID + " = ?", new String[] {String.valueOf(id)});
        db.close();
    }

    public User getUserById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = new User();
        String statement = "SELECT * FROM users WHERE id = ?";
        Cursor cursor = db.rawQuery(statement, new String[]{Integer.toString(id)});

        if(cursor.moveToFirst())
        {
            user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
            user.setStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS)));
            user.setCreated(cursor.getString(cursor.getColumnIndex(COLUMN_CREATED)));
        }
        cursor.close();
        db.close();
        return user;
    }

    public void updateContact(int id) {
        Contact contact = getContactById(id);
        Log.d(TAG, "Before: " + (contact.getFlag()));

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FLAG, 1);

        int row = db.update(TABLE_CONTACTS,values,COLUMN_ID + " = " + id, null);
        contact.setFlag();
        Log.d(TAG, "After: " + contact.getFlag());
        Log.d(TAG, "Row: " + row);
        db.close();
    }

    public int getPidByName(String pid) {
        SQLiteDatabase db = this.getReadableDatabase();

        int number = 0;
        String statement = "SELECT pid FROM questions WHERE body = ? LIMIT 1";
        String[] selArgs = {pid};

        Cursor cursor = db.rawQuery(statement, selArgs);
        if(cursor!=null)
        {
            if(cursor.moveToFirst())
            {
                number = cursor.getInt(0);
            }
        }
        cursor.close();
        db.close();

        return number;
    }

    public List<Contact> getAllContacts()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Contact> contacts = new ArrayList<Contact>();
        String statement = "SELECT * FROM contacts";
        Cursor cursor = db.rawQuery(statement, null);
        if(cursor!=null)
        {
            if(cursor.moveToFirst())
            {
                do {
                    Contact contact = new Contact();
                    contact.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME)));
                    contact.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                    contact.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER)));
                    contact.setUserId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));

                    contacts.add(contact);
                } while (cursor.moveToNext());
            }
        }

        return contacts;
    }

    public String getCategoryByPid(int pid) {
        SQLiteDatabase db = this.getReadableDatabase();
        String statement = "SELECT body FROM questions WHERE pid = ? AND aid = 0 AND qid = 0";
        String[] args = {Integer.toString(pid)};
        String name = null;

        Cursor cursor = db.rawQuery(statement, args);
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                name = cursor.getString(0);
            }
        }
        cursor.close();
        db.close();

        return name;
    }

    public List<Award> getAllAwards() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Award> awards = new ArrayList<Award>();

        String statement = "SELECT * FROM awards";
        Cursor cursor = db.rawQuery(statement, null);
        if(cursor!=null)
        {
            if (cursor.moveToFirst())
            {
                do {
                    Award award = new Award();
                    award.setName(cursor.getString(cursor.getColumnIndex(COLUMN_AWARD_NAME)));
                    award.setPid(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PID))));
                    award.setValue(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_AWARD_COUNT))));

                    awards.add(award);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();

        return awards;
    }

    public boolean matching(String orig, String compare){
        String md5 = null;
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(compare.getBytes());
            byte[] digest = md.digest();
            md5 = new BigInteger(1, md.digest()).toString(32);

            Log.d(TAG, "FROM FUNCTION: " + md5);

            return md5.equals(orig);

        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    public boolean md5(String string, String password) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {
            int i = (b & 0xFF);
            if (i < 0x10) hex.append('0');
            hex.append(Integer.toHexString(i));
        }

        String newPass = hex.toString();
        return newPass.equals(password);
    }
}
