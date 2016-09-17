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
import heartbeat.social.tcs.socialhb.activity.modules.sub_modules.CSRInitCategoryDescription;
import heartbeat.social.tcs.socialhb.bean.CSRInit;
import heartbeat.social.tcs.socialhb.bean.Module;
import heartbeat.social.tcs.socialhb.utility.ModuleSelector;

/**
 * Created by admin on 22/07/16.
 */
public class CSRInitAdapter extends RecyclerView.Adapter<CSRInitAdapter.ViewCSRInitAdapter>
{

    private ArrayList<CSRInit> csr_init_categories;
    private Context context;


    public CSRInitAdapter(ArrayList<CSRInit> c_categories, Context c_ctx){
        this.csr_init_categories = c_categories;
        this.context             = c_ctx;
    }

    @Override
    public ViewCSRInitAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.csr_init_single_item, parent, false);
        ViewCSRInitAdapter avh = new ViewCSRInitAdapter(v, context, csr_init_categories);
        return avh;
    }

    @Override
    public void onBindViewHolder(ViewCSRInitAdapter holder, int position) {
        holder.v_txtView.setText(csr_init_categories.get(position).getCsr_module_name());


        Log.e("Module Image Path : ", csr_init_categories.get(position).getCsr_module_icon());
        //Picasso.with(context).load(Web_API_Config.root_image_url + modules.get(position).getImage()).error(R.drawable.image_loading_error).into(holder.v_imageView);

        //Picasso.with(context).load("http://"+modules.get(position).getImage()).into(holder.v_imageView);
        //holder.v_imageView.setImageResource(modules.get(position).getImageId());
        Picasso.with(context).load(csr_init_categories.get(position).getCsr_module_icon_id()).into(holder.v_imageView);


    }

    @Override
    public int getItemCount() {
        return csr_init_categories.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class ViewCSRInitAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{


        //  public TextView v_txtView;
        public ImageView v_imageView;
        public TextView v_txtView;
        public CardView v_cardView;
        public Context   v_ctx;
        public ArrayList<CSRInit> v_modules = new ArrayList<CSRInit>();

        public ViewCSRInitAdapter(View itemView, Context c_ctx, ArrayList<CSRInit> c_categories) {
            super(itemView);
            v_txtView   = (TextView) itemView.findViewById(R.id.csrInitTxtView);
            v_imageView = (ImageView) itemView.findViewById(R.id.csrInitImageView);
            v_cardView  = (CardView) itemView.findViewById(R.id.csrInitCard);

            //Getting Screen Size
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
            int deviceWidth = displayMetrics.widthPixels;
            int deviceHeight = displayMetrics.heightPixels;

            //setting cardsize
            v_cardView.getLayoutParams().height = (deviceHeight * 43)/100;

            v_imageView.setY((deviceHeight * 10)/100);
            v_imageView.setX((deviceHeight * 2) / 100);

            v_imageView.getLayoutParams().height = (deviceHeight * 120)/100;
            v_imageView.getLayoutParams().width  = (deviceHeight * 22)/100;

            v_txtView.setTextColor(context.getResources().getColor(R.color.colorPrimary));


            this.v_ctx     = c_ctx;
            this.v_modules = c_categories;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();

            int    module_id   = v_modules.get(position).getCsr_module_id();
            String module_name = v_modules.get(position).getCsr_module_name();



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

            Intent desc_intent = new Intent(context, CSRInitCategoryDescription.class);
            desc_intent.putExtra("module_id", String.valueOf(module_id));
            desc_intent.putExtra("module_name", module_name);
            desc_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.v_ctx.startActivity(desc_intent);
        }

    }
}


