package heartbeat.social.tcs.socialhb.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.activity.modules.sub_modules.DonatedItemStatusDescription;
import heartbeat.social.tcs.socialhb.bean.SingleDonateItem;

/**
 * Created by admin on 21/11/16.
 */
public class DonatedItemStatusAdapter extends RecyclerView.Adapter<DonatedItemStatusAdapter.ViewDonateItemStatusAdapter>{


    private ArrayList<SingleDonateItem> donated_items;
    private Context context;


    public DonatedItemStatusAdapter(ArrayList<SingleDonateItem> c_donated_Item, Context c_ctx){
        this.donated_items  = c_donated_Item;
        this.context        = c_ctx;
    }

    @Override
    public ViewDonateItemStatusAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.donated_item_status_item, parent, false);
        ViewDonateItemStatusAdapter avh = new ViewDonateItemStatusAdapter(v, context, donated_items);
        return avh;
    }

    @Override
    public void onBindViewHolder(ViewDonateItemStatusAdapter holder, int position)
    {
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .rect();
        TextDrawable txtDrawable = builder.build(String.valueOf(position+1), color1);

        holder.v_txt_donation_number.setImageDrawable(txtDrawable);
        holder.v_txt_donation_type.setText(donated_items.get(position).getDonateCategory().getCategory() + " (" +donated_items.get(position).getNoOfItems()+")");
        holder.v_txt_donation_status.setText(donated_items.get(position).getDonateItemStatus().getStatus());

        if(donated_items.get(position).getDonateItemStatus().getId() == 1){
            holder.v_image_view_status.setImageResource(R.drawable.pending);
        }else if(donated_items.get(position).getDonateItemStatus().getId() == 2){
            holder.v_image_view_status.setImageResource(R.drawable.pending);
        }else if(donated_items.get(position).getDonateItemStatus().getId() == 3){
            holder.v_image_view_status.setImageResource(R.drawable.approved);
        }else if(donated_items.get(position).getDonateItemStatus().getId() == 4){
            holder.v_image_view_status.setImageResource(R.drawable.pending);
        }else if(donated_items.get(position).getDonateItemStatus().getId() == 5){
            holder.v_image_view_status.setImageResource(R.drawable.rejected);
       }


    }

    @Override
    public int getItemCount()
    {
        return donated_items.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class ViewDonateItemStatusAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{


        public ImageView  v_txt_donation_number;
        public TextView   v_txt_donation_type;
        public TextView   v_txt_donation_status;
        public ImageView  v_image_view_status;
        public Context    v_ctx;
        public ArrayList<SingleDonateItem> v_donated_items = new ArrayList<SingleDonateItem>();

        public ViewDonateItemStatusAdapter(View itemView, Context c_ctx, ArrayList<SingleDonateItem> c_donated_items) {
            super(itemView);

            v_txt_donation_number  = (ImageView) itemView.findViewById(R.id.image_view_circular_number);
            v_txt_donation_type    = (TextView) itemView.findViewById(R.id.txt_circular_heading);
            v_txt_donation_status  = (TextView) itemView.findViewById(R.id.txt_circular_status);
            v_image_view_status    = (ImageView) itemView.findViewById(R.id.status_image_view);
            this.v_ctx             = c_ctx;
            this.v_donated_items   = c_donated_items;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            /*int position = getAdapterPosition();
            Intent intent1 = new Intent(this.v_ctx,  LeaveDescriptionActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("leave_application_number", leaves.get(position).getLeave_application_number());
            this.v_ctx.startActivity(intent1);*/

            int position = getAdapterPosition();
            Intent intent1 = new Intent(this.v_ctx, DonatedItemStatusDescription.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("donated_item_id", String.valueOf(donated_items.get(position).getId()));
            this.v_ctx.startActivity(intent1);
        }

    }
}