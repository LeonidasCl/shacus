package com.example.pc.shacus.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.pc.shacus.R;

import io.rong.imkit.fragment.ConversationFragment;

/**
 * Created by 融云 on 15/7/28.
 * fragmet 动态集成
 * 李嘉文 16/9/12
 * 单聊Activity中的Fragment
 */
public class ConversationDynamicFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rong_activity, container, false);

        ConversationFragment fragment = new ConversationFragment();
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(io.rong.imlib.model.Conversation.ConversationType.PRIVATE.getName().toLowerCase())
                .appendQueryParameter("title", "私信页面").build();
        fragment.setUri(uri);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.rong_content, fragment);
        transaction.commit();

        return view;
    }
}
