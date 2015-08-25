package com.owncloud.android;



public class UserData {



    private static UserData instance;

    public static UserData GetInstance() {
        return instance;
    }


    static {
        instance = new UserData();
    }

    
        public String id;
        public String displayname;
        public String email;
        public String usedSpace;
        public String totalSpace;

        
        public void resetData(){
            id=null;
            displayname=null;
            email=null;
            usedSpace=null;
            totalSpace=null;
        }


        public void setId(String id) {
            this.id = id;
        }
        public void setDisplayname(String displayname) {
            this.displayname = displayname;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public void setUsedSpace(String usedSpace) {
            this.usedSpace = usedSpace;
        }
        public void setTotalSpace(String totalSpace) {
            this.totalSpace = totalSpace;
        }
        
        
        
        
        public String getId() {
            return id;
        }
        public String getDisplayname() {
            return displayname;
        }
        public String getEmail() {
            return email;
        }
        public String getUsedSpace() {
            return usedSpace;
        }
        public String getTotalSpace() {
            return totalSpace;
        }
 

}
