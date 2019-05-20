package com.example.proveedoresregistro_da;

import java.util.ArrayList;

public class ListItem {

    private ArrayList<String> datos;
    private int id;

    public ListItem(ArrayList<String> datos)
    {
        this.datos = datos;
    }

    public ListItem(String precio, String urlFoto, String id)
    {
        datos = new ArrayList<>();
    }

    public ArrayList<String> getDatos(){
        return datos;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
