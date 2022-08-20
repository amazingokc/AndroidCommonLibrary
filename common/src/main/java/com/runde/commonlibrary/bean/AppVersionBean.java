package com.runde.commonlibrary.bean;

import java.io.Serializable;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-02-15 下午 1:54
 * 文件描述：
 */

public class AppVersionBean implements Serializable {

    private LastestVersionBean lastestVersion;
    private String currentVersionAppKey;

    public LastestVersionBean getLastestVersion() {
        return lastestVersion;
    }

    public void setLastestVersion(LastestVersionBean lastestVersion) {
        this.lastestVersion = lastestVersion;
    }

    public String getCurrentVersionAppKey() {
        return currentVersionAppKey;
    }

    public void setCurrentVersionAppKey(String currentVersionAppKey) {
        this.currentVersionAppKey = currentVersionAppKey;
    }

    @Override
    public String toString() {
        return "Version{" +
                "lastestVersion=" + lastestVersion +
                ", currentVersionAppKey='" + currentVersionAppKey + '\'' +
                '}';
    }

    public static class LastestVersionBean {
        private int versionId;
        private String appName;
        private String appPlatform;
        private String appVersionName;
        private String appVersionCode;
        private String uploadLog;
        private String minimumVersion;
        private boolean allowOneLogin;
        private String appKey;
        private String downloadUrl;
        private String size;
        private String downPlatform;
        private boolean isForce;


        public boolean isForce() {
            return isForce;
        }

        public void setForce(boolean force) {
            isForce = force;
        }

        public int getVersionId() {
            return versionId;
        }

        public void setVersionId(int versionId) {
            this.versionId = versionId;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getAppPlatform() {
            return appPlatform;
        }

        public void setAppPlatform(String appPlatform) {
            this.appPlatform = appPlatform;
        }

        public String getAppVersionName() {
            return appVersionName;
        }

        public void setAppVersionName(String appVersionName) {
            this.appVersionName = appVersionName;
        }

        public String getAppVersionCode() {
            return appVersionCode;
        }

        public void setAppVersionCode(String appVersionCode) {
            this.appVersionCode = appVersionCode;
        }

        public String getUploadLog() {
            return uploadLog;
        }

        public void setUploadLog(String uploadLog) {
            this.uploadLog = uploadLog;
        }

        public String getMinimumVersion() {
            return minimumVersion;
        }

        public void setMinimumVersion(String minimumVersion) {
            this.minimumVersion = minimumVersion;
        }

        public boolean isAllowOneLogin() {
            return allowOneLogin;
        }

        public void setAllowOneLogin(boolean allowOneLogin) {
            this.allowOneLogin = allowOneLogin;
        }

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getDownPlatform() {
            return downPlatform;
        }

        public void setDownPlatform(String downPlatform) {
            this.downPlatform = downPlatform;
        }

        @Override
        public String toString() {
            return "LastestVersionBean{" +
                    "versionId=" + versionId +
                    ", appName='" + appName + '\'' +
                    ", appPlatform='" + appPlatform + '\'' +
                    ", appVersionName='" + appVersionName + '\'' +
                    ", appVersionCode='" + appVersionCode + '\'' +
                    ", uploadLog='" + uploadLog + '\'' +
                    ", minimumVersion='" + minimumVersion + '\'' +
                    ", allowOneLogin=" + allowOneLogin +
                    ", appKey='" + appKey + '\'' +
                    ", downloadUrl='" + downloadUrl + '\'' +
                    ", size='" + size + '\'' +
                    ", downPlatform='" + downPlatform + '\'' +
                    ", isForce=" + isForce +
                    '}';
        }
    }
}
