package sk.android.training.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import sk.android.training.R;
import sk.android.training.database.TableDbAdapter;


public class MyDialogFragment extends DialogFragment {

    private SharedPreferences prefs;
    private String prefName = "UserPref";

    private static final String FIRST_NAME_KEY = "first_name";
    private static final String LAST_NAME_KEY = "last_name";
    private static final String HOMETOWN_KEY = "hometown";
    private static final String COUNTRY_KEY = "country";
    private static final String SPORT_KEY = "sport";
    private static final String WEIGHT_KEY = "weight";
    private static final String HEIGHT_KEY = "height";
    private static final String HR_MAX_KEY = "HR_max";
    private static final String HR_BASAL_KEY = "HR_basal";
    private static final String VO2_KEY = "VO2";
    private static final String FAT_KEY = "fat";
    private static final String INFO_KEY = "info";

    private static final int NAME_DIALOG_ID = 1;
    private static final int SPORT_DIALOG_ID = 2;
    private static final int HOMETOWN_DIALOG_ID = 3;
    private static final int COUNTRY_DIALOG_ID = 4;
    private static final int WEIGHT_DIALOG_ID = 5;
    private static final int HEIGHT_DIALOG_ID = 6;
    private static final int HRMAX_DIALOG_ID = 7;
    private static final int HRBASAL_DIALOG_ID = 8;
    private static final int VO2_DIALOG_ID = 9;
    private static final int FAT_DIALOG_ID = 10;
    private static final int INFO_DIALOG_ID = 11;
    private static final int ADD_SPORT_DIALOG_ID = 12;
    private static final int EDIT_ACTIVITY_DIALOG = 13;
    private static final int ABOUT_DIALOG_ID = 14;

    private Cursor c;

    private TableDbAdapter db;
    private String update;

