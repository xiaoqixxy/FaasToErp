package com.alibaba.work.faas.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.work.faas.config.KingdeeConfig;
import com.kingdee.bos.webapi.entity.IdentifyInfo;
import com.kingdee.bos.webapi.sdk.K3CloudApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 谢欣杨
 * @deprecated 金蝶相关工具类
 */
@Component
public class KingdeeUtil {

    @Autowired
    private KingdeeConfig kingdeeConfig;




    /**
     *
     * @param buildData 数据格斯 ["金蝶字段key":[金蝶字段value集合],"金蝶字段key":{金蝶字段value集合}]
     * @return buildDataAfter 数据格斯 [{金蝶字段key:金蝶字段value集合,金蝶字段key:金蝶字段value集合},{...}]
     */

    // 将[[],[]]格式数据转换为[{},{}]
    public Object analysisDataAfter(Map<String, Object> buildData) {
        List<JSONObject> buildDataAfter = new ArrayList<>();

        // 获取最大行数
        int dataCount = getMaxRowCount(buildData);

        // 封装数据
        for (int i = 0; i < dataCount; i++) {
            JSONObject rowBuildDataAfter = new JSONObject();
            for (Map.Entry<String, Object> entry : buildData.entrySet()) {
                Object entryValue = entry.getValue();

                if (entryValue instanceof List) {
                    // 如果值是 List，根据当前行数提取数据
                    List<?> listValue = (List<?>) entryValue;
                    if (listValue.size() > i) {
                        rowBuildDataAfter.put(entry.getKey(), listValue.get(i));
                    }
                } else if (entryValue instanceof Map) {
                    // 如果值是嵌套的 Map，递归处理
                    Map<String, Object> nestedMap = (Map<String, Object>) entryValue;
                    JSONObject nestedObject = new JSONObject();
                    //如果不只有编码，则直接当作对象存入，特殊情请自定义代码处理
                    if (nestedMap.size() > 1) {
                        rowBuildDataAfter.put(entry.getKey(), nestedObject);
                    }
                    for (Map.Entry<String, Object> nestedEntry : nestedMap.entrySet()) {
                        Object nestedValue = nestedEntry.getValue();

                        if (nestedValue instanceof List) {
                            // 如果嵌套值是 List，根据当前行数提取数据
                            List<?> nestedList = (List<?>) nestedValue;
                            if (nestedList.size() > i) {
//                                JSONArray array = new JSONArray();
//                                array.add(nestedList.get(i));
//                                nestedObject.put(nestedEntry.getKey(), array);
                                nestedObject.put(nestedEntry.getKey(), nestedList.get(i));
                            }
                        } else {
                            // 直接加入嵌套字段
                            nestedObject.put(nestedEntry.getKey(), nestedValue);
                        }
                    }
                    rowBuildDataAfter.put(entry.getKey(), nestedObject);
                } else {
                    // 普通值，直接加入结果
                    rowBuildDataAfter.put(entry.getKey(), entryValue);
                }
            }
            if (!rowBuildDataAfter.isEmpty()) {
                buildDataAfter.add(rowBuildDataAfter);
            }
        }

        return buildDataAfter;
    }


    private int getMaxRowCount(Map<String, Object> buildData) {
        return buildData.values().stream()
                .mapToInt(value -> {
                    if (value instanceof List) {
                        return ((List<?>) value).size();
                    } else if (value instanceof Map) {
                        // 如果是嵌套 Map，递归计算其最大行数
                        return getMaxRowCount((Map<String, Object>) value);
                    }
                    return 1; // 非列表/嵌套对象默认计为 1 行
                })
                .max()
                .orElse(1);
    }

