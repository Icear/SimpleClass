package indi.github.icear.simpleclass.deleteimported;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Explode;

import indi.github.icear.simpleclass.BaseViewModule;

/**
 * Created by icear on 2017/11/16.
 * DeleteImportedViewModule
 */

public class DeleteImportedViewModule implements BaseViewModule {
    private DeleteImportedFragment fragment;

    @Override
    public void init(Bundle bundle) {
        fragment = DeleteImportedFragment.newInstance(bundle);
        fragment.setEnterTransition(new Explode());
        fragment.setExitTransition(new Explode());
        fragment.setReturnTransition(new Explode());
        fragment.setEnterTransition(new Explode());
        new DeleteImportedPresenter(fragment);
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }
}
