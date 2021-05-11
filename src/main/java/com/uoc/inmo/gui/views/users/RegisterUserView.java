package com.uoc.inmo.gui.views.users;

import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.stream.Collectors;

import com.uoc.inmo.gui.views.main.MainView;
import com.uoc.inmo.query.entity.user.User;
import com.uoc.inmo.query.service.UserService;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Binder.Binding;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
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

    public RegisterUserView(@Autowired UserService userService) {
        this.userService  = userService;
        addRegisterForm();
    }

    public void addRegisterForm(){
        FormLayout formLayout = new FormLayout();
        formLayout.setId("register-form-layout");

        TextField email = new TextField("Email");
        email.setPlaceholder("email");

        PasswordField password = new PasswordField("Password");

        formLayout.add(email, password);

        Binder<User> binder = new Binder<>();

        User newUser = new User();

        Label infoLabel = new Label();
        NativeButton save = new NativeButton("Save");
        NativeButton reset = new NativeButton("Reset");

        // Button bar
        HorizontalLayout actions = new HorizontalLayout();
        actions.add(save, reset);
        save.getStyle().set("marginRight", "10px");

        // Both email and password cannot be empty
        SerializablePredicate<String> emailOrPasswordPredicate = value -> !email
        .getValue().trim().isEmpty()
        || !password.getValue().trim().isEmpty();

        Binding<User, String> emailBinding = binder.forField(email)
            .withNullRepresentation("")
            .withValidator(emailOrPasswordPredicate,
                    "Please specify your email")
            .withValidator(new EmailValidator("Incorrect email address"))
            .bind(User::getEmail, User::setEmail);

        Binding<User, String> passwordBinding = binder.forField(password)
            .withNullRepresentation("")
            .withValidator(emailOrPasswordPredicate,
                "Please specify your password")
            .bind(User::getPassword, User::setPassword);

        // Trigger cross-field validation when the other field is changed
        email.addValueChangeListener(event -> passwordBinding.validate());
        password.addValueChangeListener(event -> emailBinding.validate());

        // Click listeners for the buttons
        save.addClickListener(event -> {
            if (binder.writeBeanIfValid(newUser)) {
                
                if(userService.createUser(newUser) == null){
                    infoLabel.setText("Error!");
                } else {
                    infoLabel.setText("Saved bean values: " + newUser);
                }
            } else {
                BinderValidationStatus<User> validate = binder.validate();
                String errorText = validate.getFieldValidationStatuses()
                        .stream().filter(BindingValidationStatus::isError)
                        .map(BindingValidationStatus::getMessage)
                        .map(Optional::get).distinct()
                        .collect(Collectors.joining(", "));
                infoLabel.setText("There are errors: " + errorText);
            }
        });
        reset.addClickListener(event -> {
            // clear fields by setting null
            binder.readBean(null);
            infoLabel.setText("");
        });

        add(formLayout, actions, infoLabel);
    }
}