    public List<Object> getValuesByKey(Object obj, String targetKey) {
        List<Object> result = new ArrayList<>();
        Set<Object> seen = new HashSet<>(); // 用于去重

        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (entry.getKey().equals(targetKey)) {
                    if (!seen.contains(entry.getValue())) {
                        result.add(entry.getValue());
                        seen.add(entry.getValue());
                    }
                }
                result.addAll(getValuesByKey(entry.getValue(), targetKey));
            }
        } else if (obj instanceof List) {
            for (Object element : (List<?>) obj) {
                result.addAll(getValuesByKey(element, targetKey));
            }
        }
        return result;
    }

    /**
     * 将宜搭数据按指定格式封装成ERP请求参数方法入口
     * @param formData
     * @param regex
     * @return
     */
    public Map<String, Object> buildData(Map<String, ?> formData, JSONObject regex,K3CloudApi api) {
        Map<String, Object> buildResult = new LinkedHashMap<>();

        for (String key : regex.keySet()) {
            Object value = regex.get(key);

            if (value instanceof String || value instanceof Number || value instanceof Boolean) {
                buildResult.put(key, analysisData(formData, value, api));
            } else if (value instanceof JSONObject) {
                // 避免重复解析
                if (!buildResult.containsKey(key)) {
                    buildResult.put(key, buildData(formData, (JSONObject) value,api));
                }
            } else if (value instanceof JSONArray) {
                buildResult.put(key, buildData(formData, (JSONArray) value, api));
            }
        }
        return buildResult;
    }


    public Object buildData(Map<String, ?> formData, JSONArray regexList,K3CloudApi api) {
        List<Object> listResult = new ArrayList<>();

        regexList.forEach(item -> {
            if (item instanceof String || item instanceof Number || item instanceof Boolean) {
                listResult.add(analysisData(formData, item, api));
            } else if (item instanceof JSONObject) {
                listResult.add(analysisDataAfter(buildData(formData, (JSONObject) item,api)));
            }
        });

        return listResult.size() == 1 ? listResult.get(0) : listResult;
    }

    /**
     * 解析结果的后置方法
     * @param regex
     * @return
     */
    public Object analysisData(Map<String, ?> formData, Object regex, K3CloudApi api) {
        if (regex instanceof Number || regex instanceof Boolean) {
            return regex;
        }
        if (regex instanceof String) {
            String regexStr = (String) regex;

            if (regexStr.contains("Field_")) {
                List<Object> valuesByKey = getValuesByKey(formData, regexStr);

                afterAnalysisData(valuesByKey,regexStr, api);

                // 确保单值或列表类型一致性
                return valuesByKey.size() == 1 ? valuesByKey.get(0) : valuesByKey;
            } else {
                return regexStr;
            }
        }
        return formData.get(regex);
    }

    private void afterAnalysisData(List<Object> valuesByKey, String regex, K3CloudApi api) {
        if (regex.startsWith("employeeField") || regex.startsWith("departmentSelectField")) {
            for (int i = 0; i < valuesByKey.size(); i++) {
                Object o = valuesByKey.get(i);
                if (o instanceof List) {
                    List<?> list = (List<?>) o;
                    List<Object> updatedList = new ArrayList<>();
                    for (Object item : list) {
                        if (item instanceof String) {
                            updatedList.add(applyTransformation((String) item, regex, api));
                        } else {
                            updatedList.add(item);
                        }
                    }
                    valuesByKey.set(i, updatedList.size() == 1 ? updatedList.get(0) : updatedList); // 单值直接替换
                } else if (o instanceof String) {
                    valuesByKey.set(i, applyTransformation((String) o, regex, api));
                }
            }
        }

        if (regex.startsWith("dateField")) {
            // Use 24-hour format ("HH" instead of "hh") for proper time representation
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // Set a specific timezone if needed
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); // Or any time zone you prefer

            for (int i = 0; i < valuesByKey.size(); i++) {
                Object value = valuesByKey.get(i);
                if (value instanceof Long) { // Check if the value is a timestamp
                    try {
                        Date date = new Date((Long) value);
                        valuesByKey.set(i, sdf.format(date)); // Replace with formatted date string
                    } catch (Exception e) {
                        // Keep the original value in case of any error
                    }
                }
            }
        }

    }

    private String applyTransformation(String value, String after, K3CloudApi api) {
        if (after.startsWith("employeeField")) {
            return getCodeByUserName(value);
        } else if (after.startsWith("departmentSelectField")) {
            return getCodeByDeptName(value);
        }
        return value; // 如果无匹配条件，则返回原值
    }

    public List<String> buildNumberData(Map<String, ?> formData, String regex) {
        Map<String, Object> buildResult = new LinkedHashMap<>();

        Object o = analysisData(formData, regex, null);

        if (o instanceof List) {
            return (List<String>) o;
        }else {
            ArrayList<String> strings = new ArrayList<>();
            String s = String.valueOf(o);
            if (s != null && !s.isEmpty()) {
                strings.add(s);
            }
            return strings;
        }
    }

