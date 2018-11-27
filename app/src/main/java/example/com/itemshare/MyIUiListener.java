package example.com.itemshare;


import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * Created by hasee on 2018/12/20.
 */

public class MyIUiListener implements IUiListener {
    @Override
public void onComplete(Object o) {
// 操作成功
}
@Override
public void onError(UiError uiError) {
// 分享异常
}
@Override
public void onCancel() {
// 取消分享
}

}
