package am.amsave;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    CommentsDataSource datasource;

    Context context = this;
    final String MyPREFERENCES = "myPreferences";
    final String KEY = "myKey";
    final String fileName = "/sdcard/myFileName.txt";
    EditText et;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datasource = new CommentsDataSource(this);
        try {
            datasource.open();
        } catch (SQLException e) {
//            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "datasource not open",
                    Toast.LENGTH_LONG).show();
        }

        final EditText et = (EditText) findViewById(R.id.editText);
        Button bt_saveKeyValue = (Button) findViewById(R.id.bt_saveKeyValue);
        Button bt_saveFile = (Button) findViewById(R.id.bt_saveFile);
        Button bt_saveSQLite = (Button) findViewById(R.id.bt_saveSQLite);

        Button bt_readKeyValue = (Button) findViewById(R.id.bt_readKeyValue);
        Button bt_readFile = (Button) findViewById(R.id.bt_ReadFile);
        Button bt_readSQLite = (Button) findViewById(R.id.bt_read_SQLite);

        bt_saveKeyValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = context.getSharedPreferences(
                        MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                String value = String.valueOf(et.getText());
                editor.putString(KEY, value);
                editor.commit();
            }
        });

        bt_readKeyValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = context.getSharedPreferences(
                        MyPREFERENCES, Context.MODE_PRIVATE);
                String value = sharedPref.getString(KEY, "default value");
                et.setText(value);
            }
        });

        bt_saveFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File myFile = new File(fileName);
                    myFile.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter =
                            new OutputStreamWriter(fOut);
                    String value = String.valueOf(et.getText());
                    myOutWriter.append(value);
                    myOutWriter.close();
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        bt_readFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File myFile = new File(fileName);
                    FileInputStream fIn = new FileInputStream(myFile);
                    BufferedReader myReader = new BufferedReader(
                            new InputStreamReader(fIn));
                    String aDataRow = "";
                    String aBuffer = "";
                    while ((aDataRow = myReader.readLine()) != null) {
                        aBuffer += aDataRow + "\n";
                    }
                    String value = aBuffer.replace(aBuffer.substring(aBuffer.length() - 1), "");
                    et.setText(value);
                    myReader.close();
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        bt_saveSQLite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = String.valueOf(et.getText());
//                Comment comment =
                datasource.createComment(value);
            }
        });

        bt_readSQLite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Comment> comments = datasource.getAllComments();
                String value = "";
                for (Comment s : comments) {
                    value += s.getComment() + "|";
                }
                et.setText(value);
            }
        });
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
