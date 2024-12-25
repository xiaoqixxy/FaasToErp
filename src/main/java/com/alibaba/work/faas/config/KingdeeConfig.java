package com.alibaba.work.faas.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "kingdee")
public class KingdeeConfig {

    private List<KingdeeAccount> kingdee;

    public List<KingdeeAccount> getKingdee() {
        return kingdee;
    }

    public void setKingdee(List<KingdeeAccount> kingdee) {
        this.kingdee = kingdee;
    }

    // 获取默认配置
    public KingdeeAccount getDefaultConfig() {
        return kingdee.stream()
                .filter(KingdeeAccount::isDefault)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("未找到默认的金蝶配置"));
    }

    // 根据名称获取配置
    public KingdeeAccount getConfigByName(String name) {
        return kingdee.stream()
                .filter(account -> account.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("未找到指定的金蝶配置"));
    }

    // 内部类：KingdeeAccount
    public static class KingdeeAccount {
        private String name;
        private String url;
        private String accountId;
        private String username;
        private String password;
        private String appId;
        private String appKey;
        private boolean isDefault;

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public boolean isDefault() {
            return isDefault;
        }

        public boolean getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(boolean isDefault) {
            this.isDefault = isDefault;
        }

        public void setDefault(boolean isDefault) {
            this.isDefault = isDefault;
        }
    }
}
