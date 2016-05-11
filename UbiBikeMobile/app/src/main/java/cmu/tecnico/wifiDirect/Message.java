package cmu.tecnico.wifiDirect;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Toninho on 11/05/2016.
 */
public class Message {
    String type;
    String content;
    String toUser;

    String hash;

    public static final String TYPE_POINTS = "POINTS";
    public static final String TYPE_MSG = "MSG";

    public Message(String type, String content, String toUser){
        this.type = type;
        this.content = content;
        this.toUser = toUser;
    }

    public Message(int points){
        this.type = TYPE_POINTS;
        this.content = ""+points;
    }

    public Message(String msg){
        this.type = TYPE_MSG;
        this.content = msg;
    }

    public String toJSON(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", type);
            jsonObject.put("content", content);
        }catch(JSONException e){
            e.toString();
        }
        return jsonObject.toString();
    }
}
