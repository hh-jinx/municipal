package android.jlu.com.municipalmanage.fragment;

import android.jlu.com.municipalmanage.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by beyond on 17/4/11.
 */

public class UserMainFragment extends BaseFragment1 {

    public static BaseFragment1 newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name", name);
        BaseFragment1 fragment = new UserMainFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);
        TextView tv_show = (TextView) view.findViewById(R.id.tv_show);
        tv_show.setText("UserMainFragment");
        return view;
    }
}
