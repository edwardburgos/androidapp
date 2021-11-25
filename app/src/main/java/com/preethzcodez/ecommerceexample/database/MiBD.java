package com.preethzcodez.ecommerceexample.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.preethzcodez.ecommerceexample.model.Carrit;
import com.preethzcodez.ecommerceexample.model.Deseos;
import com.preethzcodez.ecommerceexample.model.Persona;
import com.preethzcodez.ecommerceexample.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class MiBD extends SQLiteOpenHelper {

    private static final String NOMBRE_BD="mit36";
    private static final int VERSION_DB=1;

    private static final String ID = "id";
    private static final String PDT_ID = "producto_id";
    private static final String ID_ORD = "orden_id";
    private static final String VAR_ID = "precio";
    private static final String QUANTITY = "cantidad";
    private static final String ShoppingCartTable = "carrito";
    private static final String WishListTable = "megusta";
    private static final String PDT_NAME = "producto_nombre";
    private static final String PORTADA = "portada";
    private static final String EMAIL = "email";

    private static final String PASSWORD = "clave";
    private static final String MOBILE = "celular";
    private static final String DIRECCION = "direccion";
    private static final String ProductsTable = "productos";
    private static final String NAME = "nombre";
    private static final String OrderHistoryTable = "order_historial";
    private static final String UserTable = "user_detalles";


    // Create User Table
    private static final String CREATE_USER_TABLE = "CREATE TABLE " + UserTable + "("
            + EMAIL + " TEXT PRIMARY KEY,"
            + NAME + " TEXT NOT NULL,"
            + MOBILE + " TEXT NOT NULL,"
            + DIRECCION + " TEXT NOT NULL,"
            + PASSWORD + " TEXT NOT NULL" + ")";

    // Create Products Table
    private static final String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + ProductsTable + "("
            + PDT_ID + " INTEGER NOT NULL,"
            + VAR_ID + " DECIMAL NOT NULL"+ ")";
    // Create Shopping Cart Table
    private static final String CREATE_SHOPPING_CART_TABLE = "CREATE TABLE " + ShoppingCartTable + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + PDT_ID + " INTEGER NOT NULL,"
            + PDT_NAME + " TEXT NOT NULL,"
            + PORTADA + " TEXT NOT NULL,"
            + VAR_ID + " DECIMAL NOT NULL,"
            + QUANTITY + " INTEGER NOT NULL"+")";

    // Create Order History Table
    private static final String CREATE_ORDER_HISTORY_TABLE = "CREATE TABLE " + OrderHistoryTable + "("
            + ID + " INTEGER PRIMARY KEY,"
            + ID_ORD + " INTEGER NOT NULL,"
            + PDT_ID + " INTEGER NOT NULL,"
            + VAR_ID + " INTEGER NOT NULL,"
            + QUANTITY + " INTEGER NOT NULL,"
            + PORTADA + " TEXT NOT NULL,"
            + PDT_NAME + " TEXT NOT NULL" + ")";
    // Create Wish List Table
    private static final String CREATE_WISHLIST_TABLE = "CREATE TABLE " + WishListTable + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + PDT_ID + " INTEGER NOT NULL,"
            + VAR_ID + " DECIMAL NOT NULL,"
            + EMAIL + " TEXT NOT NULL" + ")";


    public MiBD(@Nullable Context context) {
        super(context, NOMBRE_BD, null, VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_SHOPPING_CART_TABLE);
        db.execSQL(CREATE_WISHLIST_TABLE);
        db.execSQL(CREATE_ORDER_HISTORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserTable);
        db.execSQL("DROP TABLE IF EXISTS " + ProductsTable);
        db.execSQL("DROP TABLE IF EXISTS " + ShoppingCartTable);
        db.execSQL("DROP TABLE IF EXISTS " + WishListTable);
        db.execSQL("DROP TABLE IF EXISTS " + OrderHistoryTable);


        // Create tables again
        onCreate(db);
    }

    // Insert Products
    public void insertProducts(int pdt_id, Double precio) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PDT_ID, pdt_id);
        values.put(VAR_ID, precio);

        // Check If Value Already Exists
        boolean isUpdate = false;

        String selectQuery = "SELECT * FROM " + ProductsTable + " WHERE " + PDT_ID + "=?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(pdt_id)});

        if (cursor.moveToFirst()) {
            isUpdate = true;
        }
        cursor.close();

        if (isUpdate) {
            db.update(ProductsTable, values, PDT_ID + " = ?",
                    new String[]{String.valueOf(pdt_id)});
            db.close();
            return;
        }

        db.insert(ProductsTable, null, values);
        db.close();
    }
    // Insert Product Into Cart
    public long insertIntoCart(int pdt_id, String pdt_name,String portada,Double var_id, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PDT_ID, pdt_id);
        values.put(PDT_NAME, pdt_name);
        values.put(PORTADA, portada);
        values.put(VAR_ID, var_id);
        values.put(QUANTITY, quantity);


        // Check If Value Already Exists
        String selectQuery = "SELECT * FROM " + ShoppingCartTable + " WHERE " + PDT_ID + "=? AND " + VAR_ID + "=?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{ String.valueOf(pdt_id), String.valueOf(var_id)});
        if (cursor.moveToFirst()) {
            cursor.close();
            return -1;
        }
        cursor.close();
        return db.insert(ShoppingCartTable, null, values);
    }

    // Get Cart Item Count
    public int getCartItemCount() {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ShoppingCartTable;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }

    // Get Shopping Cart Items
    public List<Carrit> getCartItems() {
        List<Carrit> shoppingCart = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + ShoppingCartTable;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Carrit cart = new Carrit();
                int id = cursor.getInt(cursor.getColumnIndex(ID));
                int productId = cursor.getInt(cursor.getColumnIndex(PDT_ID));
                String prd_name=cursor.getString(cursor.getColumnIndex(PDT_NAME));
                String portada=cursor.getString(cursor.getColumnIndex(PORTADA));
                double variantId = cursor.getInt(cursor.getColumnIndex(VAR_ID));
                int quantity = cursor.getInt(cursor.getColumnIndex(QUANTITY));

              //  Product product = getProductDetailsById(productId, email);
              //  Variant variant = getVariantDetailsById(variantId);

              //  cart.setId(id);
                cart.setCod_produc(productId);
                cart.setItemQuantity(quantity);
                cart.setProduct(prd_name);
                cart.setPortada(portada);
                cart.setVariant(variantId);

                // Adding to list
                shoppingCart.add(cart);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return cart items list
        return shoppingCart;
    }

    // Delete Cart Item By Id
    public boolean deleteCartItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ShoppingCartTable, PDT_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }


    // Remove Item From Wish List
    public boolean removeShortlistedItem(int pdt_id, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(WishListTable, PDT_ID + "=? AND " + EMAIL + "=?", new String[]{String.valueOf(pdt_id), email}) > 0;
    }

    // Get Wishlist Items
    public List<Persona> getShortListedItems(String email) {
        List<Persona> productList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + WishListTable + " WHERE " + EMAIL + "=?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{email});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                int pdt_id = cursor.getInt(cursor.getColumnIndex(PDT_ID));
                String nome = cursor.getString(cursor.getColumnIndex(PDT_NAME));
                String link = cursor.getString(cursor.getColumnIndex(PORTADA));
                String precio = cursor.getString(cursor.getColumnIndex(VAR_ID));
                Persona product = new Persona();
                 product.setId(pdt_id);
                product.setPrecio(precio);
                product.setTitulo(nome);
                product.setPortada(link);
                // Adding to list
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return product list
        return productList;
    }
    // Delete Cart Items
    public void deleteCartItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ShoppingCartTable, null, null);
    }

    // Insert Order
  /*  public void insertOrderHistory(List<Cart> shoppingCart, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < shoppingCart.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(PDT_ID, shoppingCart.get(i).getProduct().getId());
            values.put(VAR_ID, shoppingCart.get(i).getVariant().getId());
            values.put(QUANTITY, shoppingCart.get(i).getItemQuantity());
            values.put(EMAIL, email);
            db.insert(OrderHistoryTable, null, values);
        }
        db.close();
    }

    // Insert Order
    public void insertOrderHistory(List<Cart> shoppingCart, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < shoppingCart.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(PDT_ID, shoppingCart.get(i).getProduct());
            values.put(QUANTITY, shoppingCart.get(i).getItemQuantity());
            values.put(EMAIL, email);
            db.insert(OrderHistoryTable, null, values);
        }
        db.close();
    }*/

    // Register User
    public long registerUser(String name, String email, String mobile, String password, String direccion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String selectQuery = "SELECT * FROM " + UserTable + " WHERE " + EMAIL + "=? AND " + PASSWORD + "=?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{email,password});
        if (cursor.moveToFirst()) {
            cursor.close();
            return -1;
        }else{
            values.put(NAME, name);
            values.put(EMAIL, email);
            values.put(MOBILE, mobile);
            values.put(PASSWORD, password);
            values.put(DIRECCION, direccion);
        }

        cursor.close();
        return db.insert(UserTable, null, values);
    }


    public long registUser(Usuario user) {
        Usuario usuario = new Usuario();
     String email=user.getEmail();
     String password= user.getClave();
        // Select Query
        String selectQuery = "SELECT * FROM " + UserTable + " WHERE " + EMAIL + "=? AND " + PASSWORD + "=?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{email,password});
        ContentValues values = new ContentValues();
        if (cursor.moveToFirst()) {
            cursor.close();

        }else{

        }
        cursor.close();
        db.close();

        return db.insert(UserTable, null, values);
    }
    // Get User
    // Get User
    public Usuario getUser(String email, String password) {
        Usuario usuario = new Usuario();

        // Select Query
        String selectQuery = "SELECT * FROM " + UserTable + " WHERE " + EMAIL + "=? AND " + PASSWORD + "=?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{email,password});

        if (cursor.moveToFirst()) {
            usuario.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
            usuario.setNombre(cursor.getString(cursor.getColumnIndex(NAME)));
            usuario.setClave(cursor.getString(cursor.getColumnIndex(PASSWORD)));
            usuario.setTelefono(cursor.getString(cursor.getColumnIndex(MOBILE)));
            usuario.setDireccion(cursor.getString(cursor.getColumnIndex(DIRECCION)));
        }
        cursor.close();
        db.close();

        // return user
        return usuario;
    }

    // Update Cart Item Quantity
    public void updateItemQuantity(int quantity, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(QUANTITY, quantity);
        db.update(ShoppingCartTable, values, PDT_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    // Add Item Into Wish List
    public long shortlistItem(int pdt_id, String email, double precio) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PDT_ID, pdt_id);
        values.put(EMAIL, email);
        values.put(VAR_ID,precio);
        return db.insert(WishListTable, null, values);
    }

    // Check Product In Wish List
    public boolean isShortlistedItem(int pdt_id, String email) {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + WishListTable + " WHERE " + EMAIL + "=? AND " + PDT_ID + "=?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{email, String.valueOf(pdt_id)});

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }

    // Get Product Details By Id
    public Persona getProductDetailsById(int id, String email) {
        Persona producto = new Persona();


        // Select All Query
        String selectQuery = "SELECT  * FROM " + WishListTable + " WHERE " + PDT_ID + "=? AND " + EMAIL +"=?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id),email});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            producto.setId(cursor.getInt(cursor.getColumnIndex(PDT_ID)));;
            boolean isShortlistedItem = isShortlistedItem(producto.getId(), email);
            producto.setShortlisted(isShortlistedItem);

        }
        cursor.close();
        db.close();

        // return product
        return producto;
    }

    // Check Product In Wish List
    public double buscar(int pdt_id, String email) {
        Deseos deseos = new Deseos();
        double miprecio=0.0;

        // Select All Query
        String selectQuery = "SELECT  * FROM " + WishListTable + " WHERE " + EMAIL + "=? AND " + PDT_ID + "=?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{email, String.valueOf(pdt_id)});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            deseos.setId(cursor.getInt(cursor.getColumnIndex(ID)));;
            boolean isShortlistedItem = isShortlistedItem(deseos.getId(), email);
            deseos.setShortlisted(isShortlistedItem);
            miprecio=cursor.getInt(cursor.getColumnIndex(VAR_ID));

        }
        cursor.close();
        db.close();

        // return product
        return miprecio;
    }

    // Check Product In Wish List
    public double miprecio(int pdt_id) {
        Deseos deseos = new Deseos();
        double precio=0.0;

        // Select All Query
        String selectQuery = "SELECT  * FROM " + ProductsTable + " WHERE " + PDT_ID + "=?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(pdt_id)});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            precio=cursor.getDouble(cursor.getColumnIndex(VAR_ID));
        }
        cursor.close();
        db.close();

        // return product
        return precio;
    }

    // Insert Order
    public void productorden(int cod_ped ,int pdt_id, Double var_id, int quantity,String porta, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
           // values.put(PDT_ID, shoppingCart.get(i).getProduct().getId());
            //values.put(VAR_ID, shoppingCart.get(i).getVariant().getId());
        String selectQuery = "SELECT * FROM " + OrderHistoryTable + " WHERE " + ID_ORD + "=? AND " + PDT_ID + "=?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(cod_ped), String.valueOf(pdt_id)});
        if (cursor.moveToFirst()) {
            cursor.close();
        }else{
            values.put(ID_ORD,cod_ped);
            values.put(PDT_ID, pdt_id);
            values.put(VAR_ID, var_id);
            values.put(QUANTITY, quantity);
            values.put(PORTADA, porta);
            values.put(PDT_NAME, name);
            db.insert(OrderHistoryTable, null, values);
        }

        db.close();


    }

    // Get Order History
    public List<Carrit> getOrders(String cod_ped) {
        List<Carrit> shoppingCart = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + OrderHistoryTable + " WHERE " + ID_ORD + "=?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{cod_ped});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Carrit cart = new Carrit();
                int id = cursor.getInt(cursor.getColumnIndex(ID));
                int productId = cursor.getInt(cursor.getColumnIndex(PDT_ID));
                int quantity = cursor.getInt(cursor.getColumnIndex(QUANTITY));
                String producto = cursor.getString(cursor.getColumnIndex(PDT_NAME));
                String portada = cursor.getString(cursor.getColumnIndex(PORTADA));
                Double precio = cursor.getDouble(cursor.getColumnIndex(VAR_ID));

                cart.setId(id);
                cart.setCod_produc(productId);
                cart.setPortada(portada);
                cart.setVariant(precio);
                cart.setProduct(producto);
                cart.setItemQuantity(quantity);

                // Adding to list
                shoppingCart.add(cart);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return order items list
        return shoppingCart;
    }

}
