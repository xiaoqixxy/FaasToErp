package com.alibaba.work.faas.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.work.faas.constant.KingdeeConstant;
import com.aliyun.dingtalkyida_1_0.models.GetFormDataByIDResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface DistributeService {

    JSONObject distribute(String modelId, String formUuid) throws Exception;
}
