/* 
    Esame: Applicazioni e Servizi Web 2013-14
    Autori: Fabrizio Masini, Luca Sangiorgi
    Matricola: 0000680876, 0000681281
*/
package asw1025;

import java.io.Serializable;

public class SnippetData implements Serializable{
    private String id;
    private String user;
    private String title;
    private String code;
    private String language;
    private String date;
    //private String like;
    //private Boolean likeIt;   // Variabile usata per indicare che all'utente piace questa poesia
    
    public SnippetData(String id,String user,String title, String code, String language, String date) {
        this.id=id;
        this.user=user;
        this.title=title;
        this.code=code;
        this.language=language;
        this.date=date;
        //this.like=like;
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
    public String getDate(){
        return date;
    }
    
    public void setDate(String date){
        this.date=date;
    }
    
}
