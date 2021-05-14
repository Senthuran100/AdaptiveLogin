package com.example.login.model;

public class browserObject {
    private String UserAgent;
    private String Plugins;
    private String TimeZone;
    private String Fonts;
    private String MimeTypes;
    private String CPU;
    private String Device;
    private String browser;
    private String SoftwareVersion;
    private String Resolution;
    private String ColorDepth;
    private String browserVersion;
    private String OS;
    private String OS_version;
    private String Engine;
    private String EngineVersion;
    private Long canvasFingerPrint;
    private Long browserAttribute;
    private Long deviceAttribute;

    public String getUserAgent() {
        return UserAgent;
    }

    public void setUserAgent(String userAgent) {
        UserAgent = userAgent;
    }

    public String getPlugins() {
        return Plugins;
    }

    public void setPlugins(String plugins) {
        Plugins = plugins;
    }

    public String getTimeZone() {
        return TimeZone;
    }

    public void setTimeZone(String timeZone) {
        TimeZone = timeZone;
    }

    public String getFonts() {
        return Fonts;
    }

    public void setFonts(String fonts) {
        Fonts = fonts;
    }

    public String getMimeTypes() {
        return MimeTypes;
    }

    public void setMimeTypes(String mimeTypes) {
        MimeTypes = mimeTypes;
    }

    public String getCPU() {
        return CPU;
    }

    public void setCPU(String CPU) {
        this.CPU = CPU;
    }

    public String getDevice() {
        return Device;
    }

    public void setDevice(String device) {
        Device = device;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getSoftwareVersion() {
        return SoftwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        SoftwareVersion = softwareVersion;
    }

    public String getResolution() {
        return Resolution;
    }

    public void setResolution(String resolution) {
        Resolution = resolution;
    }

    public String getColorDepth() {
        return ColorDepth;
    }

    public void setColorDepth(String colorDepth) {
        ColorDepth = colorDepth;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public String getOS() {
        return OS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }

    public String getOS_version() {
        return OS_version;
    }

    public void setOS_version(String OS_version) {
        this.OS_version = OS_version;
    }

    public String getEngine() {
        return Engine;
    }

    public void setEngine(String engine) {
        Engine = engine;
    }

    public String getEngineVersion() {
        return EngineVersion;
    }

    public void setEngineVersion(String engineVersion) {
        EngineVersion = engineVersion;
    }

    public Long getCanvasFingerPrint() {
        return canvasFingerPrint;
    }

    public void setCanvasFingerPrint(Long canvasFingerPrint) {
        this.canvasFingerPrint = canvasFingerPrint;
    }

    public Long getBrowserAttribute() {
        return browserAttribute;
    }

    public void setBrowserAttribute(Long browserAttribute) {
        this.browserAttribute = browserAttribute;
    }

    public Long getDeviceAttribute() {
        return deviceAttribute;
    }

    public void setDeviceAttribute(Long deviceAttribute) {
        this.deviceAttribute = deviceAttribute;
    }
}
