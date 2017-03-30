package alex.rps;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MenuFragment extends Fragment {

    LinearLayout linearLayoutFindOpponent;
    private Socket socket;
    private Emitter.Listener handleNewChat;
    Activity activity;

    public MenuFragment(Socket socket) {
        this.socket = socket;
    }

    public MenuFragment() {

    }

//    public MenuFragment(Socket socket) {
//        Log.d("MenuFragment ", "MenuFragment Socket");
//        this.socket = socket;
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        LinearLayout linearLayoutFindOpponent = (LinearLayout) view.findViewById(R.id.linearLayoutFindOpponent);
        if(socket == null) {
            socket = SocketIOClient.getInstance();
        } else if (!socket.connected()) {
            socket = SocketIOClient.getInstance();
        }
        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("Disconect", "Disconnect");
            }
        });

        handleNewChat = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                String obj = (String) args[0];
                Log.d("go", obj);
            }

        };
        socket.on(Socket.EVENT_DISCONNECT,handleNewChat);
        linearLayoutFindOpponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //При нажатии на поиск соперника, надо отправить завпрос , что мы ищем соперника.
                // После того , как получили ответ, что соперник найден перекидываем его на другой фрагмент
                socket.emit("join", "Alex");
                Log.d("MenuFragment ", "MenuFragment join");
                SearchForAnOpponentFragment fragment2 = new SearchForAnOpponentFragment(socket);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutForFragments, fragment2);
                fragmentTransaction.commit();
            }
        });
        return view;

    }
}
