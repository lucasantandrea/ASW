package asw1025;

import java.io.Serializable;

public class SnippetData implements Serializable{
    private String id;
    private String user;
    private String title;
    private String code;
    private String language;
    private String date_creation;
    private String mod;
    private String code_mod;
    private String date_lasmodprop;
    private String date_lasmod;
    private String user_mod;
    
    
    
   
    public SnippetData(String id,String user,String title, String code, String language, String date_creation, String mod, String code_mod, String date_lasmodprop, String date_lasmod, String user_mod) {
        this.id=id;
        this.user=user;
        this.title=title;
        this.code=code;
        this.language=language;
        this.date_creation=date_creation;
        this.mod = mod;
        this.code_mod = code_mod;
        this.date_lasmodprop = date_lasmodprop;
        this.date_lasmod = date_lasmod;
        this.user_mod = user_mod;
    }
    
    public String getId(){
        return id;
    }
    
    public void setId(String id){
        this.id=id;
    }
    
    public String getUser(){
        return user;
    }
    
    public void setUser(String user){
        this.user=user;
    }
    public String getTitle(){
        return title;
    }
    
    public void setTitle(String title){
        this.title=title;
    }
    public String getCode(){
        return code;
    }
    
    public void setCode(String testo){
        this.code=testo;
    }
    
    public String getLanguage(){
        return language;
    }
    
    public void setLanguage(String language){
        this.language=language;
    }
    public String getDate_creation(){
        return date_creation;
    }
    
    public void setDate_creation(String date){
        this.date_creation=date;
    }
    
    public String getMod() {
        return mod;
    }

    public void setMod(String mod) {
        this.mod = mod;
    }

    public String getCode_mod() {
        return code_mod;
    }

    public void setCode_mod(String code_mod) {
        this.code_mod = code_mod;
    }

    public String getDate_lasmodprop() {
        return date_lasmodprop;
    }

    public void setDate_lasmodprop(String date_lasmodprop) {
        this.date_lasmodprop = date_lasmodprop;
    }

    public String getDate_lasmod() {
        return date_lasmod;
    }

    public void setDate_lasmod(String date_lasmod) {
        this.date_lasmod = date_lasmod;
    }

    public String getUser_mod() {
        return user_mod;
    }

    public void setUser_mod(String user_mod) {
        this.user_mod = user_mod;
    }

      
}
