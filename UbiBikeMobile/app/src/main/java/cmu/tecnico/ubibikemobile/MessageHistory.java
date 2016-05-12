package cmu.tecnico.ubibikemobile;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Toninho on 11/05/2016.
 */
public class MessageHistory {

    public String history;
    Context context;

    public MessageHistory(Context context){
        this.context = context;
    }

    public void writeFile(String name, String data){
        try{
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(name+".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }catch (IOException e){
            Log.d("Exception",e.getMessage());
        }
    }

    public String readFile(String name){
        String history = "";
        try{
            InputStream inputStream = context.openFileInput(name);
            if(inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                history = stringBuilder.toString();
            }
        }catch (FileNotFoundException e){
            Log.e("read file","File not found: "+e.toString());
        }catch (IOException e){
            Log.e("read file", "file not found: "+e.toString());
        }
        return history;
    }
}
