package demo.thaonguyen.showimage;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> listImage = new ArrayList<String>();
    int count = 0;
    boolean check = true;
    RequestQueue requestQueue;
    ViewFlipper ivShow;
    ProgressDialog pd;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        new JsonTask().execute("https://cloudprojectcnt.azurewebsites.net/api/Default/");
        ivShow = findViewById(R.id.vFlipper);
//        for(String url : listImage){
//            addImage(url);
//        }
        ivShow.setFlipInterval(2000);
        ivShow.setAutoStart(true);
    }

    public void addImage(String url){
        ImageView imageView = new ImageView(this);
        Picasso.with(MainActivity.this)
                .load(url)
                .into(imageView);
        ivShow.addView(imageView);

    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
//            txtJson.setImageResource(result);
            try {

                List<Block> list = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(result);


                for (int i = 1;i < jsonArray.length();i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    ObjectMapper mapper = new ObjectMapper();
                    Block block = mapper.readValue(jsonObj.toString(), Block.class);
                    String img = Encryption(block.data,14);
                    System.out.println(Encryption(block.data,14));
                    addImage(img);
//                    listImage.add(img);
//
//                    list.add(block);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        public String Encryption(String data, int privateKey)
        {
            int numEncryption = EncryptionNumber(privateKey);
            String strEncryption = EncryptionString(data, numEncryption);
            return strEncryption;
        }
        public int EncryptionNumber(int privateKey)
        {
            return ((privateKey * 5) % 12);
        }
//        public void show() {
//            Handler handler1 = new Handler();
//            handler1.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//
//                    count++;
//                    if(count == listImage.size()){
//                        count = 0;
//                    }
//                }
//            }, 5000);
//
//        }
        public String EncryptionString(String data, int numEncryption)
        {
            String m = "";
            for(int i =0; i< data.length(); i++)
            {
                char a = (char)((int)data.charAt(i) - numEncryption);
                m = m + a;
            }
            return m;
        }
    }
}
