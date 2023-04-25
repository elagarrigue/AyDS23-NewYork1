package ayds.newyork.songinfo.moredetails.fulllogic;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import retrofit2.Response;

public class GSonImpl {
    private JsonObject jObj;
    private JsonObject responseObj;
    private JsonElement docsElement;
    private JsonElement url;
    private Gson gson;
    public  GSonImpl(Response<String> callResponse){
        gson = new Gson();
        jObj = gson.fromJson(callResponse.body(), JsonObject.class);
        responseObj = jObj.get("response").getAsJsonObject();
        docsElement = responseObj.get("docs").getAsJsonArray().get(0).getAsJsonObject().get("abstract");
        url = responseObj.get("docs").getAsJsonArray().get(0).getAsJsonObject().get("web_url");
    }

    public JsonElement getDocsElement(){
        return docsElement;
    }

    public JsonElement getUrl(){
        return url;
    }
}
