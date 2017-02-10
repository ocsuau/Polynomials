import java.util.Arrays;

public class Polynomial {

    //Variables miembro float y StringBuilder (Lo considero variable miembro para generarlo al crear el objeto y no cada vez que
    //se llama a toString(),
    private float [] mon;
    private StringBuilder monStr = new StringBuilder("");

    //Constructor por defecto. Genera Polynomio con valor 0
    public Polynomial() {
        mon = new float[]{0};
        monStr.append("0");
    }

    //Constructor a partir de array tipo float. Generamos el StringBuilder
    public Polynomial(float[] cfs) {
        mon = cfs;
        passToString();
    }

    //Constructor a partir de String. Generamos array tipo float y el StringBuilder con los valores factorizados.
    public Polynomial(String s) {
        //Si el primer valor es positivo, le ponemos el signo delante para no cambiar el código solo por el primer monomio.
        //(si el valor es negativo, ya viene con el signo incluído).
        s = (s.charAt(0) != '-') ? '+' + s : s;

        //Juntamos los signos con sus respectivos monomios para no tener operadores matemáticos ocupando elementos de la matriz de strings.
        //Seguidamente creamos la matriz cortando el string por cada espacio.
        s = s.replace(" + "," +");
        s = s.replace(" - "," -");
        String [] monOm = s.split(" ");

        //Creamos matriz donde almacenamos los exponentes (con el orden respectivo a sus coeficientes) y al mismo tiempo calculamos el
        //mayor exponente, que nos permite inicializar la variable miembro float con la longitud fija.
        int [] expI = calcExpStr(monOm);

        //Asignamos valores al float miembro y los colocamos en las posiciones correspondientes dependiendo de sus exponentes. Seguidamente,
        //construímos el stringBuilder miembro.
        assignValues(monOm, expI);
        passToString();
    }

    //Guardamos los exponentes en un array para poder colocar los coeficientes en la posición correspondiente del array miembro.
    //También calculamos el valor del mayor exponente para inicializar el array miembro con tamaño fijo.
    private int[] calcExpStr(String[] monOm) {
        int[] retoorn = new int[monOm.length];
        int maxExpo = 0;
        //Comprobamos si el elemento string en cuestión contiene el carácter "x" y, en ese caso, si contiene el carácter "^" y no va
        //seguido de un 1, el exponente será igual a el número que siga al carácter "^", si no, será igual a 1.

        //Si no contiene "x", el exponente será igual a 0.
        for (int i = 0; i < monOm.length; i++) {
            if (monOm[i].contains("x")) {
                retoorn[i] = (monOm[i].contains("^") && !(monOm[i].contains("^1 "))) ? Integer.parseInt(monOm[i].substring(monOm[i].indexOf("^") + 1)) : 1;
            } else {
                retoorn[i] = 0;
            }

            //Calculamos el mayor exponente.
            if (maxExpo < retoorn[i]) {
                maxExpo = retoorn[i];
            }
        }

        //Inicializamos el array miembro con una longitud igual al mayor exponente.
        this.mon = new float[maxExpo + 1];
        return retoorn;
    }

    //Calculamos el coeficiente correspondiente a partir del array de string y lo sumamos al valor que está en la posición correspondiente
    // en el array miembro a partir de la longitud del array miembro menos el exponente correspondiente a ese coeficiente. (De este modo,
    //al mismo tiempo que asignamos los coeficientes en sus respectivas posiciones, los factorizamos)
    private void assignValues(String[] monOm, int[] expI) {
        for (int i = 0; i < monOm.length; i++) {
            if (monOm[i].contains("x")) {
                //El primer carácter de cada string es su signo, el segundo solo puede ser el primer dígito del coeficiente o la incógnita
                if (monOm[i].charAt(1) == 'x') {
                    //En caso de que el segundo carácter sea la incógnita, dependiendo del signo, sumaremos 1 o -1.
                    mon[(mon.length - 1) - expI[i]] += (monOm[i].charAt(0) == '-') ? -1 : 1;
                } else {
                    //Si el segundo carácter no es la incógnita, significa que existe coeficiente y por lo tanto podemos hacer un parseInt
                    //del substring del elemento en cuestión hasta la posición de la incógnita.
                    mon[(mon.length - 1) - expI[i]] += Integer.parseInt(monOm[i].substring(0, monOm[i].indexOf("x")));
                }
            } else {
                //En este punto sabemos que el elemento en cuestión no contiene incógnita, así que realizamos parseInt de elemento en cuestión.
                mon[(mon.length - 1) - expI[i]] += Integer.parseInt(monOm[i]);
            }
        }
    }

