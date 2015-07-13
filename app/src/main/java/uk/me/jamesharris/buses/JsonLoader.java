package uk.me.jamesharris.buses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by james on 02/05/15.
 */
public class JsonLoader {
    public static ArrayList<String> getUrlLines(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            if (conn.getResponseCode()!=HttpURLConnection.HTTP_OK) {
                return null;
            }
            String line;
            ArrayList<String> res = new ArrayList<String>();
            while ((line = in.readLine())!=null) {
                res.add(line);
            }

            return res;
        } finally {
            conn.disconnect();
        }

    }

    public static String getUrl(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            if (conn.getResponseCode()!=HttpURLConnection.HTTP_OK) {
                return null;
            }
            StringBuilder lines = new StringBuilder();
            String line;
            while ((line = in.readLine())!=null) {
                lines.append(line);
            }
            return lines.toString();
        } finally {
            conn.disconnect();
        }

    }

}

