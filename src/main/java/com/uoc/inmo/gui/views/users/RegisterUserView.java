package com.uoc.inmo.gui.views.users;

import java.lang.invoke.MethodHandles;

import com.uoc.inmo.gui.GuiConst;
import com.uoc.inmo.gui.views.main.MainView;
import com.uoc.inmo.query.entity.user.Inmobiliaria;
import com.uoc.inmo.query.entity.user.Particular;
import com.uoc.inmo.query.entity.user.User;
import com.uoc.inmo.query.service.UserService;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Binder.Binding;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "signup", layout = MainView.class)
@RouteAlias(value = "signup", layout = MainView.class)
@PageTitle("Register User")
@CssImport("./views/users/register-user-view.css")
public class RegisterUserView extends Div {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserService userService;

    public Binder<User> userBinder;
    public Binder<Inmobiliaria> professionalBinder;
    public Binder<Particular> particularBinder;

    public Div particularDiv;
    public Div professionalDiv;

    public RegisterUserView(@Autowired UserService userService) {
        this.userService  = userService;

        add(createRegisterForm());

        addClassName("inmo-background");
    }


    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("register-button-layout");

        // Button bar
        Button save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(e -> saveUser(e));

        Button reset = new Button("Reset");
        
        reset.addClickListener(event -> {
            // clear fields by setting null
            userBinder.readBean(null);
            professionalBinder.readBean(null);
            particularBinder.readBean(null);
        });

        buttonLayout.add(save, reset);

