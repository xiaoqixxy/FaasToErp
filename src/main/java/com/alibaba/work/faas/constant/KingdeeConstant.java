package com.alibaba.work.faas.constant;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class KingdeeConstant {

    public static final Map<String, List<JSONObject>> KINGDEE_REGEX = new HashMap<>();

    static {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = null;
        try {
            resources = resolver.getResources("classpath:kingdee/*");
            // 遍历资源并获取输入流
            for (Resource resource : resources) {
                if (resource.isReadable()) { // 确保文件可读
                    System.out.println("文件名称: " + resource.getFilename());
                    try (InputStream inputStream = resource.getInputStream()) {
                        // 将 InputStream 转换为 String
                        StringBuilder stringBuilder = new StringBuilder();
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                stringBuilder.append(line);
                            }
                        }
                        // 使用 LinkedHashMap 解析 JSON 保持字段顺序
                        JSONObject jsonObject = JSONObject.parseObject(
                                stringBuilder.toString(),
                                Feature.OrderedField // 使用 LinkedHashMap 解析 JSON 保持顺序
                        );

                        // 处理 JSON 数据
                        String tableKey = jsonObject.getJSONObject("YiDa").getString("FormUuid");
                        if (KINGDEE_REGEX.containsKey(tableKey)) {
                            KINGDEE_REGEX.get(tableKey).add(jsonObject);
                        } else {
                            List<JSONObject> list = new ArrayList<>();
                            list.add(jsonObject);
                            KINGDEE_REGEX.put(tableKey, list);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
