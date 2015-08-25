package com.owncloud.android;



public class StaticData {



    private static StaticData instance;

    public static StaticData GetInstance() {
        return instance;
    }


    static {
        instance = new StaticData();
    }

    protected String baseUrl = "192.168.100.105/owncloud";
    protected String joinUrl = "http://" + baseUrl;
    protected String managerId = "root";
    protected String managerPw = "131313";
    protected String httpBaseUrl = "http://" + managerId + ":" + managerPw + "@" + baseUrl;
    
    

    public String getManagerId() {
        return managerId;
    }



    public String getManagerPw() {
        return managerPw;
    }



    public String getHttpBaseUrl() {
        return httpBaseUrl;
    }



    public String getJoinUrl() {
        return joinUrl;
    }

        
 

}
