package com.alibaba.work.faas.model;

import com.kingdee.bos.webapi.entity.SaveParam;

public class SaveParamNotVerify<T> extends SaveParam<T> {
    Boolean IsVerifyBaseDataField;

    public SaveParamNotVerify(T data) {
        super(data);
        IsVerifyBaseDataField = false;
    }
}
