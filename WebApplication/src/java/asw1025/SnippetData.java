package asw1025;

import java.io.Serializable;

public class SnippetData implements Serializable{
    private String id;
    private String user;
    private String title;
    private String code;
    private String language;
    private String date;
   
    public SnippetData(String id,String user,String title, String code, String language, String date) {
        this.id=id;
        this.user=user;
        this.title=title;
        this.code=code;
        this.language=language;
        this.date=date;
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
