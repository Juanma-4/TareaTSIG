package utilidades;

import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import dominio.Usuario;

public class UsuarioAdapter implements JsonSerializer<Usuario> {
	
	@Override
	public JsonElement serialize(Usuario usuario, Type type, JsonSerializationContext usc) {

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("mail", usuario.getMail());
		jsonObject.addProperty("pass", usuario.getPassword());
		return jsonObject;
	}
}

