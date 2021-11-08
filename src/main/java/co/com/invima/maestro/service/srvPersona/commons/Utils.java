package co.com.invima.maestro.service.srvPersona.commons;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

@Component
@Slf4j
public class Utils {
    public  static final String  HOST_SERVICES ="http://srvenviarnotificacioncorreo-des-transversales.apps.ocp4devqa.invima.gov.co/";
    public static final String ENVIAR_CORREO_URI = HOST_SERVICES+"v1/enviarNotificacionCorreo/enviar";
    public static final String URLBD= "jdbc:sqlserver://192.168.1.35;database=maestra";
    public static final String comisionado= "${comisionado}";
    public static final String funcionario= "${funcionario}";
    public static final String ciudadano= "${ciudadano}";
    public static final String primerNombre= "${primerNombre}";
    public static final String primerApellido= "${primerApellido}";
    public static final String numSolicitud= "${numSolicitud}";
    public static final String codigoVerificacion= "${codigoVerificacion}";
    public static final String fecha= "${fechaSolicitud}";
    public static final String sala= "${sala}";
    public static final String link= "${link}";
    public static final String usuario= "${usuario}";
    public static final String rol = "${rol}";
    public static final String empresa = "${empresa}";
    public static final String URL_USUARIO_SERVICES = "http://srvusuario-des-transversales.apps.ocp4devqa.invima.gov.co/v1/Usuario/guardarUsuarioPersona";


    public void cargarDriver() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e1) {
            log.error("Error 1: " + e1);
            e1.printStackTrace();
        }
    }

    public Connection realizarConexion() {
        Properties connectionProps = new Properties();
        Connection conn;
        try {
            connectionProps.put("user", "usr_tramites");
            connectionProps.put("password", "usr_tramites*");

            conn = DriverManager.getConnection(URLBD, connectionProps);
            conn.setSchema("dbo");
            return conn;
        } catch (Exception e) {
            log.error("Error 2: " + e);
            e.printStackTrace();
            return null;
        }

    }
}
