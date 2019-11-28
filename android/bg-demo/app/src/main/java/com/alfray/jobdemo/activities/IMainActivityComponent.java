package com.alfray.jobdemo.activities;

import dagger.Subcomponent;

@Subcomponent
public interface IMainActivityComponent {
    void inject(MainActivity mainActivity);

    @Subcomponent.Factory
    interface Factory {
        IMainActivityComponent create();
    }
}
