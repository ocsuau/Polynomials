import java.util.Arrays;

public class Polynomial {
    private float [] mon;
    private StringBuilder monStr = new StringBuilder("");

    // Constructor per defecte. Genera un polinomi zero
    public Polynomial() {
        mon = new float[]{0};
        passToString();
    }

    // Constructor a partir dels coeficients del polinomi en forma d'array
    public Polynomial(float[] cfs) {
        mon = cfs;
        passToString();
    }

    // Constructor a partir d'un string
    public Polynomial(String s) {
        s = s.replace(" + "," +");
        s = s.replace(" - "," -");
        String [] monOm = s.split(" ");
        monOm[0] = (monOm[0].charAt(0) != '-') ? '+' + monOm[0]: monOm[0];
        int [] expI = calcExpStr(monOm);
        assignValues(monOm, expI);
        passToString();
    }

    // Suma el polinomi amb un altre. No modifica el polinomi actual (this). Genera un de nou
    public Polynomial add(Polynomial p) {
        if(p.monStr.charAt(0) == '-'){
            p.monStr.delete(0,1);
            p.monStr.insert(0," - ");
        }
        else{
            p.monStr.insert(0," + ");
        }
        return new Polynomial (this.monStr +""+ p.monStr);
    }

    // Multiplica el polinomi amb un altre. No modifica el polinomi actual (this). Genera un de nou
    public Polynomial mult(Polynomial p2) {
        float[] provisional = new float[(this.mon.length + p2.mon.length) - 1];
        Polynomial result = new Polynomial();
        for (int i = 0; i < this.mon.length; i++) {
            if (this.mon[i] == 0) {
                continue;
            }
            for (int j = 0; j < p2.mon.length; j++) {
                if (p2.mon[j] == 0) {
                    continue;
                }
                    provisional[provisional.length - (((this.mon.length - i)) + ((p2.mon.length - j) - 1))] = this.mon[i] * p2.mon[j];
            }
            result = result.add(new Polynomial(provisional));
            Arrays.fill(provisional, 0);
        }
        return result;
    }

    // Divideix el polinomi amb un altre. No modifica el polinomi actual (this). Genera un de nou
    // Torna el quocient i també el residu (ambdós polinomis)
    public Polynomial[] div(Polynomial p2) {
        Polynomial provi = new Polynomial(this.monStr.toString());
        Polynomial indiValue;
        Polynomial [] result = new Polynomial [] {new Polynomial(), new Polynomial()};
        float [] provisional;
        while(provi.mon.length >= p2.mon.length) {
            provisional = new float [(provi.mon.length - p2.mon.length) + 1];
            provisional[0] = ((int) (provi.mon[0] / p2.mon[0]));
            indiValue = new Polynomial(provisional);
            result[0] = result[0].add(indiValue);
            indiValue = p2.mult(indiValue);
            indiValue.changeSign(indiValue);
            indiValue = new Polynomial(indiValue.mon);
            provi = provi.add(indiValue);
        }
        result[1] = new Polynomial(provi.toString());
        return result;
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
        float [] retoorn;
        int expQuad = bQuad();
        float provisional;
        if ((this.mon.length - Zvalue(this.mon) == 2) && this.mon[this.mon.length - 1] != 0) {
            provisional = this.mon[this.mon.length - 1];
            provisional *= -1;
            if ((this.mon.length - 1) % 2 == 0) {
                if (this.mon[this.mon.length - 1] < 0) {
                    return new float[]{(float) (Math.pow(provisional, 1 / (this.mon.length - 1f))) * -1, (float) Math.pow(provisional, 1 / (this.mon.length - 1f))};
                }
            } else {
                if (this.mon[this.mon.length - 1] < 0) {
                    return new float[]{(float) (Math.pow(provisional, 1 / (this.mon.length - 1f)))};
                } else {
                    provisional *= -1;
                    return new float[]{(float) (Math.pow(provisional, 1 / (this.mon.length - 1f))) * -1};
                }
            }
        }
        else if(this.mon.length - Zvalue(this.mon) == 3 || expQuad > 0){
            retoorn = secDegree(this.mon);
            if(this.mon.length - 1 == 2){
                return retoorn;
            }
            else{
                for (int i = 0; i < retoorn.length; i++) {
                    retoorn[i] = (float) (Math.pow(retoorn[i], 1 / (float) expQuad));
                }
                float[] retoorn2 = new float[retoorn.length * 2];
                return createNegative(retoorn2, retoorn);
            }
        }

        /*float provi;
        float[] retoorn;
        if (this.mon.length - Zvalue(this.mon) == 2) {
            if ((this.mon[this.mon.length - 1] > 0 && (this.mon.length - 1) % 2 == 0) || this.mon[this.mon.length - 1] == 0) {
                return null;
            } else {
                if (this.mon[this.mon.length - 1] > 0 && (this.mon.length - 1) % 2 != 0) {
                    retoorn = new float[]{(float) (Math.pow(this.mon[this.mon.length - 1], 1 / (this.mon.length - 1f))) * -1};
                    return retoorn;
                } else {
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
        } else if (this.mon.length - Zvalue(this.mon) == 3) {
            if (this.mon.length - 1 == 2) {
                return secDegree(this.mon);
            } else {
                int expProvi = bQuad();
                if (expProvi > 0) {
                    retoorn = secDegree(this.mon);
                    for (int i = 0; i < retoorn.length; i++) {
                        retoorn[i] = (float) (Math.pow(retoorn[i], 1 / (float) expProvi));
                    }
                    float[] retoorn2 = new float[retoorn.length * 2];
                    createNegative(retoorn2, retoorn);
                    Arrays.sort(retoorn2);
                    return retoorn2;
                }
            }
        }
        else{
            return calcRuf();
        }*/
            return null;
    }