//    public Map<String, Object> buildData(Map<String, ?> formData, JSONObject regex) {
//        System.err.println(regex);
//        //封装数据
//        Map<String, Object> buildResult = new LinkedHashMap<>();
//        for (Map.Entry<String, Object> entry : regex.entrySet()) {
//            if (entry.getValue() instanceof String || entry.getValue() instanceof Number || entry.getValue() instanceof Boolean) {
//                buildResult.put(entry.getKey(), analysisData(formData,entry.getValue()));
//            }
//            if (entry.getValue() instanceof JSONObject) {
//                buildResult.put(entry.getKey(), buildData(formData,(JSONObject) entry.getValue()));
//            }
//            if (entry.getValue() instanceof JSONArray) {
//                buildResult.put(entry.getKey(), buildData(formData,(JSONArray) entry.getValue()));
//            }
//        }
//
//        return buildResult;
//    }
//
//
//
//    public List buildData(Map<String, ?> formData, JSONArray regexList) {
//        //封装数据
//        List<Map<String, Object>> listResult = new ArrayList<>();
//
//        for (Object regex : regexList) {
//            Map<String,Object> analysisMap = new LinkedHashMap<>();
//            if (regex instanceof String || regex instanceof Number || regex instanceof Boolean) {
//                analysisMap.put(String.valueOf(regex), analysisData(formData,regex));
//            }
//            if (regex instanceof JSONObject) {
//                analysisMap.put(String.valueOf(regex), analysisDataAfter(buildData(formData,(JSONObject) regex)));
//            }
//            listResult.add(analysisMap);
//        }
//
//
//        return listResult;
//    }

    /**
     * 通过公司名称获取对应账套api连接
     * @param
     * @return
     */
    public K3CloudApi createApi(JSONObject regexObj, Map<String, ?> yidaFormData) {
        Object accountName = analysisData(yidaFormData, regexObj.getJSONObject("YiDa").getString("AccountName"), null);
        if (!(accountName instanceof String)) {
            return createApi(null);
        }
        return createApi((String) accountName);
    }

    public K3CloudApi createApi(String name) {
        KingdeeConfig.KingdeeAccount config;
        if (name == null) {
            config = kingdeeConfig.getDefaultConfig();
        }else {
            config = kingdeeConfig.getConfigByName(name);
        }
        IdentifyInfo identifyInfo = new IdentifyInfo();
        identifyInfo.setUserName(config.getUsername());
//        identifyInfo.setPwd(config.getPassword());
        identifyInfo.setAppId(config.getAppId());
        identifyInfo.setAppSecret(config.getAppKey());
        identifyInfo.setdCID(config.getAccountId());
        identifyInfo.setlCID(2052);
        identifyInfo.setServerUrl(config.getUrl());
        return new K3CloudApi(identifyInfo);
    }

    private final Map<String,String> SUBJECT_MAPPING = new HashMap<>();
    {
        SUBJECT_MAPPING.put("","");
    }

    /**
     * 解析映射借方科目
     * @param subject
     * @return
     */
    public String parseAccountSubject(String subject) {
        return SUBJECT_MAPPING.getOrDefault(subject,"6601.02");
    }

    /**
     * 获取对应用户名在金蝶系统的编码
     * @param userName
     * @return
     */
    public String getCodeByUserName(String userName,K3CloudApi api) {

        return "0000001_21_1";
    }

    public String getCodeByUserName(String userName) {

        return getCodeByUserName(userName,null);
    }

    /**
     * 获取对部门在金蝶系统的编码
     * @param deptName
     * @return
     */
    public String getCodeByDeptName(String deptName,K3CloudApi api) {

        return "04";
    }

    public String getCodeByDeptName(String deptName) {

        return getCodeByDeptName(deptName,null);
    }

    /*    //将[[],[]]格式数据转换为[{},{}]
    public Object analysisDataAfter (Map<String, Object> buildData) {
        List<JSONObject> buildDataAfter = new ArrayList<>();

        //获取最大行数
        int dataCount = buildData.values().stream()  // 获取 Map 中的所有值并创建流
                .filter(value -> value instanceof List<?>) // 过滤出数组类型
                .mapToInt(value -> ((List<?>) value).size())  // 映射为数组长度
                .max().orElse(1);

        //封装数据
        for (int i = 0; i < dataCount; i++) {
            JSONObject rowBuildDataAfter = new JSONObject();
            for (Map.Entry<String, Object> entry : buildData.entrySet()) {
                if (entry.getValue() instanceof List) {
                    List<?> entryValue = (List<?>) entry.getValue();
                    if (entryValue.size() > i) {
                        rowBuildDataAfter.put(entry.getKey(),entryValue.get(i));
                    }
                } else {
                    rowBuildDataAfter.put(entry.getKey(),entry.getValue());
                }
            }
            if (!rowBuildDataAfter.isEmpty()) {
                buildDataAfter.add(rowBuildDataAfter);
            }
        }

        return buildDataAfter;
    }*/