        return buttonLayout;
    }


    private void saveUser(ClickEvent<Button> e) {
        User newUser = new User();

        if(userBinder.writeBeanIfValid(newUser)){
            String tipo = newUser.getTipo();

            if(tipo.equals(User.TYPE_PARTICULAR))
                saveParticular(newUser);
            else
                saveProfesional(newUser);

        } else {
            new Notification(GuiConst.NOTIFICATION_SAVE_ERROR, GuiConst.NOTIFICATION_TIME, GuiConst.NOTIFICACION_POSITION).open();
        }
    }

    private void saveParticular(User newUser) {
        Particular newParticular = new Particular();

        if(particularBinder.writeBeanIfValid(newParticular)){
            if(userService.createParticular(newUser, newParticular) != null){
                new Notification(GuiConst.NOTIFICATION_SAVE_OK, GuiConst.NOTIFICATION_TIME, GuiConst.NOTIFICACION_POSITION).open();
                navegateTo(GuiConst.PAGE_LOGIN);
            } else {
                new Notification(GuiConst.NOTIFICATION_SAVE_ERROR, GuiConst.NOTIFICATION_TIME, GuiConst.NOTIFICACION_POSITION).open();
            }
        } else {
            new Notification(GuiConst.NOTIFICATION_SAVE_ERROR, GuiConst.NOTIFICATION_TIME, GuiConst.NOTIFICACION_POSITION).open();
        }
    }

    private void saveProfesional(User newUser) {
        Inmobiliaria newInmobiliaria = new Inmobiliaria();

        if(professionalBinder.writeBeanIfValid(newInmobiliaria)){
            if(userService.createInmobiliaria(newUser, newInmobiliaria) != null){
                new Notification(GuiConst.NOTIFICATION_SAVE_OK, GuiConst.NOTIFICATION_TIME, GuiConst.NOTIFICACION_POSITION).open();
                navegateTo(GuiConst.PAGE_LOGIN);
            } else {
                new Notification(GuiConst.NOTIFICATION_SAVE_ERROR, GuiConst.NOTIFICATION_TIME, GuiConst.NOTIFICACION_POSITION).open();
            }
        } else {
            new Notification(GuiConst.NOTIFICATION_SAVE_ERROR, GuiConst.NOTIFICATION_TIME, GuiConst.NOTIFICACION_POSITION).open();
        }
    }

    public Div createRegisterForm(){

        Div formDiv = new Div();
        formDiv.setId("register-form-container");
        formDiv.addClassName("container");

        Div formLayout = new Div();
        formLayout.setId("register-form-layout");

        userBinder = new Binder<>();
        professionalBinder = new Binder<>();
        particularBinder = new Binder<>();

        // Common data

        //Type
        Label typeLabel = new Label("Tipo");
        typeLabel.addClassNames("col-sm-1", "col-form-label");

        RadioButtonGroup<String> typeRadioGroup = new RadioButtonGroup<>();
        typeRadioGroup.setItems(User.TYPE_PARTICULAR, User.TYPE_PROFESIONAL);
        typeRadioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        typeRadioGroup.setValue(User.TYPE_PARTICULAR);

        typeRadioGroup.addValueChangeListener(e -> typeChanged(e));

        Div typeRadioGroupDiv = new Div(typeRadioGroup);
        typeRadioGroupDiv.addClassNames("col-sm-10");

        Div typeDiv = new Div(typeLabel,typeRadioGroupDiv);
        typeDiv.addClassNames("form-group", "row");

        //Email
        Label emailLabel = new Label("Email");
        emailLabel.addClassNames("col-sm-1", "col-form-label");

        TextField email = new TextField();
        email.setPlaceholder("Email");
        email.addClassName("full-width");

        Div emailInputDiv = new Div(email);
        emailInputDiv.addClassNames("col-sm-4");

        Div emailDiv = new Div(emailLabel,emailInputDiv);
        emailDiv.addClassNames("form-group", "row");

        //Password
        Label passwordLabel = new Label("password");
        passwordLabel.addClassNames("col-sm-1", "col-form-label");

        PasswordField password = new PasswordField();
        password.addClassName("full-width");

        Div passwordInputDiv = new Div(password);
        passwordInputDiv.addClassNames("col-sm-4");

        Div passwordDiv = new Div(passwordLabel,passwordInputDiv);
        passwordDiv.addClassNames("form-group", "row");

        // Particular 

        //Name
        Label nameLabel = new Label("Nombre");
        nameLabel.addClassNames("col-sm-1", "col-form-label");

        TextField name = new TextField();
        name.setPlaceholder("Nombre");
        name.addClassName("full-width");

        Div nameInputDiv = new Div(name);
        nameInputDiv.addClassNames("col-sm-4");

        Div nameDiv = new Div(nameLabel,nameInputDiv);
        nameDiv.addClassNames("form-group", "row");

        //Surname        
        Label surnameLabel = new Label("Apellidos");
        surnameLabel.addClassNames("col-sm-1", "col-form-label");

        TextField surname = new TextField();
        surname.setPlaceholder("Apellidos");
        surname.addClassName("full-width");

        Div surnameInputDiv = new Div(surname);
        surnameInputDiv.addClassNames("col-sm-4");

        Div surnameDiv = new Div(surnameLabel,surnameInputDiv);
        surnameDiv.addClassNames("form-group", "row");

        particularDiv = new Div(nameDiv, surnameDiv);
        particularDiv.setId("particular-div");

        // Professional

        //CIF
        Label cifLabel = new Label("CIF");
        cifLabel.addClassNames("col-sm-1", "col-form-label");

        TextField cif = new TextField();
        cif.setPlaceholder("CIF");
        cif.addClassName("full-width");

        Div cifInputDiv = new Div(cif);
        cifInputDiv.addClassNames("col-sm-4");

        Div cifDiv = new Div(cifLabel,cifInputDiv);
        cifDiv.addClassNames("form-group", "row");

        //Name
        Label professionalNameLabel = new Label("Nombre");
        professionalNameLabel.addClassNames("col-sm-1", "col-form-label");

        TextField professionalName = new TextField();
        professionalName.setPlaceholder("Nombre");
        professionalName.addClassName("full-width");

        Div professionalNameInputDiv = new Div(professionalName);
        professionalNameInputDiv.addClassNames("col-sm-4");

        Div professionalNameDiv = new Div(professionalNameLabel,professionalNameInputDiv);
        professionalNameDiv.addClassNames("form-group", "row");

        //Phone
        Label phoneLabel = new Label("Teléfono");
        phoneLabel.addClassNames("col-sm-1", "col-form-label");

        TextField phone = new TextField();
        phone.addClassName("full-width");

        Div phoneInputDiv = new Div(phone);
        phoneInputDiv.addClassNames("col-sm-4");

        Div phoneDiv = new Div(phoneLabel,phoneInputDiv);
        phoneDiv.addClassNames("form-group", "row");

        //Web
        Label webLabel = new Label("Web");
        webLabel.addClassNames("col-sm-1", "col-form-label");

        TextField web = new TextField();
        web.setPlaceholder("Web");
        web.addClassName("full-width");

        Div webInputDiv = new Div(web);
        webInputDiv.addClassNames("col-sm-4");

        Div webDiv = new Div(webLabel,webInputDiv);
        webDiv.addClassNames("form-group", "row");

        //Address
        Label addressLabel = new Label("Dirección");
        addressLabel.addClassNames("col-sm-1", "col-form-label");

        TextField address = new TextField();
        address.setPlaceholder("Dirección");
        address.addClassName("full-width");

        Div addressInputDiv = new Div(address);
        addressInputDiv.addClassNames("col-sm-4");

        Div addressDiv = new Div(addressLabel,addressInputDiv);
        addressDiv.addClassNames("form-group", "row");

        //Description
        Label descriptionLabel = new Label("Descripción");
        descriptionLabel.addClassNames("col-sm-1", "col-form-label");

        TextArea description = new TextArea();
        description.setMaxLength(200);
        description.setPlaceholder("Descripción ...");
        description.addClassName("full-width");

        Div descriptionInputDiv = new Div(description);
        descriptionInputDiv.addClassNames("col-sm-4");

        Div descriptionDiv = new Div(descriptionLabel,descriptionInputDiv);
        descriptionDiv.addClassNames("form-group", "row");

        professionalDiv = new Div(cifDiv, professionalNameDiv, phoneDiv, webDiv, addressDiv, descriptionDiv);
        professionalDiv.setId("professional-div");
        professionalDiv.setClassName("d-none");


        formLayout.add(typeDiv, emailDiv, passwordDiv, particularDiv, professionalDiv);

        // Binders

        // Both email and password cannot be empty
        SerializablePredicate<String> emailOrPasswordPredicate = value -> !email
        .getValue().trim().isEmpty()
        || !password.getValue().trim().isEmpty();

        Binding<User, String> emailBinding = userBinder.forField(email)
            .withNullRepresentation("")
            .withValidator(emailOrPasswordPredicate,
                    "Introduce un email")
            .withValidator(new EmailValidator("Email no válido"))
            .bind(User::getEmail, User::setEmail);

        Binding<User, String> passwordBinding = userBinder.forField(password)
            .withNullRepresentation("")
            .withValidator(emailOrPasswordPredicate,
                "Introduce una contraseña")
            .bind(User::getPassword, User::setPassword);

        // Trigger cross-field validation when the other field is changed
        email.addValueChangeListener(event -> passwordBinding.validate());
        password.addValueChangeListener(event -> emailBinding.validate());


        Binding<User, String> typeBinding = userBinder.forField(typeRadioGroup)
            .withNullRepresentation(User.TYPE_PARTICULAR)
            .bind(User::getTipo, User::setTipo);

        Binding<Particular, String> nameBinding = particularBinder.forField(name)
            .withNullRepresentation("")
            .bind(Particular::getName, Particular::setName);

        Binding<Particular, String> surnameBinding = particularBinder.forField(surname)
            .withNullRepresentation("")
            .bind(Particular::getSurname, Particular::setSurname);

        Binding<Inmobiliaria, String> cifBinding = professionalBinder.forField(cif)
            .withNullRepresentation("")
            .bind(Inmobiliaria::getCif, Inmobiliaria::setCif);

        Binding<Inmobiliaria, String> professionalNameBinding = professionalBinder.forField(professionalName)
            .withNullRepresentation("")
            .bind(Inmobiliaria::getName, Inmobiliaria::setName);

            Binding<Inmobiliaria, String> phoneBinding = professionalBinder.forField(phone)
            .withNullRepresentation("")
            .bind(Inmobiliaria::getPhone, Inmobiliaria::setPhone);

            Binding<Inmobiliaria, String> webBinding = professionalBinder.forField(web)
            .withNullRepresentation("")
            .bind(Inmobiliaria::getWeb, Inmobiliaria::setWeb);

            Binding<Inmobiliaria, String> addressBinding = professionalBinder.forField(address)
            .withNullRepresentation("")
            .bind(Inmobiliaria::getAddress, Inmobiliaria::setAddress);

            Binding<Inmobiliaria, String> descriptionBinding = professionalBinder.forField(description)
            .withNullRepresentation("")
            .bind(Inmobiliaria::getDescription, Inmobiliaria::setDescription);
        
        formDiv.add(formLayout, createButtonLayout());

        return formDiv;

    }

    private void typeChanged(ComponentValueChangeEvent<RadioButtonGroup<String>, String> e) {
        if(e.getValue().equals(User.TYPE_PARTICULAR)){
            particularDiv.removeClassName("d-none");
            professionalDiv.addClassName("d-none");
        } else {
            professionalDiv.removeClassName("d-none");
            particularDiv.addClassName("d-none");
        }
    }

    public void navegateTo(String url){
        if(this.getUI().isPresent()){
            this.getUI().get().navigate(url);
        }
    }
}
