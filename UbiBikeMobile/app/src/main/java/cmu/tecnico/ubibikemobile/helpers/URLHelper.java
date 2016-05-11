package cmu.tecnico.ubibikemobile.helpers;

import android.content.res.Resources;
import android.util.Pair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import cmu.tecnico.ubibikemobile.R;

public class URLHelper {
    Resources resources;

    public URLHelper(Resources resources) {
        this.resources = resources;
    }

    public Pair<Integer, List<String>> fetchUrl(String resource) throws IOException {
        URL url = new URL(resources.getString(R.string.protocol), resources.getString(R.string.domain),
                resources.getInteger(R.integer.port), resources.getString(R.string.base) + resource);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        BufferedReader rd;

        if(conn.getResponseCode() == 200) {
            rd = new BufferedReader(new InputStreamReader(
                    new BufferedInputStream(conn.getInputStream()), StandardCharsets.UTF_8));
        } else {
            rd = new BufferedReader(new InputStreamReader(
                    new BufferedInputStream(conn.getErrorStream()), StandardCharsets.UTF_8));
        }

        List<String> lines = new ArrayList<String>();
        String line;
        while ((line = rd.readLine()) != null) {
            lines.add(line);
        }

        return new Pair<Integer, List<String>>(conn.getResponseCode(), lines);
    }
}
