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
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.activity.modules.UtilityModule;
import heartbeat.social.tcs.socialhb.bean.Module;
import heartbeat.social.tcs.socialhb.bean.UtilityCategory;
import heartbeat.social.tcs.socialhb.utility.ModuleSelector;
import heartbeat.social.tcs.socialhb.utility.UtilityModuleSelector;

/**
 * Created by admin on 26/07/16.
 */
public class UtilityCatAdapter extends RecyclerView.Adapter<UtilityCatAdapter.ViewUtilityCatAdapter>
{

    private ArrayList<UtilityCategory> utility_enabled_modules;
    private Context context;


    public UtilityCatAdapter(ArrayList<UtilityCategory> c_utility_enabled_modules, Context c_ctx){
        this.utility_enabled_modules = c_utility_enabled_modules;
        this.context = c_ctx;
    }

    @Override
    public ViewUtilityCatAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_utility_item, parent, false);
        ViewUtilityCatAdapter avh = new ViewUtilityCatAdapter(v, context, utility_enabled_modules);
        return avh;
    }

    @Override
    public void onBindViewHolder(ViewUtilityCatAdapter holder, int position) {
        holder.v_utilityCategory.setText(utility_enabled_modules.get(position).getUtility_cat_name());

        //Picasso.with(context).load(Web_API_Config.root_image_url + modules.get(position).getImage()).error(R.drawable.image_loading_error).into(holder.v_imageView);

        //Picasso.with(context).load("http://"+modules.get(position).getImage()).into(holder.v_imageView);
        //holder.v_imageView.setImageResource(modules.get(position).getImageId());
        //Picasso.with(context).load(modules.get(position).getModule_icon_id()).into(holder.v_imageView);


    }

    @Override
    public int getItemCount() {
        return utility_enabled_modules.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class ViewUtilityCatAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{



        public TextView v_utilityCategory;
        public CardView v_cardView;
        public Context   v_ctx;
        public ArrayList<UtilityCategory> v_utility_enabled_modules = new ArrayList<UtilityCategory>();

        public ViewUtilityCatAdapter(View itemView, Context c_ctx, ArrayList<UtilityCategory> c_utility_enabled_modules) {
            super(itemView);
            v_utilityCategory   = (TextView) itemView.findViewById(R.id.utilityCategory);
            v_cardView          = (CardView) itemView.findViewById(R.id.utilityCatCard);

            //Getting Screen Size
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
            int deviceWidth = displayMetrics.widthPixels;
            int deviceHeight = displayMetrics.heightPixels;

            //setting cardsize
            v_cardView.getLayoutParams().height = (deviceHeight * 43)/100;

            this.v_ctx     = c_ctx;
            this.v_utility_enabled_modules = c_utility_enabled_modules;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();


            int utility_module_id       = v_utility_enabled_modules.get(position).getId();
            String utility_module_name  = v_utility_enabled_modules.get(position).getUtility_cat_name();


            UtilityModuleSelector utilityModuleSelector =  new UtilityModuleSelector();
            String pack_name              =  "heartbeat.social.tcs.socialhb.activity.modules.sub_modules.";
            String main_utility_module_name       =  utilityModuleSelector.getUtilityModuleClassNameByUtilityModuleId(utility_module_id);

            String cmplt_utility_module_name      = pack_name.concat(main_utility_module_name);


            Intent intent = null;
            try {
                intent = new Intent(this.v_ctx, Class.forName(cmplt_utility_module_name));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.v_ctx.startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

    }
}