    //Sumamos polinomios. (No modificamos ningún polinomio, retornamos el resultado de la suma)
    public Polynomial add(Polynomial p) {
        //En esta función, a partir de los stringBuilder de cada objeto, los juntamos y creamos un nuevo objeto pasándole como parámetro
        //los dos stringBuilder en uno, ya que al contruir el objeto, factorizamos sus monomios, y nos ofrece los mismos resultados que
        //sumar los monomios. (Previamente, añadimos los signos de sumar y/o restar a uno de los stringBuilder para que el objeto se pueda
        //contruir correctamente.
        if(p.monStr.charAt(0) == '-'){
            p.monStr.delete(0,1);
            p.monStr.insert(0," - ");
        }
        else{
            p.monStr.insert(0," + ");
        }
        return new Polynomial (this.monStr +""+ p.monStr);
    }

    //Multiplicamos polinomios. (No modificamos ningún polinomio, retornamos el resultado de la multiplicación)
    public Polynomial mult(Polynomial p2) {
        //En provisional iremos almacenando la multiplicación de todos y cada uno de los monomios de uno de los polinomios con cada
        //uno de los monomios del otro polinomio.

        //En result iremos almacenando la suma de los polinomios que vayamos almacenando en provisional.
        float[] provisional = new float[(this.mon.length + p2.mon.length) - 1];
        Polynomial result = new Polynomial();
        //Si uno de los valores del float miembro de cualquiera de los objetos es igual a 0, volvemos a la condición de la iteración.
        //(para no hacer trabajar al algoritmo de forma inecesaria)
        for (int i = 0; i < this.mon.length; i++) {
            if (this.mon[i] == 0) {
                continue;
            }
            for (int j = 0; j < p2.mon.length; j++) {
                if (p2.mon[j] == 0) {
                    continue;
                }
                //Multiplicamos coeficientes y lo colocamos en la posición correspondiente a la suma de los exponentes de dichos coeficientes.
                provisional[provisional.length - (((this.mon.length - i)) + ((p2.mon.length - j) - 1))] = this.mon[i] * p2.mon[j];
            }
            result = result.add(new Polynomial(provisional));

            //Antes de volver a la iteración principal, reiniciamos los valores de provisional.
            Arrays.fill(provisional, 0);
        }
        return result;
    }

    //Dividimos polinomios. (No modificamos ningún polinomio, retornamos el cociente y el residuo (Array de polinomios con dos posiciones).
    public Polynomial[] div(Polynomial p2) {
        Polynomial indiValue;
        Polynomial[] result = new Polynomial[]{new Polynomial(), new Polynomial(this.mon)};
        float [] provisional;
        while (result[1].mon.length >= p2.mon.length) {
            provisional = new float[(result[1].mon.length - p2.mon.length) + 1];
            provisional[0] = ((int) (result[1].mon[0] / p2.mon[0]));
            indiValue = new Polynomial(provisional);
            result[0] = result[0].add(indiValue);
            indiValue = p2.mult(indiValue);
            indiValue.changeSign(indiValue);
            indiValue = new Polynomial(indiValue.mon);
            result[1] = result[1].add(indiValue);
        }
        return result;
    }

    //Cambiamos los signos de lo valores que debemos restar al dividendo.
    private void changeSign(Polynomial fullProduct) {
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
        else{
            return calcDivRuf();
        }
        return null;
    }

