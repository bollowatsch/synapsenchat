package at.ac.fhcampuswien.synapsenchat.connection;

import java.io.Serializable;
import java.util.List;

public class Chat implements Serializable {
    private int id;
    private String chatName;
    private List<String> msgList;

    public void setId(int Id){
        id = Id;
    }

    public void setChatName(String nameInput){
        chatName = nameInput;
    }
    public String getChatName(){
        return chatName;
    }

    public void setMsg(String input){
        msgList.add(input);
    }
    public List<String> getMsg(){
        return msgList;
    }
}
