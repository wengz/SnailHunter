package pers.wengzc.snailhunterrt.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pers.wengzc.snailhunterrt.R;
import pers.wengzc.snailhunterrt.Snail;

import java.util.Collections;
import java.util.List;

/**
 * @author wengzc
 */
public class DisplayAdapter extends RecyclerView.Adapter<DisplayAdapter.DisplayViewHolder>{

    private List<Snail> mData;

    public DisplayAdapter(List<Snail> data){
        this.mData = data;
        ensureDataNotNull();
    }

    public void updateData (List<Snail> data){
        this.mData = data;
        ensureDataNotNull();
        notifyDataSetChanged();
    }

    private void ensureDataNotNull (){
        if (mData == null){
            mData = Collections.emptyList();
        }
    }

    @NonNull
    @Override
    public DisplayAdapter.DisplayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.display_snail_list_row, viewGroup, false);
        return new DisplayViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayViewHolder displayViewHolder, int i) {
        Snail data = mData.get(i);
        displayViewHolder.updateForData(data);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class DisplayViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public DisplayViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.snail_row_text);
        }

        public void updateForData (Snail snail){
            mTextView.setText(snail.toString());
        }
    }

}
