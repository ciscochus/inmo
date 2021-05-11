package com.uoc.inmo.gui.views.main;

import java.util.Optional;

import com.uoc.inmo.gui.security.SecurityConfiguration;
import com.uoc.inmo.gui.security.SecurityUtils;
import com.uoc.inmo.gui.views.about.AboutView;
import com.uoc.inmo.gui.views.inmuebles.InmueblesView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinServlet;

/**
 * The main view is a top-level placeholder for other views.
 */
@PWA(name = "Inmo", shortName = "Inmo", enableInstallPrompt = false)
@JsModule("./styles/shared-styles.js")
@CssImport("./views/main/main-view.css")
@Push
public class MainView extends AppLayout {

    private static final String TITLE_LOGOUT = "Log out";
    private static final String TITLE_LOGIN = "Log in";
    private static final String TITLE_SIGN_UP = "Sign up";
    private static final String URL_SIGN_UP = "/ui/signup";
    private static final String URL_NEW_INMUEBLE = "/ui/createInmueble";

    
    private final Tabs menu;
    private H1 viewTitle;

    public MainView() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        // addToDrawer(createDrawerContent(menu));+
        
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        // layout.add(new DrawerToggle());
        viewTitle = new H1();

        Div logoDiv = new Div();
        logoDiv.setId("logo-div");

        Image logoImg = new Image("/icons/logo_small.png", "Logo");

        Anchor home = new Anchor("/ui/", logoImg);
        home.setId("logoHome");

        Anchor disclaimerLogoAnchor = new Anchor("https://pngtree.com/so/house-icons", "house icons png from pngtree.com");
        disclaimerLogoAnchor.addClassName("disclaimerLogoAnchor");

        logoDiv.add(home, disclaimerLogoAnchor);

        Div createInmuebleDiv = createInmuebleDiv();

        MenuBar profileMenu = createProfileMenu();
        profileMenu.addClassName("menu");

        Div profileMenuDiv = new Div(profileMenu);
        profileMenuDiv.setId("profileMenu-div");
        profileMenuDiv.setWidthFull();

        layout.add(logoDiv, viewTitle);
        
        if(createInmuebleDiv != null)
            layout.add(createInmuebleDiv);

        layout.add(profileMenuDiv);
        
        return layout;
    }

    private MenuBar createProfileMenu(){
        MenuBar menuBar = new MenuBar();

        MenuItem avatar = menuBar.addItem(new Avatar());
        avatar.setId("avatar-menu-item");

        SubMenu subMenu = avatar.getSubMenu();
        String contextPath = VaadinServlet.getCurrent().getServletContext().getContextPath();

        if(SecurityUtils.isUserLoggedIn()){
            subMenu.addItem(createLogoutItem(contextPath));
        } else {
            subMenu.addItem(createLoginItem(contextPath));
            subMenu.addItem(createSingUpItem(contextPath));
        }
        
        

        return menuBar;
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo.png", "Inmo logo"));
        logoLayout.add(new H1("Inmo"));
        layout.add(logoLayout, menu);
        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component[] createMenuItems() {
        return new Tab[]{createTab("Inmuebles", InmueblesView.class), createTab("About", AboutView.class)};
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    private Component createLoginItem(String contextPath){
        String href = contextPath;
        href += SecurityConfiguration.LOGIN_PROCESSING_URL;
        
        VaadinIcon icon = VaadinIcon.SIGN_IN;
        String title = TITLE_LOGIN;

        final Anchor a = populateLink(new Anchor(), icon, title);
        a.setHref(href);
        return a;
    }

    private Component createLogoutItem(String contextPath){
        String href = contextPath;
        VaadinIcon icon = VaadinIcon.SIGN_OUT;
        
        href += SecurityConfiguration.LOGOUT_PROCESSING_URL;
        String title = TITLE_SIGN_UP;

        final Anchor a = populateLink(new Anchor(), icon, title);
        a.setHref(href);
        return a;
    }

    private Component createSingUpItem(String contextPath){
        String href = contextPath;
        VaadinIcon icon = VaadinIcon.USER;
        
        href += URL_SIGN_UP;
        String title = TITLE_SIGN_UP;

        final Anchor a = populateLink(new Anchor(), icon, title);
        a.setHref(href);
        return a;
    }

    private Div createInmuebleDiv(){
        if(!SecurityUtils.isUserLoggedIn())
            return null;

        Div createInmuebleDiv = new Div();
        createInmuebleDiv.setId("createInmueble-div");

        String contextPath = VaadinServlet.getCurrent().getServletContext().getContextPath();

        Anchor createInmuebleAnchor = createNewInmuebleAnchor(contextPath);
        createInmuebleAnchor.setId("createInmueble-button");

        createInmuebleDiv.add(createInmuebleAnchor);

        return createInmuebleDiv;
    }

    private Anchor createNewInmuebleAnchor(String contextPath){
        String href = contextPath;
        VaadinIcon icon = VaadinIcon.PLUS_CIRCLE;
        
        href += URL_NEW_INMUEBLE;

        final Anchor a = populateLink(new Anchor(), icon);
        a.setHref(href);
        a.setTitle("Añadir inmueble");

        return a;
    }

    private static <T extends HasComponents> T populateLink(T a, VaadinIcon icon) {
		a.add(icon.create());
		return a;
	}

    private static <T extends HasComponents> T populateLink(T a, VaadinIcon icon, String title) {
        a = populateLink(a, icon);
		a.add(" "+title);
		return a;
	}
}
