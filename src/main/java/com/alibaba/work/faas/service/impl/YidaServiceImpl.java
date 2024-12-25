package com.alibaba.work.faas.service.impl;

import com.alibaba.work.faas.config.DingConfig;
import com.alibaba.work.faas.service.YidaService;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponseBody;
import com.aliyun.dingtalkyida_1_0.models.*;
import com.aliyun.dingtalkyida_1_0.Client;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;
import com.alibaba.work.faas.util.DingOpenApiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YidaServiceImpl implements YidaService {
    private static final Logger log = LoggerFactory.getLogger(YidaServiceImpl.class);

    @Autowired
    private DingConfig dingConfig;

    @Override
    public GetFormDataByIDResponseBody getDataById(String id) throws Exception {
        Client client = DingOpenApiUtil.createClient();
        GetFormDataByIDHeaders getFormDataByIDHeaders = new GetFormDataByIDHeaders();
        getFormDataByIDHeaders.xAcsDingtalkAccessToken = getToken();
        GetFormDataByIDRequest getFormDataByIDRequest = new GetFormDataByIDRequest()
                .setAppType(dingConfig.getYida().getAppType())
                .setSystemToken(dingConfig.getYida().getSystemToken())
                .setUserId(dingConfig.getYida().getUserId())
                .setLanguage(dingConfig.getYida().getLanguage());
        try {
            GetFormDataByIDResponse response = client.getFormDataByIDWithOptions(id, getFormDataByIDRequest, getFormDataByIDHeaders, new RuntimeOptions());
            log.info("查询宜搭数据 => {}", response.getBody().getFormData());
            return response.getBody();
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
            throw err;

        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
            throw err;
        }
    }

    @Override
    public Integer updateData(String data, String instanceId) throws Exception {
        Client client = DingOpenApiUtil.createClient();
        UpdateFormDataHeaders updateFormDataHeaders = new UpdateFormDataHeaders();
        updateFormDataHeaders.xAcsDingtalkAccessToken = getToken();
        UpdateFormDataRequest updateFormDataRequest = new UpdateFormDataRequest()
                .setAppType(dingConfig.getYida().getAppType())
                .setSystemToken(dingConfig.getYida().getSystemToken())
                .setUserId(dingConfig.getYida().getUserId())
                .setLanguage(dingConfig.getYida().getLanguage())
                .setFormInstanceId(instanceId)
                .setUseLatestVersion(false)
                .setUpdateFormDataJson(data);
        try {
            UpdateFormDataResponse response = client.updateFormDataWithOptions(updateFormDataRequest, updateFormDataHeaders, new RuntimeOptions());
            log.info("修改宜搭数据 => {}", response.getStatusCode());
            if (response.getStatusCode() != 200) {
                throw new RuntimeException("编号回写宜搭异常 => " + response);
            }
            return response.getStatusCode();
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
            throw err;
        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
            throw err;
        }
    }

    public static com.aliyun.dingtalkoauth2_1_0.Client createClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkoauth2_1_0.Client(config);
    }

    public String getToken() throws Exception {
        com.aliyun.dingtalkoauth2_1_0.Client client = createClient();
        GetAccessTokenRequest getAccessTokenRequest = new com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest()
                .setAppKey(dingConfig.getAppKey())
                .setAppSecret(dingConfig.getAppSecret());
        try {
            GetAccessTokenResponse accessToken = client.getAccessToken(getAccessTokenRequest);
            GetAccessTokenResponseBody body = accessToken.getBody();
            log.info("获取宜搭Token => {}", body.getAccessToken());
            return body.getAccessToken();
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
            throw err;
        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
            throw err;
        }
    }
}
