package com.alibaba.work.faas.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.work.faas.model.SaveParamNotVerify;
import com.alibaba.work.faas.service.KingdeeService;
import com.alibaba.work.faas.service.YidaService;
import com.alibaba.work.faas.util.CommonUtil;
import com.alibaba.work.faas.util.KingdeeUtil;
import com.google.gson.Gson;
import com.kingdee.bos.webapi.entity.OperateParam;
import com.kingdee.bos.webapi.entity.OperatorResult;
import com.kingdee.bos.webapi.entity.SaveParam;
import com.kingdee.bos.webapi.entity.SaveResult;
import com.kingdee.bos.webapi.sdk.K3CloudApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
public class KingdeeServiceImpl implements KingdeeService {

    private static final Logger log = LoggerFactory.getLogger(KingdeeServiceImpl.class);

    @Autowired
    private KingdeeUtil kingdeeUtil;

    @Autowired
    private YidaService yidaService;

    @Override
    public JSONObject todoXSHT(Map<String, ?> formData, String modelId) {
        //封装数据
        Map<String, Object> DATA_XSHT_SB = buildData_HSHT(null,"SB");


        try {
            //获取API
            K3CloudApi api = kingdeeUtil.createApi(null);
            //
            SaveResult save = api.save("", new SaveParam<Map>(DATA_XSHT_SB));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public JSONObject todoXSDD(Map<String, ?> formData, String modelId) {
        //封装数据
        Map<String, Object> DATA = buildData_XSDD(formData);

        try {
            //获取API
            K3CloudApi api = kingdeeUtil.createApi(null);
            //
            SaveResult result = api.save("SAL_SaleOrder", new SaveParam<Map>(DATA));
            Gson gson = new Gson();
            if (result.isSuccessfully()) {
                JSONObject json = JSONObject.parseObject(gson.toJson(result.getResult()));
                log.info("销售订单保存接口: {}", json);
                //回写数据回宜搭
                String erpId = json.getString("Id");
                String erpNumber = json.getString("Number");
                JSONObject updateFormDataJson = new JSONObject();
                updateFormDataJson.put("textField_m4cbq726", erpId);
                updateFormDataJson.put("textField_m4c7t9mm", erpNumber);
                yidaService.updateData(updateFormDataJson.toJSONString(), modelId);

                return json;
            } else {
                log.info("销售订单保存接口: " + gson.toJson(result.getResult()));
                throw new RuntimeException(gson.toJson(result.getResult()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JSONObject todoPZ(Map<String, ?> formData, String modelId) {
        //封装数据
        Map<String, Object> DATA = buildData_PZ(formData);

        try {
            //获取API
            K3CloudApi api = kingdeeUtil.createApi(null);
            //
            SaveResult result = api.save("GL_VOUCHER", new SaveParam<Map>(DATA));
            Gson gson = new Gson();
            if (result.isSuccessfully()) {
                JSONObject json = JSONObject.parseObject(gson.toJson(result.getResult()));
                log.info("凭证保存接口: {}", json);
                //回写数据回宜搭
                String erpId = json.getString("Id");
                String erpNumber = json.getString("Number");
                JSONObject updateFormDataJson = new JSONObject();
                updateFormDataJson.put("textField_m4s6w7or", erpId);
                updateFormDataJson.put("textField_m4s6w7os", erpNumber);
                yidaService.updateData(updateFormDataJson.toJSONString(), modelId);

                return json;
            } else {
                log.info("凭证保存接口: " + gson.toJson(result.getResult()));
                throw new RuntimeException(gson.toJson(result.getResult()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JSONObject todoXM(Map<String, ?> formData, String modelId) {
        //封装数据
        Map<String, Object> DATA = buildData_XM(formData);

        try {
            //获取API
            K3CloudApi api = kingdeeUtil.createApi(null);
            //
            SaveResult result = api.save("BAS_PreBaseDataOne", new SaveParam<Map>(DATA));
            Gson gson = new Gson();
            if (result.isSuccessfully()) {
                JSONObject json = JSONObject.parseObject(gson.toJson(result.getResult()));
                log.info("项目保存接口: {}", json);
                //回写数据回宜搭
                String erpId = json.getString("Id");
                String erpNumber = json.getString("Number");
                JSONObject updateFormDataJson = new JSONObject();
                updateFormDataJson.put("textField_m4cbq726", erpId);
                updateFormDataJson.put("textField_m4c7t9mm", erpNumber);
                yidaService.updateData(updateFormDataJson.toJSONString(), modelId);

                return json;
            } else {
                log.info("凭证保存接口: " + gson.toJson(result.getResult()));
                throw new RuntimeException(gson.toJson(result.getResult()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JSONObject todoTH(Map<String, ?> formData, String modelId) {
        if ("销售退货单".equals(formData.get("selectField_m4tjl62s"))) {
            return todoXSTH(formData,modelId);
        }else {
            return todoCGTH(formData,modelId);
        }
    }

    @Override
    public JSONObject todoCGTH(Map<String, ?> formData, String modelId) {
        //封装数据
        Map<String, Object> DATA = buildData_CGTH(formData);

        try {
            //获取API
            K3CloudApi api = kingdeeUtil.createApi(null);
            //
            Gson gson = new Gson();
            SaveParam<Map> mapSaveParam = new SaveParam<>(DATA);
            log.info("采购退货保存接口参数: {}", gson.toJson(mapSaveParam));
            SaveResult result = api.save("PUR_MRB", mapSaveParam);

            if (result.isSuccessfully()) {
                JSONObject json = JSONObject.parseObject(gson.toJson(result.getResult()));
                log.info("采购退货保存接口: {}", json);
                //回写数据回宜搭
                String erpId = json.getString("Id");
                String erpNumber = json.getString("Number");
                JSONObject updateFormDataJson = new JSONObject();
                updateFormDataJson.put("textField_m4xypkuk", erpId);
                updateFormDataJson.put("textField_m4xypkul", erpNumber);
                yidaService.updateData(updateFormDataJson.toJSONString(), modelId);

                return json;
            } else {
                log.info("采购退货保存接口: " + gson.toJson(result.getResult()));
                throw new RuntimeException(gson.toJson(result.getResult()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JSONObject todoXSTH(Map<String, ?> formData, String modelId) {
        //封装数据
        Map<String, Object> DATA = buildData_XSTH(formData);

        try {
            //获取API
            K3CloudApi api = kingdeeUtil.createApi(null);
            //
            SaveResult result = api.save("SAL_RETURNSTOCK", new SaveParam<Map>(DATA));
            Gson gson = new Gson();
            if (result.isSuccessfully()) {
                JSONObject json = JSONObject.parseObject(gson.toJson(result.getResult()));
                log.info("销售退货保存接口: {}", json);
                //回写数据回宜搭
                String erpId = json.getString("Id");
                String erpNumber = json.getString("Number");
                JSONObject updateFormDataJson = new JSONObject();
                updateFormDataJson.put("textField_m4xypkuk", erpId);
                updateFormDataJson.put("textField_m4xypkul", erpNumber);
                yidaService.updateData(updateFormDataJson.toJSONString(), modelId);
                return json;
            } else {
                log.info("销售退货保存接口: " + gson.toJson(result.getResult()));
                throw new RuntimeException(gson.toJson(result.getResult()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> buildData_XSTH(Map<String, ?> formData) {
        Map<String, Object> data = new LinkedHashMap<>();

        // 单据类型
        Map<String, Object> billTypeMap = new HashMap<>();
        billTypeMap.put("FNUMBER", "XSTHD01_SYS");
        data.put("FBillTypeID", billTypeMap);
        // 日期
        data.put("FDate", CommonUtil.getCurrentTimeString());
        // 客户编号
        Map<String, Object> retCustMap = new HashMap<>();
        retCustMap.put("FNumber", formData.get("textField_m4xxaxs9"));
        data.put("FRetcustId", retCustMap);
        // 转移业务类型
        Map<String, Object> transferBizTypeMap = new HashMap<>();
        transferBizTypeMap.put("FNumber", "OverOrgSal");
        data.put("FTransferBizType", transferBizTypeMap);

        // 实体数据集合
        List<Map<String, Object>> entityList = new ArrayList<>();
        List<Map<String, Object>> detail = (List<Map<String, Object>>) formData.get("tableField_m3gqza5x");
        for (Map<String, Object> rowDetail : detail) {
            // 构建单个实体数据
            Map<String, Object> entity = new HashMap<>();
            // 物料编码
            Map<String, Object> materialMap = new HashMap<>();
            materialMap.put("FNUMBER", rowDetail.get("textField_m3gqza62"));
            entity.put("FMaterialId", materialMap);
            // 实际数量
            entity.put("FRealQty", rowDetail.get("numberField_m3gqza6e"));
            // 退货类型
            Map<String, Object> returnTypeMap = new HashMap<>();
            returnTypeMap.put("FNUMBER", "THLX01_SYS");
            entity.put("FReturnType", returnTypeMap);
            // 发货日期
            entity.put("FDeliveryDate", CommonUtil.getCurrentTimeString());
            // 来源单据类型
            entity.put("FSrcBillTypeID", "SAL_SaleOrder");
            // 来源单据编号
            entity.put("FSrcBillNo", rowDetail.get("textField_m50bngcw"));
            // 来源单据分录内码
            entity.put("FSOEntryId", rowDetail.get("textField_m50bngcx"));
            // 入库日期
            entity.put("FINSTOCKDATE", CommonUtil.getCurrentTimeString());
            // 仓库信息
            Map<String, Object> stockMap = new HashMap<>();
            stockMap.put("FNUMBER", "01");
            entity.put("FStockId", stockMap);
            // 仓库状态
            Map<String, Object> stockStatusMap = new HashMap<>();
            stockStatusMap.put("FNUMBER", "KCZT01_SYS");
            entity.put("FStockstatusId", stockStatusMap);
            // 将实体添加到实体集合中
            entityList.add(entity);
        }
        // 将实体集合添加到主数据
        data.put("FEntity", entityList);


        // 最终的 rowData 即为生成的对应 Java 数据结构


        return data;
    }

    private Map<String, Object> buildData_CGTH(Map<String, ?> formData) {
        Map<String, Object> data = new LinkedHashMap<>();

        // 单据类型
        Map<String, Object> billTypeMap = new HashMap<>();
        billTypeMap.put("FNUMBER", "TLD01_SYS");
        data.put("FBillTypeID", billTypeMap);
        // 业务类型
        data.put("FBusinessType", "CG");
        // 日期
        data.put("FDate", CommonUtil.getCurrentTimeString());
        // MR类型
        data.put("FMRTYPE", "A");
        // MR模式
        data.put("FMRMODE", "A");
        // 是否转换
        data.put("FIsConvert", false);
        // 供应商
        Map<String, Object> supplierMap = new HashMap<>();
        supplierMap.put("FNumber", formData.get("textField_m50bngcy"));
        data.put("FSupplierID", supplierMap);
        // 接收方
        Map<String, Object> acceptorMap = new HashMap<>();
        acceptorMap.put("FNumber", formData.get("textField_m50bngcy"));
        data.put("FACCEPTORID", acceptorMap);
        // 所有者类型（头部）
        data.put("FOwnerTypeIdHead", "BD_Supplier");
        // 所有者 ID（头部）
        Map<String, Object> ownerIdHeadMap = new HashMap<>();
        ownerIdHeadMap.put("FNumber", formData.get("textField_m50bngcy"));
        data.put("FOwnerIdHead", ownerIdHeadMap);
        // 账户类型
        data.put("FACCTYPE", "Q");
        // 明细数据集合
        List<Map<String, Object>> entryList = new ArrayList<>();
        List<Map<String, Object>> detail = (List<Map<String, Object>>) formData.get("tableField_m3gqza5x");
        for (Map<String, Object> rowDetail : detail) {
            // 构建单个明细数据
            Map<String, Object> entry = new LinkedHashMap<>();
            // 物料编码
            Map<String, Object> materialMap = new HashMap<>();
            materialMap.put("FNumber", rowDetail.get("textField_m3gqza62"));
            entry.put("FMATERIALID", materialMap);
            // 实际退货数量
            entry.put("FRMREALQTY", rowDetail.get("numberField_m3gqza6e"));
            // 采购订单分录 ID
//            entry.put("FPOORDERENTRYID", rowDetail.get("textField_m50bngcw"));
            // 所有者类型
            entry.put("FOWNERTYPEID", "BD_Supplier");
            // 所有者 ID
            Map<String, Object> ownerIdMap = new HashMap<>();
            ownerIdMap.put("FNumber", formData.get("textField_m50bngcy"));
            entry.put("FOWNERID", ownerIdMap);
            // 来源单据类型
            entry.put("FSRCBillTypeId", "PUR_PurchaseOrder");
            // 来源单据编号
            entry.put("FSRCBillNo", formData.get("textField_m50bngd0"));
            // 将明细添加到明细集合中
            entryList.add(entry);
        }
        // 将明细集合添加到主数据
        data.put("FPURMRBENTRY", entryList);


        return data;
    }

    private Map<String, Object> buildData_XM(Map<String, ?> formData) {
        Map<String, Object> data = new HashMap<>();

        data.put("FNumber",  formData.get("serialNumberField_m3e2sejq"));
        data.put("FName", formData.get("textField_m3e2sejs"));

        return data;
    }

    private Map<String, Object> buildData_PZ(Map<String, ?> formData) {
        Map<String, Object> data = new HashMap<>();

        // 账簿
        Map<String, Object> FAccountBookIDMap = new HashMap<>();
        FAccountBookIDMap.put("FNumber", "001");
        data.put("FAccountBookID", FAccountBookIDMap);

        // 日期
        data.put("FDate", CommonUtil.getCurrentTimeString());

        // 凭证字
        Map<String, Object> FVOUCHERGROUPIDMap = new HashMap<>();
        FVOUCHERGROUPIDMap.put("FNumber", "PZZ1");
        data.put("FVOUCHERGROUPID", FVOUCHERGROUPIDMap);

        // 凭证号
        data.put("FVOUCHERGROUPNO", "0");

        // 状态
        data.put("FDocumentStatus", "Z");

        // 明细
        List<Map<String, Object>> FEntityList = new ArrayList<>();
        List<Map<String, Object>> entities = (List<Map<String, Object>>) formData.get("tableField_lw4nayuf");

        for (Map<String, Object> entity : entities) {
            Map<String, Object> entityMap = new HashMap<>();
            // 摘要
            entityMap.put("FEXPLANATION", entity.get("textareaField_lvixcqqk"));

            // 科目编码
            Map<String, Object> FACCOUNTIDMap = new HashMap<>();
            FACCOUNTIDMap.put("FNumber", kingdeeUtil.parseAccountSubject((String) entity.get("textField_m4s1lvlt")));
            entityMap.put("FACCOUNTID", FACCOUNTIDMap);

            // 核算维度
            Map<String, Object> FDetailIDMap = new HashMap<>();
            entityMap.put("FDetailID", FDetailIDMap);
            // 核算维度-部门
            Map<String, Object> FDETAILID_FFLEX5Map = new HashMap<>();
            FDETAILID_FFLEX5Map.put("FNumber", kingdeeUtil.getCodeByDeptName(((List<String>) entity.get("departmentSelectField_m4s1lvli")).get(0),null));
            FDetailIDMap.put("FDETAILID__FFLEX5", FDETAILID_FFLEX5Map);
            // 核算维度-员工
            Map<String, Object> FDETAILID_FFLEX7Map = new HashMap<>();
            FDETAILID_FFLEX7Map.put("FNumber", kingdeeUtil.getCodeByUserName(((List<String>) entity.get("employeeField_lwbkib8o")).get(0),null));
            FDetailIDMap.put("FDETAILID__FFLEX7", FDETAILID_FFLEX7Map);

            // 币别
            Map<String, Object> FCURRENCYIDMap = new HashMap<>();
            FCURRENCYIDMap.put("FNumber", "PRE001");
            entityMap.put("FCURRENCYID", FCURRENCYIDMap);

            // 汇率类型
            Map<String, Object> FEXCHANGERATETYPEMap = new HashMap<>();
            FEXCHANGERATETYPEMap.put("FNumber", "HLTX01_SYS");
            entityMap.put("FEXCHANGERATETYPE", FEXCHANGERATETYPEMap);

            // 填充其他数值
            entityMap.put("FEXCHANGERATE", "1.0");
            entityMap.put("FAMOUNTFOR", entity.get("numberField_lvixcqqj"));
            entityMap.put("FDEBIT", entity.get("numberField_lvixcqqj"));
            entityMap.put("FCREDIT", "0");

            FEntityList.add(entityMap);
        }

        //添加贷方科目
        Map<String, Object> entityMap = new HashMap<>();
        // 摘要
        entityMap.put("FEXPLANATION", "");

        // 科目编码
        Map<String, Object> FACCOUNTIDMap = new HashMap<>();
        FACCOUNTIDMap.put("FNumber", formData.get("textField_m4s1lvls"));
        entityMap.put("FACCOUNTID", FACCOUNTIDMap);

        // 币别
        Map<String, Object> FCURRENCYIDMap = new HashMap<>();
        FCURRENCYIDMap.put("FNumber", "PRE001");
        entityMap.put("FCURRENCYID", FCURRENCYIDMap);

        // 汇率类型
        Map<String, Object> FEXCHANGERATETYPEMap = new HashMap<>();
        FEXCHANGERATETYPEMap.put("FNumber", "HLTX01_SYS");
        entityMap.put("FEXCHANGERATETYPE", FEXCHANGERATETYPEMap);

        // 填充其他数值
        entityMap.put("FEXCHANGERATE", "1.0");
        entityMap.put("FAMOUNTFOR", formData.get("numberField_lwbp005u"));
        entityMap.put("FDEBIT", "0");
        entityMap.put("FCREDIT", formData.get("numberField_lwbp005u"));

        FEntityList.add(entityMap);

        // 将 FEntity 数据添加到 final map
        data.put("FEntity", FEntityList);

        System.err.println(data); // 打印结果（可删除）
        return data;
    }

    private Map<String, Object> buildData_XSDD(Map<String, ?> formData) {
        Map<String, Object> data = new HashMap<String, Object>();
        // 销售合同内码
        data.put("FContractId", formData.get("textField_m4qqrut3"));
        // 单据类型
        Map<String, Object> billTypeMap = new HashMap<>();
        billTypeMap.put("FNUMBER", formData.get("selectField_m4i9e08q_id"));
        data.put("FBillTypeID", billTypeMap);
        // 部门 todo
//        Map<String, Object> saleDeptMap = new HashMap<>();
//        saleDeptMap.put("FNUMBER", "04");
//        data.put("FSaleDeptId", saleDeptMap);
        // 客户
        Map<String, Object> custMap = new HashMap<>();
        custMap.put("FNUMBER", formData.get("textField_m4rrw592"));
        data.put("FCustId", custMap);
        // 日期
        data.put("FDate", CommonUtil.getCurrentTimeString());
        // 经办人
        Map<String, Object> salerMap = new HashMap<>();
        salerMap.put("FNUMBER", kingdeeUtil.getCodeByUserName(((List<String>) formData.get("employeeField_m3za7c28")).get(0),null));
        data.put("FSalerId", salerMap);
        // 合同类型
        Map<String, Object> contractTypeMap = new HashMap<>();
        contractTypeMap.put("FNUMBER", "A0001");
        data.put("F_XMHD_Assistant_83g", contractTypeMap);
        // 项目名称
        Map<String, Object> projectMap = new HashMap<>();
        projectMap.put("FNUMBER", formData.get("textField_m3za7c2g"));
        data.put("F_XMHD_Base_o54", projectMap);
        // 合同编号
        data.put("F_XMHD_Text_uk", formData.get("textField_m4qqrut5"));
        // 合同名称及标的
        data.put("F_XMHD_Text_fg2", formData.get("textField_m4qqrut4" + "-设备"));

        // 订单明细
        List<Map<String, Object>> fSaleOrderEntry = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> detail = (List<Map<String, Object>>) formData.get("tableField_m3za7c22");
        for (Map<String, Object> rowDetail : detail) {
            Map<String, Object> rowFSaleOrderEntry = new HashMap<>();
            // 物料编码
            Map<String, Object> materialMap = new HashMap<>();
            materialMap.put("FNUMBER", rowDetail.get("textField_m3za7c24"));
            rowFSaleOrderEntry.put("FMaterialId", materialMap);
            // 销售单位
            // Map<String, Object> unitMap = new HashMap<>();
            // unitMap.put("FNUMBER", rowDetail.get("textField_m3za7c24"));
            // rowFSaleOrderEntry.put("FUnitID", unitMap);
            // 计价单位
            // Map<String, Object> priceUnitMap = new HashMap<>();
            // priceUnitMap.put("FNUMBER", rowDetail.get("textField_m3za7c24"));
            // rowFSaleOrderEntry.put("FPriceUnitId", priceUnitMap);
            // 销售数量
            rowFSaleOrderEntry.put("FQty", rowDetail.get("numberField_m3za7c27"));
            // 要货日期
            rowFSaleOrderEntry.put("FDeliveryDate", CommonUtil.getCurrentTimeString());
            // 结算组织
            // Map<String, Object> settleMap = new HashMap<>();
            // settleMap.put("FNumber", "tai");
            // rowFSaleOrderEntry.put("FMaterialId", settleMap);
            // 库存单位
            // Map<String, Object> stockUnitMap = new HashMap<>();
            // stockUnitMap.put("FNumber", "tai");
            // rowFSaleOrderEntry.put("FStockUnitID", stockUnitMap);

            fSaleOrderEntry.add(rowFSaleOrderEntry);
        }
        data.put("FSaleOrderEntry",fSaleOrderEntry);
        System.err.println(data);
        return data;
    }

    public Map<String, Object> buildData_HSHT(Map<String, ?> formData, String flag) {
        Map<String, Object> data = new HashMap<String, Object>();
        return data;
    }

    @Override
    public JSONObject executeRegexList(List<JSONObject> regexList, Map<String, ?> yidaFormData,String yidaModelId) {
        log.info("匹配到{}条同步金蝶系统数据处理规则",regexList.size());
        JSONObject result = new JSONObject();
        JSONArray kingdeeSaveResult = new JSONArray();

        try {
            for (JSONObject regexObj : regexList) {
                log.info("===========================================");
                log.info("正在处理同步金蝶系统解析规则: {}",regexObj);
                JSONArray Execute = regexObj.getJSONObject("YiDa").getJSONArray("Execute");
                if (Execute.isEmpty()) {
                    log.info("无需要执行的方法,跳过该条解析: {}",regexObj);
                } else {
                    JSONObject oneExecuteResult = new JSONObject();
                    log.info("待执行的方法列表: {}",Execute);
                    log.info("开始获取金蝶连接API");
                    K3CloudApi api = kingdeeUtil.createApi(regexObj,yidaFormData);
                    log.info("成功获取金蝶连接API");
                    //获取已存在的ERP编码啊
                    List<String> erpNumbers = kingdeeUtil.buildNumberData(yidaFormData,regexObj.getJSONObject("YiDa").getString("ERP_NUMBER"));
                    for (Object executeObj : Execute) {
                        log.info("开始执行方法: {}",executeObj.toString());
                        JSONObject executeResult = null;
                        if ("unAudit".equals(executeObj.toString())) {
                            executeResult = unAudit(erpNumbers,regexObj.getString("Table"),api);
                        }
                        if ("save".equals(executeObj.toString())) {
                            executeResult = save(regexObj,yidaFormData,api);
                            if (!erpNumbers.contains(executeResult.getString("Number"))) {
                                erpNumbers.add(executeResult.getString("Number"));
                            }
                            oneExecuteResult.put("Number", executeResult.getString("Number"));
                            oneExecuteResult.put("Id", executeResult.getString("Id"));
                        }
                        if ("submit".equals(executeObj.toString())) {
                            executeResult = submit(erpNumbers,regexObj.getString("Table"),api);
                        }
                        if ("audit".equals(executeObj.toString())) {
                            executeResult = audit(erpNumbers,regexObj.getString("Table"),api);
                        }
                        oneExecuteResult.put(executeObj.toString(), executeResult);
                        log.info("结束执行方法: {}", executeObj);
                    }

                    //回写数据回宜搭
                    if (regexObj.getJSONObject("YiDa").getBoolean("WriteBack") && oneExecuteResult.containsKey("Id")) {
                        JSONObject updateFormDataJson = new JSONObject();
                        updateFormDataJson.put(regexObj.getJSONObject("YiDa").getString("ERP_ID"), oneExecuteResult.getString("Id"));
                        updateFormDataJson.put(regexObj.getJSONObject("YiDa").getString("ERP_NUMBER"), oneExecuteResult.getString("Number"));
                        log.info("开始宜搭数据回写: {}", updateFormDataJson);
                        Integer i = yidaService.updateData(updateFormDataJson.toJSONString(), yidaModelId);
                        log.info("结束宜搭数据回写 {}",i);
                    }

                    kingdeeSaveResult.add(oneExecuteResult);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status",false);
            result.put("error",e.getMessage());
            return result;
        }
        result.put("status",true);
        result.put("kingdeeSaveResult", kingdeeSaveResult);
        return result;
    }

    public JSONObject save(Map<String, Object> param, String tableName,K3CloudApi api) {
        try {
            Gson gson = new Gson();
            //保存数据
            log.info("调用WebApi保存接口开始,触发表单:{} ==> 请求报文: {}",tableName, gson.toJson(new SaveParamNotVerify<Map>(param)));
            SaveResult saveResult = api.save(tableName, new SaveParamNotVerify<Map>(param));
            JSONObject saveResultJson = JSONObject.parseObject(gson.toJson(saveResult.getResult()));
            if (saveResult.isSuccessfully()) {
                log.info("调用WebApi保存接口成功,触发表单:{} ==> 返回报文: {}",tableName, saveResultJson);
                return saveResultJson.getJSONObject("ResponseStatus").getJSONArray("SuccessEntitys").getJSONObject(0);
            } else {
                log.info("调用WebApi保存接口失败,触发表单:{} ==> 返回报文: {}",tableName, saveResultJson);
                throw new RuntimeException(saveResultJson.toJSONString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject save(JSONObject regexObj, Map<String, ?> yidaFormData, K3CloudApi api) {
        try {
            log.info("执行金蝶保存接口: Start");
            log.info("开始生成金蝶报文数据");
            Map<String, Object> kingdeeParam = kingdeeUtil.buildData(yidaFormData, regexObj.getJSONObject("Model"),api);
            log.info("成功生成金蝶报文数据: {}",new Gson().toJson(kingdeeParam));
            JSONObject saveResult = save(kingdeeParam, regexObj.getString("Table"), api);
            log.info("执行金蝶保存接口: End");
            return saveResult;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject submit(List<String> numbers, String tableName, K3CloudApi api) {
        try {
            Gson gson = new Gson();
            OperateParam operateParam = new OperateParam();
            operateParam.setNumbers(numbers);
            log.info("调用WebApi提交接口开始,触发表单:{} ==> 请求报文: {}",tableName, gson.toJson(operateParam));
            OperatorResult saveResult = api.submit(tableName, operateParam);
            JSONObject operatorResultJson = JSONObject.parseObject(gson.toJson(saveResult.getResult()));
            if (saveResult.isSuccessfully()) {
                log.info("调用WebApi提交接口成功,触发表单:{} ==> 返回报文: {}",tableName, operatorResultJson);
                return operatorResultJson;
            } else {
                log.info("调用WebApi提交接口失败,触发表单:{} ==> 返回报文: {}",tableName, operatorResultJson);
                throw new RuntimeException(operatorResultJson.toJSONString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject submit(JSONObject regexObj, Map<String, ?> yidaFormData, K3CloudApi api) {
        try {
            log.info("执行金蝶提交接口: Start");
            List<String> kingdeeParam = kingdeeUtil.buildNumberData(yidaFormData, regexObj.getJSONObject("YiDa").getString("ERP_NUMBER"));
            if (kingdeeParam == null || kingdeeParam.isEmpty()) {
                log.info("未获取到ERP编码，不执行保存操作");
                return null;
            }
            JSONObject unAuditResult = submit(kingdeeParam, regexObj.getString("Table"),api);
            log.info("执行金蝶提交接口: End");
            return unAuditResult;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject audit(List<String> numbers, String tableName, K3CloudApi api) {
        try {
            Gson gson = new Gson();
            OperateParam operateParam = new OperateParam();
            operateParam.setNumbers(numbers);
            log.info("调用WebApi审核接口开始,触发表单:{} ==> 请求报文: {}",tableName, gson.toJson(operateParam));
            OperatorResult saveResult = api.audit(tableName, operateParam);
            JSONObject operatorResultJson = JSONObject.parseObject(gson.toJson(saveResult.getResult()));
            if (saveResult.isSuccessfully()) {
                log.info("调用WebApi审核接口成功,触发表单:{} ==> 返回报文: {}",tableName, operatorResultJson);
                return operatorResultJson;
            } else {
                log.info("调用WebApi审核接口失败,触发表单:{} ==> 返回报文: {}",tableName, operatorResultJson);
                throw new RuntimeException(operatorResultJson.toJSONString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject audit(JSONObject regexObj, Map<String, ?> yidaFormData, K3CloudApi api) {
        try {
            log.info("执行金蝶审核接口: Start");
            List<String> kingdeeParam = kingdeeUtil.buildNumberData(yidaFormData, regexObj.getJSONObject("YiDa").getString("ERP_NUMBER"));
            if (kingdeeParam == null || kingdeeParam.isEmpty()) {
                log.info("未获取到ERP编码，不执行保存操作");
                return null;
            }
            JSONObject unAuditResult = audit(kingdeeParam, regexObj.getString("Table"),api);
            log.info("执行金蝶审核接口: End");
            return unAuditResult;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject unAudit(List<String> numbers, String tableName, K3CloudApi api) {
        try {
            Gson gson = new Gson();
            if (numbers == null || numbers.isEmpty() || (numbers.size() == 1 && numbers.get(0).isEmpty())) {
                log.info("未获取到ERP编码，不执行反审核操作");
                return null;
            }
            OperateParam operateParam = new OperateParam();
            operateParam.setNumbers(numbers);
            log.info("调用WebApi反审核接口开始,触发表单:{} ==> 请求报文: {}",tableName, gson.toJson(operateParam));
            OperatorResult saveResult = api.unAudit(tableName, operateParam);
            JSONObject operatorResultJson = JSONObject.parseObject(gson.toJson(saveResult.getResult()));
            if (saveResult.isSuccessfully()) {
                log.info("调用WebApi反审核接口成功,触发表单:{} ==> 返回报文: {}",tableName, operatorResultJson);
                return operatorResultJson;
            } else {
                log.info("调用WebApi反审核接口失败,触发表单:{} ==> 返回报文: {}",tableName, operatorResultJson);
                throw new RuntimeException(operatorResultJson.toJSONString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject unAudit(JSONObject regexObj, Map<String, ?> yidaFormData, K3CloudApi api) {
        try {
            log.info("执行金蝶反审核接口: Start");
            List<String> kingdeeParam = kingdeeUtil.buildNumberData(yidaFormData, regexObj.getJSONObject("YiDa").getString("ERP_NUMBER"));
            if (kingdeeParam == null || kingdeeParam.isEmpty() || (kingdeeParam.size() == 1 && kingdeeParam.get(0).isEmpty())) {
                log.info("未获取到ERP编码，不执行反审核操作");
                return null;
            }
            JSONObject unAuditResult = unAudit(kingdeeParam, regexObj.getString("Table"),api);
            log.info("执行金蝶反审核接口: End");
            return unAuditResult;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
