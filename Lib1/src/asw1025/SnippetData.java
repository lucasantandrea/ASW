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
    private String dateCreation;
    private String mod;
    private String codeMod;
    private String userMod;
    private String lastUserMod;
    private String dateLastModProp;
    private String dateLastMod;    
    
   
    public SnippetData(String id,String creator,String title, String code, String language, String dateCreation, String mod, String codeMod, String userMod, String lastUserMod, String dateLastModProp, String dateLastMod) {
        this.id=id;
        this.creator=creator;
        this.title=title;
        this.code=code;
        this.language=language;
        this.dateCreation=dateCreation;
        this.mod = mod;
        this.codeMod = codeMod;
        this.userMod = userMod;
        this.lastUserMod = lastUserMod;
        this.dateLastModProp = dateLastModProp;
        this.dateLastMod = dateLastMod;
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
    public String getDateCreation(){
        return dateCreation;
    }
    public void setDateCreation(String date){
        this.dateCreation=date;
    }
    public String getMod() {
        return mod;
    }
    public void setMod(String mod) {
        this.mod = mod;
    }
    public String getCodeMod() {
        return codeMod;
    }
    public void setCodeMod(String codeMod) {
        this.codeMod = codeMod;
    }
    public String getUserMod() {
        return userMod;
    }
    public void setUserMod(String mod) {
        this.userMod = mod;
    }
    public String getLastUserMod() {
        return lastUserMod;
    }
    public void setLastUserMod(String mod) {
        this.lastUserMod = mod;
    }
    public String getDateLastModProp() {
        return dateLastModProp;
    }
    public void setDateLastModProp(String dateLastModProp) {
        this.dateLastModProp = dateLastModProp;
    }
    public String getDateLastMod() {
        return dateLastMod;
    }
    public void setDateLastMod(String dateLastMod) {
        this.dateLastMod = dateLastMod;
    }
}
