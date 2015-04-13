/*    
 Esame ASW 2014-2015
 Autori: Luca Santandrea, Matteo Mariani, Antonio Leo Folliero, Francesco Degli Angeli
 Matricola: 0900050785
 Gruppo: 1025
 */
package asw1025;

import javax.servlet.*;
import java.io.*;

public class AsyncAdapter implements AsyncListener{
    @Override
    public void onComplete(AsyncEvent event) throws IOException {}
    @Override
    public void onError(AsyncEvent event) throws IOException {}
    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {}
    @Override
    public void onTimeout(AsyncEvent event) throws IOException {}
}
