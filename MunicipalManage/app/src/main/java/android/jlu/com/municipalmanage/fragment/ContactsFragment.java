package android.jlu.com.municipalmanage.fragment;

import android.content.Intent;
import android.jlu.com.municipalmanage.R;
import android.jlu.com.municipalmanage.baseclass.Contacts;
import android.jlu.com.municipalmanage.baseclass.UriSet;
import android.jlu.com.municipalmanage.utils.RetrofitGetContacts;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by beyond on 17/4/11.
 */

public class ContactsFragment extends BaseFragment1{
    private RecyclerView contactsRecylerView;
    private ContactsAdapter mAdapter;
    private List<Contacts.EmpBeanBean> mEmpBeanBeen = new ArrayList<>();

    public static ContactsFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        ContactsFragment fragment = new ContactsFragment();
        fragment.setArguments(args);
        return fragment;
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        contactsRecylerView = (RecyclerView) view.findViewById(R.id.contacts_recyler_view);
        updateUI();
        return view;
    }


    private void updateUI() {
        //开启网络请求
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UriSet.SERVER_URI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitGetContacts retrofitGetContacts = retrofit.create(RetrofitGetContacts.class);

        Call<Contacts> call = retrofitGetContacts.getContacts();
       call.enqueue(new Callback<Contacts>() {
           @Override
           public void onResponse(Call<Contacts> call, Response<Contacts> response) {
               mEmpBeanBeen = response.body().getEmpBean();
               contactsRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
               //初始化构造器
               mAdapter = new ContactsAdapter(mEmpBeanBeen);
               contactsRecylerView.setAdapter(mAdapter);
           }

           @Override
           public void onFailure(Call<Contacts> call, Throwable t) {

               Toast.makeText(getActivity(),"网络错误，请检查网络",Toast.LENGTH_SHORT).show();
           }
       });
    }
    private  class ContactsHolder extends  RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView telTextView;
        public TextView titleTextView;

        public ContactsHolder(View itemView){
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.contacts_name);
            telTextView = (TextView) itemView.findViewById(R.id.contacts_tel);
            titleTextView = (TextView) itemView.findViewById(R.id.contacts_tilte);
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
        private List<Contacts.EmpBeanBean> mEmpBeanBeen1 = new ArrayList<>();

        public ContactsAdapter(List<Contacts.EmpBeanBean> mlist){
            mEmpBeanBeen1 = mlist;
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
            Contacts.EmpBeanBean empBeanBean = mEmpBeanBeen1.get(position);

            holder.nameTextView.setText(empBeanBean.getName());
            holder.telTextView.setText(empBeanBean.getPhone());
            holder.titleTextView.setText(empBeanBean.getTitle());
        }

        @Override
        public int getItemCount() {
            return mEmpBeanBeen1.size();
        }
    }
}
