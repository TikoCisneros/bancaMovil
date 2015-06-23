package banca.controller;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import banca.model.dao.entities.Cuenta;
import banca.model.manager.ManagerCajero;



@FacesConverter("cuentaConverter")
public class CuentaConverter implements Converter{
	 public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
	        if(value != null && value.trim().length() > 0) {	        	
	            ManagerCajero service = new ManagerCajero();
	            try {
					return service.findCuentaByNro(value);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
	        }
	        else {
	            return null;
	        }
	    }
	 
	    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
	    	if(object != null) {
	    		return String.valueOf(((Cuenta) object).getNroCuenta());
	        }
	        else {
	            return null;
	        }
	    }   
}
