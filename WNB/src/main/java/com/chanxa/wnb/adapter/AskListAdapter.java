package com.chanxa.wnb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chanxa.wnb.R;
import com.chanxa.wnb.bean.onLineShop.Ask;
import com.chanxa.wnb.bean.onLineShop.AskOptions;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CHANXA on 2015/1/21.
 */
public class AskListAdapter extends BaseAdapter {
    private Context context;
    private boolean isShowCorrectIcon = false;
    private boolean isAllYes = false;
    ArrayList<Ask> askArrayList = null;
    ArrayList<Integer> selectArrayList = new ArrayList<>();

    public AskListAdapter(Context context, ArrayList<Ask> askArrayList) {
        this.context = context;
        this.askArrayList = askArrayList;
    }

    @Override
    public int getCount() {
        for (int i = 0; i < askArrayList.size(); i++) {
            selectArrayList.add(-1);
        }
        return askArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_ask, null, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Ask ask = askArrayList.get(position);
        viewHolder.mTvTopicTitle.setText("问题 " + (position + 1));
        viewHolder.mTvTopic.setText(ask.getName());
        if (isShowCorrectIcon) {
            viewHolder.mImgAskCorrect.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mImgAskCorrect.setVisibility(View.INVISIBLE);
        }
        ArrayList<AskOptions> optionses = ask.getAskOptionsArrayList();
        if (optionses != null) {
            for (int i = 0; i < optionses.size(); i++) {
                final AskOptions askOptions = optionses.get(i);
                View view = LayoutInflater.from(context).inflate(R.layout.item_answer, null, false);
                /*if (view.getTag() == null) {
                    view.setTag(false);
                }*/
                Button button = (Button) view.findViewById(R.id.btn_answer);
                TextView answerDetails = (TextView) view.findViewById(R.id.tv_answer_details);
                button.setText(askOptions.getName());
                answerDetails.setText(askOptions.getContent());
                final ViewHolder finalViewHolder = viewHolder;
                final int index = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isShowCorrectIcon = false;
                        for (int i = 0; i < finalViewHolder.mContainerAnswer.getChildCount(); i++) {
                            View childView = finalViewHolder.mContainerAnswer.getChildAt(i);
                            childView.setSelected(false);
                        }
                        selectArrayList.set(position, index);
                        v.setSelected(true);
                        /*if (ask.getAnswer().equals(askOptions.getName())) {
                            finalViewHolder.mImgAskCorrect.setImageResource(R.drawable.ask_yes);
                            //isAllYes = true;
                        } else {
                            finalViewHolder.mImgAskCorrect.setImageResource(R.drawable.ask_no);
                            isAllYes = false;
                        }*/
                        if (isShowCorrectIcon) {
                            finalViewHolder.mImgAskCorrect.setVisibility(View.VISIBLE);
                        } else {
                            finalViewHolder.mImgAskCorrect.setVisibility(View.INVISIBLE);
                        }
                    }
                });
              /*  if (ask.getAnswer().equals(askOptions.getName())) {
                    finalViewHolder.mImgAskCorrect.setImageResource(R.drawable.ask_yes);
                    //isAllYes = true;
                } else {
                    finalViewHolder.mImgAskCorrect.setImageResource(R.drawable.ask_no);
                    isAllYes = false;
                }*/
                viewHolder.mContainerAnswer.addView(view);
            }
        }

        if (selectArrayList.get(position) != -1) {
            for (int i = 0; i < viewHolder.mContainerAnswer.getChildCount(); i++) {
                View childView = viewHolder.mContainerAnswer.getChildAt(i);
                childView.setSelected(selectArrayList.get(position) == i);
            }
            AskOptions askOptions = optionses.get(selectArrayList.get(position));
            if (ask.getAnswer().equals(askOptions.getName())) {
                viewHolder.mImgAskCorrect.setImageResource(R.drawable.ask_yes);
                //isAllYes = true;
            } else {
                viewHolder.mImgAskCorrect.setImageResource(R.drawable.ask_no);
                isAllYes = false;
            }
        }
        convertView.setTag(viewHolder);
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        isAllYes = true;
        super.notifyDataSetChanged();
    }

    public boolean isAllYes() {
        boolean isCheck = false;
        for (Integer integer : selectArrayList) {
            if (integer != -1) {
                isCheck = true;
            }
        }
        return isCheck && isAllYes;
    }

    public void setAllYes(boolean isAllYes) {
        this.isAllYes = isAllYes;
    }

    public boolean isShowCorrectIcon() {
        return isShowCorrectIcon;
    }

    public void setShowCorrectIcon(boolean isShowCorrectIcon) {
        this.isShowCorrectIcon = isShowCorrectIcon;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_list_ask.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Inmite Developers (http://inmite.github.io)
     */
    static class ViewHolder {
        @InjectView(R.id.tv_topic_title)
        TextView mTvTopicTitle;
        @InjectView(R.id.img_ask_correct)
        ImageView mImgAskCorrect;
        @InjectView(R.id.tv_topic)
        TextView mTvTopic;
        @InjectView(R.id.container_answer)
        LinearLayout mContainerAnswer;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
