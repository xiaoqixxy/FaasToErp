package com.alibaba.work.faas.service;

import com.aliyun.dingtalkyida_1_0.models.GetFormDataByIDResponseBody;
import org.springframework.stereotype.Service;

@Service
public interface YidaService {
    GetFormDataByIDResponseBody getDataById(String id) throws Exception;

    Integer updateData(String data, String formUuid) throws Exception;
}