    public static MyDialogFragment newInstance(int id) {
        MyDialogFragment f = new MyDialogFragment();

        Bundle args = new Bundle();
        args.putInt("id", id);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        int id = getArguments().getInt("id");
        prefs = getActivity().getSharedPreferences(prefName, getActivity().MODE_PRIVATE);

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db = new TableDbAdapter(getActivity());


        if (id == NAME_DIALOG_ID) {

            final View v = inflater.inflate(R.layout.dialog_edit_text, null);
            final EditText nameEdit = (EditText) v.findViewById(R.id.nameEditDialog);
            final EditText nameEdit2 = (EditText) v.findViewById(R.id.surnameEditDialog);

            nameEdit.setText(prefs.getString(FIRST_NAME_KEY, ""));
            nameEdit2.setText(prefs.getString(LAST_NAME_KEY, ""));

            return new AlertDialog.Builder(getActivity())
                    .setTitle("Set new value")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setView(v)
                    .setPositiveButton("Set", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                            SharedPreferences.Editor editor = prefs.edit();

                            if (nameEdit.getText().toString().length() != 0) {
                                editor.putString(FIRST_NAME_KEY, nameEdit.getText().toString());
                            }
                            if (nameEdit2.getText().toString().length() != 0) {
                                editor.putString(LAST_NAME_KEY, nameEdit2.getText().toString());
                            }
                            editor.commit();

                            TextView nameText = (TextView) getActivity().findViewById(R.id.nameEdit);
                            nameText.setText(prefs.getString(FIRST_NAME_KEY, "") + " " + prefs.getString(LAST_NAME_KEY, ""));

                            update = "UPDATE USER SET first_name = '" + prefs.getString(FIRST_NAME_KEY, "") + "', last_name = '" + prefs.getString(LAST_NAME_KEY, "") +
                                    "' WHERE username = '" + prefs.getString("username", "") + "';";
                            db.open();
                            db.insertSyncQuery(update);
                            db.close();

                            if (nameText.getText().toString().length() != 0) {
                                nameText.setTextColor(Color.WHITE);
                            }

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dismiss();
                        }
                    })
                    .create();
        }

        if (id == HOMETOWN_DIALOG_ID) {

            final EditText edit = new EditText(getActivity());

            edit.setHint("hometown");
            edit.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            edit.setText(prefs.getString(HOMETOWN_KEY, ""));

            AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
            dialog.setView(edit, 10, 10, 10, 10);
            dialog.setTitle("Set new value");
            dialog.setIcon(android.R.drawable.ic_dialog_info);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dismiss();
                }
            });
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Set", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                    TextView text = (TextView) getActivity().findViewById(R.id.homeEdit);

                    if (edit.getText().toString().length() != 0) {

                        text.setText(edit.getText().toString());
                        text.setTextColor(Color.WHITE);

                    } else {
                        text.setText("Not set");
                        text.setTextColor(Color.GRAY);
                    }
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString(HOMETOWN_KEY, edit.getText().toString());
                    editor.commit();

                    update = "UPDATE USER SET hometown = '" + prefs.getString(HOMETOWN_KEY, "") + "' WHERE username = '" + prefs.getString("username", "") + "';";
                    db.open();
                    db.insertSyncQuery(update);
                    db.close();
                }
            });

            return dialog;
        }

        if (id == COUNTRY_DIALOG_ID) {

            final EditText edit = new EditText(getActivity());

            edit.setHint("country");
            edit.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            edit.setText(prefs.getString(COUNTRY_KEY, ""));

            AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
            dialog.setView(edit, 10, 10, 10, 10);
            dialog.setTitle("Set new value");
            dialog.setIcon(android.R.drawable.ic_dialog_info);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dismiss();
                }
            });
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Set", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    TextView text = (TextView) getActivity().findViewById(R.id.countryEdit);

                    if (edit.getText().toString().length() != 0) {

                        text.setText(edit.getText().toString());
                        text.setTextColor(Color.WHITE);

                    } else {
                        text.setText("Not set");
                        text.setTextColor(Color.GRAY);
                    }

                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString(COUNTRY_KEY, edit.getText().toString());
                    editor.commit();

                    update = "UPDATE USER SET country = '" + prefs.getString(COUNTRY_KEY, "") + "' WHERE username = '" + prefs.getString("username", "") + "';";
                    db.open();
                    db.insertSyncQuery(update);
                    db.close();
                }
            });

            return dialog;
        }

        if (id == SPORT_DIALOG_ID) {

            db = new TableDbAdapter(getActivity());
            db.open();

            c = db.getAllSports();
            getActivity().startManagingCursor(c);

            return new AlertDialog.Builder(getActivity())
                    .setTitle("Choose new value")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setCursor(c, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            TextView text = (TextView) getActivity().findViewById(R.id.sportEdit);

                            SharedPreferences.Editor editor = prefs.edit();

                            Cursor cursor = db.getSport(which + 1);
                            getActivity().startManagingCursor(cursor);

                            editor.putString(SPORT_KEY, cursor.getString(0));
                            editor.commit();

                            text.setText(cursor.getString(0));
                            text.setTextColor(Color.WHITE);

                            update = "UPDATE USER SET sport = '" + prefs.getString(SPORT_KEY, "") + "' WHERE username = '" + prefs.getString("username", "") + "';";
                            db.insertSyncQuery(update);

                            db.close();
                            getActivity().stopManagingCursor(c);
                            getActivity().stopManagingCursor(cursor);
                        }
                    }, TableDbAdapter.KEY_SPORT_TITLE)
                    .create();
        }

        if (id == WEIGHT_DIALOG_ID) {

            final EditText edit = new EditText(getActivity());

            edit.setHint("weight");
            edit.setInputType(InputType.TYPE_CLASS_NUMBER);
            edit.setText(prefs.getString(WEIGHT_KEY, ""));

            AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
            dialog.setView(edit, 10, 10, 10, 10);
            dialog.setTitle("Set new value");
            dialog.setIcon(android.R.drawable.ic_dialog_info);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dismiss();
                }
            });
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Set", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                    TextView text = (TextView) getActivity().findViewById(R.id.weightEdit);

                    if (edit.getText().toString().length() != 0) {

                        text.setText(edit.getText().toString());
                        text.setTextColor(Color.WHITE);
                    } else {
                        text.setText("--");
                        text.setTextColor(Color.GRAY);
                    }
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString(WEIGHT_KEY, edit.getText().toString());
                    editor.commit();

                    update = "UPDATE USER SET weight = '" + prefs.getString(WEIGHT_KEY, "") + "' WHERE username = '" + prefs.getString("username", "") + "';";
                    db.open();
                    db.insertSyncQuery(update);
                    db.close();
                }
            });

            return dialog;
        }

        if (id == HEIGHT_DIALOG_ID) {

            final EditText edit = new EditText(getActivity());

            edit.setHint("height");
            edit.setInputType(InputType.TYPE_CLASS_NUMBER);
            edit.setText(prefs.getString(HEIGHT_KEY, ""));

            AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
            dialog.setView(edit, 10, 10, 10, 10);
            dialog.setTitle("Set new value");
            dialog.setIcon(android.R.drawable.ic_dialog_info);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dismiss();
                }
            });
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Set", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    TextView text = (TextView) getActivity().findViewById(R.id.heightEdit);

                    if (edit.getText().toString().length() != 0) {

                        text.setText(edit.getText().toString());
                        text.setTextColor(Color.WHITE);
                    } else {
                        text.setText("--");
                        text.setTextColor(Color.GRAY);
                    }

                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString(HEIGHT_KEY, edit.getText().toString());
                    editor.commit();

                    update = "UPDATE USER SET height = '" + prefs.getString(HEIGHT_KEY, "") + "' WHERE username = '" + prefs.getString("username", "") + "';";
                    db.open();
                    db.insertSyncQuery(update);
                    db.close();
                }
            });

            return dialog;
        }

        if (id == HRMAX_DIALOG_ID) {

            final EditText edit = new EditText(getActivity());

            edit.setHint("HR max");
            edit.setInputType(InputType.TYPE_CLASS_NUMBER);
            edit.setText(prefs.getString(HR_MAX_KEY, ""));

            AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
            dialog.setView(edit, 10, 10, 10, 10);
            dialog.setTitle("Set new value");
            dialog.setIcon(android.R.drawable.ic_dialog_info);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dismiss();
                }
            });
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Set", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    TextView text = (TextView) getActivity().findViewById(R.id.HRmaxEdit);

                    if (edit.getText().toString().length() != 0) {

                        text.setText(edit.getText().toString());
                        text.setTextColor(Color.WHITE);
                    } else {
                        text.setText("--");
                        text.setTextColor(Color.GRAY);
                    }
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString(HR_MAX_KEY, edit.getText().toString());
                    editor.commit();

                    update = "UPDATE USER SET hrMax = '" + prefs.getString(HR_MAX_KEY, "") + "' WHERE username = '" + prefs.getString("username", "") + "';";
                    db.open();
                    db.insertSyncQuery(update);
                    db.close();
                }
            });

            return dialog;
        }

        if (id == HRBASAL_DIALOG_ID) {

            final EditText edit = new EditText(getActivity());

            edit.setHint("HR basal");
            edit.setInputType(InputType.TYPE_CLASS_NUMBER);
            edit.setText(prefs.getString(HR_BASAL_KEY, ""));

            AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
            dialog.setView(edit, 10, 10, 10, 10);
            dialog.setTitle("Set new value");
            dialog.setIcon(android.R.drawable.ic_dialog_info);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dismiss();
                }
            });
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Set", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    TextView text = (TextView) getActivity().findViewById(R.id.HRbasalEdit);

                    if (edit.getText().toString().length() != 0) {

                        text.setText(edit.getText().toString());
                        text.setTextColor(Color.WHITE);
                    } else {
                        text.setText("--");
                        text.setTextColor(Color.GRAY);
                    }
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString(HR_BASAL_KEY, edit.getText().toString());
                    editor.commit();

                    update = "UPDATE USER SET hrRest = '" + prefs.getString(HR_BASAL_KEY, "") + "' WHERE username = '" + prefs.getString("username", "") + "';";
                    db.open();
                    db.insertSyncQuery(update);
                    db.close();
                }
            });

            return dialog;
        }

        if (id == VO2_DIALOG_ID) {

            final EditText edit = new EditText(getActivity());

            edit.setHint("VO2 max");
            edit.setInputType(InputType.TYPE_CLASS_NUMBER);
            edit.setText(prefs.getString(VO2_KEY, ""));

            AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
            dialog.setView(edit, 10, 10, 10, 10);
            dialog.setTitle("Set new value");
            dialog.setIcon(android.R.drawable.ic_dialog_info);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dismiss();
                }
            });
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Set", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    TextView text = (TextView) getActivity().findViewById(R.id.VO2edit);

                    if (edit.getText().toString().length() != 0) {


                        text.setText(edit.getText().toString());
                        text.setTextColor(Color.WHITE);
                    } else {
                        text.setText("--");
                        text.setTextColor(Color.GRAY);
                    }
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString(VO2_KEY, edit.getText().toString());
                    editor.commit();

                    update = "UPDATE USER SET vo2 = '" + prefs.getString(VO2_KEY, "") + "' WHERE username = '" + prefs.getString("username", "") + "';";
                    db.open();
                    db.insertSyncQuery(update);
                    db.close();
                }
            });

            return dialog;
        }

        if (id == FAT_DIALOG_ID) {

            final EditText edit = new EditText(getActivity());

            edit.setHint("fat");
            edit.setInputType(InputType.TYPE_CLASS_NUMBER);
            edit.setText(prefs.getString(FAT_KEY, ""));

            AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
            dialog.setView(edit, 10, 10, 10, 10);
            dialog.setTitle("Set new value");
            dialog.setIcon(android.R.drawable.ic_dialog_info);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dismiss();
                }
            });
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Set", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    TextView text = (TextView) getActivity().findViewById(R.id.fatEdit);

                    if (edit.getText().toString().length() != 0) {

                        text.setText(edit.getText().toString());
                        text.setTextColor(Color.WHITE);
                    } else {
                        text.setText("--");
                        text.setTextColor(Color.GRAY);
                    }
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString(FAT_KEY, edit.getText().toString());
                    editor.commit();

                    update = "UPDATE USER SET fat = '" + prefs.getString(FAT_KEY, "") + "' WHERE username = '" + prefs.getString("username", "") + "';";
                    db.open();
                    db.insertSyncQuery(update);
                    db.close();
                }
            });

            return dialog;
        }

        if (id == INFO_DIALOG_ID) {

            final EditText edit = new EditText(getActivity());

            edit.setHint("info");
            edit.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            edit.setText(prefs.getString(INFO_KEY, ""));
            edit.setSingleLine(false);
            edit.setMinLines(2);

            AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
            dialog.setView(edit, 10, 10, 10, 10);
            dialog.setTitle("Set new value");
            dialog.setIcon(android.R.drawable.ic_dialog_info);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dismiss();
                }
            });
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Set", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    TextView text = (TextView) getActivity().findViewById(R.id.detailsEdit);

                    if (edit.getText().toString().length() != 0) {

                        text.setText(edit.getText().toString());
                        text.setTextColor(Color.WHITE);
                    } else {
                        text.setText("write something...");
                        text.setTextColor(Color.GRAY);
                    }
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString(INFO_KEY, edit.getText().toString());
                    editor.commit();

                    update = "UPDATE USER SET info = '" + prefs.getString(INFO_KEY, "") + "' WHERE username = '" + prefs.getString("username", "") + "';";
                    db.open();
                    db.insertSyncQuery(update);
                    db.close();
                }
            });

            return dialog;
        }

        if (id == ADD_SPORT_DIALOG_ID) {

            db = new TableDbAdapter(getActivity());
            db.open();

            LinearLayout lin = new LinearLayout(getActivity());
            lin.setOrientation(LinearLayout.VERTICAL);

            final EditText edit = new EditText(getActivity());

            lin.addView(edit);

            edit.setHint("sport");
            edit.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

            AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
            dialog.setView(lin, 10, 10, 10, 10);
            dialog.setTitle("Add new sport");
            dialog.setIcon(android.R.drawable.ic_dialog_info);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    //dismiss();
                }
            });
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Set", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                    db.createSport(edit.getText().toString());
                    db.close();
                }
            });

            return dialog;

        }


        return null;
    }


}