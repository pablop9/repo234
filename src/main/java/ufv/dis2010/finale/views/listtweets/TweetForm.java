package ufv.dis2010.finale.views.listtweets;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import lombok.Data;
import ufv.dis2010.finale.model.Tweet;
import ufv.dis2010.finale.views.MainLayout;

@Data
@Route(value = "form", layout = MainLayout.class)
public class TweetForm extends FormLayout {

    //Este atributo me permite ligar los campos del formulario con los campos de la clase Tweet
    Binder<Tweet> binder = new BeanValidationBinder<>(Tweet.class);

    //El tweet que contendrá el formulario
    Tweet tweet;

    //Los dos campos que tendrá el formulario
    TextField author = new TextField("Author");
    TextField message = new TextField("Message");


    //Botones del formulario
    Button save = new Button("Save");
    //Button edit = new Button("Edit");
    Button cancel = new Button("Cancel");

    public TweetForm() {

        addClassName("tweet-form");
        binder.bindInstanceFields(this);

        add(
                author,
                message,
                createButtonLayout()
        );

    }

    /**
     * Cuando seleccione un tweet de la lista se llamará a este metodo.
     * Lo primero que hago es asignar el tweet al atributo correspondiente.
     * Luego uso el binder.readBean() para que se muestre el contenido del tweet
     * en los campos del formulario
     * @param tweet
     */
    public void setTweet(Tweet tweet){
        this.tweet = tweet;
        binder.readBean(tweet);
    }

    /**
     * Pongo bonitos los botones y les añado los eventos que tienen que realizar.
     * @return
     */
    private Component createButtonLayout() {

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        //edit.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        //Cuando haga click en el boton de save se llamará al metodo validateAndSave()
        save.addClickListener(event -> validateAndSave());
        //edit.addClickListener(event -> fireEvent(new EditEvent(this, tweet)));

        //Cuando haga click en el botón de cancel, se llamarña al evento CloseEvent
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        //Esto es para poder hacer click con las teclas, en el caso del save me servirá el Enter
        //En el caso de cancel me servirá el escape
        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        //Meto los botones en un layaout horizantal para que salgan uno al lado del otro
        return new HorizontalLayout(save, cancel);
    }


    /**
     * Guarda el tweet en la lista de tweets
     */
    private void validateAndSave() {
        try {
            //Usando el binder guardo en el atributo tweet lo que se ha escrito en el formulario
            binder.writeBean(tweet);
            //Tweets.getInstance().addTweet(tweet);
            //Disparo un evento de tipo guardar para que se guarde el tweet
            fireEvent(new SaveEvent(this, tweet));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Events
    //Clases para controlar los eventos del formulario
    public static abstract class TweetFormEvent extends ComponentEvent<TweetForm> {
        private Tweet tweet;

        protected TweetFormEvent(TweetForm source, Tweet tweet) {
            super(source, false);
            this.tweet = tweet;
        }

        public Tweet getTweet() {
            return tweet;
        }
    }

    public static class SaveEvent extends TweetFormEvent {
        SaveEvent(TweetForm source, Tweet tweet) {
            super(source, tweet);
        }
    }

    public static class CloseEvent extends TweetFormEvent {
        CloseEvent(TweetForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
