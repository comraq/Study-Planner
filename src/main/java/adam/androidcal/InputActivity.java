package adam.androidcal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.sql.Date;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;

/**
 * Created by adam on 28/02/16.
 */
public class InputActivity extends AppCompatActivity implements
  View.OnClickListener, TimePickerFragment.TimePickerListener,
  DatePickerFragment.DatePickerListener {

  private TextView startTime, endTime;
  private TextView startDate, endDate;
  private NumberPicker studyLength;
  private Button submitButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.input_activity);

    startDate = (TextView) findViewById(R.id.text_view_start_date);
    startTime = (TextView) findViewById(R.id.text_view_start_time);
    endDate = (TextView) findViewById(R.id.text_view_end_date);
    endTime = (TextView) findViewById(R.id.text_view_end_time);
    studyLength = (NumberPicker) findViewById(R.id.number_picker_study_length);
    submitButton = (Button) findViewById(R.id.button_submit);

    startDate.setOnClickListener(this);
    startTime.setOnClickListener(this);
    endDate.setOnClickListener(this);
    endTime.setOnClickListener(this);
    submitButton.setOnClickListener(this);

    studyLength.setMinValue(1);
    studyLength.setMaxValue(10);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.text_view_start_time:
        TimePickerFragment startTimePicker = TimePickerFragment.newInstance(R.id.text_view_start_time);
        startTimePicker.show(getFragmentManager(), "startTimePicker");
        break;
      case R.id.text_view_start_date:
        DatePickerFragment startDatePicker = DatePickerFragment.newInstance(R.id.text_view_start_date);
        startDatePicker.show(getFragmentManager(), "startDatePicker");
        break;
      case R.id.text_view_end_time:
        TimePickerFragment endTimePicker = TimePickerFragment.newInstance(R.id.text_view_end_time);
        endTimePicker.show(getFragmentManager(), "endTimePicker");
        break;
      case R.id.text_view_end_date:
        DatePickerFragment endDatePicker = DatePickerFragment.newInstance(R.id.text_view_end_date);
        endDatePicker.show(getFragmentManager(), "endDatePicker");
        break;
      case R.id.button_submit:
        insertStudyEvents();
        break;
      default:
        //Do Nothing
    }
  }

  private void insertStudyEvents() {

  }

  @Override
  public void updateTime(int viewId, int hour, int min) {
    TextView view = (TextView) findViewById(viewId);
    String minutes = (min < 10)? "0" + min : String.valueOf(min);
    view.setText(hour + ":" + minutes);
  }

  @Override
  public void updateDate(int viewId, int year, int month, int day) {
    TextView view = (TextView) findViewById(viewId);
    Date d = new Date(year - 1900, month, day);

    DateFormat df = DateFormat.getDateInstance();
    view.setText(df.format(d));
  }
}
