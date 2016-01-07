package nure.ofm.ocm;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddTaskActivity extends AppCompatActivity {

    EditText title;
    EditText desription;
    EditText executor;
    EditText dueTo;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);
        activity = this;
        Intent intent = getIntent();

        Button btn2 = (Button)findViewById(R.id.add_button);

        btn2.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                new AddTask().execute("http://10.0.2.2:3000/addtask", "{\"user\":\"" + executor.getText() + "\"}");
            }

        });
    }

    class AddTask extends AsyncTask<String, Void, Boolean> {

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
                return true;
            } catch (Exception e) {
                this.exception = e;
                return false;
            }
        }

        protected void onPostExecute(Boolean feed) {
            if(res == "OK"){
                Toast.makeText(activity, res, Toast.LENGTH_LONG).show();
            }

        }
    }

}

