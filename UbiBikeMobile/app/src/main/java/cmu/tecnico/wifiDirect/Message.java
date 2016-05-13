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
    String fromUser;

    String hash;

    public static final String TYPE_POINTS = "POINTS";
    public static final String TYPE_MSG = "MSG";
    public static final String TYPE_NAME = "NAME";

    public Message(String type, String content, String toUser, String fromUser) {
        this.type = type;
        this.content = content;
        this.toUser = toUser;
        this.fromUser = fromUser;
    }

    public Message(int points) {
        this.type = TYPE_POINTS;
        this.content = "" + points;
    }

    public Message(int points, String toUser, String fromUser) {
        this.type = TYPE_POINTS;
        this.content = "" + points;
        this.toUser = toUser;
        this.fromUser = fromUser;
    }

    public Message(String msg) {
        this.type = TYPE_MSG;
        this.content = msg;
    }

    public Message(String msg, String toUser, String fromUser) {
        this.type = TYPE_MSG;
        this.content = msg;
        this.toUser = toUser;
        this.fromUser = fromUser;
    }

    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", type);
            jsonObject.put("content", content);
            jsonObject.put("toUser", toUser);
            jsonObject.put("fromUser", fromUser);
        } catch (JSONException e) {
            e.toString();
        }
        return jsonObject.toString();
    }
}
