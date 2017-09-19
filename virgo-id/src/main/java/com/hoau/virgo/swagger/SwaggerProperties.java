package com.hoau.virgo.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 刘德云
 * @version V1.0
 * @title: SwaggerProperties
 * @package com.hoau.zodiac.springboot.autoconfig.swagger
 * @description ${TO_DO}
 * @date 2017/8/8
 */
@ConfigurationProperties(prefix = "virgo.swagger")
public class SwaggerProperties {

    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 版本
     */
    private String version;
    /**
     * 使用条款
     */
    private String termsOfServiceUrl;
    /**
     * 作者
     */
    private String contactName;
    /**
     * 网站链接文字
     */
    private String license;
    /**
     * 网站链接
     */
    private String licenseUrl;
    /**
     * 文档生成接口路径
     */
    private String path;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTermsOfServiceUrl() {
        return termsOfServiceUrl;
    }

    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
