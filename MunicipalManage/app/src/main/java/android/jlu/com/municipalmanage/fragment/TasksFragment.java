package android.jlu.com.municipalmanage.fragment;

import android.content.Intent;
import android.jlu.com.municipalmanage.R;
import android.jlu.com.municipalmanage.activity.TaskItemActivity;
import android.jlu.com.municipalmanage.baseclass.Task;
import android.jlu.com.municipalmanage.baseclass.UriSet;
import android.jlu.com.municipalmanage.utils.RetrofitGetTasks;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class TasksFragment extends BaseFragment1 {

    private static final String TAG = "TasksFragment";
    private RecyclerView mRecyclerView;
    private List<Task.ProBeanBean>  mProBeanBeen = new ArrayList<>();
    private TaskAdapter Adapter;
    private SwipeRefreshLayout swipeRefresh;

    public static TasksFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        TasksFragment fragment = new TasksFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks,container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.task_recycler_view);
        swipeRefresh = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        initDate();
         swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.blue));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });
        return view;

    }

    private void  initDate(){
        //开启网络请求
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UriSet.SERVER_URI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitGetTasks retrofitGetTasks = retrofit.create(RetrofitGetTasks.class);
        Call<Task> call = retrofitGetTasks.getTasks();
        call.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                mProBeanBeen = response.body().getProBean();
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                //初始化构造器
                Adapter = new TaskAdapter(mProBeanBeen);
                mRecyclerView.setAdapter(Adapter);
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Toast.makeText(getActivity(),"网络错误，请检查网络",Toast.LENGTH_SHORT).show();
            }
        });
    }
        private void refreshList(){
    getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
            initDate();
            Adapter.notifyDataSetChanged();
            swipeRefresh.setRefreshing(false);
        }
    });


      }

    //实现adapter和ViewHolder
    private class TasksHolder extends RecyclerView.ViewHolder{
        public TextView find_time,find_address,task_type,task_desc;
        public  TasksHolder(View itemView) {
            super(itemView);
            find_time = (TextView)itemView.findViewById(R.id.find_time);
            find_address = (TextView)itemView.findViewById(R.id.find_address);
            task_type = (TextView)itemView.findViewById(R.id.task_type);
            task_desc = (TextView)itemView.findViewById(R.id.task_desc);


        }
    }


    private class TaskAdapter extends RecyclerView.Adapter<TasksHolder>{
        private List<Task.ProBeanBean> probeanlist;
        //构造方法，初始化列表
        public TaskAdapter(List<Task.ProBeanBean> prolist){
            probeanlist = prolist;

        }

        @Override
        public TasksHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            View view = layoutInflater.inflate(R.layout.tasks_item,parent,false);
            final TasksHolder holder = new TasksHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Task.ProBeanBean pro = probeanlist.get(position);
                    Intent intent = new Intent(getActivity(), TaskItemActivity.class);
                    String time1 = pro.getFind_time().replace("T","");//去除时间格式不对，里面的T
                    //转化为string
                    String id = String.valueOf(pro.getId());
                    String state = String.valueOf(pro.getPro_state());
                    intent.putExtra("task_id",id);
                    intent.putExtra("task_time",time1);
                    intent.putExtra("task_address",pro.getAddress());
                    intent.putExtra("task_type",pro.getType());
                    intent.putExtra("task_state",state);
                    intent.putExtra("task_desc",pro.getSite_desc());
                    intent.putExtra("task_pic",pro.getFind_pic());
                    intent.putExtra("task_video",pro.getFind_video());
                    startActivity(intent);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(TasksHolder holder, int position) {
            Task.ProBeanBean proBeanBean = probeanlist.get(position);

            String time = proBeanBean.getFind_time().replace("T","");//去除时间格式不对，里面的T
            holder.find_time.setText(time);
            holder.find_address.setText(proBeanBean.getAddress());
            holder.task_type.setText(proBeanBean.getType());
            holder.task_desc.setText(proBeanBean.getSite_desc());

        }

        @Override
        public int getItemCount() {
            return probeanlist.size();
        }
    }


}