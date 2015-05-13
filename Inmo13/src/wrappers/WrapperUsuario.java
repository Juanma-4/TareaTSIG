package wrappers;

import com.google.gson.annotations.SerializedName;

public class WrapperUsuario {

	@SerializedName("mail")
	private String mail;
	@SerializedName("password")
	private String password;

	public WrapperUsuario() {
	}

	public WrapperUsuario(String mail, String password) {

		this.mail = mail;
		this.password = password;

	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

}
