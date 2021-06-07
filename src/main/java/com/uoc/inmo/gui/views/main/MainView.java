package com.uoc.inmo.gui.views.main;

import java.util.Optional;

import com.uoc.inmo.gui.GuiConst;
import com.uoc.inmo.gui.security.SecurityUtils;
import com.uoc.inmo.gui.views.about.AboutView;
import com.uoc.inmo.gui.views.inmuebles.InmueblesView;
import com.uoc.inmo.query.entity.user.User;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
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
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * The main view is a top-level placeholder for other views.
 */
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
@PWA(name = "Inmo", shortName = "Inmo", enableInstallPrompt = false)
@JavaScript("https://code.jquery.com/jquery-3.5.1.slim.min.js")
@JavaScript("https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js")
@StyleSheet("https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css")
@JavaScript("https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js")
@JavaScript("https://cdn.jsdelivr.net/npm/chart.js")
@CssImport("./views/main/js/priceChart.js")
@JsModule("./styles/shared-styles.js")
@CssImport("./views/main/main-view.css")
@Push
public class MainView extends AppLayout {

    private final Tabs menu;
    private H1 viewTitle;

    public MainView() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
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

        Anchor home = new Anchor(GuiConst.INDEX_URL, logoImg);
        home.setId("logoHome");

        Anchor disclaimerLogoAnchor = new Anchor("https://pngtree.com/so/house-icons", "house icons png from pngtree.com");
        disclaimerLogoAnchor.addClassName("disclaimerLogoAnchor");

        logoDiv.add(home, disclaimerLogoAnchor);

        MenuBar profileMenu = createProfileMenu();
        profileMenu.addClassName("menu");

        Div profileMenuDiv = new Div(profileMenu);
        profileMenuDiv.setId("profileMenu-div");

        layout.add(logoDiv, viewTitle);

        Div userTitle = new Div();
        userTitle.setId("user-title");

        if(SecurityUtils.isUserLoggedIn()){

            String type = "";
            if(SecurityUtils.isProfesionalLoggedUser()) {
                type = User.TYPE_PROFESIONAL;
            } else if(SecurityUtils.isParticularLoggedUser()) {
                type = User.TYPE_PARTICULAR;
            }

            userTitle.add(new Span(SecurityUtils.getEmailLoggedUser()+" - "+type));
            
        }

        layout.add(userTitle, profileMenuDiv);
        
        return layout;
    }

    private MenuBar createProfileMenu(){
        MenuBar menuBar = new MenuBar();

        MenuItem avatar = menuBar.addItem(new Avatar());
        avatar.setId("avatar-menu-item");

        SubMenu subMenu = avatar.getSubMenu();
        String contextPath = VaadinServlet.getCurrent().getServletContext().getContextPath();

        if(SecurityUtils.isUserLoggedIn()){
            if(SecurityUtils.isProfesionalLoggedUser()){
                subMenu.addItem(createMisInmueblesItem(contextPath));
                subMenu.addItem(createNewInmuebleItem(contextPath));
            }
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
        href += GuiConst.LOGIN_PROCESSING_URL;
        
        VaadinIcon icon = VaadinIcon.SIGN_IN;
        String title = GuiConst.TITLE_LOGIN;

        final Anchor a = populateLink(new Anchor(), icon, title);
        a.setHref(href);
        return a;
    }

    private Component createLogoutItem(String contextPath){
        String href = contextPath;
        VaadinIcon icon = VaadinIcon.SIGN_OUT;
        
        href += GuiConst.LOGOUT_PROCESSING_URL;
        String title = GuiConst.TITLE_LOGOUT;

        final Anchor a = populateLink(new Anchor(), icon, title);
        a.setHref(href);
        return a;
    }

    private Component createMisInmueblesItem(String contextPath){
        String href = contextPath;
        
        href += GuiConst.URL_MIS_INMUEBLES;
        String title = GuiConst.TITLE_MIS_INMUEBLES;

        final Anchor a = new Anchor(href, title);
        return a;
    }

    private Component createNewInmuebleItem(String contextPath){
        String href = contextPath;
        
        href += GuiConst.URL_NEW_INMUEBLE;
        String title = GuiConst.TITLE_NEW_INMUEBLE;

        final Anchor a = new Anchor(href, title);
        return a;
    }

    private Component createSingUpItem(String contextPath){
        String href = contextPath;
        VaadinIcon icon = VaadinIcon.USER;
        
        href += GuiConst.URL_SIGN_UP;
        String title = GuiConst.TITLE_SIGN_UP;

        final Anchor a = populateLink(new Anchor(), icon, title);
        a.setHref(href);
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

    public void addJavaScript(String url){
        if(this.getUI().isPresent()){
            this.getUI().get().getPage().addJavaScript(url);
        }
    }
}
