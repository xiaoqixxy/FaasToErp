package com.alibaba.work.faas.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dingtalk")
public class DingConfig {

    private String appKey;
    private String appSecret;

    private Yida yida;

    // Getters and Setters
    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public Yida getYida() {
        return yida;
    }

    public void setYida(Yida yida) {
        this.yida = yida;
    }

    public static class Yida {
        private String appType;
        private String systemToken;
        private String userId;
        private String language;

        // Getters and Setters
        public String getAppType() {
            return appType;
        }

        public void setAppType(String appType) {
            this.appType = appType;
        }

        public String getSystemToken() {
            return systemToken;
        }

        public void setSystemToken(String systemToken) {
            this.systemToken = systemToken;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }
    }
}
