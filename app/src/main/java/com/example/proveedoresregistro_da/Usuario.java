package com.example.proveedoresregistro_da;

public class Usuario {
    private int id;
    private String nombreProveedor;
    private String direccion;
    private String rfc;
    private int cuenta_a_depositar;

    private String usuario;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private static final Usuario info = new Usuario();

    public static Usuario getInstance() {
        return info;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public int getCuenta_a_depositar() {
        return cuenta_a_depositar;
    }

    public void setCuenta_a_depositar(int cuenta_a_depositar) {
        this.cuenta_a_depositar = cuenta_a_depositar;
    }
}
