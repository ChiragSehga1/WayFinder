package application.Handler;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static application.service.connection.*;

public class Handler extends TextWebSocketHandler {
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {

        String payload = message.getPayload();
        System.out.println("Received: " + payload);//format for message = "function:/function used/,attribute1:/attribute/,..."
        HashMap<String, String> request = new HashMap<String, String>();
        for (String key : payload.split(",")) {
            String[] pair = key.split(":");
            request.put(pair[0], pair[1]);
        }
        String reply = "";
        switch(request.get("function")) {
            case "login":
                if (login(request.get("username"), request.get("password"))){
                    reply = "success";
                }
                else {
                    reply = "fail";
                }
                break;
            case "sign_up":
                if (sign_up(request.get("username"), request.get("name"), request.get("contact"), request.get("lastLocation"), request.get("password"))){
                    reply = "success";
                }
                else {
                    reply = "fail";
                }
                break;
            case "delete_account":
                delete_account(request.get("username"));
                reply = "success";
                break;
            case "addRequest":
                addRequest(request.get("sender"),request.get("receiver"));
                reply = "success";
                break;
            case "removeRequest":
                removeRequest(request.get("sender"),request.get("receiver"));
                reply = "success";
                break;
            case "addFriend":
                if (addFriend(request.get("username1"),request.get("username2"))){
                    reply = "success";
                }
                else {
                    reply = "fail";
                }
                break;
            case "removeFriend":
                removeFriend(request.get("username1"),request.get("username2"));
                reply = "success";
                break;
            case "getPendingRequests":
                List<String> Pending_requests =  getPendingRequests(request.get("username"));
                reply = String.join(",",Pending_requests);
                break;
            case "getFriends":
                List<String> friends = getFriends(request.get("username"));
                reply = String.join(",",friends);
                break;
            case "getLocation":
                reply = (request.get("username"));
                break;
            case "updatelocation":
                updatelocation(request.get("username"), request.get("location"));
                reply = "success";
                break;
            case "getUserDetails":
                getUserDetails(request.get("username"));
                reply = "success";
                break;
            default:
                reply = "invalid";
                break;
        }


        session.sendMessage(new TextMessage("Echo: " + reply));
    }
}
