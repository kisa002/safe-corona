package com.haeyum.safecorona;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haeyum.safecorona.models.InfectedRoute;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class DetailInfectedRouteAdapter extends RecyclerView.Adapter<DetailInfectedRouteAdapter.ViewHolder> {
    private ArrayList<InfectedRoute> mData = null ;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvRoute;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            tvDate = itemView.findViewById(R.id.tv_main_detail_rv_item_date) ;
            tvRoute = itemView.findViewById(R.id.tv_main_detail_rv_item_route) ;
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    DetailInfectedRouteAdapter(ArrayList<InfectedRoute> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public DetailInfectedRouteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_rv_main_detail_route, parent, false) ;
        DetailInfectedRouteAdapter.ViewHolder vh = new DetailInfectedRouteAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(DetailInfectedRouteAdapter.ViewHolder holder, int position) {
//        String text = mData.get(position).getDate();
        holder.tvDate.setText(mData.get(position).getDate());
        holder.tvRoute.setText(mData.get(position).getRoute());

//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.height = 200; //height recycleviewer
//        holder.itemView.setLayoutParams(params);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}
