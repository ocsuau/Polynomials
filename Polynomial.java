import java.util.Arrays;

public class Polynomial {

    //fields
    private Monomio [] mon;
    private StringBuilder monStr = new StringBuilder();

    //constructors

    // Constructor per defecte. Genera un polinomi zero
    public Polynomial() {
        createValueNull();
    }

    // Constructor a partir dels coeficients del polinomi en forma d'array
    public Polynomial(float[] cfs) {
        int count = Zvalue(cfs);
        if(count != cfs.length){
            int [] exponents = fillExp(cfs.length);
            createMonArray(cfs, count, exponents);
            passToString(mon, monStr);
        }
        else{
            createValueNull();
        }
    }

    // Constructor a partir d'un string
    public Polynomial(String s) {
        String [] monOm = s.split(" ");
        float [] coeF = calcCoefStr(monOm);
        int [] expI = calcExpStr(monOm);
        int count = Zvalue(coeF);
        if(count != coeF.length){
            sortExp(coeF, expI);
            createMonArray(coeF, count, expI);
            int nulls = factMon(mon);
            if(nulls > 0){
                createNewMon(nulls);
            }
            if(mon.length == 1 && mon[0].getCoeficiente() == 0){
                createValueNull();
            }
            else {
                passToString(mon, monStr);
            }
        }
        else {
            createValueNull();
        }
    }


    //methods

    // Suma el polinomi amb un altre. No modifica el polinomi actual (this). Genera un de nou
    public Polynomial add(Polynomial p) {
        if(p.monStr.charAt(0) == '-'){
            p.monStr.delete(0,1);
            p.monStr.insert(0," - ");
        }
        else{
            p.monStr.insert(0," + ");
        }
        Polynomial result = new Polynomial (this.monStr +""+ p.monStr);
        return result;
    }

    // Multiplica el polinomi amb un altre. No modifica el polinomi actual (this). Genera un de nou
    public Polynomial mult(Polynomial p2) {
        Monomio [][] results;
        Polynomial op1;
        Polynomial op2;
        if(this.mon.length >= p2.mon.length){
            results = new Monomio [p2.mon.length][this.mon.length];
            op1 = p2;
            op2 = this;
        }
        else{
            results = new Monomio [this.mon.length][p2.mon.length];
            op1 = this;
            op2 = p2;
        }

        for(int i = 0; i < op1.mon.length; i++) {
            for (int j = 0; j < op2.mon.length; j++) {
                results[i][j] = new Monomio(op1.mon[i].getCoeficiente() * op2.mon[j].getCoeficiente(), op1.mon[i].getExponent() + op2.mon[j].getExponent());
            }
        }
        return null;
    }

    // Divideix el polinomi amb un altre. No modifica el polinomi actual (this). Genera un de nou
    // Torna el quocient i també el residu (ambdós polinomis)
    public Polynomial[] div(Polynomial p2) {
       return null;
    }

    // Troba les arrels del polinomi, ordenades de menor a major
    public float[] roots() {
        return null;
    }

    // Torna "true" si els polinomis són iguals. Això és un override d'un mètode de la classe Object

    @Override
    public boolean equals(Object o) {
        if(o instanceof Polynomial) {
            Polynomial content = (Polynomial) o;
            if (this.toString().equals(content.toString())){
                return true;
            }
        }
        return false;
    }

    // Torna la representació en forma de String del polinomi. Override d'un mètode de la classe Object

    @Override
    public String toString() {
            return monStr.toString();
    }

    void createValueNull(){
        mon = new Monomio [1];
        mon[0] = new Monomio(0,0);
        monStr.append("0");
    }

    int Zvalue(float [] cfs){
        int count = 0;
        for(int i = 0; i < cfs.length; i++){
            if(cfs[i] == 0){
                count ++;
            }
        }
        return count;
    }

    int [] fillExp(int qExponents){
        int [] retoorn = new int [qExponents];
        for(int i = 0; i < retoorn.length; i ++){
            retoorn[i] = (retoorn.length - i) - 1;
        }
        return retoorn;
    }

    void createMonArray(float [] cfs, int count, int [] exponents){
        mon = new Monomio[cfs.length - count];
        for(int i = 0, j = 0; i < cfs.length; i++){
           if(cfs[i] != 0){
                mon[j++] = new Monomio(cfs[i], exponents[i]);
            }
        }
    }

