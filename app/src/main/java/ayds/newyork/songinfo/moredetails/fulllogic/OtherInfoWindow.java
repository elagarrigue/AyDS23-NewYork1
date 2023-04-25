package ayds.newyork.songinfo.moredetails.fulllogic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ayds.newyork.songinfo.R;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import retrofit2.Response;

public class OtherInfoWindow extends AppCompatActivity {

  public final static String ARTIST_NAME_EXTRA = "artistName";
  private TextView textPane2;
  private DataBase dataBase = null;
  private final String imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU";
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_other_info);
    textPane2 = findViewById(R.id.textPane2);
    open(getIntent().getStringExtra("artistName"));
  }

  public void getArtistInfo(String artistName) {
    NYTimesAPI NYTimesAPI = NYTimesAPIImpl.createNYTimesAPI();
    Log.e("TAG","artistName " + artistName);
        new Thread(() -> {
          String text = DataBase.getInfo(dataBase, artistName);
          if (text != null) { // exists in db
            text = "[*]" + text;
          } else { // get from service
            try {
              Response<String> callResponse = NYTimesAPI.getArtistInfo(artistName).execute();
              Log.e("TAG","JSON " + callResponse.body());
              GSonImpl gSon = new GSonImpl(callResponse);
              if (gSon.getDocsElement() == null) {
                text = "No Results";
              } else {
                text = gSon.getDocsElement().getAsString().replace("\\n", "\n");
                text = textToHtml(text, artistName);
                DataBase.saveArtist(dataBase, artistName, text);
              }
              findViewByID(gSon.getUrl().getAsString());
              /*final String urlString = gSon.getUrl().getAsString();
              findViewById(R.id.openUrlButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  Intent intent = new Intent(Intent.ACTION_VIEW);
                  intent.setData(Uri.parse(urlString));
                  startActivity(intent);
                }
              });*/
            } catch (IOException e1) {
              Log.e("TAG", "Error " + e1);
              e1.printStackTrace();
            }
          }
          Log.e("TAG","Get Image from " + imageUrl);
          final String finalText = text;
          runOnUiThread( () -> {
            Picasso.get().load(imageUrl).into((ImageView) findViewById(R.id.imageView));
            textPane2.setText(Html.fromHtml( finalText));
          });
        }).start();
  }

  private void findViewByID(String urlString){
    findViewById(R.id.openUrlButton).setOnClickListener(v -> {
      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.setData(Uri.parse(urlString));
      startActivity(intent);
    });
  }

  private void open(String artist) {
    dataBase = new DataBase(this);
    DataBase.saveArtist(dataBase, "test", "sarasa");
    Log.e("TAG", ""+ DataBase.getInfo(dataBase,"test"));
    Log.e("TAG",""+ DataBase.getInfo(dataBase,"nada"));
    getArtistInfo(artist);
  }

  public static String textToHtml(String text, String term) {
    StringBuilder builder = new StringBuilder();
    builder.append("<html><div width=400>");
    builder.append("<font face=\"arial\">");
    String textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replaceAll("(?i)" + term, "<b>" + term.toUpperCase() + "</b>");
    builder.append(textWithBold);
    builder.append("</font></div></html>");
    return builder.toString();
  }
}