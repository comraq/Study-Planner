package adam.androidcal;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by adam on 28/02/16.
 */
public class DatePickerFragment extends DialogFragment
  implements DatePickerDialog.OnDateSetListener {

  private static String CLOCK_ID = "clock_id";

  private DatePickerListener listener;
  private int clockViewId;

  public interface DatePickerListener {
    void updateDate(int viewId, int year, int month, int day);
  }

  public static DatePickerFragment newInstance(int clockViewId) {
    Bundle args = new Bundle();
    args.putInt(CLOCK_ID, clockViewId);


    DatePickerFragment fragment = new DatePickerFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the current time as the default values for the picker
    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int day = c.get(Calendar.DAY_OF_MONTH);
    int month = c.get(Calendar.MONTH);

    this.clockViewId = getArguments().getInt(CLOCK_ID);

    // Create a new instance of TimePickerDialog and return it
    return new DatePickerDialog(getActivity(), this, year, month,
      day);
  }

  public void onDateSet(DatePicker view, int year, int month, int day) {
    // Do something with the time chosen by the user
    listener.updateDate(clockViewId, year, month, day);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      listener = (DatePickerListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString() + " must implement TimePickerListener");
    }
  }
}
