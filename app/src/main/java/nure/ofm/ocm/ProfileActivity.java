package nure.ofm.ocm;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.ArrayList;

import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    JSONArray result;
    TextView textView;
    ArrayList<Task> tasks = new ArrayList<Task>();
    BoxAdapter boxAdapter;
    ListView lvMain;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        textView = (TextView) findViewById(R.id.textView1);
        activity = this;
        Intent intent = getIntent();
        textView.setText(intent.getExtras().getString("user"));
        new RetrieveIdTask().execute("http://10.0.2.2:3000/getusertasks", "{\"username\":\"" + intent.getExtras().getString("user") + "\"}");
        Button btn2 = (Button)findViewById(R.id.add_butt);

        btn2.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                Intent intent = new Intent(activity, AddTaskActivity.class);
                startActivity(intent);
            }

        });
    }

    class RetrieveIdTask extends AsyncTask<String, Void, Boolean> {

        private Exception exception;
        String res;

        protected Boolean doInBackground(String... urls) {
            try {
                OkHttpClient client = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, urls[1]);
                Request request = new Request.Builder()
                        .url(urls[0])
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                res = response.body().string();
                result = new JSONArray(res);
                return true;
            } catch (Exception e) {
                this.exception = e;
                return false;
            }
        }

        protected void onPostExecute(Boolean feed) {
            for (int i=0; i < result.length(); i++)
            {
                try {
                    JSONObject oneObject = result.getJSONObject(i);
                    // Pulling items from the array
                    String title = oneObject.getString("title");
                    String executor = oneObject.getString("username");
                    String dueTo = oneObject.getString("dueTo");
                    String description = oneObject.getString("description");

                    tasks.add(new Task(title, executor, dueTo, description));

                } catch (JSONException e) {
                    // Oops
                }
            }
            boxAdapter = new BoxAdapter(activity, tasks);
            lvMain = (ListView) findViewById(R.id.lvMain);
            lvMain.setAdapter(boxAdapter);

        }
    }

}

