package com.example.proveedoresregistro_da;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterElementos extends RecyclerView.Adapter<RecyclerViewAdapterElementos.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;
    private RecyclerViewAdapterElementos.onClickListenerRecycleItem monClickListenerRecycleItem;
    private int opcion;

    public RecyclerViewAdapterElementos(List<ListItem> listItems, Context context, RecyclerViewAdapterElementos.onClickListenerRecycleItem monClickListenerRecycleItem, int opcion) {
        this.listItems = listItems;
        this.context = context;
        this.opcion = opcion;

        this.monClickListenerRecycleItem = monClickListenerRecycleItem;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterElementos.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.elemento_registrado, viewGroup,false);

        return new RecyclerViewAdapterElementos.ViewHolder(v, monClickListenerRecycleItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterElementos.ViewHolder viewHolder, int i) {
        ListItem listItem = listItems.get(i);

        switch (opcion)
        {
            case 0:
                viewHolder.dato1.setText(listItem.getDatos().get(0));
                break;
            case 1:
                viewHolder.dato1.setText(listItem.getDatos().get(0));
                viewHolder.dato2.setText(listItem.getDatos().get(1));
                viewHolder.dato3.setText(listItem.getDatos().get(2));
                break;
            case 2:
                viewHolder.dato1.setText(listItem.getDatos().get(0));
                viewHolder.dato2.setText(listItem.getDatos().get(1));
                viewHolder.dato3.setText(listItem.getDatos().get(2));
                break;
            case 3:
                viewHolder.dato1.setText(listItem.getDatos().get(0));
                viewHolder.dato2.setText(listItem.getDatos().get(1));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView dato1;
        public TextView dato2;
        public TextView dato3;

        RecyclerViewAdapterElementos.onClickListenerRecycleItem onClickListenerRecycleItemi;

        public ViewHolder(@NonNull View itemView, RecyclerViewAdapterElementos.onClickListenerRecycleItem onClickListenerRecycleItemi) {
            super(itemView);

            dato1 = itemView.findViewById(R.id.dato1);
            dato2 = itemView.findViewById(R.id.dato2);
            dato3 = itemView.findViewById(R.id.dato3);

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
