package pers.wengzc.snailhunterrt.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pers.wengzc.snailhunterrt.R;
import pers.wengzc.snailhunterrt.Snail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author wengzc
 */
public class DisplayAdapter extends RecyclerView.Adapter<DisplayAdapter.DisplayViewHolder>{

    private List<Snail> mData;

    public DisplayAdapter(List<Snail> data){
        this.mData = data;
    }

    public void updateData (List<Snail> data){
        this.mData = new ArrayList<>();
        this.mData.addAll(data);
        notifyDataSetChanged();
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
            StringBuilder sb = new StringBuilder();
            String fontColor = snail.leafInvoke ? FONT_COLOT_LEAF_INVOKE : FONT_COLOT_NORMAL;
            sb.append(elementToHtmlString(fontColor, "进程号:"+snail.processId));
            sb.append(elementToHtmlString(fontColor,"线程号:"+snail.threadId));
            sb.append(elementToHtmlString(fontColor,"线程名:"+snail.threadName));
            sb.append(elementToHtmlString(fontColor,"包名:"+snail.packageName));
            sb.append(elementToHtmlString(fontColor,"类名:"+snail.className));
            sb.append(elementToHtmlString(fontColor,"方法名:"+snail.methodName));
            sb.append(elementToHtmlString(fontColor,"是否主线程:"+snail.isMainThread));
            sb.append(elementToHtmlString(fontColor,"时间限制(ms):"+snail.timeConstraint));
            sb.append(elementToHtmlString(fontColor,"实际运行时间(ms):"+(snail.executeTime/1000000f)));
            mTextView.setText(Html.fromHtml(sb.toString()));
        }

        public static final String SEPARATOR = "\r\n";

        private static final String FONT_COLOT_NORMAL = "#DDDDDD";
        private static final String FONT_COLOT_LEAF_INVOKE = "#FF0066";

        private String elementToHtmlString (String fontColor, String element){
            String htmlString = element.replaceAll(SEPARATOR, "<br/>");
            htmlString += "<br/>";
            htmlString = String.format("<font color='"+fontColor+"'>%s</font>", htmlString);
            htmlString += "<br/>";
            return htmlString;
        }
    }

}
