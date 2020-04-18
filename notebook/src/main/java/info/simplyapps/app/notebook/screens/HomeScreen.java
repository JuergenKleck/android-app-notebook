
package info.simplyapps.app.notebook.screens;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import info.simplyapps.app.notebook.R;
import info.simplyapps.app.notebook.SystemHelper;
import info.simplyapps.app.notebook.storage.DBDriver;
import info.simplyapps.app.notebook.storage.StorageUtil;
import info.simplyapps.app.notebook.storage.dto.Entry;
import info.simplyapps.appengine.screens.GenericScreenTemplate;

public class HomeScreen extends GenericScreenTemplate {

    private ViewGroup lTable;

    private Dialog editDialog;
    private TextView lblTitle;

    List<Entry> entries = new ArrayList<>();
    Entry currentEntry = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lTable = findViewById(R.id.tableLayoutList);
        lblTitle = findViewById(R.id.section_label);

        TextView lblAdd = findViewById(R.id.section_add);
        lblAdd.setOnClickListener(onAddClick);

        // App initializations
        createEditDialog();
        entries.clear();
        currentEntry = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            actionSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.updateLists();
    }

    @Override
    public int getScreenLayout() {
        return R.layout.homescreen;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void prepareStorage(Context context) {
        StorageUtil.prepareStorage(getApplicationContext());
    }

    @Override
    public void onPermissionResult(String s, boolean b) {
    }

    public void actionSettings() {
        Intent wordIntent = new Intent(this, ConfigureScreen.class);
        wordIntent.setData(getIntent().getData());
        startActivity(wordIntent);
    }

    private void createEditDialog() {
        // prepare edit dialog
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        inflater.inflate(R.layout.editdialog, null);
        editDialog = new Dialog(this, R.style.Theme_AppCompat_Dialog);
        editDialog.setContentView(R.layout.editdialog);

        Button bOk = editDialog.findViewById(R.id.btn_ok);
        Button bCancel = editDialog.findViewById(R.id.btn_cancel);
        Button bDelete = editDialog.findViewById(R.id.btn_delete);
        bOk.setOnClickListener(onEditDialogOk);
        bCancel.setOnClickListener(onEditDialogCancel);
        bDelete.setOnClickListener(onEditDialogDelete);
    }

    public void openEditDialog() {
        Button delete = editDialog.findViewById(R.id.btn_delete);
        TextView tvTitle = editDialog.findViewById(R.id.tv_title);
        EditText editText = editDialog.findViewById(R.id.editText);
        if (currentEntry.id > 0L) {
            tvTitle.setText(getString(R.string.lbl_edit));
            editText.setText(currentEntry.text);
            delete.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setText(getString(R.string.lbl_add));
            editText.setText("");
            delete.setVisibility(View.GONE);
        }

        // popup window
        editDialog.show();
    }

    OnClickListener onAddClick = v -> {
        currentEntry = new Entry();
        openEditDialog();
    };

    OnClickListener onEditDialogOk = new OnClickListener() {
        public void onClick(View v) {
            EditText editText = editDialog.findViewById(R.id.editText);

            if (currentEntry != null) {
                currentEntry.text = editText.getText().toString();

                boolean exists = currentEntry.id > 0L;
                if (DBDriver.getInstance().store(currentEntry)) {
                    if (!exists) {
                        SystemHelper.addEntry(currentEntry);
                    }
                    updateLists();
                }
            }
            currentEntry = null;
            editDialog.dismiss();
        }
    };


    OnClickListener onEditDialogDelete = new OnClickListener() {
        public void onClick(View v) {

            if (currentEntry != null) {
                boolean exists = currentEntry.id > 0L;
                if (exists && DBDriver.getInstance().delete(currentEntry)) {
                    SystemHelper.getEntries().remove(currentEntry);
                    updateLists();
                }
            }
            currentEntry = null;
            editDialog.dismiss();
        }
    };

    OnClickListener onEditDialogCancel = new OnClickListener() {
        @Override
        public void onClick(View v) {
            currentEntry = null;
            editDialog.dismiss();
        }
    };

    public void updateLists() {
        lTable.removeAllViews();
        List<Entry> list = SystemHelper.getEntries();
        for (int i = 0; i < list.size(); i++) {
            addRow(list.get(i));
        }
        lblTitle.setText(MessageFormat.format(getString(R.string.lbl_title), list.size()));
    }

    private void addRow(Entry entry) {
        View row = null;
        // ROW INFLATION
        LayoutInflater inflater = (LayoutInflater) this.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (entry != null) {
            assert inflater != null;
            row = inflater.inflate(R.layout.listitem, lTable, false);
        } else {
            assert inflater != null;
            row = inflater.inflate(R.layout.listitemempty, lTable, false);
        }
        if (row != null) {
            if (entry != null) {
                TextView idField;
                TextView txt;
                TextView more;

                row.setOnLongClickListener(onLongClickListener);
                row.setOnClickListener(onRowClickListener);

                txt = row.findViewById(R.id.listitem_text);
                more = row.findViewById(R.id.listitem_marker2);

                if (!SystemHelper.isAutoTextSize(getApplicationContext()) && SystemHelper.getTextSize(getApplicationContext()) > 0) {
                    txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, SystemHelper.getTextSize(getApplicationContext()));
                }

                idField = row.findViewById(R.id.listitem_id);
                idField.setText(Long.toString(entry.id));

                String text = entry.text;
                if (!entry.expand && entry.text.length() > 25) {
                    text = entry.text.substring(0, 25) + "...";
                    txt.setLines(1);
                    more.setVisibility(View.VISIBLE);
                } else {
                    more.setVisibility(View.GONE);
                }
                txt.setMaxWidth(getScreenWidth() - 50);
                txt.setText(text);
            }
            // Get reference to TextView
            lTable.addView(row);
        }

    }

    OnClickListener onRowClickListener = v -> {
        TextView tv = v.findViewById(R.id.listitem_id);
        Long idx = Long.valueOf(tv.getText().toString());
        currentEntry = SystemHelper.getEntry(idx);
        if (currentEntry != null) {
            currentEntry.expand = !currentEntry.expand;
            updateLists();
        }
    };

    OnLongClickListener onLongClickListener = v -> {
        TextView tv = v.findViewById(R.id.listitem_id);
        Long idx = Long.valueOf(tv.getText().toString());
        currentEntry = SystemHelper.getEntry(idx);
        if (currentEntry != null) {
            openEditDialog();
        }
        return false;
    };

}
