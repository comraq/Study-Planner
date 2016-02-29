package adam.androidcal;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by adam on 28/02/16.
 */
public class TimePickerFragment extends DialogFragment
  implements TimePickerDialog.OnTimeSetListener {

  private static String CLOCK_ID = "clock_id";

  private TimePickerListener listener;
  private int clockViewId;

  public interface TimePickerListener {
    void updateTime(int viewId, int hour, int min);
  }

  public static TimePickerFragment newInstance(int clockViewId) {
    Bundle args = new Bundle();
    args.putInt(CLOCK_ID, clockViewId);


    TimePickerFragment fragment = new TimePickerFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the current time as the default values for the picker
    final Calendar c = Calendar.getInstance();
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);

    this.clockViewId = getArguments().getInt(CLOCK_ID);

    // Create a new instance of TimePickerDialog and return it
    return new TimePickerDialog(getActivity(), this, hour, minute,
      DateFormat.is24HourFormat(getActivity()));
  }

  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    // Do something with the time chosen by the user
    listener.updateTime(clockViewId, hourOfDay, minute);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      listener = (TimePickerListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString() + " must implement TimePickerListener");
    }
  }
}
