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

import io.socket.client.Socket;


public class EndGameFragment extends Fragment {


//    public MenuFragment(Socket socket) {
//        Log.d("MenuFragment ", "MenuFragment Socket");
//        this.socket = socket;
//    }
    LinearLayout linearLayoutBack;
    Socket socket;
    TextView textViewEnd;
    boolean b;
    public EndGameFragment(Socket socket) {
        this.socket = socket;
    }

    public EndGameFragment(Socket socket, boolean b) {
        this.socket = socket;
        this.b = b;
        Log.d("b", "b");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_end_game, container, false);
        linearLayoutBack = (LinearLayout) view.findViewById(R.id.linearLayoutBack);
        textViewEnd = (TextView) view.findViewById(R.id.textViewEnd);
        linearLayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(b) {
                    Log.d("b true", "b true");
                    textViewEnd.setText(getString(R.string.you_win));
                } else{
                    Log.d("b false", "b false");
                    textViewEnd.setText(getString(R.string.you_lose));
                }
                socket.emit("leaveFromRoom", "leaveFromRoom");
                MenuFragment fragment2 = new MenuFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutForFragments, fragment2);
                fragmentTransaction.commit();
            }
        });
        return view;
    }
}
