package ca.bcit.simple_whatsapp;

class MemberData {

    private String name;
    private String color;

    //default constructor
    public MemberData(){}

    //overloaded constructor with String name and color
    public MemberData(String name, String color)
    {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

}
