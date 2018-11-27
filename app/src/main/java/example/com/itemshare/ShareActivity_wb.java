package example.com.itemshare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;

public class ShareActivity_wb extends AppCompatActivity implements WbShareCallback {
    public static final String TAG = "Chunna=ShareActivity_wb";
    private ImageView ivQRCode;

    WbShareHandler shareHandler;





     void initView() {
        ivQRCode = (ImageView)findViewById(R.id.iv_share_QRcode);

    }

    void initWeiBo() {
        WbSdk.install(this,new AuthInfo(this, Constants.APP_KEY_WEIBO,Constants.REDIRECT_URL, Constants.SCOPE));//创建微博API接口类对象
        shareHandler = new WbShareHandler(this);
        shareHandler.registerApp();
    }



    @Override
    public void onWbShareSuccess() {
        Log.d(TAG,"分享成功");
    }

    @Override
    public void onWbShareCancel() {
        Log.d(TAG,"分享取消");
    }

    @Override
    public void onWbShareFail() {
        Log.d(TAG,"分享失败");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shareHandler.doResultIntent(intent,this);
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
     void sendMultiMessage(boolean hasText, boolean hasImage) {

        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (hasText) {
            weiboMessage.textObject = getTextObj();
        }
        if (hasImage) {
            weiboMessage.imageObject = getImageObj(this);
        }
        shareHandler.shareMessage(weiboMessage, false);
    }

    /**
     * 创建文本消息对象。
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.actionUrl = "http://www.baidu.com";
        textObject.text = "我在智能分享平台看见一件不错的商品，推荐给大家\t"+textObject.actionUrl;
        textObject.title = "xxxx";
        return textObject;
    }

    /**
     * 创建图片消息对象。
     * @return 图片消息对象。
     */
    private ImageObject getImageObj(Context context) {
        ImageObject imageObject = new ImageObject();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_share_qrcode);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

}
