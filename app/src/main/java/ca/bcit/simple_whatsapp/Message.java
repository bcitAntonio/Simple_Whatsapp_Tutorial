package ca.bcit.simple_whatsapp;

public class Message {

    private String text;
    private MemberData data;
    private boolean belongsToCurrentUser;

    //overloaded constructor for string text, MemberData data, and boolean beLongToCurrentUser
    public Message(String text, MemberData data, boolean belongsToCurrentUser)
    {
        this.text = text;
        this.data = data;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public String getText() {
        return text;
    }

    public MemberData getData() {
        return data;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }
}
