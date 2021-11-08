package com.example.fateofcard.model;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RoomModel {
    public Map<String,Boolean> users=new HashMap<>();
    public Map<String,player> players=new HashMap();
    public Map<String,usingCards> usedCards=new HashMap<>();
    public Map<String,Boolean> turn=new HashMap<>();
    public Map<String,somebody> somebodyshands=new HashMap<>();
    public Map<String,handcards> yellowcards=new HashMap<>();


    public static class handcards{
        public String uid;
        public String yellowcard;
    }
    public static class usingCards{
        public String usingCard;
        public String stack;
        public String mark;
        public String item;
    }

    int i=1;
public static class somebody{
    public String handcard;


}

    public static class player {
    public String uid;
    }
}
