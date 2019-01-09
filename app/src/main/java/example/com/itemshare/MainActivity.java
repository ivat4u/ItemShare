package example.com.itemshare;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class MainActivity extends AppCompatActivity implements WbShareCallback {
    private Tencent mTencent;
    final String TAG = "Chunna=ShareActivity";
    ImageView ivQRCode;
    WbShareHandler shareHandler;
    private MyIUiListener mIUiListener;
    String mAppid="1108060298";
    Bitmap bitmap;
    public static Bitmap getBitmap(String path) throws IOException {

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200){
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }
        private List<Item> ItemList = new ArrayList<Item>();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            initItems();  // 初始化水果数据


            if (mTencent == null ) {
                mTencent = Tencent.createInstance(mAppid, this);
            }

            mTencent = Tencent.createInstance("1108060298",getApplicationContext());
            ItemAdapter adapter = new ItemAdapter(MainActivity.this,
                    R.layout.item_item, ItemList);

            ListView listView = (ListView) findViewById(R.id.list_view);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?>parent, View view, int position, long id){
                    Item Item=ItemList.get(position);
                    Intent intent=new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri CONTENT_URI_BROWSERS = Uri.parse("http://123.207.247.90/sharegoods/inform.html");
                    intent.setData(CONTENT_URI_BROWSERS);
                    intent.setComponent(new ComponentName("com.android.browser", "com.android.browser.BrowserActivity"));
                    startActivity(intent);
                    Toast.makeText(MainActivity.this,Item.getName(),Toast.LENGTH_SHORT).show();
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
                @Override
                public boolean onItemLongClick(AdapterView<?>parent, View view, int position, long id){
                    Item Item=ItemList.get(position);
                    Bitmap img=Item.getImageId();
                    Dialog      dialog = new Dialog(MainActivity.this);
                    dialog.setTitle("分享：");
                    dialog.setContentView(R.layout.dialog);
                    ImageView   vie_weibo=(ImageView)dialog.findViewById(R.id.weibo);
                    ImageView   vie_weixin=(ImageView)dialog.findViewById(R.id.weixin);
                    ImageView   vie_qq=(ImageView)dialog.findViewById(R.id.qq);

                            vie_weibo.setOnClickListener(new View.OnClickListener() {
                                private void initWeiBo() {
                                    WbSdk.install(MainActivity.this,new AuthInfo(MainActivity.this, Constants.APP_KEY_WEIBO,Constants.REDIRECT_URL, Constants.SCOPE));//创建微博API接口类对象
                                    shareHandler = new WbShareHandler(MainActivity.this);
                                    shareHandler.registerApp();
                                }

                                /**
                                 * 用户点击分享按钮，唤起微博客户端进行分享。
                                 */
                                protected void onNewIntent(Intent intent) {
                                    MainActivity.super.onNewIntent(intent);
                                    shareHandler.doResultIntent(intent,MainActivity.this);
                                }

                                /**
                                 * 第三方应用发送请求消息到微博，唤起微博分享界面。
                                 */
                                private void sendMessageToWb(boolean hasText, boolean hasImage) {
                                    sendMultiMessage(hasText, hasImage);
                                }

                                /**
                                 * 第三方应用发送请求消息到微博，唤起微博分享界面。
                                 */
                                private void sendMultiMessage(boolean hasText, boolean hasImage) {

                                    WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                                    if (hasText) {
                                        weiboMessage.textObject = getTextObj();
                                    }
                                    if (hasImage) {
                                        weiboMessage.imageObject = getImageObj(MainActivity.this);
                                    }
                                    shareHandler.shareMessage(weiboMessage, false);
                                }

                                /**
                                 * 创建文本消息对象。
                                 * @return 文本消息对象。
                                 */
                                private TextObject getTextObj() {
                                    TextObject textObject = new TextObject();
                                    textObject.text = "我在智能分享平台看见一件不错的商品，推荐给大家\t";
                                    textObject.title = "购物推荐";
                                    textObject.actionUrl = "http://www.baidu.com";
                                    return textObject;
                                }

                                /**
                                 * 创建图片消息对象。
                                 * @return 图片消息对象。
                                 */
                                private ImageObject getImageObj(Context context) {
                                    ImageObject imageObject = new ImageObject();
                                    Bitmap  bitmap = img;
                                    imageObject.setImageObject(bitmap);
                                    return imageObject;
                                }
                                @Override
                                public void onClick(View v) {
                                        initWeiBo();
                                        sendMessageToWb(true,true);
                                    }



                            });
                    vie_weixin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String APP_ID=Constants.APP_ID;
                            IWXAPI api = WXAPIFactory.createWXAPI(MainActivity.this, APP_ID, false);
                            api.registerApp(APP_ID);
                                WXWebpageObject webpage = new WXWebpageObject();
                            webpage.webpageUrl = "http://www.xxxx.com/wap/showShare/";//收到分享的好友点击会跳转到这个地址里面去
                            WXMediaMessage msg = new WXMediaMessage(webpage);
                            msg.title = "我在智能分享平台看见一件不错的商品，推荐给大家\t";
                            msg.description = "我在智能分享平台看见一件不错的商品，推荐给大家\t";
                            try
                            {
                                Bitmap thumbBmp = Bitmap.createScaledBitmap(img, 150, 150, true);
                                img.recycle();
                                msg.setThumbImage(thumbBmp);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            SendMessageToWX.Req req = new SendMessageToWX.Req();
                            req.transaction = String.valueOf(System.currentTimeMillis());
                            req.message = msg;
                            req.scene = SendMessageToWX.Req.WXSceneTimeline;
                            api.sendReq(req);
                        }
                    });
                    vie_qq.setOnClickListener(new View.OnClickListener() {
                        public  Uri bitmap2uri(Context c, Bitmap b) {
                            File path = new File(c.getCacheDir() + File.separator + System.currentTimeMillis() + ".jpg");
                            try {
                                OutputStream os = new FileOutputStream(path);
                                b.compress(Bitmap.CompressFormat.JPEG, 100, os);
                                os.close();
                                return Uri.fromFile(path);
                            } catch (Exception ignored) {
                            }
                            return null;
                        }
                        @Override
                        public void onClick(View v) {
                            final Bundle params = new Bundle();
                            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                            params.putString(QQShare.SHARE_TO_QQ_TITLE, "购物分享");
                            params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  "我在智能分享平台看见一件不错的商品，推荐给大家\t");
                            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  "http://www.qq.com/news/1.html");
                            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, String.valueOf(bitmap2uri(MainActivity.this,img)));
                            params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "智能购物分享");
                            mTencent.shareToQQ(MainActivity.this, params, new MyIUiListener());
                        }
                    });
                    ;

                    dialog.show();

                    return  true;
                    }

            });
            FrameLayout inludelayout=(FrameLayout)findViewById(R.id.menu_new);
            ImageButton new_item=(ImageButton)inludelayout.findViewById(R.id.fab);
            new_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setIcon(R.drawable.icon_29);
                    builder.setTitle("上传一个商品");
                    View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_xinjian, null);
                    builder.setView(view);
                    final EditText name_good = (EditText)view.findViewById(R.id.name_good);
                    final EditText price = (EditText)view.findViewById(R.id.price);
                    final EditText imagegood= (EditText)view.findViewById(R.id.image_good);
                    final EditText class_good = (EditText)view.findViewById(R.id.class_good);

                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            String a = name_good.getText().toString().trim();
                            String b = price.getText().toString().trim();
                            String url = imagegood.getText().toString().trim();
                            String d= class_good.getText().toString().trim();
                            Random rand=new Random();
                            Bitmap bp= null;
                            try {
                                bp = getBitmap(url);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            adapter.add(new Item(a,rand.nextInt(100),b,5,"0",bp,1,d));
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
{
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });builder.show();

                }
            });
        }



        private void initItems() {
            for (int i = 0; i < 2; i++) {
                Item apple = new Item("life", 1,"100",6,"17", BitmapFactory.decodeResource (getResources(),R.drawable.ic_good1),2,"生活");
                ItemList.add(apple);
                Item banana = new Item("sport", 2,"25",7,"777", BitmapFactory.decodeResource (getResources(),R.drawable.ic_good2),2,"运动");
                ItemList.add(banana);
                Item orange = new Item("dight", 3,"99",9,"555", BitmapFactory.decodeResource (getResources(),R.drawable.ic_good3),101,"摄影");
                ItemList.add(orange);
                Item watermelon= new Item("test", 4,"123",4,"50", BitmapFactory.decodeResource (getResources(),R.drawable.ic_good4),101,"运动");
                ItemList.add(watermelon);
                Item pear = new Item("game", 5,"469",4,"50", BitmapFactory.decodeResource (getResources(),R.drawable.ic_good5),101,"运动");
                ItemList.add(pear);
                Item grape= new Item("life2", 11,"101",4,"50",BitmapFactory.decodeResource (getResources(),R.drawable.ic_good6),101,"娱乐");
                ItemList.add(grape);
                Item pineapple= new Item("soprt", 22,"666",4,"50", BitmapFactory.decodeResource (getResources(),R.drawable.ic_good7),101,"游戏");
                ItemList.add(pineapple);
                Item strawberry = new Item("game", 55,"444",4,"50", BitmapFactory.decodeResource (getResources(),R.drawable.ic_good8),101,"游戏");
                ItemList.add(strawberry);
                Item cherry= new Item("tatat", 66,"11",4,"111", BitmapFactory.decodeResource (getResources(),R.drawable.ic_good5),101,"游戏");
                ItemList.add(cherry);
                Item mango = new Item("dada", 111,"1",4,"50", BitmapFactory.decodeResource (getResources(),R.drawable.ic_good9),101,"生活");
                ItemList.add(mango);
            }
        }
    public void onWbShareSuccess() {
        Log.d(TAG,"分享成功");
    }


    public void onWbShareCancel() {
        Log.d(TAG,"分享取消");
    }


    public void onWbShareFail() {
        Log.d(TAG,"分享失败");
    }



}



