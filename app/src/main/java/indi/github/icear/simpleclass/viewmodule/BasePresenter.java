package indi.github.icear.simpleclass.viewmodule;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by icear on 2017/10/6.
 * Presenter基础接口
 */

public interface BasePresenter {
    void onCreate(Context context, Bundle bundle);

    void onStart();
}
