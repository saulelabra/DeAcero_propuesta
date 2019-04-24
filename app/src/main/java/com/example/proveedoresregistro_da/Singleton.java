package com.example.proveedoresregistro_da;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class Singleton {
    private static Singleton mSingletonInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mContext;

    /**
     *  hemos creado una instancia Singleton con el constructor privado para poder utilizarlo
     *  internamente
     */
    private Singleton(Context context) {
        mContext = context;

        /** Adicionalmente tenemos instancias de RequestQueue e ImageLoader.
         * En el método RequestQueue verificamos si el objeto ya existe, si no, usamos el método
         * mas conveniente de Volley.newRequestQueue(), pasamos el contexto de la aplicación y devolvemos la
         * referencia a RequestQueue.
         */
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                /**
                 * ImageCache es una interfaz de adaptador de caché. Se utiliza como un caché L1 antes del envío a Volley.
                 * Agregamos un objeto LruCache del paquete java.util.LruCache e implementamos los métodos de interfaz
                 * getBitmap() y putBitmap().
                 */
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            // tamaño del cache ver: https://developer.android.com/training/displaying-bitmaps/cache-bitmap.html
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    /**
     * El bloque synchronized lleva entre paréntesis la referencia a un objeto. Cada vez que un thread
     * intenta acceder a un bloque sincronizado le pregunta a ese objeto si no hay algún otro thread que ya
     * tenga el lock para ese objecto. En otras palabras, le pregunta si no hay otro thread ejecutando algun
     * bloque sincronizado con ese objeto, si el lock está tomado por otro thread, entonces el thread actual
     * es suspendido y puesto en espera hasta que el lock se libere. Si el lock está libre, entonces el thread
     * actual toma el lock del objeto y entra a ejecutar el bloque. Al tomar el lock, cuando venga el proximo
     * thread a intentar ejecutar un bloque sincronizado con ese objeto, será puesto en espera.
     */
    public static synchronized Singleton getInstance(Context context) {
        if (mSingletonInstance == null) {
            mSingletonInstance = new Singleton(context);
        }
        return mSingletonInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // Tomado de Stack Overflow
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * Este método toma un tipo genérico y lo añade a la cola de solicitudes. En el constructor,
     * creamos un objeto ImageLoader utilizando el objeto RequestQueue y un nuevo objeto ImageCache
     * clase que recibe un tipo de dato genérico, T es el tipo genérico que será reemplazado por un tipo real,
     * T es el nombre que damos al parámetro genérico, Este nombre se sustituirá por el tipo real que se le pasará a la clase.
     */
    public <T> void addToRequestQueue(Request<T> req,String tag) {
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    /**
     * Este método se utiliza para cancelar una solicitud utilizando una etiqueta.
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
