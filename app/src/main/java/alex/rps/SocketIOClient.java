package alex.rps;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketIOClient {

private static Socket mSocket;

private static void initSocket() {
    //дом"http://192.168.0.103:4000/"  влад"http://192.168.1.8:4000/"
    try {
       mSocket = IO.socket("http://192.168.0.101:4000/");
        mSocket.connect();
    } catch (URISyntaxException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
    }
}

public static Socket getInstance() {
    if (mSocket != null) {
        return mSocket;
    } else {
        initSocket();
        return mSocket;
    }
} }