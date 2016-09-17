package heartbeat.social.tcs.socialhb.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.activity.modules.sub_modules.CSRInitCategoryDescription;
import heartbeat.social.tcs.socialhb.bean.CSRInit;
import heartbeat.social.tcs.socialhb.bean.CSRInitCatData;

/**
 * Created by admin on 23/07/16.
 */
  public class CSRInitCatDataAdapter extends RecyclerView.Adapter<CSRInitCatDataAdapter.ViewCSRInitCatDataAdapter>
 {

    private ArrayList<CSRInitCatData> csr_init_category_data_list;
    private Context context;


    public CSRInitCatDataAdapter(ArrayList<CSRInitCatData> c_category_data, Context c_ctx){
        this.csr_init_category_data_list = c_category_data;
        this.context                     = c_ctx;
    }

    @Override
    public ViewCSRInitCatDataAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_csr_init_cat_data_item, parent, false);
        ViewCSRInitCatDataAdapter avh = new ViewCSRInitCatDataAdapter(v, context, csr_init_category_data_list);
        return avh;
    }

    @Override
    public void onBindViewHolder(ViewCSRInitCatDataAdapter holder, int position) {
        holder.v_cat_heading_txtView.setText(csr_init_category_data_list.get(position).getCat_data_heading());
        holder.v_cat_data_txtView.setText(csr_init_category_data_list.get(position).getCat_data());


        Log.e("Cat Data ", csr_init_category_data_list.get(position).getCat_data());
        //Picasso.with(context).load(Web_API_Config.root_image_url + modules.get(position).getImage()).error(R.drawable.image_loading_error).into(holder.v_imageView);

        //Picasso.with(context).load("http://"+modules.get(position).getImage()).into(holder.v_imageView);
        //holder.v_imageView.setImageResource(modules.get(position).getImageId());
       //Picasso.with(context).load(csr_init_category_data_list.get(position).getCsr_module_icon_id()).into(holder.v_imageView);


    }

    @Override
    public int getItemCount() {
        return csr_init_category_data_list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class ViewCSRInitCatDataAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView v_cat_data_txtView, v_cat_heading_txtView;
        public CardView v_cardView;
        public Context   v_ctx;
        public ArrayList<CSRInitCatData> v_cat_data = new ArrayList<CSRInitCatData>();

        public ViewCSRInitCatDataAdapter(View itemView, Context c_ctx, ArrayList<CSRInitCatData> c_category_data) {
            super(itemView);
            v_cat_heading_txtView    = (TextView) itemView.findViewById(R.id.catDataHeadingTxtView);
            v_cat_data_txtView       = (TextView) itemView.findViewById(R.id.catDataTxtView);
            v_cardView               = (CardView) itemView.findViewById(R.id.moduleCard);
            this.v_ctx               = c_ctx;
            this.v_cat_data          = c_category_data;



            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();

            int    cat_data__id   = v_cat_data.get(position).getId();
            String cat_data       = v_cat_data.get(position).getCat_data();


          /*  ModuleSelector moduleSelector =  new ModuleSelector();
            String pack_name              =  "heartbeat.social.tcs.socialhb.activity.modules.";
            String main_module_name       =  moduleSelector.getClassNameByModuleId(module_id);

            String cmplt_module_name      = pack_name.concat(main_module_name);


            Intent intent = null;
            try {
                intent = new Intent(this.v_ctx, Class.forName(cmplt_module_name));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.v_ctx.startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }*/


        }

    }
}



