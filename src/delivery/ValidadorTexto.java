package delivery;

import java.util.Objects;
import java.util.regex.Pattern;

public class ValidadorTexto {

	private static final Pattern MAIL_PATTERN =
			Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
	
	private ValidadorTexto() {
		
	}
	
	public static String validadorTextoObligatorio(String texto, String campo) {
		
		Objects.requireNonNull(texto, "El campo " +  campo + " no puede ser null");
		
		String textoNormalizado = texto.trim();
		
		if (textoNormalizado.isEmpty()){
			
			throw new IllegalArgumentException("El campo" + campo + " no puede estar vacio");
		}
		
		return textoNormalizado;
	}
	
	public static String validarMail(String mail, String campo) {
		
		Objects.requireNonNull(mail, "El campo " + campo + " no puede ser null" );
		
		String textoNormalizado = validadorTextoObligatorio( mail,  campo).toLowerCase();
		
		if (!MAIL_PATTERN.matcher(textoNormalizado).matches()) {
			throw new IllegalArgumentException("Mail no valido");
		}
		
		return textoNormalizado;
	}
}
