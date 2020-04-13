package info.simplyapps.app.notebook.screens;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import info.simplyapps.app.notebook.Constants;
import info.simplyapps.app.notebook.R;
import info.simplyapps.app.notebook.SystemHelper;
import info.simplyapps.app.notebook.storage.DBDriver;
import info.simplyapps.app.notebook.storage.FileDriver;
import info.simplyapps.app.notebook.storage.StorageUtil;
import info.simplyapps.app.notebook.storage.StoreData;
import info.simplyapps.appengine.screens.GenericScreenTemplate;
import info.simplyapps.appengine.storage.dto.Configuration;

public class ConfigureScreen extends GenericScreenTemplate {

    private Dialog backupDialog;
    private EditText backupPath;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createBackupDialog();

        Button btn = (Button) findViewById(R.id.btn_back_cfg);
        btn.setOnClickListener(onButtonBack);

        Button bBackup = (Button) findViewById(R.id.btn_backup);
        bBackup.setOnClickListener(onButtonBackup);

        CheckBox cbTextSize;
        SeekBar sbTextSize;

        cbTextSize = (CheckBox) findViewById(R.id.configure_autotextsize);
        cbTextSize.setOnClickListener(onClickAutoTextSize);
        cbTextSize.setChecked(SystemHelper.isAutoTextSize(getApplicationContext()));
        sbTextSize = (SeekBar) findViewById(R.id.configure_seektextsize);
        sbTextSize.setOnSeekBarChangeListener(onTextSizeChangeListener);
        if (SystemHelper.getTextSize(getApplicationContext()) > 0) {
            TextView tv = (TextView) findViewById(R.id.configure_displaytextsize);
            tv.setText(Integer.toString(SystemHelper.getTextSize(getApplicationContext())) + Constants.CONFIG_TEXT_UNIT);
            sbTextSize.setProgress(SystemHelper.getTextSize(getApplicationContext()) - Constants.CONFIG_TEXT_ADD);
        }
    }

    OnClickListener onButtonBack = new OnClickListener() {
        public void onClick(View v) {
            actionBack();
        }
    };

    OnClickListener onButtonBackup = new OnClickListener() {
        public void onClick(View v) {
            actionBackup();
        }
    };

    private void setAutoTextSize(String cnstTextSize, String defTextSize) {
        Configuration config = SystemHelper.getConfiguration(cnstTextSize, defTextSize);
        config.value = Boolean.toString(!Boolean.valueOf(config.value).booleanValue());
        DBDriver.getInstance().store(config);
    }

    OnClickListener onClickAutoTextSize = new OnClickListener() {
        public void onClick(View v) {
            setAutoTextSize(Constants.CONFIG_AUTOTEXTSIZE, Constants.DEFAULT_CONFIG_AUTOTEXTSIZE);
        }
    };

    private void updateTextSize(SeekBar seekBar, int progress, int textViewId, String cnstTextSize, String defTextSize) {
        TextView tv = (TextView) LinearLayout.class.cast(seekBar.getParent()).findViewById(textViewId);
        tv.setText(Integer.toString(progress + Constants.CONFIG_TEXT_ADD) + Constants.CONFIG_TEXT_UNIT);
        int newSize = progress + Constants.CONFIG_TEXT_ADD;
        Configuration config = SystemHelper.getConfiguration(cnstTextSize, defTextSize);
        config.value = Integer.toString(newSize);
        DBDriver.getInstance().store(config);
    }

    OnSeekBarChangeListener onTextSizeChangeListener = new OnSeekBarChangeListener() {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            updateTextSize(seekBar, progress, R.id.configure_displaytextsize, Constants.CONFIG_TEXTSIZE, Constants.DEFAULT_CONFIG_TEXTSIZE);
        }
    };

    private void actionBack() {
        this.finish();
    }

    private void actionBackup() {
        openBackupDialog();
    }

    @Override
    public int getScreenLayout() {
        return R.layout.configurescreen;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void prepareStorage(Context context) {
        StorageUtil.prepareStorage(getApplicationContext());
    }

    OnClickListener onBackupDialogImport = new OnClickListener() {
        public void onClick(View v) {
            readWriteExternalFile(false);
            DBDriver.getInstance().write(StoreData.getInstance());
            backupDialog.dismiss();
        }
    };

    OnClickListener onBackupDialogExport = new OnClickListener() {
        public void onClick(View v) {
            readWriteExternalFile(true);
            backupDialog.dismiss();
        }
    };

    OnClickListener onBackupDialogCancel = new OnClickListener() {
        public void onClick(View v) {
            backupDialog.dismiss();
        }
    };

    private void readWriteExternalFile(boolean write) {
        FileDriver.readWriteExternalFile(this, write, backupPath.getText().toString());
    }

    private void createBackupDialog() {

        // prepare backup dialog
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View border = inflater.inflate(R.layout.backupdialog, null);
        backupDialog = new Dialog(this, R.style.Theme_Dialog);
        backupDialog.setContentView(R.layout.backupdialog);

        Button bExport = (Button) backupDialog.findViewById(R.id.backup_export);
        Button bImport = (Button) backupDialog.findViewById(R.id.backup_import);
        Button bCancel = (Button) backupDialog.findViewById(R.id.backup_cancel);
        bExport.setOnClickListener(onBackupDialogExport);
        bImport.setOnClickListener(onBackupDialogImport);
        bCancel.setOnClickListener(onBackupDialogCancel);

        backupPath = (EditText) backupDialog.findViewById(R.id.backup_path);
    }

    public void openBackupDialog() {
        backupPath.setText(Environment.getExternalStorageDirectory().getPath());
        backupDialog.show();
    }


}