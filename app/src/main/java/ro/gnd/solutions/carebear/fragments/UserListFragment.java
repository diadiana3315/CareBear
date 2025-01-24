package ro.gnd.solutions.carebear.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ro.gnd.solutions.carebear.R;
import ro.gnd.solutions.carebear.adapters.FriendAdapter;
import ro.gnd.solutions.carebear.models.Friend;

public class UserListFragment extends Fragment {

    private RecyclerView recyclerView;
    private FriendAdapter friendAdapter;
    private List<Friend> friendsList;

    private FirebaseDatabase database;
    private String loggedUserId;

    public UserListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_list, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get the logged user ID
        loggedUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();

        // Initialize the list of friends
        friendsList = new ArrayList<>();

        // Load the list of friends from Firebase
        loadFriendsList();

        // Set up the close button (X button)
        ImageButton closeButton = rootView.findViewById(R.id.btn_close_user_list);
        closeButton.setOnClickListener(v -> {
            // Pop the current fragment off the back stack and return to the ChatsListFragment
            if (isAdded()) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return rootView;
    }

    private void loadFriendsList() {
        DatabaseReference friendsRef = database.getReference("users").child(loggedUserId).child("friends");

        friendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Friend friend = snapshot.getValue(Friend.class);
                    if (friend != null) {
                        friendsList.add(friend);
                    }
                }

                // Once the data is loaded, set the adapter to display the friends
                friendAdapter = new FriendAdapter(getContext(), friendsList);
                recyclerView.setAdapter(friendAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error if needed
            }
        });
    }
}
