package heartbeat.social.tcs.socialhb.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import heartbeat.social.tcs.socialhb.bean.Module;
import heartbeat.social.tcs.socialhb.utility.ModuleSelector;

/**
 * Created by admin on 22/07/16.
 */
public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ViewModuleAdapter>
{

    private ArrayList<Module> modules;
    private Context context;


    public ModuleAdapter(ArrayList<Module> c_modules, Context c_ctx){
        this.modules = c_modules;
        this.context = c_ctx;
    }

    @Override
    public ViewModuleAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.module_single_item, parent, false);
        ViewModuleAdapter avh = new ViewModuleAdapter(v, context, modules);
        return avh;
    }

    @Override
    public void onBindViewHolder(ViewModuleAdapter holder, int position) {
         holder.v_txtView.setText(modules.get(position).getModule_name());
        //holder.v_imageView.setImageResource(R.drawable.transport);
       /*      String uri = "@drawable/transport";
             String uri = "@drawable/"+modules.get(position).getImage().toString().toLowerCase();
             int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
             Log.e("Image Name : ", uri);
             Drawable res = context.getResources().getDrawable(imageResource);
             holder.v_imageView.setImageDrawable(res);


         */

        Log.e("Module Image Path : ", modules.get(position).getModule_icon());
        //Picasso.with(context).load(Web_API_Config.root_image_url + modules.get(position).getImage()).error(R.drawable.image_loading_error).into(holder.v_imageView);

        //Picasso.with(context).load("http://"+modules.get(position).getImage()).into(holder.v_imageView);
        //holder.v_imageView.setImageResource(modules.get(position).getImageId());
        Picasso.with(context).load(modules.get(position).getModule_icon_id()).into(holder.v_imageView);


    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class ViewModuleAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{


        //  public TextView v_txtView;
        public ImageView v_imageView;
        public TextView  v_txtView;
        public CardView  v_cardView;
        public Context   v_ctx;
        public ArrayList<Module> v_modules = new ArrayList<Module>();

        public ViewModuleAdapter(View itemView, Context c_ctx, ArrayList<Module> c_modules) {
            super(itemView);
            v_txtView   = (TextView) itemView.findViewById(R.id.moduleTxtView);
            v_imageView = (ImageView) itemView.findViewById(R.id.moduleImageView);
            v_cardView  = (CardView) itemView.findViewById(R.id.moduleCard);

            //Getting Screen Size
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
            int deviceWidth = displayMetrics.widthPixels;
            int deviceHeight = displayMetrics.heightPixels;

            //setting cardsize
            v_cardView.getLayoutParams().height = (deviceHeight * 43)/100;

            v_imageView.setY((deviceHeight * 10)/100);
            v_imageView.setX((deviceHeight * 3) / 100);

            v_imageView.getLayoutParams().height = (deviceHeight * 120)/100;
            v_imageView.getLayoutParams().width  = (deviceHeight * 20)/100;

            v_txtView.setTextColor(context.getResources().getColor(R.color.colorPrimary));


            this.v_ctx     = c_ctx;
            this.v_modules = c_modules;

            itemView.setOnClickListener(this);
            //v_cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();

            int    module_id   = v_modules.get(position).getId();
            String module_name = v_modules.get(position).getModule_name();


            ModuleSelector moduleSelector =  new ModuleSelector();
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
            }

        }

    }
}


