package com.lhl.ui;

import android.view.View;

public interface IUi {
    int layout();

    IUi bindModel(int id, Object model);

    View getRoot();
}
