package org.apache.jsp.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.sun.xml.bind.Util;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.*;

public final class registration_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList<String>(3);
    _jspx_dependants.add("/jsp/../WEB-INF/jspf/navbar.jspf");
    _jspx_dependants.add("/jsp/../WEB-INF/jspf/sidebar.jspf");
    _jspx_dependants.add("/jsp/../WEB-INF/jspf/footer.jspf");
  }

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<!DOCTYPE html>\r\n");
      out.write("\r\n");
      out.write("    \r\n");
      out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n");
      out.write("        <title>Registrazione</title>\r\n");
      out.write("    \r\n");
      out.write(" \r\n");
      out.write("    <body>\r\n");
      out.write("        <div id=\"container\">\r\n");
      out.write("            <div id=\"navbar\">\r\n");
      out.write("                ");
      out.write("\n");
      out.write("\n");
      out.write("<h2>NAVBAR</h2>\n");
      out.write("\r\n");
      out.write("            </div>\r\n");
      out.write("            <div id=\"content\">\r\n");
      out.write("                <div class=\"miniInsideContent\">\r\n");
      out.write("                    <form name=\"registrationForm\" method=\"POST\" class=\"contentBox\">\r\n");
      out.write("                        \r\n");
      out.write("                        First name: <input type=\"text\" name=\"firstname\" value=");
if(request.getAttribute("nome")!=null){
      out.write(' ');
      out.print( request.getAttribute("nome"));
} else {
      out.write('"');
      out.write('"');
}
      out.write(" ><br> \r\n");
      out.write("                        Last name: <input type=\"text\" name=\"lastname\" value=");
if(request.getAttribute("cognome")!=null){
      out.write(' ');
      out.print( request.getAttribute("cognome"));
} else {
      out.write('"');
      out.write('"');
}
      out.write(" ><br>\r\n");
      out.write("                        User name <input type=\"text\" name=\"user\">\r\n");
      out.write("                        ");

                        // Controllo se l'username immesso è univoco
                        if(request.getAttribute("errorUserRegistration")!=null){ 
      out.write("\r\n");
      out.write("                            <label id=\"errorRegistration\">Username \"");
      out.print( request.getAttribute("errorUserRegistration") );
      out.write("\" gia' utilizzato!</label>\r\n");
      out.write("                        ");
 } 
      out.write("<br>\r\n");
      out.write("                        Password: <input type=\"password\" name=\"pass\"><br>\r\n");
      out.write("                        <input type=\"submit\" value=\"Conferma\">\r\n");
      out.write("                    </form>\r\n");
      out.write("                        \r\n");
      out.write("                </div>\r\n");
      out.write("            </div>            \r\n");
      out.write("            <div id=\"sidebar\">\r\n");
      out.write("                ");
      out.write("\n");
      out.write("\n");
      out.write("<h2>SIDEBAR</h2>\n");
      out.write("\r\n");
      out.write("            </div>            \r\n");
      out.write("            <div id=\"footer\">\r\n");
      out.write("                ");
      out.write("\n");
      out.write("\n");
      out.write("<h2>FOOTER</h2>\n");
      out.write("\r\n");
      out.write("            </div>\r\n");
      out.write("        </div>\r\n");
      out.write("    </body>\r\n");
      out.write("    \r\n");
      out.write("\r\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
