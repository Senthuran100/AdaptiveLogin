package com.example.login.payload;

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


    public Long getCanvasFingerPrint() {
        return canvasFingerPrint;
    }

    public Long getBrowserAttribute() {
        return browserAttribute;
    }

    public Long getDeviceAttribute() {
        return deviceAttribute;
    }
}
