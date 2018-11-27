package example.com.itemshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hasee on 2017/8/15.
 */

public class ItemAdapter extends ArrayAdapter<Item>{
    private int resourceID;
    public ItemAdapter(Context context, int textViewResourceId, List<Item>objects){
        super(context,textViewResourceId,objects);
        resourceID=textViewResourceId;
    }
@Override
public View getView(int position, View convertView, ViewGroup parent){
Item item=getItem(position);
    View view;
    ViewHolder viewHolder;
    if(convertView==null){
 view= LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
    viewHolder=new ViewHolder();
        viewHolder.ItemImage=(ImageView)view.findViewById(R.id.item_image);
        viewHolder.ItemName=(TextView)view.findViewById(R.id.item_name);
        viewHolder.ItemPrice=(TextView)view.findViewById(R.id.item_price);
        viewHolder.ItemScore=(TextView)view.findViewById(R.id.item_score);
        viewHolder.ItemNumber=(TextView)view.findViewById(R.id.item_number);



    view.setTag(viewHolder);
    }
    else{ view=convertView;
    viewHolder=(ViewHolder)view.getTag();}
    viewHolder.ItemImage.setImageResource(Integer.parseInt(item.getImageId()));
    viewHolder.ItemName.setText("商品名称:"+item.getName());
    viewHolder.ItemPrice.setText("价格: ￥"+String.valueOf(item.getPrice()));
    viewHolder.ItemScore.setText("平均得分:"+String.valueOf(item.getScore()));
    viewHolder.ItemNumber.setText("点赞数: "+String.valueOf(item.getNumber()));
    return view;}

    class ViewHolder
    {
    ImageView ItemImage;
    TextView ItemName;
    TextView ItemPrice;
    TextView ItemScore;
    TextView ItemNumber;
    }

}


