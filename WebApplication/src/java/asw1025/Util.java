/*    
    Esame ASW 2014-2015
    Autori: Luca Santandrea, Matteo Mariani, Antonio Leo Folliero, Francesco Degli Angeli
    Gruppo: 1025
*/
package asw1025;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;
import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServlet;


public class Util {
    // SEMAFORI MUTEX che permettono di accedere in modo esclusivo alle risorse condivise, come i file XML e la lista degli AsyncContext
    public static Semaphore mutexPersoneFile = new Semaphore(1);
    public static Semaphore mutexSnippetFile = new Semaphore(1);
    public static Semaphore mutexLikeFile = new Semaphore(1);
    public static Semaphore mutexAsyncContextList = new Semaphore(1);
    
    // Indirizzo web del sito
    //public static final String BASE = "http://isi-tomcat.csr.unibo.it:8080/~luca.santandrea6/";
    public static final String BASE = "http://localhost:8080/WebApplication/";
    
    /*
        Funzione che permette di ottenere PATH nel formato corretto in base al S.O. in uso
    */
    public static String getCorrectFilePath(HttpServlet servlet,String filename){
        String WebAppPath = servlet.getServletContext().getRealPath("");
        if (System.getProperty("os.name").startsWith("Windows")) {
            return WebAppPath + "\\WEB-INF\\xml\\"+filename;
        } else {
            return WebAppPath +  "/WEB-INF/xml/"+filename; 
        }
    }
    
    public static String convertDateToString(Date date){
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
    }
    
    public static Date convertStringtoDate (String date) throws ParseException{
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return dateFormat.parse(date);
    }
    
    public static Boolean compareTwoDateSameDay (Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }   
}
