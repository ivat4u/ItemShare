package example.com.itemshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

        private List<Item> ItemList = new ArrayList<Item>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            initItems();  // 初始化水果数据
            ItemAdapter adapter = new ItemAdapter(MainActivity.this,
                    R.layout.item_item, ItemList);
            ListView listView = (ListView) findViewById(R.id.list_view);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?>parent, View view, int position, long id){
                    Item Item=ItemList.get(position);
                    Toast.makeText(MainActivity.this,Item.getName(),Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void initItems() {
            for (int i = 0; i < 2; i++) {
                Item apple = new Item("Apple", R.drawable.apple_pic,1.5,25,"Beijing");
                ItemList.add(apple);
                Item banana = new Item("Banana", R.drawable.banana_pic,0.8,8,"Shanghai");
                ItemList.add(banana);
                Item orange = new Item("Orange", R.drawable.orange_pic,2,39,"Chengdu");
                ItemList.add(orange);
                Item watermelon = new Item("Watermelon", R.drawable.watermelon_pic,33,28,"XINfn");
                ItemList.add(watermelon);
                Item pear = new Item("Pear", R.drawable.pear_pic,33,44,"Majdtou");
                ItemList.add(pear);
                Item grape = new Item("Grape", R.drawable.grape_pic,3,22,"Dsad1");
                ItemList.add(grape);
                Item pineapple = new Item("Pineapple", R.drawable.pineapple_pic,223,13,"Ameir");
                ItemList.add(pineapple);
                Item strawberry = new Item("Strawberry", R.drawable.strawberry_pic,235,222,"MHdse");
                ItemList.add(strawberry);
                Item cherry = new Item("Cherry", R.drawable.cherry_pic,198,2333,"Njdie");
                ItemList.add(cherry);
                Item mango = new Item("Mango", R.drawable.mango_pic,23.5,199,"Ijdu");
                ItemList.add(mango);
            }
        }
    }





