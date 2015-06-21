/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package banca.controller.servicios;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author FabianBG.
 * Servel de logeo de aplicaciones java web.
 *
 */
@WebServlet(name = "ServletLog", urlPatterns = {"/login", "/logout"})
public class ServletLog extends HttpServlet {

	private static final long serialVersionUID = 1L;
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    	//switch para cada caso de servicos
    	
    	if ( request.getServletPath().equalsIgnoreCase("/login") ) {
            //processLogin(request, response);
        }
        else {
            request.getSession().invalidate();
            request.getSession().setAttribute("SessionUser",null);
            
            request.setAttribute("msg", "You have successfully logged out of system");
            System.out.print("\nYou have successfully logged out of system.");
            response.sendRedirect("/");
        }
        
        
        
    }
/*
 * {status: 'EA', valor: 'Error de acceso.'}
 * {status: 'OK', valor: 'Correo enviado'}
 * {status: 'OK', valor: 'Correo enviado'}
 * {status: 'OK', valor: {cuatna psadasdkas}}
 * 
 * 
 * */
    
    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("uid");
        String password = request.getParameter("pwd");
        
        //si primer logeo enviar token
        //segundo logeo verificar token
//        try {
//        	//logeo en la BD
//        	ManagerGestionApp m = new ManagerGestionApp();
//        	Usuario u = m.logUsuario(username, password);
//        	
//        	if(u != null)
//        	{
//        		System.out.print("Creando session.\n");
//        		request.getSession().setAttribute("SessionUser", u);
//        		System.out.print("Usuario logeado: "+u.getUsuNick()+" as "+u.getRol().getRolNombre());
//                System.out.print("\nRedireccionando a: ui/main.xhtml\n");
//            	response.getWriter().write("/ui/main.xhtml");
//        	}else
//        	{
//        		System.out.print("\nUsuario nulo.");
//        		response.getWriter().write("Error: Datos erroneos. Comprueba tus datos.");
//        	}        
//        } catch(Exception e){
//        	//e.printStackTrace();
//        	System.out.print("\nUsuario nulo.");
//        	response.getWriter().write("Error: Datos erroneos. Comprueba tus datos.");
//        }
//        finally
//        {
//        	response.getWriter().close();
//        }
        //
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
