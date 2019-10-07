package mychatappp.networking;

import java.util.Map;

import application.Client;

/**
 *
 * @author Abrar
 */
public interface WritableGUI {   
    void writeMsg(String Msg);
    void writeFriendList(Map<String,Client> Clients); 
}