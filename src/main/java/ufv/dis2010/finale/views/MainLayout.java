package ufv.dis2010.finale.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import ufv.dis2010.finale.views.listtweets.ListTweetsView;

/**
 * Vista principal, es la que sirve de layout para el resto de vistas
 */
public class MainLayout extends AppLayout {

    public MainLayout(){
        createHeader();
        createDrawer();
    }

    /**
     * Aqui se crea el menu de la izquierda de la pantalla para navegar
     */
    private void createDrawer() {
        //El objeto RouterLink me permite nevegar entre vistas
        //Le paso como parametro el texto que quiero que muestre y la vista a la que quiero que me lleve
        RouterLink listView = new RouterLink("List Tweets", ListTweetsView.class);

        //Esta opción simplemente permite que cuando tenga el cursor encima se subraye el texto, es para que haga bonito
        listView.setHighlightCondition(HighlightConditions.sameLocation());

        //Aqui ire añadiendo las distintas listas dentro de un verical layout pues para que se pongan en vertical
        addToDrawer(new VerticalLayout(
                listView
        ));

    }

    /**
     * Creación de la cabecera de nuestra pagina web
     */
    private void createHeader() {
        H1 logo = new H1("Examen DIS Final");
        logo.addClassNames("text-l", "m-m");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);

        //Parametros al texto y asi simplemente para que se ponga chupiguay
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }
}