    float [] secDegree(float [] monF) {
        float[] retoorn;
        int count = 0;
        float a = 0, b = 0, c = 0;
        for (float f : monF) {
            if (f != 0) {
                if (count == 0) {
                    a = f;
                } else if (count == 1) {
                    b = f;
                } else {
                    c = f;
                }
                count++;
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
        for(int i = 1; i < this.mon.length; i++){
            if(this.mon[i] == 0){
                continue;
            }
            else if((this.mon.length - 1) - i > 1 && (this.mon.length - 1) - i == provisional / 2){
                provisional = (this.mon.length - 1) - i;
            }
            else if(i != this.mon.length - 1){
                return -1;
            }
        }
        return provisional;
    }

    float [] calcRuf(){
        int divPro= 1;
        int divPro2;
        int nRuf = (this.mon.length - 1) - 2;
        float [] retoorn1 = new float[nRuf];
        Polynomial rufProv2 = new Polynomial (this.mon);
        float [] rufProv3 = new float [rufProv2.mon.length - 1];
        for(int i = 0; i < nRuf; i++) {
            while (divPro != rufProv2.mon[rufProv2.mon.length - 1]) {
                if (rufProv2.mon[rufProv2.mon.length - 1] % divPro == 0) {
                    divPro2 = Ruffini(rufProv2, rufProv3, divPro);
                    if (Math.abs(divPro2) == Math.abs(divPro)) {
                        rufProv2 = new Polynomial (rufProv3);
                        rufProv3 = new float [rufProv2.mon.length - 1];
                        retoorn1[i] =  divPro2;
                        divPro = 1;
                        break;
                    }
                    else if(Math.abs(divPro) != Math.abs(rufProv2.mon[rufProv2.mon.length - 1])){
                        divPro++;
                    }
                    else{
                        break;
                    }
                }
            }
        }
        float [] retoorn2 = secDegree(rufProv2.mon);
        float [] finalRetoorn = new float[retoorn1.length + retoorn2.length];
        reasignarValores(finalRetoorn, retoorn1);
        reasignarValores(finalRetoorn, retoorn2);
        Arrays.sort(finalRetoorn);
        return finalRetoorn;
    }

    void reasignarValores(float [] finalRetoorn, float [] retoorn){
        int provisional = 0;
        for(int j = 0; j < finalRetoorn.length;j++){
            if(finalRetoorn[j] == 0){
                finalRetoorn[j] = retoorn[provisional++];
            }
            if(provisional == retoorn.length){
                break;
            }
        }
    }
    float [] createNegative(float [] re2, float [] re1){
        for(int i = 0, count = 0; i < re2.length; i++){
            re2[i++] = re1[count++];
            re2[i] = re2[i - 1] * -1;
        }
        Arrays.sort(re2);
        return re2;
    }

    int Ruffini(Polynomial rufProv2, float [] rufProv3, int divPro){
        double provisional;
        rufProv3[0] = rufProv2.mon[0];
        for(int i = 1; i < rufProv2.mon.length; i++){
            provisional = divPro*rufProv3[i - 1];
            rufProv3[i] = rufProv2.mon[i] + (float) provisional;
            if(i == rufProv3.length - 1){
                provisional = divPro * rufProv3[i];
                if(rufProv2.mon[i + 1] + (float) provisional == 0){
                    return divPro;
                }
                else{
                    if(divPro > 0){
                        rufProv3[0] = rufProv2.mon[0];
                        i = 0;
                        divPro *= -1;
                    }
                    else{
                        return 0;
                    }
                }
            }
        }
        return 0;
    }

    // Torna "true" si els polinomis són iguals. Això és un override d'un mètode de la classe Object
    @Override
    public boolean equals(Object o){
        if(o instanceof Polynomial){
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

    int Zvalue(float [] cfs){
        int count = 0;
        for(float prov : cfs){
            if(prov == 0){
                count ++;
            }
        }
        return count;
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
                    monStr.append((mon[i] > 0) ? " + " : " - ");
                }
            }
            if(!(Math.abs(mon[i]) == 1) || ((i == mon.length - 1) && (Math.abs(mon[i]) == 1))){
                monStr.append(Math.abs((int) mon[i]));
            }
            if((mon.length -i) - 1 > 0){
                monStr.append("x");
                if((mon.length - i) - 1 > 1){
                    monStr.append("^" + ((mon.length -i) -1));
                }
            }
        }
        if(monStr.length() == 0){
            monStr.append("0");
        }
    }

    void assignValues(String [] monOm, int [] expI){
        for(int i = 0; i < monOm.length; i++){
            if(monOm[i].contains("x")){
                if(monOm[i].charAt(1) == 'x') {
                    mon[(mon.length - 1) - expI[i]] += (monOm[i].charAt(0) == '-') ? -1 : 1;
                }
                else{
                    mon[(mon.length - 1) - expI[i]] += Integer.parseInt(monOm[i].substring(0, monOm[i].indexOf("x")));
                }
            }
            else{
                mon[(mon.length - 1) - expI[i]] += Integer.parseInt(monOm[i]);
            }
        }
    }

    int [] calcExpStr(String [] monOm){
        int [] retoorn = new int [monOm.length];
        int maxExpo = 0;
        for(int i = 0; i < monOm.length; i++){
            if(monOm[i].contains("x")){
                retoorn[i] = (monOm[i].contains("^") && !(monOm[i].contains("^1 "))) ? Integer.parseInt(monOm[i].substring(monOm[i].indexOf("^") + 1)) : 1;
            }
            else{
                retoorn[i] = 0;
            }
            if(maxExpo < retoorn[i]){
                maxExpo = retoorn[i];
            }
        }
        this.mon = new float[maxExpo + 1];
        return retoorn;
    }
}