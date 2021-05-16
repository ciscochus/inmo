package com.uoc.inmo.gui;

import com.vaadin.flow.component.notification.Notification.Position;

public class GuiConst {
    
    
    public static final String LOGIN_PROCESSING_URL = "/ui/login";
    public static final String LOGIN_FAILURE_URL = "/ui/login?error";
    public static final String LOGIN_URL = LOGIN_PROCESSING_URL;
    
    public static final String LOGOUT_PROCESSING_URL = "/ui/logout";
    
    public static final String TITLE_LOGOUT = "Log out";
    public static final String TITLE_LOGIN = "Log in";
    public static final String TITLE_SIGN_UP = "Sign up";
    public static final String TITLE_NEW_INMUEBLE = "Añadir inmueble";
    public static final String TITLE_MIS_INMUEBLES = "Mis inmuebles";

    public static final String INDEX_URL = "/ui/inmuebles";
    public static final String URL_SIGN_UP = "/ui/signup";
    public static final String URL_NEW_INMUEBLE = "/ui/createInmueble";
    public static final String URL_MIS_INMUEBLES = "/ui/misInmuebles";

    public static final String LOGOUT_SUCCESS_URL = LOGIN_PROCESSING_URL;

    public static final String PAGE_LISTADO_INMUEBLES = "inmuebles";
    public static final String PAGE_LISTADO_MIS_INMUEBLES = "misInmuebles";
    public static final String PAGE_UPDATE_INMUEBLE = "updateInmueble";

    public static final String NOTIFICATION_SAVE_OK = "Cambios guardados correctamente";
    public static final String NOTIFICATION_SAVE_ERROR = "Se ha producido un error";
    public static final int NOTIFICATION_TIME = 3000;
    public static final Position NOTIFICACION_POSITION = Position.TOP_END;

    public static final int MAX_IMAGES_UPLOAD = 10;
}
