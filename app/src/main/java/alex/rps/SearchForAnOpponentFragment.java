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
import android.widget.TextView;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class SearchForAnOpponentFragment extends Fragment {
    LinearLayout linearGO;
    LinearLayout linearLayoutYes;
    LinearLayout linearLayoutNo;
    TextView textViewGo;
    private Socket socket;
    private Emitter.Listener handleJoin;
    private Emitter.Listener handleNewChat2;
    private Emitter.Listener handleNewChat3;
    Activity activity;
    private boolean go = false;
    private int quantity = 0;
    private boolean opponentIsReady = false;
    private boolean iIsReady = false;
    private long time;
    public SearchForAnOpponentFragment(Socket socket) {
        this.socket = socket;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_for_an_opponent, container, false);
        linearGO = (LinearLayout) view.findViewById(R.id.linearGO);
        linearLayoutNo = (LinearLayout) view.findViewById(R.id.linearLayoutNo);
        linearLayoutYes = (LinearLayout) view.findViewById(R.id.linearLayoutYes);
        textViewGo = (TextView) view.findViewById(R.id.textViewGo);

//
//  socket = SocketIOClient.getInstance();
        //socket.connect();
        Log.d("join", "hanfleJoin");
        handleJoin = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                String obj = (String) args[0];
                Log.d("join", obj);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!go) {
                            Log.d("SearchFragment ", "SearchFragment GO");
                            textViewGo.setText(getResources().getString(R.string.you_are_ready));
                            linearLayoutNo.setVisibility(View.VISIBLE);
                            linearLayoutYes.setVisibility(View.VISIBLE);
                            go = true;
                        }
                    }
                });
            }
        };

        handleNewChat2 = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                boolean obj = (Boolean) args[0];
                time = (Long) args[1];
                Log.d("isReady"," opponentIsReady: "  +  obj);
                opponentIsReady = true;
                if (go && iIsReady && opponentIsReady) {
                    DuelFragment fragment2 = new DuelFragment(socket, time);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayoutForFragments, fragment2);
                    fragmentTransaction.commit();
                }
            }
        };

        handleNewChat3 = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                boolean obj = (Boolean) args[0];
                Log.d("disconnect", " disconnect "  +  obj);
                socket.emit("searchOpponentAgain",true);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewGo.setText(getResources().getString(R.string.search_opponent));
                        linearLayoutNo.setVisibility(View.GONE);
                        linearLayoutYes.setVisibility(View.GONE);
                        go = false;
                    }
                });
            }
        };


        linearLayoutYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (go && !iIsReady) {
                    Log.d("SearchFragment ", "SearchFragment iIsReady");
                    socket.emit("isReady", "true");
                    iIsReady = true;
                }
                if (go && iIsReady && opponentIsReady) {
                    DuelFragment fragment2 = new DuelFragment(socket,time);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayoutForFragments, fragment2);
                    fragmentTransaction.commit();
                }
            }
        });

        linearLayoutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.emit("isNotReady", "false");
                MenuFragment fragment2 = new MenuFragment(socket);
                socket.off("endGame", handleNewChat3);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutForFragments, fragment2);
                fragmentTransaction.commit();
            }
        });

        socket.on("join",handleJoin);
        socket.on("opIsReady",handleNewChat2);
        socket.on("endGame", handleNewChat3);
        return view;
    }
}
