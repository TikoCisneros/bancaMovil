package banca.controller.servicios;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import banca.model.dao.entities.Cliente;
import banca.model.manager.ManagerServicios;

/**
 * Servlet implementacion de servicios para Banca Movil
 */
@WebServlet(name = "ServletServicesMobil", urlPatterns = { "/loginCM", "/logoutCM",
		"/historialTCM", "/historialTcCM", "/transferenciaCM", "/sesionCM", "/cuentasCM"})
public class ServletServicesMobil extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String HOST = "http://bancawm-utnedu.rhcloud.com/BancaWM";
	private ManagerServicios mngServ;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletServicesMobil() {
		super();
		mngServ = new ManagerServicios();
	}

	private void addCorsHeader(HttpServletResponse response) { 	
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "POST, GET");
		response.addHeader("Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept");
		response.setContentType ("text/html;charset=utf-8");
	}

	/**
	 * RequestDispatcher view =
	 * request.getRequestDispatcher("html/mypage.html"); view.forward(request,
	 * response); Procesa la peticion HTTP de los metodos <code>GET</code> y
	 * <code>POST</code>.
	 * 
	 * @param request
	 *            servlet peticion
	 * @param response
	 *            servlet respuesta
	 * @throws ServletException
	 *             si existe un error de servlet-specific
	 * @throws IOException
	 *             si existe un error de I/O
	 */
	protected void processRequestGET(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		addCorsHeader(response);
		// switch para cada caso de servicos
		//String path = request.getServletPath();
		
	}

	/**
	 * RequestDispatcher view =
	 * request.getRequestDispatcher("html/mypage.html"); view.forward(request,
	 * response); Procesa la peticion HTTP de los metodos <code>GET</code> y
	 * <code>POST</code>.
	 * 
	 * @param request
	 *            servlet peticion
	 * @param response
	 *            servlet respuesta
	 * @throws ServletException
	 *             si existe un error de servlet-specific
	 * @throws IOException
	 *             si existe un error de I/O
	 */
	protected void processRequestPOST(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		addCorsHeader(response);
		JSONObject o = getBodyData(request);
		// switch para cada caso de servicos
		String path = request.getServletPath();
		if (path.equalsIgnoreCase("/loginCM")) {
			login(request, response, o);
		} else if (path.equalsIgnoreCase("/logoutCM")) {
			logout(request, response, o);
		} else if (path.equalsIgnoreCase("/transferenciaCM")) {
			transferencia(request, response, o);
		} else if (path.equalsIgnoreCase("/historialTCM")) {
			verHistorial(request, response, o);
		} else if (path.equalsIgnoreCase("/historialTcCM")) {
			verHistorialT(request, response, o);
		}
		else if (path.equalsIgnoreCase("/sesionCM")) {
			sesion(request, response, o);
		}else if (path.equalsIgnoreCase("/cuentasCM")) {// get
			verCuentas(request, response, o);
		}
	}

	/******************************************* METODOS *******************************************/

	/**
	 * Permite el logeo de usuario
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void login(HttpServletRequest request,
			HttpServletResponse response, JSONObject data) throws IOException,
			ServletException {
		String username = data.get("usr").toString();
		String password = data.get("pwd").toString();
		String ip = getClientIpAddr(request);
		System.out.println("Log user :" + username);
		try {
			Cliente u = mngServ.findClienteByAliasPassM(username, password);
			// Si la cuenta esta desactivada
			if (u.getCmBloqueo().equals(Cliente.CMOBIL_BLOQUEADA))
				throw new Exception("Cuenta DESACTIVADA");
			// insertar sesion
			mngServ.crearSesionM(u.getIdCli());
			// insertar conexion
			mngServ.insertarIPCM(u.getIdCli(), ip);
			response.getWriter().write(
					mngServ.jsonMensajes("OK", mngServ.usrLog(u)));
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(
					mngServ.jsonMensajes("EA", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
	}

	/**
	 * Permite el deslogeo de usuario
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void logout(HttpServletRequest request, HttpServletResponse response, JSONObject data)
			throws IOException, ServletException {
		try {
			Integer c = Integer.parseInt(data.get("idc").toString());
			mngServ.cerrarSesionM(c);
			response.getWriter().write(
					mngServ.jsonMensajes("OK", "Sesi&oacute;n finalizada correctamente."));
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(
					mngServ.jsonMensajes("EA", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
	}
	/**
	 * Permite ver la valide de lasesion
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void sesion(HttpServletRequest request, HttpServletResponse response, JSONObject data)
			throws IOException, ServletException {
		try {
			Integer c = Integer.parseInt(data.get("idc").toString());
			String p = data.get("p").toString();
			long s = mngServ.checkSesionM(c, p);
			if(s == 0)
				throw new Exception("No hay sesiones validas.");
			response.getWriter().write(
					mngServ.jsonMensajes("OK", s));
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(
					mngServ.jsonMensajes("EA", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
	}

	/**
	 * Devuelve el historial de Transferencias de un cliente
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void verHistorial(HttpServletRequest request,
			HttpServletResponse response, JSONObject data) throws IOException, ServletException {
		try {
			String p = data.get("p").toString();
			Integer c = Integer.parseInt(data.get("idc").toString());
			long s = mngServ.checkSesionM(c, p);
			if(s==0)
				throw new Exception("No hay una sesion activa");
			response.getWriter().write(
					mngServ.jsonMensajes("OK", mngServ.transfXcli(c)));
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(
					mngServ.jsonMensajes("ES", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
	}
	/**
	 * Devuelve el historial de transacciones de un cliente
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void verHistorialT(HttpServletRequest request,
			HttpServletResponse response, JSONObject data) throws IOException, ServletException {
		try {
			String p = data.get("p").toString();
			Integer c = Integer.parseInt(data.get("idc").toString());
			long s = mngServ.checkSesionM(c, p);
			if(s==0)
				throw new Exception("No hay una sesion activa");
			response.getWriter().write(
					mngServ.jsonMensajes("OK", mngServ.trancXcli(c)));
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(
					mngServ.jsonMensajes("EA", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
	}
	/**
	 * Devuelve las cuentas del cliente su nro saldo y tipo.
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@SuppressWarnings("unused")
	private void verCuentas(HttpServletRequest request,
			HttpServletResponse response, JSONObject data) throws IOException, ServletException {
		String PIN = request.getParameter("PIN");
		try {
			String p = data.get("p").toString();
			Integer c = Integer.parseInt(data.get("idc").toString());
			long s = mngServ.checkSesionM(c, p);
			if(s==0)
				throw new Exception("No hay una sesion activa");
			response.getWriter().write(
					mngServ.jsonMensajes("OK", mngServ.cuentasXCli(c)));
		} catch (Exception e) {
			response.getWriter().write(
					mngServ.jsonMensajes("EA", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
	}

	/**
	 * Realiza una transferencia
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void transferencia(HttpServletRequest request,
			HttpServletResponse response, JSONObject data) throws IOException,
			ServletException {
		try {
			String nroO = data.get("nroO").toString();
			String nroD = data.get("nroD").toString();
			String p = data.get("p").toString();
			Integer c = Integer.parseInt(data.get("idc").toString());
			long s = mngServ.checkSesionM(c, p);
			if(s==0)
				throw new Exception("No hay una sesion activa");
			if(nroO.equals(nroD))
				throw new Exception("La cuenta de origen y destino es la misma");
			BigDecimal monto = new BigDecimal(data.get("monto").toString());
			monto = monto.setScale(2, RoundingMode.DOWN);
			Cliente cliente = mngServ.findClienteById(c);
			String token = UUID.randomUUID().toString();
			if (monto.doubleValue() < 0)
				throw new Exception("El monto es 0 o menor que 0.");
			String idt = mngServ
					.crearTransferencia(monto, nroO, nroD, c, token);
			System.out.println("Identificador de transferencia: " + idt
					+ " token: " + token);
			if (idt == null || idt == "")
				throw new Exception("El codigo de transferencia es nulo.");
			Mailer.generateAndSendEmail(
					cliente.getCorreo(),
					"Validaci&oacute;n de transacci&oacute;n",
					"<h1>Validaci&oacute;n de transacci&oacute;n</h1>"
							+ "<p>Transferencia de "
							+ monto.doubleValue()
							+ "$."
							+ "<br>a la cuenta Nro. "
							+ nroD
							+ "</p>"
							+ "<p>Has clic al siguiente enlace para validar la transacci&oacute;n:"
							+ "<br> <a href='" + HOST
							+ "/bancaWeb/validateT?t=" + idt
							+ "&tk=" + token + "'>"
							+ "VALIDAR TRANSFERENCIA</a></p>");
			System.out.println("Correo enviado.");
			response.getWriter()
					.write(mngServ
							.jsonMensajes("OK",
									"Se ha enviado un correo a tu cuenta registrada para validar la transacci&oacute;n."));
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(
					mngServ.jsonMensajes("EA", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequestGET(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequestPOST(request, response);
	}

	/*-METODOS UTILITARIOS-*/
	private String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	private JSONObject getBodyData(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		JSONObject o = null;
		try {
			BufferedReader reader = request.getReader();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			JSONParser parser = new JSONParser();
			System.out.println(sb.toString());
			o = (JSONObject) parser.parse(sb.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}

}
