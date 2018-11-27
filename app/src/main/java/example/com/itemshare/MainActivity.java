package example.com.itemshare;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements WbShareCallback {
    private Tencent mTencent;
    final String TAG = "Chunna=ShareActivity";
    ImageView ivQRCode;
    WbShareHandler shareHandler;
    private MyIUiListener mIUiListener;
    String mAppid="1108060298";

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
                    Toast.makeText(MainActivity.this,Item.getName(),Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.this,ShareActivity_wb.class);
                    startActivity(intent);
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
                @Override
                public boolean onItemLongClick(AdapterView<?>parent, View view, int position, long id){
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
                                    Bitmap  bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_share_qrcode);
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
                            IWXAPI api = WXAPIFactory.createWXAPI(MyApplication.getContext(), APP_ID, false);
                            api.registerApp(APP_ID);
                            WXWebpageObject webpage = new WXWebpageObject();
                            webpage.webpageUrl = "http://www.xxxx.com/wap/showShare/";//收到分享的好友点击会跳转到这个地址里面去
                            WXMediaMessage msg = new WXMediaMessage(webpage);
                            msg.title = "我在智能分享平台看见一件不错的商品，推荐给大家\t";
                            msg.description = "我在智能分享平台看见一件不错的商品，推荐给大家\t";
                            try
                            {
                                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.sen_img);
                                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
                                bmp.recycle();
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
                        @Override
                        public void onClick(View v) {
                            final Bundle params = new Bundle();
                            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                            params.putString(QQShare.SHARE_TO_QQ_TITLE, "购物分享");
                            params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  "我在智能分享平台看见一件不错的商品，推荐给大家\t");
                            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  "http://www.qq.com/news/1.html");
                            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
                            params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "智能购物分享");
                            mTencent.shareToQQ(MainActivity.this, params, new MyIUiListener());
                        }
                    });
                    ;

                    dialog.show();

                    return  true;
                    }

            });
        }



        private void initItems() {
            for (int i = 0; i < 2; i++) {
                Item apple = new Item("Apple", 12,"25",4,"50", R.drawable.apple_pic+"",101,"Food");
                ItemList.add(apple);
                Item banana = new Item("Apple", 12,"25",4,"50", R.drawable.banana_pic+"",101,"Food");
                ItemList.add(banana);
                Item orange = new Item("Apple", 12,"25",4,"50", R.drawable.orange_pic+"",101,"Food");
                ItemList.add(orange);
                Item watermelon= new Item("Apple", 12,"25",4,"50", R.drawable.watermelon_pic+"",101,"Food");
                ItemList.add(watermelon);
                Item pear = new Item("Apple", 12,"25",4,"50", R.drawable.pear_pic+"",101,"Food");
                ItemList.add(pear);
                Item grape= new Item("Apple", 12,"25",4,"50", R.drawable.grape_pic+"",101,"Food");
                ItemList.add(grape);
                Item pineapple= new Item("Apple", 12,"25",4,"50", R.drawable.pineapple_pic+"",101,"Food");
                ItemList.add(pineapple);
                Item strawberry = new Item("Apple", 12,"25",4,"50", R.drawable.strawberry_pic+"",101,"Food");
                ItemList.add(strawberry);
                Item cherry= new Item("Apple", 12,"25",4,"50", R.drawable.cherry_pic+"",101,"Food");
                ItemList.add(cherry);
                Item mango = new Item("Apple", 12,"25",4,"50", R.drawable.mango_pic+"",101,"Food");
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



