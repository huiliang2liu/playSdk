package com.lhl.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

class DataBinding {
    private final ViewDataBinding binding;

    public DataBinding(@LayoutRes int layout, Context context) {
        this(layout, null, LayoutInflater.from(context));
    }

    public DataBinding(@LayoutRes int layout, @NonNull ViewGroup group) {
        this(layout, group, LayoutInflater.from(group.getContext()));
    }

    public DataBinding(@LayoutRes int layout, @NonNull ViewGroup group, @NonNull LayoutInflater inflater) {
        binding = DataBindingUtil.inflate(inflater, layout, group, false);
    }

    public View getRoot() {
        return binding.getRoot();
    }

    public DataBinding bindModel(int id, Object model) {
        binding.setVariable(id, model);
        return this;
    }

    public ViewDataBinding getBinding() {
        return binding;
    }
}
