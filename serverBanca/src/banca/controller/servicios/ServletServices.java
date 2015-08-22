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
import banca.model.dao.entities.Cuenta;
import banca.model.manager.ManagerServicios;

/**
 * Servlet implementacion de servicios para Banca Movil
 */
@WebServlet(name = "ServletServices", urlPatterns = { "/login", "/logout",
		"/pass", "/mail", "/historialT", "/historialTc", "/poli", "/dismov", "/cuentas",
		"/transferencia", "/VTransferencia", "/sesion","/regusr", "/vc", "/VRegistro",
		"/Vmail", "/nCM", "/pwdCM", "/rpinCM", "/aCM", "/dCM" })
public class ServletServices extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String HOST = "http://localhost:8080/serverBanca";
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
	protected void processRequestGET(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException 
	{
		// switch para cada caso de servicos
		String path = request.getServletPath();
		if (path.equalsIgnoreCase("/sesion")) {
			getSesion(request, response);
		}else if (path.equalsIgnoreCase("/logout")) {// get
			logout(request, response);
		}else if (path.equalsIgnoreCase("/historialT")) {// get
			verHistorial(request, response);
		}else if (path.equalsIgnoreCase("/historialTc")) {// get
			verHistorialT(request, response);
		}else if (path.equalsIgnoreCase("/cuentas")) {// get
			verCuentas(request, response);
		}else if (path.equalsIgnoreCase("/VTransferencia")) {
			validarTransferencia(request, response);
		}else if (path.equalsIgnoreCase("/VRegistro")) {
			validarRegistro(request, response);
		}else if (path.equalsIgnoreCase("/Vmail")) {
			validarSetMail(request, response);
		}else if (path.equalsIgnoreCase("/rpinCM")) {
			resetPinMovil(request, response);
		}else if (path.equalsIgnoreCase("/aCM")) {
			activarMovil(request, response);
		}else if (path.equalsIgnoreCase("/dCM")) {
			desactivarMovil(request, response);
		}
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

		 JSONObject o = getBodyData(request);
		 System.out.println(o.toJSONString());
		// switch para cada caso de servicos
		String path = request.getServletPath();
		if (path.equalsIgnoreCase("/login")) {
			login(request, response, o);
		} else if (path.equalsIgnoreCase("/pass")) {
			setPass(request, response, o);
		} else if (path.equalsIgnoreCase("/mail")) {
			setMail(request, response, o);
		} else if (path.equalsIgnoreCase("/poli")) {
			aceptaPoliticas(request, response);
		} else if (path.equalsIgnoreCase("/dismov")) {
			disableCliMovil(request, response, o);
		} else if (path.equalsIgnoreCase("/transferencia")) {
			transferencia(request, response,o);
		} else if (path.equalsIgnoreCase("/regusr")){
			registro(request, response, o);
		}else if(path.equalsIgnoreCase("/vc")){
			validarCuenta(request, response, o);
		}else if(path.equals("/nCM")){
			nuevoCliMovil(request, response, o);
		}else if(path.equals("/pwdCM")){
			cambioPwdMovil(request, response, o);
		}
	}
	

	/******************************************* METODOS *******************************************/
	
	/**
	 * Permite el registro de usuarios
	 * @param request
	 * @param response
	 * @param data
	 * @throws IOException
	 * @throws ServletException
	 */
	public void registro(HttpServletRequest request, HttpServletResponse response, JSONObject data)
			throws IOException, ServletException {
		String ci = data.get("ced").toString();
		String mail = data.get("mal").toString();
		String alias = data.get("als").toString();
		try {
			mngServ.registroWeb(HOST, ci, mail, alias);
			response.getWriter().write(
					mngServ.jsonMensajes("OK", "Registro correcto!, se envio un correo para validar su cuenta"));
		} catch (Exception e) {
			response.getWriter().write(
					mngServ.jsonMensajes("EA", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
	}
	
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
			//Si la cuenta no ha sido verificada
			if( u.getBloqueda().equals(Cliente.NO_VERIFICADA)){
				throw new Exception("Cuenta no verificada, revise su correo");
			}
			//primera vez que se logea o no cambia pass
			String ok = "SL";
			if (u.getFechaUltmCon() == null || u.getBloqueda().equals(Cliente.SIN_CAMBIO_DATOS)) {
				ok = "FL"; // para pedir que cambiar pass
			}
			// insertar conexion
			mngServ.insertIpDateConn(u.getIdCli(), ip);
			// crear session
			// poner solo id o datos esenciales si pones toda la entidad mucha carga
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
	 * Devuelve el historial de transacciones de un cliente
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void verHistorialT(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try {
			Integer c = (Integer) request.getSession().getAttribute(
					"SessionUser");
			response.getWriter().write(
					mngServ.jsonMensajes("OK", mngServ.trancXcli(c)));
		} catch (Exception e) {
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
	@SuppressWarnings("unused")
	private void enviarPIN(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
	}
	/**
	 * Envia los daots del usuario en sesion
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void getSesion(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		Integer c = (Integer) request.getSession().getAttribute(
				"SessionUser");
		try {
			Cliente u = mngServ.findClienteById(c);
			response.getWriter().write(
					mngServ.jsonMensajes("OK", mngServ.usrLog(u)));
		} catch (Exception e) {
			response.getWriter().write(
					mngServ.jsonMensajes("EA", "No has accedido al sistema."));
			e.printStackTrace();
		}
		finally
		{
			response.getWriter().close();
		}
	}
	/**
	 * Validar cuenta
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void validarCuenta(HttpServletRequest request, HttpServletResponse response,
			JSONObject data)
			throws IOException, ServletException {
		String nro = data.get("nro").toString();
		int c = (Integer) request.getSession().getAttribute(
				"SessionUser");
		try {
			if(c == 0)
				mngServ.jsonMensajes("EA", "No has accedido al sistema.");
			else
			{
				Cuenta cn = mngServ.findCuentaByNro(nro);
				if(cn != null && cn.getNroCuenta().length() > 4)
					response.getWriter().write(
						mngServ.jsonMensajes("OK", "La cuenta existe."));
				else
					mngServ.jsonMensajes("EA", "No existe.");
			}
			
		} catch (Exception e) {
			response.getWriter().write(
					mngServ.jsonMensajes("EA", "No has accedido al sistema."));
			e.printStackTrace();
		}
		finally
		{
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
			HttpServletResponse response, JSONObject data) throws IOException, ServletException {
		String pass = data.get("pwd").toString();
		String npass = data.get("npd").toString();
		String cpass = data.get("cpd").toString();
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
			HttpServletResponse response, JSONObject data) throws IOException, ServletException {
		String mail = data.get("mail").toString();
		try {
			Integer idusr = (Integer) request.getSession().getAttribute(
					"SessionUser");
			Mailer.generateAndSendEmail(
					mail,
					"Validación de Correo Electrónico",
					"<h1>Validación de Correo Electrónico</h1>"
							+ "<p>Has clic al siguiente enlace para validar su correo:"
							+ "<br> <a href='" + HOST + "/bancaWeb/index.html#/validateM?id="
							+ idusr + "&ml=" + mail + "'>"
							+ "VALIDAR NUEVO CORREO</a></p>");
			System.out.println("Correo enviado.");
			response.getWriter().write(
					mngServ.jsonMensajes("OK", "Se ha enviado un mensaje al nuevo correo para validarlo."));
		} catch (Exception e) {
			response.getWriter().write(
					mngServ.jsonMensajes("EA", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
	}
	
	
	private void validarSetMail(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try {
			Integer usr = Integer.parseInt(request.getParameter("id"));
			String mail = request.getParameter("ml");
			mngServ.cambiarMail(usr, mail);
			response.getWriter().write(
					mngServ.jsonMensajes("OK", "Se validó y cambió su correo correctamente"));
		}  catch (Exception e) {
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
	@SuppressWarnings("unused")
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
		try {
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
	@SuppressWarnings("unused")
	private void verCuentas(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String PIN = request.getParameter("PIN");
		try {
			Integer c = (Integer) request.getSession().getAttribute(
					"SessionUser");
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
			HttpServletResponse response, JSONObject data) throws IOException, ServletException {
		try {
			String nroO =  data.get("nroO").toString();
			String nroD = data.get("nroD").toString();
			if(nroO.equals(nroD))
				throw new Exception("La cuenta de origen y destino es la misma");
			BigDecimal monto = new BigDecimal(data.get("monto").toString());
			Integer c = (Integer) request.getSession().getAttribute(
					"SessionUser");
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
					"Validación de transacción",
					"<h1>Validación de transacción</h1>"
							+ "<p>Transferencia de "
							+ monto.doubleValue()
							+ "$."
							+ "<br>a la cuenta Nro. "
							+ nroD
							+ "</p>"
							+ "<p>Has clic al siguiente enlace para validar la transacción:"
							+ "<br> <a href='" + HOST + "/bancaWeb/index.html#/validateT?t="
							+ idt + "&tk=" + token + "'>"
							+ "VALIDAR TRANSFERENCIA</a></p>");
			System.out.println("Correo enviado.");
			response.getWriter()
					.write(mngServ
							.jsonMensajes("OK",
									"Se ha enviado un correo a tu cuenta registrada para validar la transacción."));
		} catch (Exception e) {
			e.printStackTrace();
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
			mngServ.validarTransferencia(Long.parseLong(t), tk);
			response.getWriter().write(
					mngServ.jsonMensajes("OK",
							"Se ha validado la transferencia."));
		} catch (Exception e) {
			response.getWriter().write(
					mngServ.jsonMensajes("EA", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
	}
	
	/**
	 * Valida el registro de usuario
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void validarRegistro(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try {
			String token = request.getParameter("tk");
			String usr = request.getParameter("id");
			mngServ.validarRegistroWeb(Integer.parseInt(usr), token);
			response.getWriter().write(
					mngServ.jsonMensajes("OK",
							"Se ha validado su cuenta."));
		}  catch (Exception e) {
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
			HttpServletResponse response, JSONObject data) throws IOException, ServletException {
		String motivo = data.get("mt").toString();
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
	 * Permite crear una nueva cuenta móvil
	 * @param request
	 * @param response
	 * @param data
	 * @throws IOException
	 * @throws ServletException
	 */
	public void nuevoCliMovil(HttpServletRequest request,
			HttpServletResponse response, JSONObject data) throws IOException, ServletException {
		String mpwd = data.get("mpwd").toString();
		String mpwdc = data.get("mpwdc").toString();	
		try {
			Integer c = (Integer) request.getSession().getAttribute(
					"SessionUser");
			mngServ.crearCM(c, mpwd, mpwdc);
			response.getWriter().write(
					mngServ.jsonMensajes("OK",
							"Cuenta creada correctamente, revise su correo electrónico."));
		} catch (Exception e) {
			response.getWriter().write(
					mngServ.jsonMensajes("EA", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
	}
	
	/**
	 * Permite el cambio de password de la cuenta móvil
	 * @param request
	 * @param response
	 * @param data
	 * @throws IOException
	 * @throws ServletException
	 */
	public void cambioPwdMovil(HttpServletRequest request,
			HttpServletResponse response, JSONObject data) throws IOException, ServletException {
		String mpwd = data.get("mpwd").toString();
		String npwd = data.get("npwd").toString();
		String cpwd = data.get("cpwd").toString();
		try {
			Integer c = (Integer) request.getSession().getAttribute(
					"SessionUser");
			mngServ.cambioPassCM(c, mpwd, npwd, cpwd);
			response.getWriter().write(
					mngServ.jsonMensajes("OK",
							"Cambio de clave correcto"));
		} catch (Exception e) {
			response.getWriter().write(
					mngServ.jsonMensajes("EA", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
	}
	
	/**
	 * Resetea el ping de la cuenta móvil
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void resetPinMovil(HttpServletRequest request,
				HttpServletResponse response) throws IOException, ServletException {
		try {
			Integer c = (Integer) request.getSession().getAttribute(
					"SessionUser");
			mngServ.cambioPinCM(c);
			response.getWriter().write(
					mngServ.jsonMensajes("OK", "Se ha enviado su nuevo PIN al correo de su cuenta."));
		} catch (Exception e) {
			response.getWriter().write(
					mngServ.jsonMensajes("EA", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
	}
	
	/**
	 * Reactiva una cuenta móvil
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void activarMovil(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try {
			Integer c = (Integer) request.getSession().getAttribute(
					"SessionUser");
			mngServ.activarCM(c);
			response.getWriter().write(
					mngServ.jsonMensajes("OK", "Se ha reactivado correctamente su cuenta."));
		} catch (Exception e) {
			response.getWriter().write(
					mngServ.jsonMensajes("EA", e.getMessage()));
		} finally {
			response.getWriter().close();
		}
	}
	
	/**
	 * Desactiva una cuenta móvil
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void desactivarMovil(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try {
			Integer c = (Integer) request.getSession().getAttribute(
					"SessionUser");
			mngServ.desactivarCM(c);
			response.getWriter().write(
					mngServ.jsonMensajes("OK", "Se ha desactivado correctamente su cuenta."));
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
