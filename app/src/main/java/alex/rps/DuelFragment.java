package alex.rps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class DuelFragment extends Fragment {

    private int countUser = 0;
    private int countOpponent = 0;
    private boolean clickPistol = false;
    private boolean isMove = false;
    private Socket socket;
    private Emitter.Listener handleNewChat;
    private Emitter.Listener handleNewChat2;
    private Emitter.Listener handleNewChat3;
    private String handOfOpponent = "";
    LinearLayout linearLayoutOpponent;
    LinearLayout linearLayoutPistol;
    LinearLayout linearLayoutR;
    LinearLayout linearLayoutP;
    LinearLayout linearLayoutS;
    TextView textViewTime;
    TextView textView;
    TextView textViewMyScore;
    TextView textViewScoreOfOpponent;
    long time;
    Timer timer;
    long t1;
    public DuelFragment(Socket socket) {
        this.socket = socket;
    }

    public DuelFragment(Socket socket, long time) {
        this.socket = socket;
        this.time = time;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_duel, container, false);

        //linearLayoutOpponent = (LinearLayout) view.findViewById(R.id.linearLayoutOpponent);
        linearLayoutPistol = (LinearLayout) view.findViewById(R.id.linearLayoutPistol);
        linearLayoutR = (LinearLayout) view.findViewById(R.id.linearLayoutR);
        linearLayoutP = (LinearLayout) view.findViewById(R.id.linearLayoutP);
        linearLayoutS = (LinearLayout) view.findViewById(R.id.linearLayoutS);
        //textViewTime = (TextView) view.findViewById(R.id.textViewTime);
        textViewMyScore = (TextView) view.findViewById(R.id.textViewMyScore);
        textView = (TextView) view.findViewById(R.id.textView);
        textViewScoreOfOpponent = (TextView) view.findViewById(R.id.textViewScoreOfOpponent);
//        new CountDownTimer(3000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//                // Do nothing
//
//
//            }
//
//            public void onFinish() {
//                //mTask.cancel(true);
//            }
//
//        }.start();
//        timer = new Timer();
//        timer.schedule(new TimerTask()
//        {
//            @Override
//            public void run()
//            {
//                // open digital window
//                // your code here
//            }
//        }, 20000 );


        //socket.connect();
       // socket.emit("time", 3);

        handleNewChat = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                boolean obj = (Boolean) args[0];
                Log.d("message", "obj" + obj);
            }
        };

        //clickOpponent
        handleNewChat2 = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                    timer.cancel();
                    final String myMove = (String) args[0];
                    final String opMove = (String) args[1];
                    Log.d("myMove and opMove", "myMove: " + myMove + " opMove: " + opMove);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ( (opMove.equals("R") && myMove.equals("S"))
                                    || (opMove.equals("P") && myMove.equals("R"))
                                    || (opMove.equals("S") && myMove.equals("P")) ) {
                                //opWin
                                int i = Integer.parseInt(textViewScoreOfOpponent.getText().toString());
                                i++;
                                countOpponent= i;
                                if (i >=3) {
                                    EndGameFragment fragment2 = new EndGameFragment(socket, false);
                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.frameLayoutForFragments, fragment2);
                                    fragmentTransaction.commit();
                                }
                                textViewScoreOfOpponent.setText(""+i);
                            } else if (opMove.equals(myMove)) { //I win}

                            } else {
                                Log.d("else", "else");
                                int i = Integer.parseInt(textViewMyScore.getText().toString());
                                i++;
                                countUser = i;
                                if (i >= 3) {
                                    EndGameFragment fragment2 = new EndGameFragment(socket, true);
                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.frameLayoutForFragments, fragment2);
                                    fragmentTransaction.commit();
                                }
                                textViewMyScore.setText(""+i);
                            }
                            //textViewTime.setVisibility(View.VISIBLE);
                            linearLayoutPistol.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                            //shot = 0;
                            //clickPistol = false;
                            time = 3;
                            isMove = false;
//                            if(countUser < 3 && countOpponent < 3) {
//                                socket.emit("time", 3);
//                            }
                        }
                    });
//                timer.schedule(new TimerTask()
//                {
//                    @Override
//                    public void run()
//                    {
//                        // your code here
//                    }
//                }, 30000 );
                }

        };
        handleNewChat3 = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                boolean obj = (Boolean) args[0];
                Log.d("disconnect", " disconnect "  +  obj);
                socket.emit("deleteRoom",true);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MenuFragment fragment2 = new MenuFragment(socket);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayoutForFragments, fragment2);
                        fragmentTransaction.commit();
                    }
                });
            }
        };

        socket.on("clickOpponent",handleNewChat);
        socket.on("moves",handleNewChat2);
        socket.on("endGame",handleNewChat3);
        linearLayoutR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMove) {
                    socket.emit("move", "R");
                    isMove = true;
                }
            }
        });
        linearLayoutP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMove) {
                    socket.emit("move", "P");
                    isMove = true;
                }
            }
        });
        linearLayoutS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMove) {
                    socket.emit("move", "S");
                    isMove = true;
                }
            }
        });
        return view;
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.linearLayoutR:
//                socket.emit("move", "R");
//                // invisible other moves
//                break;
//            case R.id.linearLayoutP:
//                socket.emit("move", "P");
//                // invisible other moves
//                break;
//            case R.id.linearLayoutS:
//                socket.emit("move", "S");
//                // invisible other moves
//                break;
//        }
//    }
}
