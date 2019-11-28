package com.alfray.bgdemo.activities;

import dagger.Subcomponent;

@Subcomponent
public interface IMainActivityComponent {
    void inject(MainActivity mainActivity);

    @Subcomponent.Factory
    interface Factory {
        IMainActivityComponent create();
    }
}
