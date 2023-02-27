package com.commit451.jounce.sample;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Adapter for the recyclerview, which holds cheeses
 */
public class CheeseAdapter extends RecyclerView.Adapter<CheeseViewHolder> {

    private Listener mListener;
    private ArrayList<Cheese> mValues;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Cheese cheese = (Cheese) v.getTag();
            //Communicate to the activity that the item was long pressed
            mListener.onItemClicked(cheese);
        }
    };

    public CheeseAdapter(Listener listener) {
        mListener = listener;
        mValues = new ArrayList<>();
    }

    public void setData(Collection<Cheese> cheeses) {
        mValues.addAll(cheeses);
        notifyDataSetChanged();
    }

    @Override
    public CheeseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CheeseViewHolder holder = CheeseViewHolder.newInstance(parent);
        holder.itemView.setOnClickListener(mOnClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CheeseViewHolder holder, int position) {
        Cheese cheese = getItemAt(position);
        holder.bind(cheese);
        holder.itemView.setTag(cheese);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private Cheese getItemAt(int position) {
        return mValues.get(position);
    }

    public interface Listener {
        void onItemClicked(Cheese cheese);
    }
}
