package ayds.newyork.songinfo.moredetails.fulllogic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class DataBase extends SQLiteOpenHelper {
  private static final String DATABASE_NAME = "dictionary.db";
  private static final int DATABASE_VERSION = 1;

  public DataBase(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }
  @Override
  public void onCreate(SQLiteDatabase database) {
    createTable(database);
    Log.i(this.getClass().getSimpleName(), "Database created.");
  }
  @Override
  public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {  }
  private void createTable(SQLiteDatabase database) {
    String createTableSql =
      "CREATE TABLE IF NOT EXISTS artists ("
      + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
      + "name TEXT NOT NULL, "
      + "info TEXT NOT NULL, "
      + "source INTEGER NOT NULL)";
    database.execSQL(createTableSql);
  }
  public static void saveArtist(DataBase dbHelper, String artist, String info)
  {
    SQLiteDatabase database = dbHelper.getWritableDatabase();
    database.insert("artists", null, getValues(artist,info));
    database.close();
  }
  public static ContentValues getValues(String artist, String info) {
    ContentValues values = new ContentValues();
    values.put("name", artist);
    values.put("info", info);
    values.put("source", 1);
    return values;
  }
  public static String getInfo(DataBase dbHelper, String artist)
  {
    SQLiteDatabase database = dbHelper.getReadableDatabase();
    String[] columns = {
      "id",
      "name",
      "info"
    };
    String where = "name  = ?";
    String[] whereArgs = { artist };
    String sortOrder = "name DESC";
    Cursor cursor = makeQuery(database,"artists",columns,where,whereArgs,null,null,sortOrder);
    List<String> items = addItems(cursor);
    cursor.close();
    if(items.isEmpty())
      return null;
    else return items.get(0);
  }
  private static Cursor makeQuery(SQLiteDatabase database, String table, String[] columns, String where, String[] whereArgs, String group, String filter, String sortOrder ) {
    Cursor cursor = database.query(
      table,
      columns,
      where,
      whereArgs,
      group,
      filter,
      sortOrder
    );
    return cursor;
  }
  private static List<String> addItems(Cursor cursor) {
    List<String> items = new ArrayList<>();
    while(cursor.moveToNext()) {
      String info = cursor.getString(cursor.getColumnIndexOrThrow("info"));
      items.add(info);
    }
    return items;
  }
  public static void testDataBase()
  {
    Connection connection = null;
    try
    {
      connection = DriverManager.getConnection("jdbc:sqlite:./dictionary.db");
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);
      ResultSet artists = statement.executeQuery("select * from artists");
      while(artists.next())
      {
        System.out.println("id = " + artists.getInt("id") + "\n" +
                           "name = " + artists.getString("name") + "\n" +
                           "info = " + artists.getString("info") + "\n" +
                           "source = " + artists.getString("source") );
      }
    }
    catch(SQLException e)
    {
      System.err.println(e.getMessage());
    }
    finally
    {
      try
      {
        if(connection != null)
          connection.close();
      }
      catch(SQLException e)
      {
        System.err.println(e);
      }
    }
  }



}