    //Calculamos la ecuación de segundo grado
    private float[] secDegree(float[] monF) {
        float[] retoorn;
        int count = 0;
        //Asignamos los valores 0 por defecto ya que, al hacer ecuaciones de segundo grado sobre polinomios bicuadráticos, puede que no
        //tengamos los valores de todas las incógnitas (Puede que solo operemos con 1 o 2 valores)
        float a = 0, b = 0, c = 0;

        //Asignamos los valores correspondientes (no asignamos los elementos con valor 0) Este bucle es necesario porque, al hacer
        //ecuaciones de segundo grado de polinomios bicuadráticos, cabe la posibilidad que el grado sea superior a 2, y por lo tanto
        //cabe la posibilidad de asignar 0 cuando no se debe
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

    //Comprobamos que un polinomio sea bicuadrático
    private int bQuad() {
        int provisional = this.mon.length - 1;
        for(int i = 1; i < this.mon.length; i++){
            if(this.mon[i] == 0){
                continue;
            }

            //Comprobamos que el exponente sea igual a la división del exponente siguiente entre 2
            else if((this.mon.length - 1) - i > 1 && (this.mon.length - 1) - i == provisional / 2){
                provisional = (this.mon.length - 1) - i;
            }

            //Si llegamos a este punto, si no estamos operando sobre el término independiente, es porque el polinomio no es bicuadrático.
            else if(i != this.mon.length - 1){
                return -1;
            }
        }
        return provisional;
    }

    private float[] calcDivRuf() {
        float[] retoorn1 = new float[(this.mon.length - 1) - 2];
        Polynomial rufProv2 = new Polynomial (this.mon);
        float [] rufProv3 = new float [rufProv2.mon.length - 1];
        for (int i = 0, divPro = 1, divPro2; i < retoorn1.length; i++) {
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
        if (retoorn1[retoorn1.length - 1] == 0) {
            return null;
        }
        float [] retoorn2 = secDegree(rufProv2.mon);
        float [] finalRetoorn = new float[retoorn1.length + retoorn2.length];
        assignValuesRuf(finalRetoorn, retoorn1, 0);
        assignValuesRuf(finalRetoorn, retoorn2, retoorn1.length);
        Arrays.sort(finalRetoorn);
        return finalRetoorn;
    }

    private void assignValuesRuf(float[] finalRetoorn, float[] retoorn, int posInicial) {
        for (int j = 0; j < retoorn.length; j++, posInicial++) {
            finalRetoorn[posInicial] = retoorn[j];
        }
    }

    //Asignamos valores a la matriz que contendrá dos veces los valores resultantes de calcular la ecuación de segundo grado de un
    //polinomio bicuadrático. A los valores duplicados les cambiaremos el signo (Ya que la raíz de un polinomio bicuadrático retorna
    //el valor positivo y negativo.
    private float[] createNegative(float[] re2, float[] re1) {
        for(int i = 0, count = 0; i < re2.length; i++){
            re2[i++] = re1[count++];
            re2[i] = re2[i - 1] * -1;
        }
        Arrays.sort(re2);
        return re2;
    }

    private int Ruffini(Polynomial rufProv2, float[] rufProv3, int divPro) {
        float provisional;
        rufProv3[0] = rufProv2.mon[0];
        for(int i = 1; i < rufProv2.mon.length; i++){
            provisional = divPro*rufProv3[i - 1];
            rufProv3[i] = rufProv2.mon[i] + provisional;
            if(i == rufProv3.length - 1){
                provisional = divPro * rufProv3[i];
                if (rufProv2.mon[i + 1] + provisional == 0) {
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

    //Contamos qué cantidad de 0 contiene un array de coeficientes de un polinomio. (Me parece necesario este método ya que podemos
    //calcular la cantidad real de monomios de un polinomio, ya que puede que tengamos posiciones vacías por no tener valores con ese
    //grado en el polinomio.
    private int Zvalue(float[] cfs) {
        int count = 0;
        for(float prov : cfs){
            if(prov == 0){
                count ++;
            }
        }
        return count;
    }

    //Creamos el stringBuilder miembro a partir del array miembro.
    private void passToString() {
        for(int i = 0; i < mon.length; i++){
            if(mon[i] == 0){
                continue;
            }
            //Si estamos tratando el primer monomio del polinomio y es un valor negativo, añadimos el signo "-" (sin espacios)
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
}