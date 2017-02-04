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
        Polynomial provi = this;
        Polynomial [] retoorn = new Polynomial [2];
        Polynomial productOne;
        Polynomial fullProduct;
        StringBuilder sb = new StringBuilder();
        int posFirstV;
        int firstExp;
        //for(int i = 0; this.expI[i] > p2.expI[0]; i++){
        while (true){
            posFirstV = findFirstValue(provi);
            firstExp = ((provi.mon.length - 1) - posFirstV);
            if(firstExp < p2.mon.length - 1){
                retoorn[1] = new Polynomial(provi.toString());
                return retoorn;
            }
            sb.append((int) (provi.mon[posFirstV] / p2.mon[0]));
            if((firstExp) - (p2.mon.length -1) > 0){
                sb.append("x");
                if((firstExp) - (p2.mon.length -1) > 1){
                    sb.append("^" + ((firstExp) - (p2.mon.length -1)));
                }
            }
            productOne = new Polynomial(sb.toString());
            if(retoorn[0] == null){
                retoorn[0] = new Polynomial(productOne.toString());
            }
            else{
                retoorn[0] = retoorn[0].add(productOne);
            }
            fullProduct = p2.mult(productOne);
            changeSign(fullProduct);
            fullProduct = new Polynomial(fullProduct.mon);
            provi = provi.add(fullProduct);
            sb.delete(0,sb.length());
        }
    }

    private int findFirstValue(Polynomial p){
        for(int i = 0; i < p.mon.length; i++){
            if(p.mon[i] != 0){
                return i;
            }
        }
        return 0;
    }

    void changeSign(Polynomial fullProduct){
        for(int i = 0; i < fullProduct.mon.length; i++){
            if(fullProduct.mon[i] != 0){
                fullProduct.mon[i] *= -1;
            }
        }
    }

    // Troba les arrels del polinomi, ordenades de menor a major
    public float[] roots() {

        float provi;
        float [] retoorn;
        if(this.coeF.length - Zvalue(this.coeF) == 2){
            if((this.mon[this.mon.length - 1] > 0 && (this.mon.length - 1) % 2 == 0) || this.mon[this.mon.length -1] == 0) {
                return null;
            }
            else{
                if(this.mon[this.mon.length - 1] > 0 && (this.mon.length - 1) % 2 != 0){
                    retoorn = new float[]{(float)(Math.pow(this.mon[this.mon.length - 1], 1 / (float) (this.mon.length - 1f))) * -1};
                    return retoorn;
            }
                else{
                    if ((this.mon.length - 1) == 1) {
                        retoorn = new float[]{this.mon[this.mon.length - 1] * -1};
                        return retoorn;
                    }
                    provi = this.mon[this.mon.length - 1];
                    provi *= -1;
                    retoorn = new float[]{(float) (Math.pow(provi, 1 / (float) (this.mon.length - 1f))) * -1, (float) Math.pow(provi, 1 / (float) (this.mon.length - 1f))};
                    return retoorn;
                }
            }
        }
        else if(this.mon.length - Zvalue(this.mon) == 3){
            if(this.mon.length -1 == 2) {
                return retoorn = secDegree();
            }
            else{
                int expProvi = bQuad();
                if(expProvi > 0){
                    retoorn = secDegree();
//                    if(expProvi > 2) {
//                        for (int i = 0; i < retoorn.length; i++) {
//                            retoorn[i] = (float) (Math.pow(retoorn[i], 1 / (float) expProvi));
//                        }
//                    }
                    for (int i = 0; i < retoorn.length; i++) {
                        retoorn[i] = (float) (Math.pow(retoorn[i], 1 / (float) expProvi));
                    }
                    float [] retoorn2 = new float[retoorn.length * 2];
                    createNegative(retoorn2, retoorn);
                    Arrays.sort(retoorn2);
                    return retoorn2;
                }
            }
        }
        System.out.println(Arrays.toString(this.mon));
        System.out.println(Arrays.toString(this.coeF));
        System.out.println(Arrays.toString(this.expI));
        System.out.println(this.maxExp);
        return null;
    }

    float [] secDegree(){
        float [] retoorn;
        int count = 0;
        float a = 0;
        float b = 0;
        float c = 0;
        for(int i = 0; i < this.mon.length; i++){
            if(this.mon[i] != 0) {
                if (count == 0) {
                    a = this.mon[i];
                    count = 1;
                }else if (count == 1) {
                    b = this.mon[i];
                    count = 2;
                }else{
                    c = this.mon[i];
                }
            }
        }

        float disc = (float)(Math.pow(b,2) + (-4 * a * c));

        if(disc < 0){
            return null;
        }
        else if(disc == 0 ){
            retoorn = new float []{(b * -1) / (2 * a)};
        }
        else{
            retoorn = new float []{((b * -1) + (float) Math.sqrt(disc)) / (2 * a), ((b * -1) - (float)Math.sqrt(disc)) / (2 * a)};
            Arrays.sort(retoorn);
        }
        return retoorn;
    }

    int bQuad(){
        int provisional = this.mon.length - 1;
        for(int i = this.mon.length - 1; i >= 0; i--){
            if(this.mon[i] == 0){
                continue;
            }
            else if((this.mon.length - 1) - i > 1 && (this.mon.length - 1) - i < provisional){
                provisional = (this.mon.length - 1) - i;
            }
            else if(((this.mon.length - 1) - i) % provisional != 0){
                provisional = -1;
            }
        }
        return provisional;
    }
    void createNegative(float [] re2, float [] re1){
        for(int i = 0, count = 0; i < re2.length; i++){
            re2[i++] = re1[count++];
            re2[i] = re2[i - 1] * -1;
        }
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