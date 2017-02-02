/**
 * Created by Oscar on 01/02/2017.
 */
public class Monomio {

    //fields
    private int coeficiente;
    private int exponente;

    //constructors

    Monomio(){}

    Monomio(float coeficiente, int exponente){
        this.coeficiente = (int) coeficiente;
        this.exponente = exponente;
    }

    int getCoeficiente(){
        return coeficiente;
    }

    int getExponent(){
        return exponente;
    }

    public void setCoeficiente(int coeficiente){
        this.coeficiente = coeficiente;
    }

    public void setExponente(int exponente){
        this.exponente = exponente;
    }

    boolean equals(Monomio content){
        return this.coeficiente == content.coeficiente && this.exponente == content.exponente;
    }
}
