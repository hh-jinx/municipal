package android.jlu.com.municipalmanage.fragment;

import android.content.Intent;
import android.jlu.com.municipalmanage.R;
import android.jlu.com.municipalmanage.baseclass.Contacts;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by beyond on 17/4/11.
 */

public class ContactsFragment extends BaseFragment1{
    private RecyclerView contactsRecylerView;
    private ContactsAdapter mAdapter;
    private List<Contacts> mContactses = new ArrayList<>();

    public static BaseFragment1 newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        BaseFragment1 fragment = new ContactsFragment();
        fragment.setArguments(args);
        return fragment;
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        contactsRecylerView = (RecyclerView) view.findViewById(R.id.contacts_recyler_view);
        contactsRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }


    private void updateUI() {
        //初始化数据
        Contacts contacts1 = new Contacts("Bob","186xxxxxxxx");
        Contacts contacts2 = new Contacts("Echo","157xxxxxxxx");
        Contacts contacts3 = new Contacts("Alan","131xxxxxxxx");
        mContactses.add(contacts1);
        mContactses.add(contacts2);
        mContactses.add(contacts3);
        mAdapter = new ContactsAdapter(mContactses);
        contactsRecylerView.setAdapter(mAdapter);
    }
    private  class ContactsHolder extends  RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView telTextView;

        public ContactsHolder(View itemView){
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.contacts_name);
            telTextView = (TextView) itemView.findViewById(R.id.contacts_tel);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+telTextView.getText().toString()));
                    startActivity(intent);
                }
            });
        }

    }

    private class ContactsAdapter extends  RecyclerView.Adapter<ContactsHolder> {
        private List<Contacts> mContactsList;

        public ContactsAdapter(List<Contacts> mlist){
            mContactsList = mlist;
        }

        @Override
        public ContactsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.contacts_item,parent,false);
            return  new ContactsHolder(view);
        }

        @Override
        public void onBindViewHolder(ContactsHolder holder, int position) {
            Contacts mcontacts = mContactsList.get(position);
            holder.nameTextView.setText(mcontacts.getName());
            holder.telTextView.setText(mcontacts.getTel());
        }

        @Override
        public int getItemCount() {
            return mContactsList.size();
        }
    }
}
