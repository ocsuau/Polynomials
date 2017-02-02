import java.util.Arrays;

public class Polynomial {

    //fields
    private float [] mon;
    private int maxExp = 0;
    private StringBuilder monStr = new StringBuilder("");
    private float [] coeF;
    private int [] expI;
    //constructors

    // Constructor per defecte. Genera un polinomi zero
    public Polynomial() {
        createValueNull();
    }

    // Constructor a partir dels coeficients del polinomi en forma d'array
    public Polynomial(float[] cfs) {
        int count = Zvalue(cfs);
        if(count != cfs.length){
            mon = cfs;
            passToString();
        }
        else{
            createValueNull();
        }
    }

    // Constructor a partir d'un string
    public Polynomial(String s) {
        String [] monOm = s.split(" ");
        expI = calcExpStr(monOm);
        coeF = calcCoefStr(monOm);
        int count = Zvalue(coeF);
        if(count != coeF.length){
            mon = new float [maxExp + 1];
            sortExp(coeF, expI);
            factMon(expI, coeF);
            count = Zvalue(coeF);
            if(count != coeF.length) {
                toAsignValues(coeF, expI);
                passToString();
            }
            else{
                createValueNull();
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
        Polynomial p;
        float [] provisional = new float [(this.mon.length + p2.mon.length) - 1];
        Polynomial result = new Polynomial();
        Polynomial op1;
        Polynomial op2;

        //comprobar coincidencias entre op2.mon y op2 y lo correspondiente con op1
        if(this.mon.length >= p2.mon.length){
            op1 = p2;
            op2 = this;
        }
        else{
            op1 = this;
            op2 = p2;
        }

        for(int i = 0; i < op1.mon.length; i++) {
            if(op1.mon[i] == 0){
                continue;
            }
            for (int j = 0; j < op2.mon.length; j++) {
                if(op2.mon[j] == 0){
                    continue;
                }
                //fotre
                provisional[provisional.length - (((op1.mon.length - i)) + ((op2.mon.length - j) - 1))] = op1.mon[i] * op2.mon[j];
                //results[i][j] = new Monomio(op1.mon[i].getCoeficiente() * op2.mon[j].getCoeficiente(), op1.mon[i].getExponent() + op2.mon[j].getExponent());
            }
            p = new Polynomial(provisional);
            result = result.add(p);
            Arrays.fill(provisional, 0);
        }
        return result;
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
            if (this.monStr.toString().equals(content.monStr.toString())){
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
        mon = new float [1];
        mon[0] = 0;
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

    void toAsignValues(float [] coeF, int [] expI){
        Arrays.fill(mon, 0);
        for(int i = 0; i < expI.length; i++){
            if(coeF[i] == 0){
                continue;
            }
            mon[(mon.length - expI[i]) - 1] = coeF[i];
        }
    }

    void factMon(int [] expI, float [] coeF){
        for(int i = 0; i < expI.length - 1; i++){
            if(expI[i] == expI[i+1]){
                coeF[i+1] = coeF[i] + coeF[i+1];
                expI[i] = 0;
                coeF[i] = 0;
            }
        }
    }

    void passToString(){
        for(int i = 0; i < mon.length; i++){
            if(mon[i] == 0){
                continue;
            }
            if(monStr.length() == 0 && mon[i] < 0){
                monStr.append("-");
            }
            else if(i > 0){
                if(monStr.length() > 0) {
                    if (mon[i] > 0) {
                        monStr.append(" + ");
                    } else {
                        monStr.append(" - ");
                    }
                }
            }
            if(!(Math.abs(mon[i]) == 1) || ((i == mon.length - 1) && (Math.abs(mon[i]) == 1))){
                if(mon[i] < 0){
                    monStr.append(Math.abs((int) mon[i]));
                }
                else{
                    monStr.append((int) mon[i]);
                }
            }
            addExponent(i);
        }
    }

    void addExponent(int i){
        if((mon.length -i) - 1 > 0){
            monStr.append("x");
        }
        if((mon.length - i) - 1 > 1){
            monStr.append("^" + ((mon.length -i) -1));
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
            if(maxExp < retoorn[j-1]){
                maxExp = retoorn[j-1];
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