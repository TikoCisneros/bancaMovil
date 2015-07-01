package banca.controller.servicios;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
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
@WebServlet(name = "ServletServices", urlPatterns = { "/login", "/logout",
		"/pass", "/mail", "/lista", "/poli", "/dismov", "/cuentas",
		"/transferencia", "/VTransferencia" })
public class ServletServices extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String HOST = "localhost:8080";
	private ManagerServicios mngServ;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletServices() {
		super();
		mngServ = new ManagerServicios();
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
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		 JSONObject o = getBodyData(request);
		 System.out.println(o.toJSONString());
		// switch para cada caso de servicos
		String path = request.getServletPath();
		if (path.equalsIgnoreCase("/login")) {
			login(request, response, o);
		} else if (path.equalsIgnoreCase("/logout")) {// get
			logout(request, response);
		} else if (path.equalsIgnoreCase("/pass")) {
			setPass(request, response);
		} else if (path.equalsIgnoreCase("/mail")) {
			setMail(request, response);
		} else if (path.equalsIgnoreCase("/lista")) {// get
			verHistorial(request, response);
		} else if (path.equalsIgnoreCase("/poli")) {
			aceptaPoliticas(request, response);
		} else if (path.equalsIgnoreCase("/dismov")) {
			disableCliMovil(request, response);
		} else if (path.equalsIgnoreCase("/cuentas")) {// get
			verCuentas(request, response);
		} else if (path.equalsIgnoreCase("/transferencia")) {
			transferencia(request, response);
		} else if (path.equalsIgnoreCase("/VTransferencia")) {
			validarTransferencia(request, response);
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
	private void login(HttpServletRequest request, HttpServletResponse response, JSONObject data)
			throws IOException, ServletException {
		String username = data.get("usr").toString();
		String password = data.get("pwd").toString();
		String ip = getClientIpAddr(request);
		System.out.println("Log user :" +username);
		try {
			Cliente u = mngServ.findClienteByAliasPass(username, password);
			// primera vez que se logea
			String ok = "SS";
			// primera vez que se logea o no acepta politicas
			if (u.getFechaUltmCon() == null || u.getAceptaPoliticas() == null
					|| u.getAceptaPoliticas().isEmpty()) {
				ok = "PS"; // para ir a aceptar politicas
			}
			// insertar conexion
			mngServ.insertIpDateConn(u.getIdCli(), ip);
			// crear session
			// poner solo id o dataos esenciales si pones toda la entidad mucha
			// carga
			// xq ahi estan las cuentas en dentro de cuentas las trnasferencias
			// request.getSession().setAttribute("SessionUser", u);

			request.getSession().setAttribute("SessionUser", u.getIdCli());
			response.getWriter().write(
					mngServ.jsonMensajes(ok, mngServ.usrLog(u)));
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
	private void enviarPIN(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
	}

	/**
	 * Permite el deslogeo de usuario
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void logout(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		request.getSession().invalidate();
		request.getSession().setAttribute("SessionUser", null);
		response.getWriter().write(
				mngServ.jsonMensajes("OK", "Sesion finalizada"));
		response.getWriter().close();
	}

	/**
	 * Permite el cambio de contraseña
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void setPass(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String pass = request.getParameter("pwd");
		String npass = request.getParameter("npd");
		String cpass = request.getParameter("cpd");
		try {
			Integer c = (Integer) request.getSession().getAttribute(
					"SessionUser");
			mngServ.cambiarPass(c, pass, npass, cpass);
			response.getWriter().write(
					mngServ.jsonMensajes("OK", "Cambio de password correcto"));
		} catch (Exception e) {
			response.getWriter().write(
					mngServ.jsonMensajes("EA", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
	}

	/**
	 * Permite el cambio de correo
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void setMail(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String mail = request.getParameter("mail");
		try {
			Integer c = (Integer) request.getSession().getAttribute(
					"SessionUser");
			mngServ.cambiarMail(c, mail);
			response.getWriter().write(
					mngServ.jsonMensajes("OK", "Cambio de correo correcto"));
		} catch (Exception e) {
			response.getWriter().write(
					mngServ.jsonMensajes("EA", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
	}
	/**
	 * Permite el cambio de PIN
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void setPIN(HttpServletRequest request,
			HttpServletResponse response, JSONObject data) throws IOException, ServletException {
		try {
			String PIN = data.get("PIN").toString();
			String nPIN = data.get("nPIN").toString();
			Integer c = (Integer) request.getSession().getAttribute(
					"SessionUser");
			mngServ.cambiarPIN(c, PIN, nPIN);;
			response.getWriter().write(
					mngServ.jsonMensajes("OK", "Cambio de PIN correcto"));
		} catch (Exception e) {
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
			HttpServletResponse response) throws IOException, ServletException {
		String PIN = request.getParameter("PIN");
		try {
			if (PIN == null || PIN.isEmpty())
				throw new Exception("El PIN enviado es vacio o nulo.");
			Integer c = (Integer) request.getSession().getAttribute(
					"SessionUser");
			response.getWriter().write(
					mngServ.jsonMensajes("OK", mngServ.transfXcli(c)));
		} catch (Exception e) {
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
	private void verCuentas(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String PIN = request.getParameter("PIN");

		try {
			Integer c = (Integer) request.getSession().getAttribute(
					"SessionUser");
			if (PIN == null || PIN.isEmpty())
				throw new Exception("El PIN enviado es vacio o nulo.");
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
			HttpServletResponse response) throws IOException, ServletException {
		try {
			String PIN = request.getParameter("PIN");
			String nroO = request.getParameter("nroO");
			String nroD = request.getParameter("nroD");
			BigDecimal monto = new BigDecimal(request.getParameter("monto"));
			Integer c = (Integer) request.getSession().getAttribute(
					"SessionUser");
			Cliente cliente = mngServ.findClienteById(c);
			String token = UUID.randomUUID().toString();
			if (PIN == null || PIN.isEmpty())
				throw new Exception("El PIN enviado es vacio o nulo.");
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
					"Validación de transacción",
					"<h1>Validación de transacción</h1>"
							+ "<p>Transferencia de "
							+ monto.doubleValue()
							+ "$."
							+ "<br>a la cuenta Nro. "
							+ nroD
							+ "</p>"
							+ "<p>Has clic al siguiente enlace para validar la transacción:"
							+ "<br> <a href='" + HOST + "/VTransferencia?t="
							+ idt + "&tk=" + token + "'>"
							+ "</a>VALIDAR TRANSFERENCIA</p>");
			response.getWriter()
					.write(mngServ
							.jsonMensajes("OK",
									"Se ha enviado un correo a tu cuenta registrada para validar la transacción."));
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
	private void validarTransferencia(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try {
			String tk = request.getParameter("tk");
			String t = request.getParameter("t");
			mngServ.validarTransferencia(Integer.parseInt(t), tk);
			response.getWriter().write(
					mngServ.jsonMensajes("OK",
							"La transacción se completado satisfactoriamente"));
		} catch (Exception e) {
			response.getWriter().write(
					mngServ.jsonMensajes("EA", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
	}

	/**
	 * Verifica si se acepta politicas de uso de la aplicacion
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void aceptaPoliticas(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String acepta = request.getParameter("ap");// 0 (no), 1 (si)
		try {
			Integer c = (Integer) request.getSession().getAttribute(
					"SessionUser");
			if (acepta.equals("0")) {
				response.getWriter().write(
						mngServ.jsonMensajes("EA", "No acepta politicas"));
			} else {
				mngServ.aceptarPoliticas(c);
				response.getWriter().write(
						mngServ.jsonMensajes("OK", "Politicas aceltadas"));
			}
		} catch (Exception e) {
			response.getWriter().write(
					mngServ.jsonMensajes("EA", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
	}

	/**
	 * Desactiva el uso de la App Movil
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void disableCliMovil(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String motivo = request.getParameter("mt");
		try {
			Integer c = (Integer) request.getSession().getAttribute(
					"SessionUser");
			mngServ.disableMovilApp(c, motivo);
			response.getWriter().write(
					mngServ.jsonMensajes("OK",
							"Se ha desactivado correctamente"));
		} catch (Exception e) {
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
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
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
