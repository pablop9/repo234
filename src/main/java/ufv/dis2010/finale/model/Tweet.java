package ufv.dis2010.finale.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * La clase tweet, es la que contiene toda la información de los tweets que hay guardados
 */
@Data
@AllArgsConstructor
public class Tweet {

    //El campo ID me permite saber a que tweet hago referencia a la hora de editar.
    //Se podria prescindir de él y usar el método .equals() pero con el ID creo que es más sencillo
    private Integer tweetId;

    private String author;

    private String message;

    //Format --> DD-MM-YYYY
    @JsonFormat(pattern = "DD-MM-YYYY")
    private Date lastUpdated;

    //En la constructora inicializo la fecha para que no la tenga crear yo cada vez que cree un tweet
    //que sino es mucho coñazo
    public Tweet() {
        lastUpdated = new Date();
    }
}
