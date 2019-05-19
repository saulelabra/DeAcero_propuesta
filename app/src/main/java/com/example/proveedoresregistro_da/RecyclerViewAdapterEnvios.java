package com.example.proveedoresregistro_da;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapterEnvios extends RecyclerView.Adapter<RecyclerViewAdapterEnvios.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;
    private onClickListenerRecycleItem monClickListenerRecycleItem;

    public RecyclerViewAdapterEnvios(List<ListItem> listItems, Context context, onClickListenerRecycleItem monClickListenerRecycleItem) {
        this.listItems = listItems;
        this.context = context;

        this.monClickListenerRecycleItem = monClickListenerRecycleItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.envios_elemento, viewGroup,false);

        return new ViewHolder(v, monClickListenerRecycleItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ListItem listItem = listItems.get(i);



        viewHolder.tv_precio.setText(listItem.getDatos().get(0));
        viewHolder.tv_id.setText(listItem.getDatos().get(1));
        viewHolder.tv_urlFoto.setText(listItem.getDatos().get(2));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tv_precio;
        public TextView tv_id;
        public TextView tv_urlFoto;

        onClickListenerRecycleItem onClickListenerRecycleItemi;

        public ViewHolder(@NonNull View itemView, onClickListenerRecycleItem onClickListenerRecycleItemi) {
            super(itemView);

            tv_precio = itemView.findViewById(R.id.precio);
            tv_id = itemView.findViewById(R.id.id);
            tv_urlFoto = itemView.findViewById(R.id.urlFoto);

            this.onClickListenerRecycleItemi = onClickListenerRecycleItemi;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListenerRecycleItemi.onClickRecycle(getAdapterPosition());
        }
    }

    public interface onClickListenerRecycleItem{
        void onClickRecycle (int position);
    }
}
