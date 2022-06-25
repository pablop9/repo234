package ufv.dis2010.finale.model;

import com.google.gson.GsonBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Clase que contiene la lista de tweets que se van metiendo en la aplicación
 */
public class Tweets {


    //Lista de tweets
    private ArrayList<Tweet> tweets = new ArrayList<>();

    //Uso el patrón singleton, de esta forma solo se podrá crear una única instancia de esta clase
    //Para usarla habrá que hacer Tweets.getInstance()
    private static Tweets instance = new Tweets();

    /**
     * Constructora privada para que solo se pueda crear una instancia desde la propia clase
     */
    private Tweets() {
        readTweetsFromFile();
    }

    /**
     * Método estático para el patrón Singleton, devuelve la única instancia de la clase
     * Tweets
     * @return
     */
    public static Tweets getInstance() {
        return instance;
    }

    /**
     * Añade un tweet a la lista de tweets. Si el tweet no existe, es decir, su ID en null,
     * se le asigna una id y se añade a la lista.
     * Si el tweet ya existe, su id es != null se busca ese tweet, lo borro de la lista,
     * ya que los ArraList no permiten "actualizar", y lo añado de nuevo, con el mismo id
     * @param pTweet - El tweet que se quiere insertar
     * @return Boolean indicando si el proceso se ha realizado correctamente
     */
    public boolean addTweet(Tweet pTweet){
        if(pTweet.getTweetId() == null){
            //El tweet no existe, simplemente lo añado
            pTweet.setTweetId(getNextId());
            tweets.add(pTweet);
        }else{
            //El tweet existe, asi que lo tengo que actualizar

            Tweet tweetToUpdate = getTweetById(pTweet.getTweetId());
            if(tweetToUpdate == null)
                return false;

            tweets.remove(tweetToUpdate);

            tweetToUpdate.setLastUpdated(new Date());
            tweetToUpdate.setAuthor(pTweet.getAuthor());
            tweetToUpdate.setMessage(pTweet.getMessage());



            tweets.add(tweetToUpdate);
        }

        //updateJsonFile();
        return true;
    }

    /**
     * Devuelve la siguiente ID a asignar a un tweet, así me aseguro de que no haya
     * ids repetidos.
     * @return
     */
    private Integer getNextId() {

        if(tweets.isEmpty())
            return 1;
        else{
            //Esta es una función de Java 8 seria lo mismo que hacer un bucle
            return tweets.stream()
                    .map(Tweet::getTweetId)//Por cada tweet, me genero una lista que contiene solo los ids
                    .sorted()//Ordeno esta lista de IDs para obtener el mayor
                    .findFirst()//Cojo el primero de la lista, el numero mas grande
                    .orElse(-1);//Si el objeto es null devuelvo -1, sino devuelve el propio valor
        }
    }


    /**
     * Dada una id, devuelve el tweet al que corresponde esa ID
     * @param id - La id del tweet
     * @return - El tweet con la misma id, null si no existe ninguno
     */
    private Tweet getTweetById(Integer id){
        return tweets.stream()
                .filter(t -> t.getTweetId().equals(id)) //Filtro en la lista de tweets por aquellos tweets
                                                        //Cuya id correspona con la del parametro id
                .findFirst().orElse(null);        //Devuelvo el primer elemento de la lista.
    }

    /**
     * Vuelca la lista en el fichero json para así llevar un control de la misma
     */
/*    private void updateJsonFile(){
        Resource tweetsFile = new ClassPathResource("tweets.json");
        String json = new GsonBuilder().setDateFormat("DD-MM-YYYY").create().toJson(tweets.toArray());

        try {
            PrintWriter writer = new PrintWriter(tweetsFile.getFile());
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public ArrayList<Tweet> getTweets() {
        return tweets;
    }

    /**
     * Lee el fichero json, que contiene la lista inicial de tweets
     * @return - Un Boolean indicando si el proceso se ha realizado correctamente
     */
    private Boolean readTweetsFromFile(){
        //Resource tweetsFile = resourceLoader.getResource(Constants.TWEETS_FILE_PATH);
        //File tweetsFile = new File(Constants.TWEETS_FILE_PATH);
        //Leo el fichero tweets.json
        Resource tweetsFile = new ClassPathResource("tweets.json");
        String json;
        try {
            //Lo convierto a string para poder parsearlo utilizando Gson
            json = new String(tweetsFile.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            //Para no tener problemas con las fechas, en lugar de hacer new Gson().fromJson()
            //Utilizao un GsonBuilder() que permite poner opciones para los parseos que hace la libreria.
            //El que nos interesa es el setDateFormat() para decirle el formato de fechas que queremos utilizar.
            Tweet[] jsonTweets = new GsonBuilder().setDateFormat("DD-MM-YYYY").create().fromJson(json, Tweet[].class);
            if(jsonTweets == null)
                return false;
            Arrays.stream(jsonTweets).forEach(t -> tweets.add(t));
            return true;
        } catch (IOException e) {
            System.err.println("Error reading json file: " + e.getMessage());
            return false;
        }
    }

}
