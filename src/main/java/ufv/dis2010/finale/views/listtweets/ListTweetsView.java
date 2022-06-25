package ufv.dis2010.finale.views.listtweets;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.ocpsoft.prettytime.PrettyTime;
import ufv.dis2010.finale.model.Tweet;
import ufv.dis2010.finale.model.Tweets;
import ufv.dis2010.finale.views.MainLayout;

/**
 * Vista "principal" que muestra la lista de tweets
 */
@Route(value = "", layout = MainLayout.class)
public class ListTweetsView extends VerticalLayout {

    //El formulario va embebido dentro de esta vista
    TweetForm form;

    //El grid, es como una lista, que ontendrá los tweets a mostrar
    Grid<Tweet> grid = new Grid<>(Tweet.class);

    public ListTweetsView() {

        addClassName("tweet-list");

        //Esto es para que me ocupe toda la pantalla
        setSizeFull();

        configureGrid();
        configureForm();

        add(
                getToolbar(),
                getContent()
        );

        updateList();
        closeEditor();
    }

    /**
     * Este metodo es para crear el layout formado por el grid y el formulario
     * @return
     */
    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }


    /**
     * SImplemente para cuando haga algun cambio en la lista habrá que llamar a este método,
     * el cual vuelve a coger los datos de la lista de tweets y me los pondrá en el grid
     */
    private void updateList() {
        grid.setItems(Tweets.getInstance().getTweets());
    }


    /**
     * Cierro el formulario, lo vuelvo invisible
     */
    private void closeEditor() {
        form.setTweet(null);
        form.setVisible(false);
        removeClassName("editing");
    }


    /**
     * Configuro el tamaño del formulario y los eventos que tendrá este
     */
    private void configureForm() {
        form = new TweetForm();
        form.setWidth("25em");

        //Hago un listener que lo que hace es esperar a que se dispare un evento de tipo
        //SaveEvent (Está creado en la clase TweetForm), cuando eso pase se llama al metodo saveEvent()
        form.addListener(TweetForm.SaveEvent.class, this::saveTweet);
        //form.addListener(TweetForm.EditEvent.class, this::editTweet);
        form.addListener(TweetForm.CloseEvent.class, e -> closeEditor());

    }


    /**
     * Me va a permitir poner encima del grid una barra, en este caso, solo tendrá un boton para añadir un tweet.
     * Cuando le haga click se llamará al metodo addTweet(), el cual abre el formulario para crear tweets nuevos.
     * @return
     */
    private Component getToolbar(){

        Button button = new Button("Add Tweet");
        button.addClickListener(e -> addTweet());

        HorizontalLayout toolbar = new HorizontalLayout(button);
        toolbar.addClassName("toolbar");

        return toolbar;

    }

    private void addTweet() {
        //Elimino el tweet que tenga solucionado, para que no haya conflictos
        grid.asSingleSelect().clear();
        //Llamo al metodo que abre el formulario, le paso un tweet nuevo, en lugar de uno de la lista
        editTweet(new Tweet());
    }

    /**
     * Despues de pulsar el boton de save del form y saltar el evento correspondiente se llama a este metodo,
     * Lo primero que hago es guardar el tweet en la lista de tweets. Despues actualizo el grid con el nuevo tweet
     * añadido o editado
     * Por último, cierro el formulario
     * @param event
     */
    private void saveTweet(TweetForm.SaveEvent event) {
        Tweets.getInstance().addTweet(event.getTweet());
        updateList();
        closeEditor();
    }

    /**
     * Configuro el grid que mostrará los tweets
     */
    private void configureGrid(){
        grid.addClassName("tweet-grid");
        grid.setSizeFull();

        //Añado primero dos columnas llamadas author y message (Tienen que tener el mismo nombre que los campos de
        // la clase tweet)
        grid.setColumns("author", "message");
        //La columna de la fecha tiene que aparecer con el pretty time este de mierda
        //Lo que hago es decirle que en esa columna, para cada tweet, me vas a formatear su fecha de
        //ultima actualizacion utilizando prettytime, por último, a esa columna la vas a llamar LastUpdated
        grid.addColumn(tweet -> new PrettyTime().format(tweet.getLastUpdated())).setHeader("LastUpdated");

        //por ultimo digo que cada columna tenga automatice su anchura para no tener que preocuparme yo
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        //Le añado un listener a los tweets para que cuando haga click en una fila se abra el formulario y tenga
        //como contenido los campos de ese tweet para poder editarlo.
        grid.asSingleSelect().addValueChangeListener(e -> editTweet(e.getValue()));

    }

    /**
     * Abre el formulario para poder editar el tweet que toque
     * @param tweet
     */
    private void editTweet(Tweet tweet) {
        if(tweet == null){
            //Control de errores
            closeEditor();
        }else{
            //asigno el tweet a mostrar
            form.setTweet(tweet);
            //Pongo el formulario visible
            form.setVisible(true);
            addClassName("editing");
        }
    }
}
