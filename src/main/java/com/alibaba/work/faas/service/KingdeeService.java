package com.alibaba.work.faas.service;

import com.alibaba.fastjson.JSONObject;
import com.kingdee.bos.webapi.sdk.K3CloudApi;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface KingdeeService {
    JSONObject todoXSHT(Map<String, ?> formData, String modelId);

    JSONObject todoXSDD(Map<String, ?> formData, String modelId);

    JSONObject todoPZ(Map<String, ?> formData, String modelId);

    JSONObject todoXM(Map<String, ?> formData, String modelId);

    JSONObject todoCGTH(Map<String, ?> formData, String modelId);

    JSONObject todoXSTH(Map<String, ?> formData, String modelId);

    JSONObject todoTH(Map<String,?> formData, String modelId);

    JSONObject executeRegexList(List<JSONObject> regexList, Map<String, ?> yidaFormData, String yidaModelId);
}
