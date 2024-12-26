package com.alibaba.work.faas;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.alibaba.work.faas.common.AbstractEntry;
import com.alibaba.work.faas.common.FaasInputs;
import com.alibaba.work.faas.constant.DingConstant;
import com.alibaba.work.faas.constant.KingdeeConstant;
import com.alibaba.work.faas.service.DistributeService;
import com.alibaba.work.faas.service.KingdeeService;
import com.alibaba.work.faas.service.YidaService;
import com.alibaba.work.faas.util.DingOpenApiUtil;
import com.alibaba.work.faas.util.KingdeeUtil;
import com.alibaba.work.faas.util.YidaConnectorUtil;
import com.alibaba.work.faas.util.YidaConnectorUtil.ConnectorTypeEnum;
import com.aliyun.dingtalkyida_1_0.models.BatchSaveFormDataRequest;

import com.aliyun.dingtalkyida_1_0.models.GetFormDataByIDResponseBody;
import com.google.gson.Gson;
import com.kingdee.bos.webapi.sdk.K3CloudApi;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 您的业务逻辑请从此类开始
 * 
 * @Date 2023/09/21 11:16 AM
 * @Description 宜搭Faas连接器函数入口，FC handler：com.alibaba.work.faas.FaasEntry::handleRequest
 * @Version 2.0
 * @Author bufan
 * 
 **/
@Component
public class FaasEntry extends AbstractEntry {
	private static final Logger log = LoggerFactory.getLogger(FaasEntry.class);

	@Autowired
	private YidaService yidaService;
	@Autowired
	private KingdeeService kingdeeService;
	@Autowired
	private KingdeeUtil kingdeeUtil;
	@Autowired
	private DistributeService distributeService;

    @Override
    public JSONObject execute(FaasInputs faasInputs) {
        log.info("接口入参 => {}", JSON.toJSONString(faasInputs));
		//填充宜搭工具类的上下文, 必须调用此方法!!! 请不要删除
		initYidaUtil(faasInputs);
		//业务传的实际入参(如果您配置了参数映射(也就是点击了连接器工厂里的解析Body按钮并配置了各个参数描述和映射), 那么就是业务实际参数经过参数映射处理后的值)
		Map<String,Object> input = faasInputs.getInputs();

		String modelId = (String) input.get("modelId");
		String formUuid = (String) input.get("formUuid");

		log.info("宜搭表单实例ID: {}", modelId);
        log.info("宜搭表单FormUuid: {}", formUuid);

		//获取宜搭表单数据
        try {
			GetFormDataByIDResponseBody yidaData = yidaService.getDataById(modelId);
			Map<String, ?> formData = yidaData.getFormData();
			System.err.println("=====================解析测试Start====================");
			distributeService.distribute(modelId, formUuid);
			System.err.println("=====================解析测试End======================");
			switch (formUuid) {
				case DingConstant.YIDA_FORMUUID_XSHT:
					kingdeeService.todoXSHT(formData,modelId);
					break;
				case DingConstant.YIDA_FORMUUID_XSDD:
					kingdeeService.todoXSDD(formData,modelId);
					break;
				case DingConstant.YIDA_FORMUUID_FYSP:
					kingdeeService.todoPZ(formData,modelId);
					break;
				case DingConstant.YIDA_FORMUUID_XMLX:
					kingdeeService.todoXM(formData, modelId);
					break;
				case DingConstant.YIDA_FORMUUID_TH:
					kingdeeService.todoTH(formData, modelId);
					break;
				default:
					break;
			}

		} catch (Exception e) {
//            throw new RuntimeException(e);
			log.error(e.getMessage());
			e.printStackTrace();
			JSONObject result = new JSONObject();
			result.put("success",false);
			result.put("result","业务异常");
			result.put("error",e.getMessage());
			return result;
        }


		/**
		 * 返回的JSONObject并不是一定要带success、result、error, 下面的代码只是示例, 具体返回哪些key-value由您自己决定, 尽量与您在宜搭连接器工厂里配置的出参结构保持一致即可
		 */
		JSONObject result = new JSONObject();
		result.put("success",true);
		result.put("result","执行成功");
		result.put("error","");

		return result;
	}

	/**
	 * 将相关参数存到宜搭工具类里, 必须要调用此方法!!! 请不要删除!!!
	 *
	 * @param faasInputs
	 */
	private static final void initYidaUtil(FaasInputs faasInputs){
		/**
		 * 用于调用钉钉开放平台OpenAPI的accessToken, 宜搭提供的, 仅申请了钉钉开放平台的部分OpenAPI的调用权限
		 * 如果此accessToken不满足您的需求,可在钉钉开放平台创建您自己的钉钉应用并获取appKey和APPSecret并使用com.alibaba.work.faas.util.DingOpenApiUtil获取您自己的customAccessToken
		 *
		 * @see com.alibaba.work.faas.util.DingOpenApiUtil#getCustomAccessTokenThenCache(String,String)
		 */
		String accessToken = (String) faasInputs.getYidaContext().get("accessToken");
		// 设置钉开放平台访问token, 后续无需再设置
		DingOpenApiUtil.setAccessToken(accessToken);

		// try {
		//     //调用该方法就会自动存储customAccessToken, 之后请不要调用DingOpenApiUtil.setAccessToken(accessToken)将返回的customAccessToken覆盖宜搭传入的accessToken;
		//     DingOpenApiUtil.getCustomAccessTokenThenCache("您的钉钉应用appKey", "您的钉钉应用appSecret");
		// } catch (Exception e) {
		//     System.out.println("getCustomAccessTokenThenCache error:"+e.getMessage());
		// }
		/**
		 *调用宜搭连接器依赖consumeCode
		 */
		String consumeCode = (String)faasInputs.getYidaContext().get("consumeCode");
		//设置连接器消费码, 后续无需再设置
		YidaConnectorUtil.setConsumeCode(consumeCode);
	}

}