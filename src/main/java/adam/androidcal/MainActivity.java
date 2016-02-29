package adam.androidcal;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;
import android.widget.TextView;

import java.sql.Date;
import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

  private long emailCalID = 1;
  // The indices for the projection array above.
  /* private static final int PROJECTION_ID_INDEX = 0;
  private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
  private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
  private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
  */

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    Button queryButton = (Button) findViewById(R.id.query_button_id);
    Button insertButton = (Button) findViewById(R.id.insert_button_id);
    queryButton.setOnClickListener(this);
    insertButton.setOnClickListener(this);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
      }
    });

    getCalID();
  }

  private void insertEvent() {
    long startMillis = 0;
    long endMillis = 0;
    Calendar beginTime = Calendar.getInstance();
    beginTime.set(2016, 1, 28, 7, 30);
    startMillis = beginTime.getTimeInMillis();
    Calendar endTime = Calendar.getInstance();
    endTime.set(2016, 1, 28, 8, 45);
    endMillis = endTime.getTimeInMillis();

    ContentResolver cr = getContentResolver();
    ContentValues values = new ContentValues();
    values.put(Events.DTSTART, startMillis);
    values.put(Events.DTEND, endMillis);
    values.put(Events.TITLE, "InsertEventTest");
    values.put(Events.DESCRIPTION, "Group workout");
    values.put(Events.CALENDAR_ID, emailCalID);
    values.put(Events.ORGANIZER, "needtoconcentrateinschool@gmail.com");
    values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
    Uri uri = cr.insert(Events.CONTENT_URI, values);

// get the event ID that is the last element in the Uri
    long eventID = Long.parseLong(uri.getLastPathSegment());
//
// ... do something with event ID
//
//
    Log.i("myTag", "Inserted event with eventID: " + eventID);
  }

  private void queryEvent() {
    // Run query
    Cursor cur = null;
    ContentResolver cr = getContentResolver();
    /*Uri uri = Calendars.CONTENT_URI;
    String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
      + Calendars.ACCOUNT_TYPE + " = ?) AND ("
      + Calendars.OWNER_ACCOUNT + " = ?))";
    String[] selectionArgs = new String[] {"needtoconcentrateinschool@gmail.com", "com.google",
      "needtoconcentrateinschool@gmail.com"};*/

    //cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
    //cur = cr.query(uri, EVENT_PROJECTION, null, null, null);

    // Submit the query and get a Cursor object back.
    String eventSel= "((" + Calendars.DELETED + " != 1) AND (" + CalendarContract.Events.ORGANIZER + " = ?))";
    String[] eventSelArgs = new String[] {"needtoconcentrateinschool@gmail.com"};

    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    String[] EVENT_PROJ = new String[] {
      Events._ID,                           // 0
      Events.DTSTART,                 // 1
      Events.ORGANIZER,         // 2
      Events.TITLE,                 // 3
      Events.CALENDAR_ID,                           // 4
    };
    Uri eventUri = Events.CONTENT_URI;
    cur = cr.query(eventUri, EVENT_PROJ, eventSel, eventSelArgs, null);

    Log.i("myTag", "cur: " + cur.getCount());
    Log.i("myTag", "uri: " + eventUri.toString());

    // Use the cursor to step through the returned records
    while (cur.moveToNext()) {
      long calID = 0;
      String thirdCol = null;
      String secondCol = null;
      String fourthCol = null;
      String fifthCol = null;

      // Get the field values
      calID = cur.getLong(0);
      thirdCol = cur.getString(2);
      secondCol = cur.getString(1);
      fourthCol = cur.getString(3);
      fifthCol = cur.getString(4);

      Date d = new Date(Long.parseLong(secondCol));
      Log.i("myTag", "ID: " + calID + ", dtstart: " + d.toString()
            + ", organizer: " + thirdCol + ", title: " + fourthCol
            + ", CalendarID: " + fifthCol);

      // Do something with the values...
    }
    cur.close();
  }

  private void getCalID() {
    Cursor cur = null;
    ContentResolver cr = getContentResolver();
    Uri uri = Calendars.CONTENT_URI;
    /*String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
      + Calendars.ACCOUNT_TYPE + " = ?) AND ("
      + Calendars.OWNER_ACCOUNT + " = ?))";
    String[] selectionArgs = new String[] {"needtoconcentrateinschool@gmail.com", "com.google",
      "needtoconcentrateinschool@gmail.com"};*/
    String[] proj = {Calendars._ID};
    String[] selArgs = {"needtoconcentrateinschool@gmail.com"};

    cur = cr.query(uri, proj, Calendars.OWNER_ACCOUNT + " = ?", selArgs, null);

    // Use the cursor to step through the returned records
    while (cur.moveToNext()) {
      long calID = 0;

      // Get the field values
      calID = cur.getLong(0);
      emailCalID = calID;

      Log.i("myTag", "ID: " + calID);

      // Do something with the values...
      TextView idView = (TextView) findViewById(R.id.eventId);
      idView.setText("Corresponding Calendar_ID: " + String.valueOf(calID) + "\n"
        + "Default TimeZone: " + TimeZone.getDefault().getID());
    }
    cur.close();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      showInputActivity();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void showInputActivity() {
    Intent i = new Intent(this, InputActivity.class);
    startActivity(i);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.query_button_id:
        queryEvent();
        break;
      case R.id.insert_button_id:
        insertEvent();
        break;
      default:
        Log.i("myTag", "Unknown button clicked!");
    }
  }
}
