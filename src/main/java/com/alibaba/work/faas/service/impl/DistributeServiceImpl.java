package com.alibaba.work.faas.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.work.faas.constant.KingdeeConstant;
import com.alibaba.work.faas.service.DistributeService;
import com.alibaba.work.faas.service.KingdeeService;
import com.alibaba.work.faas.service.YidaService;
import com.aliyun.dingtalkyida_1_0.models.GetFormDataByIDResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DistributeServiceImpl implements DistributeService {

    private static final Logger log = LoggerFactory.getLogger(DistributeServiceImpl.class);

    @Autowired
    private YidaService yidaService;

    @Autowired
    private KingdeeService kingdeeService;

    @Override
    public JSONObject distribute(String modelId, String formUuid) throws Exception {
        log.info("宜搭表单实例ID: {}", modelId);
        log.info("宜搭表单FormUuid: {}", formUuid);

        GetFormDataByIDResponseBody yidaData = yidaService.getDataById(modelId);
        Map<String, ?> formData = yidaData.getFormData();

        JSONObject result = new JSONObject();

        List<JSONObject> regexList = KingdeeConstant.KINGDEE_REGEX.get(formUuid);
        if (regexList != null && !regexList.isEmpty()) {
            JSONObject jsonObject = kingdeeService.executeRegexList(regexList, formData,modelId);

            return jsonObject;
        }


        return null;
    }
}
