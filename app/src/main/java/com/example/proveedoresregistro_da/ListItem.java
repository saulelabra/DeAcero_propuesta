package com.example.proveedoresregistro_da;

public class ListItem {
    private String precio;
    private String urlFoto;
    private String id;

    public ListItem(String precio, String urlFoto, String id)
    {
        this.precio = precio;
        this.urlFoto = urlFoto;
        this.id = id;
    }

    public String getPrecio() {
        return precio;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public String getId() {
        return id;
    }
}
