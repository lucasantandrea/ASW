/*    
    Esame ASW 2014-2015
    Autori: Luca Santandrea, Matteo Mariani, Antonio Leo Folliero, Francesco Degli Angeli
    Matricola: 0900050785
    Gruppo: 1025
*/
package asw1025;

import java.io.Serializable;

public class SnippetData implements Serializable{
    private String id;
    private String creator;
    private String title;
    private String code;
    private String language;
    private String date_creation;
    private String mod;
    private String code_mod;
    private String user_mod;
    private String lastusermod;
    private String date_lastmodprop;
    private String date_lastmod;    
    
   
    public SnippetData(String id,String creator,String title, String code, String language, String date_creation, String mod, String code_mod, String user_mod, String lastusermod, String date_lastmodprop, String date_lastmod) {
        this.id=id;
        this.creator=creator;
        this.title=title;
        this.code=code;
        this.language=language;
        this.date_creation=date_creation;
        this.mod = mod;
        this.code_mod = code_mod;
        this.user_mod = user_mod;
        this.lastusermod = lastusermod;
        this.date_lastmodprop = date_lastmodprop;
        this.date_lastmod = date_lastmod;
    }
    
    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id=id;
    }
    public String getCreator(){
        return creator;
    }
    public void setCreator(String user){
        this.creator=user;
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
    public String getUser_Mod() {
        return user_mod;
    }
    public void setUser_Mod(String mod) {
        this.user_mod = mod;
    }
    public String getLastusermod() {
        return lastusermod;
    }
    public void setLastusermod(String mod) {
        this.lastusermod = mod;
    }
    public String getDate_lastmodprop() {
        return date_lastmodprop;
    }
    public void setDate_lastmodprop(String date_lastmodprop) {
        this.date_lastmodprop = date_lastmodprop;
    }
    public String getDate_lastmod() {
        return date_lastmod;
    }
    public void setDate_lastmod(String date_lastmod) {
        this.date_lastmod = date_lastmod;
    }
}