/*    public JSONObject analysisDataAfter(String regexStr) {
        String regex = "\\$获取ERP部门编码\\$\\(([^)]*?)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(regexStr);
        JSONObject result = new JSONObject();

        if (matcher.find()) {
            String resultKey = matcher.group(1); // 提取括号内容
            result.put("regex", resultKey);
            result.put("after", Collections.singletonList("$获取ERP部门编码$")); // 明确单值
        } else {
            result.put("regex", regexStr); // 原值
        }
        return result;
    }*/


/*    public static String toString(Object obj) {
        if (obj instanceof List) {
            List list = (List) obj;
            if (list.size() > 0) {
                return list.get(0).toString();
            }
            return "";
        }
        return obj.toString();
    }*/

/*    public void afterAnalysisData(List<Object> valuesByKey,List<String> afters) {
        for (String after : afters) {
            for (Object value : valuesByKey) {
                if (after.equals("$获取ERP部门编码$")) {
                    value = getCodeByDeptName(toString(value));
                }
                if (after.equals("$获取ERP用户编码$")) {
                    value = getCodeByUserName(toString(value));
                }
            }
        }
    }*/

/*    public Object analysisData(Map<String, ?> formData, Object regex) {
        if (regex instanceof Number || regex instanceof Boolean) {
            return regex;
        }
        if (regex instanceof String) {
            String regexStr = (String) regex;

            if (regexStr.contains("Field_")) {
                JSONObject analysisAfter = analysisDataAfter(regexStr);
                String processedKey = analysisAfter.getString("regex");
                List<Object> valuesByKey = getValuesByKey(formData, processedKey);

                List<String> afterActions = analysisAfter.getObject("after", List.class);
                if (afterActions != null && !afterActions.isEmpty()) {
                    afterAnalysisData(valuesByKey, afterActions);
                }
                // 确保单值或列表类型一致性
                return valuesByKey.size() == 1 ? valuesByKey.get(0) : valuesByKey;
            } else {
                return regexStr;
            }
        }
        return formData.get(regex);
    }*/

}
