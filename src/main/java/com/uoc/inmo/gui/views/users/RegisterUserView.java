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
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
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
        // addRegisterForm();

        add(createRegisterForm());
        add(createButtonLayout());
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
            } else {
                new Notification(GuiConst.NOTIFICATION_SAVE_ERROR, GuiConst.NOTIFICATION_TIME, GuiConst.NOTIFICACION_POSITION).open();
            }
        } else {
            new Notification(GuiConst.NOTIFICATION_SAVE_ERROR, GuiConst.NOTIFICATION_TIME, GuiConst.NOTIFICACION_POSITION).open();
        }
    }

    public FormLayout createRegisterForm(){

        FormLayout formLayout = new FormLayout();
        formLayout.setId("register-form-layout");

        userBinder = new Binder<>();
        professionalBinder = new Binder<>();
        particularBinder = new Binder<>();

        // Common data

        TextField email = new TextField("Email");
        email.setPlaceholder("email");

        PasswordField password = new PasswordField("Password");

        RadioButtonGroup<String> typeRadioGroup = new RadioButtonGroup<>();
        typeRadioGroup.setItems(User.TYPE_PARTICULAR, User.TYPE_PROFESIONAL);
        typeRadioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        typeRadioGroup.setValue(User.TYPE_PARTICULAR);

        typeRadioGroup.addValueChangeListener(e -> typeChanged(e));

        // Particular 
        TextField name = new TextField("Nombre");
        name.setPlaceholder("Nombre");

        TextField surname = new TextField("Apellidos");
        surname.setPlaceholder("Apellidos");

        particularDiv = new Div(name, surname);
        particularDiv.setId("particular-div");

        // Professional

        TextField cif = new TextField("CIF");
        cif.setPlaceholder("CIF");

        TextField professionalName = new TextField("Nombre");
        professionalName.setPlaceholder("Nombre");

        TextField phone = new TextField("Teléfono");

        TextField web = new TextField("Web");
        web.setPlaceholder("Web");

        TextField address = new TextField("Dirección");
        address.setPlaceholder("Dirección");

        TextArea description = new TextArea("Descripción");
        description.setPlaceholder("Descripción");
        description.setMaxLength(200);

        professionalDiv = new Div(cif, professionalName, phone, web, address, description);
        professionalDiv.setId("professional-div");
        professionalDiv.setClassName("invisible");


        formLayout.add(typeRadioGroup, new Div(), email, password, particularDiv, professionalDiv);

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
        
        return formLayout;

    }

    private void typeChanged(ComponentValueChangeEvent<RadioButtonGroup<String>, String> e) {
        if(e.getValue().equals(User.TYPE_PARTICULAR)){
            particularDiv.removeClassName("invisible");
            professionalDiv.addClassName("invisible");
        } else {
            professionalDiv.removeClassName("invisible");
            particularDiv.addClassName("invisible");
        }
    }


}
