package ca.bcit.simple_whatsapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Member;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;
import java.util.Random;

public class MainActivity
        extends AppCompatActivity implements RoomListener {


    @NonNull
    private static final String TAG = MainActivity.class.getName();

    private String chId = "xZ4DHOhq5mwK5QXh";
    private String rmName = "observable-room";
    private EditText textBar;
    private Scaledrone scaleDrone;
    private MessageAdapter messageAdapter;
    private ListView messageView;


    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //writing message on msg bar
        textBar = findViewById(R.id.textBar);
        messageAdapter = new MessageAdapter(this);
        messageView = findViewById(R.id.messageView);
        messageView.setAdapter(messageAdapter);


        MemberData data = new MemberData(getRandomName(), getRandomColor());
        scaleDrone = new Scaledrone(chId, data);


        scaleDrone.connect(new Listener() {
            @Override
            public void onOpen() {
                System.out.println("Scaledrone connection starts");
                scaleDrone.subscribe(rmName, MainActivity.this);
            }

            @Override
            public void onOpenFailure(Exception ex) {
                Log.e(TAG, "scaleDrone OnOpenFailure exception error",ex);
            }

            @Override
            public void onFailure(Exception ex) {
                Log.e(TAG, "scaleDrone onFailure exception error",ex);
            }

            @Override
            public void onClosed(String reason) {
                Log.e(TAG, reason);
            }
        });

    }



    //Successfully connected to room, print msg to log
    @Override
    public void onOpen(Room room) {
        Log.i(TAG,"connected");
    }

    //Connection failed print msg to log
    @Override
    public void onOpenFailure(Room room, Exception ex) {
        Log.e(TAG, "OnOpenFailure exception error",ex);

    }

    //receiving a msg from room
    @Override
    public void onMessage(Room room, final JsonNode json, final Member member) {
        // To transform the raw JsonNode into a POJO we can use an ObjectMapper
        final ObjectMapper mapper = new ObjectMapper();
        try {
            // member.clientData is a MemberData object, let's parse it as such
            final MemberData data = mapper.treeToValue(member.getClientData(), MemberData.class);
            // if the clientID of the message sender is the same as our's it was sent by us
            boolean belongsToCurrentUser = member.getId().equals(scaleDrone.getClientID());
            // since the message body is a simple string in our case we can use json.asText() to parse it as such
            // if it was instead an object we could use a similar pattern to data parsing
            final Message message = new Message(json.asText(), data, belongsToCurrentUser);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageAdapter.add(message);
                    // scroll the ListView to the last added element
                    messageView.setSelection(messageView.getCount() - 1);
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private String getRandomName() {
        String[] adjs = {"autumn", "hidden", "bitter", "misty", "silent", "empty", "dry", "dark", "summer", "icy", "delicate", "quiet", "white", "cool", "spring", "winter", "patient", "twilight", "dawn", "crimson", "wispy", "weathered", "blue", "billowing", "broken", "cold", "damp", "falling", "frosty", "green", "long", "late", "lingering", "bold", "little", "morning", "muddy", "old", "red", "rough", "still", "small", "sparkling", "throbbing", "shy", "wandering", "withered", "wild", "black", "young", "holy", "solitary", "fragrant", "aged", "snowy", "proud", "floral", "restless", "divine", "polished", "ancient", "purple", "lively", "nameless"};
        String[] nouns = {"waterfall", "river", "breeze", "moon", "rain", "wind", "sea", "morning", "snow", "lake", "sunset", "pine", "shadow", "leaf", "dawn", "glitter", "forest", "hill", "cloud", "meadow", "sun", "glade", "bird", "brook", "butterfly", "bush", "dew", "dust", "field", "fire", "flower", "firefly", "feather", "grass", "haze", "mountain", "night", "pond", "darkness", "snowflake", "silence", "sound", "sky", "shape", "surf", "thunder", "violet", "water", "wildflower", "wave", "water", "resonance", "sun", "wood", "dream", "cherry", "tree", "fog", "frost", "voice", "paper", "frog", "smoke", "star"};

        return adjs[(int) Math.floor(Math.random() * adjs.length)] +
                        "_" +
                        nouns[(int) Math.floor(Math.random() * nouns.length)];
    }

    private String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while(sb.length() < 7){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }

    //sending msg to the same room and clear EditText view
    public void sendMessage(View view)
    {
        String message = textBar.getText().toString();
        //checking for empty string
        if(message.length() > 0)
        {
            scaleDrone.publish("observable-room", message);
            textBar.getText().clear();
        }
    }
}
