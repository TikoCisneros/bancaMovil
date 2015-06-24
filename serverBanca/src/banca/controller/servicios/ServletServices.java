package banca.controller.servicios;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import banca.model.dao.entities.Cliente;
import banca.model.manager.ManagerServicios;

/**
 * Servlet implementacion de servicios para Banca Movil
 */
@WebServlet(name="ServletServices", urlPatterns={"/login","/logout","/pass","/mail","/lista"})
public class ServletServices extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private ManagerServicios mngServ;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletServices() {
        super();
        mngServ = new ManagerServicios();
    }
    
    /**
     * Procesa la peticion HTTP de los metodos
     * <code>GET</code> y
     * <code>POST</code>.
     *
     * @param request servlet peticion
     * @param response servlet respuesta
     * @throws ServletException si existe un error de servlet-specific
     * @throws IOException si existe un error de I/O
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
    		throws ServletException, IOException {
    		
    	//switch para cada caso de servicos
    	String path = request.getServletPath();
    	if(path.equalsIgnoreCase("/login")){
    		login(request, response);
    	}else if(path.equalsIgnoreCase("/logout")){
    		logout(request, response);
    	}else if(path.equalsIgnoreCase("/pass")){
    		setPass(request, response);
    	}else if(path.equalsIgnoreCase("/mail")){
    		setMail(request, response);
    	}else if(path.equalsIgnoreCase("/lista")){
    		verHistorial(request, response);
    	}
    }
    
    /*******************************************METODOS*******************************************/
    
    /**
     * Permite el logeo de usuario
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
    	 String username = request.getParameter("usr");
         String password = request.getParameter("pwd");
         String ip = request.getParameter("ipc");
         
         try {
			Cliente u = mngServ.findClienteByAliasPass(username, password);
			//primera vez que se logea
			if(u.getFechaUltmCon()==null){
				//enviar token al correo
				response.getWriter().write(mngServ.jsonMensajes("PA", "Se ha enviado el token a su correo"));
			}else{
				//insertar conexion
				mngServ.insertIpDateConn(u.getIdCli(), ip);
				//crear session
				request.getSession().setAttribute("SessionUser", u);
				response.getWriter().write(mngServ.jsonMensajes("OK", mngServ.usrLog(u)));
			}
		} catch (Exception e) {
			response.getWriter().write(mngServ.jsonMensajes("EA", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
    }
    
    /**
     * Permite el deslogeo de usuario
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
    	request.getSession().invalidate();
        request.getSession().setAttribute("SessionUser",null);
        response.getWriter().write(mngServ.jsonMensajes("OK", "Sesion finalizada"));
        response.getWriter().close();
    }
    
    /**
     * Permite el cambio de contraseña
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void setPass(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
    	 String id = request.getParameter("id");
         String pass = request.getParameter("pwd");
         String npass = request.getParameter("npd");
         String cpass = request.getParameter("cpd"); 	
    	try {
			mngServ.cambiarPass(Integer.parseInt(id), pass, npass, cpass);
			response.getWriter().write(mngServ.jsonMensajes("OK", "Cambio de password correcto"));
    	} catch (Exception e) {
			response.getWriter().write(mngServ.jsonMensajes("EA", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
    }
    
    /**
     * Permite el cambio de correo
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void setMail(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
   	 	String id = request.getParameter("id");
        String mail = request.getParameter("mail"); 	
	   	try {
			mngServ.cambiarMail(Integer.parseInt(id), mail);
			response.getWriter().write(mngServ.jsonMensajes("OK", "Cambio de correo correcto"));
	   	} catch (Exception e) {
			response.getWriter().write(mngServ.jsonMensajes("EA", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
    }
    
    /**
     * Devuelve el historial de Transferencias de un cliente
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void verHistorial(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
    	String id = request.getParameter("id");
    	try {
    		response.getWriter().write(mngServ.jsonMensajes("OK", mngServ.transfXcli(Integer.parseInt(id))));
		} catch (Exception e) {
			response.getWriter().write(mngServ.jsonMensajes("EA", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

}
