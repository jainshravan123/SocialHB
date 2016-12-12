package heartbeat.social.tcs.socialhb.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.activity.modules.sub_modules.DonatedItemStatusDescription;
import heartbeat.social.tcs.socialhb.activity.modules.sub_modules.FactDescription;
import heartbeat.social.tcs.socialhb.bean.Fact;
import heartbeat.social.tcs.socialhb.bean.SingleDonateItem;

/**
 * Created by admin on 30/11/16.
 */
public class FactAdapter extends RecyclerView.Adapter<FactAdapter.ViewFactAdapter>{


    private ArrayList<Fact> facts_list;
    private Context context;
    private String TAG = "FactAdapter";

    public FactAdapter(ArrayList<Fact> c_facts_list, Context c_ctx){
        this.facts_list  =   c_facts_list;
        this.context     =   c_ctx;
    }

    @Override
    public ViewFactAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fact_item, parent, false);
        ViewFactAdapter avh = new ViewFactAdapter(v, context, facts_list);
        return avh;
    }

    @Override
    public void onBindViewHolder(ViewFactAdapter holder, int position) {

        holder.v_txt_fact.setText(facts_list.get(position).getFact());
        holder.v_fact_city.setText(facts_list.get(position).getCity().getName());
        // holder.v_fact_img.setImageResource();
        Picasso.with(context).load(facts_list.get(position).getHostedImage().getImage_url()).into(holder.v_fact_img);

Log.e("URL", facts_list.get(position).getHostedImage().getImage_url());
    }

    @Override
    public int getItemCount() {
        return facts_list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class ViewFactAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{


        public ImageView  v_fact_img;
        public TextView   v_txt_fact;
        public TextView   v_fact_city;
        public Context    v_ctx;
        public ArrayList<Fact> v_facts_lists = new ArrayList<Fact>();

        public ViewFactAdapter(View itemView, Context c_ctx, ArrayList<Fact> c_donated_items) {
            super(itemView);

            v_fact_img  = (ImageView) itemView.findViewById(R.id.img_fact);
            v_txt_fact    = (TextView) itemView.findViewById(R.id.txt_fact);
            v_fact_city  = (TextView) itemView.findViewById(R.id.txt_fact_city);

            this.v_ctx             = c_ctx;
            this.v_facts_lists     = c_donated_items;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Intent intent1 = new Intent(this.v_ctx, FactDescription.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("fact_id", String.valueOf(facts_list.get(position).getId()));
            this.v_ctx.startActivity(intent1);


        }

    }

    public void setFilter(List<Fact> factList_p){
        Log.e(TAG, "::: Filtered Data List:::");
        for(Fact fact : factList_p){
            Log.e(TAG, fact.getCity().getName());
        }
        facts_list = new ArrayList<>();
        facts_list.addAll(factList_p);
        notifyDataSetChanged();
    }
}