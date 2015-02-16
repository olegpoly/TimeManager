package com.github.olegpoly.TimeManager.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.olegpoly.TimeManager.R;
import com.github.olegpoly.TimeManager.UiUtils.CustomRecyclerViewAdapter;
import com.github.olegpoly.TimeManager.UiUtils.Information;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

    public static final String PREF_FILE_NAME = "testpref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private RecyclerView recyclerView;
    private CustomRecyclerViewAdapter adapter;
    private View containerView;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    /**
     * form list with menu options
     * @return
     */
    public List<Information> getData() {
        List<Information> data = new ArrayList<>();
        int[] icons = {R.drawable.abc_btn_check_material};

        Resources res =  getResources();
        String[] planets = res.getStringArray(R.array.menu_options);

        for (int i = 0; i < planets.length; i++) {
            Information current = new Information();
            current.iconId = icons[0];
            current.title = planets[i];
            data.add(current);
        }

        return data;
    }

    /**
     * @param context
     * @param preferenceName  name of the preference to be saved
     * @param preferenceValue value of the preference to b saved
     */
    public static void saveToReferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,
                Context.MODE_PRIVATE); // this app is the only one that can edit the preference

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    /**
     * @param context
     * @param preferenceName name of the preference to be saved
     * @param defaultValue   default value of the preference
     */
    public static String readFromReferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,
                Context.MODE_PRIVATE); // this app is the only one that can edit the preference

        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.getBoolean(readFromReferences(getActivity(),
                KEY_USER_LEARNED_DRAWER,
                "false")); // default value

        if (savedInstanceState != null) {
            // after rotation
            mFromSavedInstanceState = true;
        }

        Toast.makeText(this.getActivity(), "onCreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        adapter = new CustomRecyclerViewAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Toast.makeText(this.getActivity(), "onCreateView", Toast.LENGTH_SHORT).show();

        return layout;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);

        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToReferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer + "");
                }

                // redraw the menu
                getActivity().invalidateOptionsMenu();

                Toast.makeText(drawerView.getContext(), "onDrawerOpened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();

                Toast.makeText(drawerView.getContext(), "onDrawerClosed", Toast.LENGTH_SHORT).show();
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(containerView);
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }
}
