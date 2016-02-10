package sk.android.training.fragments;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sk.android.training.CreateActivity;
import sk.android.training.DayLogActivity;
import sk.android.training.R;
import sk.android.training.adapter.CalendarAdapter;
import sk.android.training.database.TableDbAdapter;
import sk.android.training.utils.TimeDateFormatter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class FragmentCalendar extends Fragment {

	private Calendar month;
	private Calendar monthNext;
	private Calendar monthPrevious;
	private Date month_date;

	private CalendarAdapter adapter;
	private CalendarAdapter adapterNext;
	private CalendarAdapter adapterPrevious;

	private GridView gridview;
	private GridView gridviewNext;
	private GridView gridviewPrevious;

	private TableDbAdapter db;
	private TimeDateFormatter format;
	private SimpleDateFormat sdf;

	private TextView title;
	private String day;
	private int week_position;

	private Cursor days;
	private Cursor month_totals;

	private Context ctx;

	private View view;

	private Animation inRight;
	private Animation outRight;
	private Animation inLeft;
	private Animation outLeft;

	private static final String FIRST_NAME_KEY = "first_name";

	private static final int ID_NOW = 0;
	private static final int ID_PREVIOUS = 1;
	private static final int ID_NEXT = 2;

	GestureDetector gesture;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		DisplayMetrics metrics = new DisplayMetrics();

		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);

		if (metrics.heightPixels < 500 && metrics.widthPixels < 400) {
			view = inflater.inflate(R.layout.calendar_low, container, false);
		} else {
			view = inflater.inflate(R.layout.calendar, container, false);
		}

		ctx = getActivity();
		db = new TableDbAdapter(ctx);
		db.open();

		inRight = AnimationUtils.loadAnimation(getActivity(),
				R.anim.fragment_slide_right_enter);
		outRight = AnimationUtils.loadAnimation(getActivity(),
				R.anim.fragment_slide_right_exit);
		inLeft = AnimationUtils.loadAnimation(getActivity(),
				R.anim.fragment_slide_left_enter);
		outLeft = AnimationUtils.loadAnimation(getActivity(),
				R.anim.fragment_slide_left_exit);

		format = new TimeDateFormatter();

		gesture = new GestureDetector(getActivity(),
				new GestureDetector.SimpleOnGestureListener() {

					int swipe_Min_Distance = 100;
					int swipe_Max_Distance = 350;
					int swipe_Min_Velocity = 100;

					@Override
					public boolean onDown(MotionEvent e) {
						return true;
					}

					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {

						final float xDistance = Math.abs(e1.getX() - e2.getX());
						final float yDistance = Math.abs(e1.getY() - e2.getY());
						velocityX = Math.abs(velocityX);
						velocityY = Math.abs(velocityY);
						boolean result = false;

						if (xDistance > this.swipe_Max_Distance
								|| yDistance > this.swipe_Max_Distance) {
							return false;
						}

						if (velocityX > this.swipe_Min_Velocity
								&& xDistance > this.swipe_Min_Distance) {
							if (e1.getX() > e2.getX()) { // right to left
								nextMonth();
							} else {
								previousMonth();
							}
							result = true;
						} else if (velocityY > this.swipe_Min_Velocity
								&& yDistance > this.swipe_Min_Distance) {
							if (e1.getY() > e2.getY()) {// bottom to up

							} else {

							}
							result = true;
						}

						return result;
					}
				});

		month = Calendar.getInstance();
		monthNext = Calendar.getInstance();
		monthPrevious = Calendar.getInstance();

		monthNext.set(month.get(Calendar.YEAR), month.get(Calendar.MONTH) + 1,
				month.get(Calendar.DAY_OF_MONTH));
		monthPrevious
				.set(month.get(Calendar.YEAR), month.get(Calendar.MONTH) - 1,
						month.get(Calendar.DAY_OF_MONTH));

		month_date = new Date(month.getTimeInMillis());

		adapter = new CalendarAdapter(ctx, month);
		adapterNext = new CalendarAdapter(ctx, monthNext);
		adapterPrevious = new CalendarAdapter(ctx, monthPrevious);

		gridview = (GridView) view.findViewById(R.id.gridview);
		gridview.setAdapter(adapter);

		gridviewNext = (GridView) view.findViewById(R.id.gridviewNext);
		gridviewNext.setAdapter(adapterNext);

		gridviewPrevious = (GridView) view.findViewById(R.id.gridviewPrevious);
		gridviewPrevious.setAdapter(adapterPrevious);

		sdf = new SimpleDateFormat("MMMM yyyy", Locale.US);

		title = (TextView) view.findViewById(R.id.titleMonth);
		title.setText(sdf.format(month_date));

		getMonthTotals();
		adapter.setDates(getSessions(ID_NOW));
		adapterNext.setDates(getSessions(ID_NEXT));
		adapterPrevious.setDates(getSessions(ID_PREVIOUS));

		ImageButton previous = (ImageButton) view
				.findViewById(R.id.previousMonth);
		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				previousMonth();
			}
		});

		ImageButton next = (ImageButton) view.findViewById(R.id.nextMonth);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextMonth();
			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				TextView date = (TextView) v.findViewById(R.id.date);

				Intent i = new Intent(v.getContext(), DayLogActivity.class);
				String day = date.getText().toString();
				if (day.length() == 1) {
					day = "0" + day;
				}
				// return chosen date as string format
				i.putExtra("day", day);
				i.putExtra("month", Integer.parseInt(DateFormat.format("MM",
						month).toString()) - 1);
				i.putExtra("year", DateFormat.format("yyyy", month));
				i.putExtra("position", position);
				startActivityForResult(i, 0);
			}
		});

		registerForContextMenu(gridview);

		gridview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View v,
					int position, long id) {
				// TODO Auto-generated method stub
				TextView date = (TextView) v.findViewById(R.id.date);

				day = date.getText().toString();
				if (day.length() == 1) {
					day = "0" + day;
				}

				week_position = position;

				return false;
			}

		});

		gridview.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (gesture.onTouchEvent(event)) {
					return false;
				}
				return false;
			}
		});

		return view;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.setHeaderTitle("Select Action:");
		menu.add(0, v.getId(), 0, "Day Details");
		menu.add(0, v.getId(), 0, "Add Activity");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getTitle() == "Day Details") {

			Intent i = new Intent(ctx, DayLogActivity.class);
			if (day.length() == 1) {
				day = "0" + day;
			}
			// return chosen date as string format
			i.putExtra("day", day);
			i.putExtra("tabSelected", 1);
			i.putExtra(
					"month",
					Integer.parseInt(DateFormat.format("MM", month).toString()) - 1);
			i.putExtra("year", DateFormat.format("yyyy", month));
			i.putExtra("position", week_position);
			startActivityForResult(i, 0);

		} else if (item.getTitle() == "Add Activity") {
			Intent i = new Intent(ctx, CreateActivity.class);
			i.putExtra("day", day);
			i.putExtra("month", DateFormat.format("MM", month));
			i.putExtra("year", DateFormat.format("yyyy", month));
			startActivityForResult(i, 0);
		} else {
			return false;
		}
		return true;
	}

	private void nextMonth() {

		gridview.startAnimation(outLeft);
		gridviewNext.setVisibility(View.VISIBLE);
		gridviewNext.startAnimation(inLeft);

		adapter.nextMonth();
		adapterPrevious.nextMonth();
		adapterNext.nextMonth();

		month_date.setTime(month.getTimeInMillis());

		title.setText(sdf.format(month_date));
		getMonthTotals();

		inLeft.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				adapter.refreshDays();
				adapter.notifyDataSetChanged();

				adapter.setDates(getSessions(ID_NOW));
				adapterPrevious.setDates(getSessions(ID_PREVIOUS));
				adapterNext.setDates(getSessions(ID_NEXT));

				adapterPrevious.refreshDays();
				adapterPrevious.notifyDataSetChanged();

				gridviewNext.setVisibility(View.GONE);
				adapterNext.refreshDays();
				adapterNext.notifyDataSetChanged();
			}
		});
	}

	private void previousMonth() {

		gridview.startAnimation(outRight);
		gridviewPrevious.setVisibility(View.VISIBLE);
		gridviewPrevious.startAnimation(inRight);

		adapter.previousMonth();
		adapterPrevious.previousMonth();
		adapterNext.previousMonth();

		month_date.setTime(month.getTimeInMillis());

		title.setText(sdf.format(month_date));
		getMonthTotals();

		inRight.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				adapter.refreshDays();
				adapter.notifyDataSetChanged();

				adapter.setDates(getSessions(ID_NOW));
				adapterPrevious.setDates(getSessions(ID_PREVIOUS));
				adapterNext.setDates(getSessions(ID_NEXT));

				gridviewPrevious.setVisibility(View.GONE);
				adapterPrevious.refreshDays();
				adapterPrevious.notifyDataSetChanged();

				adapterNext.refreshDays();
				adapterNext.notifyDataSetChanged();
			}
		});
	}

	private void getMonthTotals() {

		String _date = format.formatDateMonthTotals(title.getText().toString());

		month_totals = db.getActivityMonthTotals(Integer.parseInt(_date));
		if (month_totals != null && month_totals.getCount() > 0) {
			if (month_totals.moveToFirst()) {
				// loop until it reach the end of the cursor
				do {
					getActivity().startManagingCursor(month_totals);
					TextView totalsTimeTitle = (TextView) view
							.findViewById(R.id.activityTotalTimeMonthLog);
					if (month_totals.getString(month_totals
							.getColumnIndexOrThrow("TOTAL_TIME")) == null) {
						totalsTimeTitle.setText("0");
					} else {
						String _hours = null;
						DecimalFormat df = new DecimalFormat("0.0");
						if (!month_totals.getString(
								month_totals
										.getColumnIndexOrThrow("TOTAL_TIME"))
								.equals("0")) {
							float hours = Float
									.parseFloat(month_totals.getString(month_totals
											.getColumnIndexOrThrow("TOTAL_TIME"))) / 60;
							_hours = String.valueOf(df.format(hours));
						} else {
							_hours = "0";
						}
						totalsTimeTitle.setText(_hours);

					}

					TextView totalsDaysTitle = (TextView) view
							.findViewById(R.id.activityTotalDayMonthLog);
					totalsDaysTitle.setText(month_totals.getString(month_totals
							.getColumnIndexOrThrow("TOTAL_DAYS")));

					TextView totalsUnitsTitle = (TextView) view
							.findViewById(R.id.activityTotalUnitsMonthLog);
					totalsUnitsTitle.setText(month_totals
							.getString(month_totals
									.getColumnIndexOrThrow("TOTAL_UNITS")));
					// do something here
				} while (month_totals.moveToNext());
			}
			// make sure to close the cursor
			// month_totals.close();
		}
	}

	public ArrayList<String[]> getSessions(int id) {
		int _date;

		_date = Integer.parseInt(format.formatDateMonthTotals(title.getText()
				.toString()));

		if (id == ID_NEXT) {
			_date = _date + 100;
		}
		if (id == ID_PREVIOUS) {
			_date = _date - 100;
		}
		ArrayList<String[]> dates = new ArrayList<String[]>();

		days = db.getMonthSessions(_date);
		if (days != null && days.getCount() > 0) {
			if (days.moveToFirst()) {
				// loop until it reach the end of the cursor
				do {
					getActivity().startManagingCursor(days);
					dates.add(new String[] {
							format.formatDateToDay(days.getString(days
									.getColumnIndexOrThrow(TableDbAdapter.KEY_DATE))),
							days.getString(days
									.getColumnIndexOrThrow(TableDbAdapter.KEY_SPORT)) });

				} while (days.moveToNext());
			}
			;
		}
		return dates;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getActivity().stopManagingCursor(month_totals);
		getActivity().stopManagingCursor(days);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && resultCode == 0) {
			adapter.refreshDays();
			adapter.notifyDataSetChanged();
			adapter.setDates(getSessions(ID_NOW));
			getMonthTotals();
		}
	}
}