    int factMon(Monomio [] mon){
        int cont = 0;
        for(int i = 0; i < mon.length -1; i++){
            if(mon[i].getExponent() == mon[i+1].getExponent()){
                mon[i+1].setCoeficiente(mon[i].getCoeficiente() + mon[i+1].getCoeficiente());
                mon[i] = null;
                cont ++;
            }
        }
        return cont;
    }


    void createNewMon(int nulls){
        Monomio [] content = mon;
        mon = new Monomio [mon.length - nulls];
        for(int i = 0, j = 0; i < content.length; i ++){
            if(content[i] != null){
                mon[j++] = content[i];
                if(mon[j-1].getCoeficiente() == 0){
                    mon[j-1].setExponente(0);
                }
            }
        }
    }

    String passToString(Monomio [] mon, StringBuilder str){
        for(int i = 0; i < mon.length; i++){
            if(mon[i].getCoeficiente() == 0){
                continue;
            }
            if(i == 0 && mon[i].getCoeficiente() < 0){
                monStr.append("-");
            }
            else if(i > 0 && mon[i-1].getCoeficiente() != 0){
                if(mon[i].getCoeficiente() > 0){
                    monStr.append(" + ");
                }
                else{
                    monStr.append(" - ");
                }
            }
            if(!(Math.abs(mon[i].getCoeficiente()) == 1)){
                if(mon[i].getCoeficiente() < 0){
                    monStr.append(Math.abs(mon[i].getCoeficiente()));
                }
                else{
                    monStr.append(mon[i].getCoeficiente());
                }
            }
            addExponent(i, str);
        }
        return str.toString();
    }

    void addExponent(int i, StringBuilder str){
        if(mon[i].getExponent() != 0){
            str.append("x");
        }
        if(mon[i].getExponent() > 1){
            str.append("^"+mon[i].getExponent());
        }
    }

    float [] calcCoefStr(String [] monOm){
        float [] retoorn = new float [(int)Math.ceil(monOm.length / 2d)];
        for(int i = 0, j = 0; i < monOm.length; i++){
            if(monOm[i].equals("+") || monOm[i].equals("-")){
                continue;
            }
            if(monOm[i].contains("x")){
                if(monOm[i].charAt(0) != 'x') {
                    if(monOm[i].charAt(0) == '-' && monOm[i].charAt(1) == 'x'){
                        retoorn[j++] = -1;
                        continue;
                    }
                    retoorn[j++] = Integer.parseInt(monOm[i].substring(0, monOm[i].indexOf("x")));
                }
                else{
                    retoorn[j++] = 1;
                }
            }
            else{
                retoorn[j++] = Integer.parseInt(monOm[i]);
            }
            if(i > 0 && monOm[i-1].equals("-")){
                retoorn[j-1] *= -1;
            }
        }
        return retoorn;
    }

    int [] calcExpStr(String [] monOm){
        int [] retoorn = new int [(int)Math.ceil(monOm.length / 2d)];
        for(int i = 0, j = 0; i < monOm.length; i++){
            if(monOm[i].equals("+") || monOm[i].equals("-")){
                continue;
            }
            if(monOm[i].contains("x")){
                if(monOm[i].contains("^") && !(monOm[i].contains("^1 "))){
                    retoorn[j++] = Integer.parseInt(monOm[i].substring(monOm[i].indexOf("^")+1));
                }
                else{
                    retoorn[j++] = 1;
                }
            }
            else{
                retoorn[j++] = 0;
            }
        }
        return retoorn;
    }
    void sortExp(float[] coeF, int [] expI){
        int content;
        for(int i = 0; i < expI.length; i++){
            content = expI[i];
            for(int j = i + 1; j < expI.length; j++){
                if(expI[j] > expI[i]){
                    expI[i] = expI[j];
                    expI[j] = content;
                    content = expI[i];
                    sortCoe(coeF, i, j);
                }
            }
        }
    }

    void sortCoe(float [] coeF, int i, int j){
        float content = coeF[i];
        coeF[i] = coeF[j];
        coeF[j] = content;
    }
}