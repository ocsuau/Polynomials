import java.util.Arrays;

public class Polynomial {

    /*Variables miembro float y StringBuilder (Lo considero variable miembro para generarlo al crear el objeto y no cada vez que
    se llama a toString()*/
    private float[] mon;
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

        /*Si el primer valor es positivo, le ponemos el signo delante para no cambiar el código solo por el primer monomio.
        (si el valor es negativo, ya viene con el signo incluído).*/
        s = (s.charAt(0) != '-') ? '+' + s : s;

        /*Juntamos los signos con sus respectivos monomios para no tener operadores matemáticos ocupando elementos de la matriz de strings.
        Seguidamente creamos la matriz cortando el string por cada espacio.*/
        s = s.replace(" + ", " +");
        s = s.replace(" - ", " -");
        String[] monOm = s.split(" ");

        /*Creamos matriz donde almacenamos los exponentes (con el orden respectivo a sus coeficientes) y al mismo tiempo calculamos el
        mayor exponente, que nos permite inicializar la variable miembro float con la longitud fija.*/
        int[] expI = calcExpStr(monOm);

        /*Asignamos valores al float miembro y los colocamos en las posiciones correspondientes dependiendo de sus exponentes. Seguidamente,
        construímos el stringBuilder miembro.*/
        assignValues(monOm, expI);
        passToString();
    }

    /*Guardamos los exponentes en un array para poder colocar los coeficientes en la posición correspondiente del array miembro.
    También calculamos el valor del mayor exponente para inicializar el array miembro con tamaño fijo.*/
    private int[] calcExpStr(String[] monOm) {
        int[] retoorn = new int[monOm.length];
        int maxExpo = 0;

        /*Comprobamos si el elemento string en cuestión contiene el carácter "x" y, en ese caso, si contiene el carácter "^" y no va
        seguido de un 1, el exponente será igual a el número que siga al carácter "^", si no, será igual a 1.*/

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

    /*Calculamos el coeficiente correspondiente a partir del array de string y lo sumamos al valor que está en la posición correspondiente
     en el array miembro a partir de la longitud del array miembro menos el exponente correspondiente a ese coeficiente. (De este modo,
    al mismo tiempo que asignamos los coeficientes en sus respectivas posiciones, los factorizamos)*/
    private void assignValues(String[] monOm, int[] expI) {
        for (int i = 0; i < monOm.length; i++) {
            if (monOm[i].contains("x")) {

                //El primer carácter de cada string es su signo, el segundo solo puede ser el primer dígito del coeficiente o la incógnita
                if (monOm[i].charAt(1) == 'x') {

                    //En caso de que el segundo carácter sea la incógnita, dependiendo del signo, sumaremos 1 o -1.
                    mon[(mon.length - 1) - expI[i]] += (monOm[i].charAt(0) == '-') ? -1 : 1;
                } else {
                    /*Si el segundo carácter no es la incógnita, significa que existe coeficiente y por lo tanto podemos hacer un parseInt
                    del substring del elemento en cuestión hasta la posición de la incógnita.*/
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

        /*En esta función, a partir de los stringBuilder de cada objeto, los juntamos y creamos un nuevo objeto pasándole como parámetro
        los dos stringBuilder en uno, ya que al contruir el objeto, factorizamos sus monomios, y nos ofrece los mismos resultados que
        sumar los monomios. (Previamente, añadimos los signos de sumar y/o restar a uno de los stringBuilder para que el objeto se pueda
        contruir correctamente.*/
        if (p.monStr.charAt(0) == '-') {
            p.monStr.delete(0, 1);
            p.monStr.insert(0, " - ");
        } else {
            p.monStr.insert(0, " + ");
        }
        return new Polynomial(this.monStr + "" + p.monStr);
    }

    //Multiplicamos polinomios. (No modificamos ningún polinomio, retornamos el resultado de la multiplicación)
    public Polynomial mult(Polynomial p2) {

        /*En provisional iremos almacenando la multiplicación de todos y cada uno de los monomios de uno de los polinomios con cada
        uno de los monomios del otro polinomio.*/

        //En result iremos almacenando la suma de los polinomios que vayamos almacenando en provisional.
        float[] provisional = new float[(this.mon.length + p2.mon.length) - 1];
        Polynomial result = new Polynomial();

        /*Si uno de los valores del float miembro de cualquiera de los objetos es igual a 0, volvemos a la condición de la iteración.
        (para no hacer trabajar al algoritmo de forma inecesaria)*/
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
        ////////////////////////////////////////////////////////////////////////////////
        //COMENTAR A CLASE SI AL NO PODERSE REALIZAR LA DIVISIÓN SE DEBE RETORNAR NULL//
        ////////////////////////////////////////////////////////////////////////////////
        if (this.mon.length < p2.mon.length) {
            return null;
        }

        //DEPENDIENDO DE LO QUE SE HABLE EN CLASE, COMENTAR RESULT.
        //indivalue contendrá el cociente que vayamos calculando a cada iteración para operar con él sobre el dividendo.
        Polynomial indiValue;

        //En result posición 1 metemos el dividendo, ya que al final del bucle se quedará como residuo.
        Polynomial[] result = new Polynomial[]{new Polynomial(), new Polynomial(this.mon)};

        //Creamos provisional para indicar el exponente (del cociente que vayamos calculando) a partir de su length
        float[] provisional;

        //Mientras el grado del dividendo sea mayor o igual al grado del divisor...
        while (result[1].mon.length >= p2.mon.length) {
            /*Indicamos que el length de provisional (el grado de cociente que estamos calculando) será igual al grado del dividendo
            menos el grado del divisor + 1. En su primera posición meteremos el resultado de dividir el cociente con mayor exponente
            del dividendo entre el cociente con mayor exponente del divisor (Siempre estarán en la primera posición de sus respectivos
            array miembro.*/
            provisional = new float[(result[1].mon.length - p2.mon.length) + 1];
            provisional[0] = ((int) (result[1].mon[0] / p2.mon[0]));

            /*Creamos indivalue a partir del float calculado anteriormente para poder operar con él utilizando los métodos que hemos definido
            anteriormente.*/
            indiValue = new Polynomial(provisional);

            //Lo vamos introduciendo en la posición de cocientes de result.
            result[0] = result[0].add(indiValue);

            /*Multiplicamos el valor calculado anteriormente por el divisor y lo metemos de nuevo en indivalue (para no crear variables de más).
            Seguidamente le cambiamos el signo a cada monomio, ya que debe restarse con el dividendo.*/
            indiValue = p2.mult(indiValue);
            indiValue.changeSign(indiValue);

            /*Debemos crear de nuevo indivalue con los signos cambiados ya que hemos modificado su array de float miembro, pero no su stringBuilder.
            De esta forma actualizamos su stringBuilder y podemos realizar la suma (Ya que la suma se realiza a partir de los stringBuilder
            miembro de los objetos a sumar).*/
            indiValue = new Polynomial(indiValue.mon);

            //Lo sumamos al dividendo, donde el resultado será el residuo, por eso operamos sobre la posición de residuo de result.
            result[1] = result[1].add(indiValue);
        }
        return result;
    }

    //Cambiamos los signos de lo valores que debemos restar al dividendo.
    private void changeSign(Polynomial fullProduct) {
        for (int i = 0; i < fullProduct.mon.length; i++) {
            if (fullProduct.mon[i] != 0) {
                fullProduct.mon[i] *= -1;
            }
        }
    }

    // Comprobamos si las raíces las podemos calcular con ecuaciones de primer grado, de segundo o tenemos que aplicar ruffini.
    public float[] roots() {
        float[] retoorn;
        float provisional;

        //Si el polinomio está formado por dos monomios y uno de éllos es el término independiente.
        if ((this.mon.length - Zvalue(this.mon) == 2) && this.mon[this.mon.length - 1] != 0) {

            //Cambiamos de signo el término independiente para valores si se puede operar sobre él. Lo guardamos en provisional.
            provisional = this.mon[this.mon.length - 1];
            provisional *= -1;

            /*Si el grado de la raíz es par y el término independiente es negativo (sin haberle cambiado el signo) retornamos
            los dos resultados posible.*/
            if ((this.mon.length - 1) % 2 == 0) {
                if (this.mon[this.mon.length - 1] < 0) {
                    return new float[]{(float) (Math.pow(provisional, 1 / (this.mon.length - 1f))) * -1, (float) Math.pow(provisional, 1 / (this.mon.length - 1f))};
                }
            } else {
                /*En este punto sabemos que el grado de la raíz es impar. Así que valoraremos el signo del término independiente para
                retornar un resultado u otro.*/
                if (this.mon[this.mon.length - 1] < 0) {
                    return new float[]{(float) (Math.pow(provisional, 1 / (this.mon.length - 1f)))};
                } else {
                    provisional *= -1;
                    return new float[]{(float) (Math.pow(provisional, 1 / (this.mon.length - 1f))) * -1};
                }
            }
            /*En este punto sabemos que el polinomio no está formado por dos monomios y/o está formado por dos monomios pero uno
            de éllos no es término independiente.*/

            //Comprobamos si el grado del polinomio es 2 o si es un polinomio bicuadrático y está formado por 3 monomios.
        } else if (this.mon.length - 1 == 2 || (this.mon.length - Zvalue(this.mon) <= 3 && bQuad() > 0)) {
            //Realizamos ecuación de segundo grado
            retoorn = secDegree(this.mon);

            //Si el grado del polinomio es 2, retornamos retoorn.
            if (this.mon.length - 1 == 2) {
                return retoorn;
            } else {
                /*Si el polinomio no es de grado 2, significa que es bicuadrático, así que calculamos la raíz (cuyo grado es el exponente más
                pequeño del polinomio mayor de 1) de cada resultado que nos haya devuelto el método secDegree.*/
                for (int i = 0; i < retoorn.length; i++) {
                    retoorn[i] = (float) (Math.pow(retoorn[i], 1 / (float) bQuad()));
                }
                /*Finalmente, creamos un nuevo float que contendrá los valores de retoorn y los mismos valores con los signos cambiados, acción
                que realizaremos en el método createNegative.*/
                float[] retoorn2 = new float[retoorn.length * 2];
                return createNegative(retoorn2, retoorn);
            }
        } else {
            //Si no se ha podido aplicar ninguna de las ecuaciones anteriores, retornaremos el resultado de calcDivRuff() (pudiendo ser null).
            return calcDivRuff();
        }
        return null;
    }

    //Calculamos la ecuación de segundo grado
    private float[] secDegree(float[] monF) {
        float[] retoorn;
        int count = 0;
        /*Asignamos los valores 0 por defecto ya que, al hacer ecuaciones de segundo grado sobre polinomios bicuadráticos, puede que
        no tengamos los valores de todas las incógnitas (Puede que solo operemos con 1 o 2 valores)*/
        float a = 0, b = 0, c = 0;

        /*Asignamos los valores correspondientes (no asignamos los elementos con valor 0) Este bucle es necesario porque, al hacer
        ecuaciones de segundo grado de polinomios bicuadráticos, cabe la posibilidad que el grado sea superior a 2, y por lo tanto
        cabe la posibilidad de asignar 0 cuando no se debe*/
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
        //Ecuación de segundo grado
        float disc = (float) (Math.pow(b, 2) + (-4 * a * c));
        if (disc < 0) {
            return null;
        } else if (disc == 0) {
            retoorn = new float[]{(b * -1) / (2 * a)};
        } else {
            retoorn = new float[]{((b * -1) + (float) Math.sqrt(disc)) / (2 * a), ((b * -1) - (float) Math.sqrt(disc)) / (2 * a)};
            Arrays.sort(retoorn);
        }
        return retoorn;
    }

    //Comprobamos que un polinomio sea bicuadrático
    private int bQuad() {
        int provisional = this.mon.length - 1;
        for (int i = 1; i < this.mon.length; i++) {
            if (this.mon[i] == 0) {
                continue;
            }

            //Comprobamos que el exponente sea igual a la división del exponente siguiente entre 2
            else if ((this.mon.length - 1) - i > 1 && (this.mon.length - 1) - i == provisional / 2) {
                provisional = (this.mon.length - 1) - i;
            }

            //Si llegamos a este punto, si no estamos operando sobre el término independiente, es porque el polinomio no es bicuadrático.
            else if (i != this.mon.length - 1) {
                return -1;
            }
        }
        return provisional;
    }

    //Método donde iremos comprobando los divisores correctos al aplicar ruffini a través del método Ruffini.
    private float[] calcDivRuff() {
        /*En la variable retoorn1 iremos almacenando los divisores correctos de aplicar ruffini al polinomio en cuestión. (Sólo los de
        Ruffini, ya dejaremos de aplicar Ruffini en el momento que podamos aplicar la ecuación de segundo grado.*/
        float[] retoorn1 = new float[(this.mon.length - 1) - 2];

        //La variable ruffProv2 la creamos para no operar directamente sobre el objeto en cuestión.
        Polynomial ruffProv2 = new Polynomial(this.mon);

        /*La variable ruffProv3 contendrá el polinomio resultante de aplicar Ruffini a ruffProv2. (Tiene la misma longitud menos 1 ya que,
        al aplicar Ruffini sobre un polinomio, obtenemos un nuevo polinomio de un grado inferior.*/
        float[] ruffProv3 = new float[ruffProv2.mon.length - 1];

        /*Aplicaremos Ruffini tantas veces hasta conseguir que el polinomio en cuestión sea de grado 2. (El length de retoorn1 es igual
        al grado del polinomio en cuestión menos 1 (para no contar el término independiente) y menos 2 más, para asegurarnos de que,
        cuando sea de grado dos, aplicaremos ecuación de segundo grado.*/

        /*Creamos las variables divPro y divPro2 para ir almacenando los divisores del término independiente del polinomio y compararlos
        después de pasarlos por el método Ruffini (Ruffini puede que nos devuelva el mismo valor que le hemos pasado, o el mismo valor
        con signo negativo)*/
        //bucle:
        for (int i = 0, divPro = 1, divPro2; i < retoorn1.length; i++) {

            /*Mientras el divisor que estamos comprobando sea distinto al coeficiente del monomio con menor exponente del polinomio
            en cuestión (Si el divisor llega a ser igual al coeficiente del monomio con menor exponente del polinomio, es porque al
            polinomio no se le ha podido aplicar ruffini, y por lo tanto no tiene solución*/
            while (divPro != ruffProv2.mon[ruffProv2.mon.length - 1]) {

                /*Comprobamos que el número en cuestión es divisor del coeficiente del monomio con menor exponente del polinomio.
                Si es así, llamamos al método Ruffini pasándole el polinomio al que queremos aplicar Ruffini, el float donde
                queremos guardar el polinomio resultante de aplicar Ruffini al polinomio inicial, y el divisor candidato para realizar
                Ruffini correctamente.

                El resultado del Método Ruffini lo meteremos en la variable divPro2, y éste podrá ser el mismo número que le hemos
                pasado, el mismo número con signo negativo o el número 0 (si es 0, sabemos que el divisor que le hemos pasado no es
                el correcto para aplicar ruffini*/
                if (ruffProv2.mon[ruffProv2.mon.length - 1] % divPro == 0) {
                    divPro2 = Ruffini(ruffProv2, ruffProv3, divPro);

                    /*Si el valor absoluto del divisor que le hemos pasado a Ruffini ese igual al valor absoluto del divisor que nos
                    ha retornado Ruffini, podemos almacenar el divisor como uno de los resultado, asignamos a ruffProv2 el polinomio
                    ruffProv3 (El polinomio resultado de aplicar ruffini al polinomio inicial, el cuál aplicaremos de nuevo ruffini si
                    fuera necesario) y creamos de nuevo ruffProv3 con una posición menos.

                    Finalmente reiniciamos divPro y salimos del bucle*/
                    if (Math.abs(divPro2) == Math.abs(divPro)) {
                        ruffProv2 = new Polynomial(ruffProv3);
                        ruffProv3 = new float[ruffProv2.mon.length - 1];
                        retoorn1[i] = divPro2;
                        divPro = 1;
                        break;

                        /*Si el divisor no es el candidato adecuado, si es distinto al término independiente del polinomio, lo incrementamos.
                        Si no, salimos del bucle*/
                    } else if (Math.abs(divPro) != Math.abs(ruffProv2.mon[ruffProv2.mon.length - 1])) {
                        divPro++;
                    } else {
                        /*retoorn1 = null;
                        break bucle;*/
                        break;
                    }
                }
            }
        }//COMENTAR EN CLASE QUE SE DEBE RETORNAR EN CASO DE QUE RUFFINI NO SE CUMPLA PARCIALMENTE.
        /*if (retoorn1[retoorn1.length - 1] == 0 || retoorn1 == null) {*/
        /*En caso de que no se haya podido aplicar ruffini desde el principio, retornamos null*/
        if (retoorn1[retoorn1.length - 1] == 0) {
            return null;
        }

        /*Si llegamos a este punto significa que hemos encontrado los resultados correctos aplicando ruffini.
        Seguidamente, aplicamos la ecuación de segundo grado al polinomio reultante de haberle aplicado ruffini hasta que éste se ha
        convertido en un polinomio de grado dos. Metemos el resultado en retoorn2*/
        float[] retoorn2 = secDegree(ruffProv2.mon);

        /*En finalRetoorn almacenaremos los resultados obtenidos con ruffini y los obtenidos con la ecuación de segundo grado*/
        float[] finalRetoorn = new float[retoorn1.length + retoorn2.length];

        /*Llamamos al método assignValuesRuff para pasar los resultado obtenidos con ruffini y con la ecuación de segundo grado a
        finalRetoorn. (Como parámetros, le pasamos finalRetoorn, el array que queremos reasignar a finalRetoorn y la posición
        de finalRetoorn donde debe empezar a introducir los valores*/
        assignValuesRuff(finalRetoorn, retoorn1, 0);

        /*Comprobamos que la ecuación de segundo grado no haya retornado un resultado nulo*/
        if (retoorn2 != null) {
            assignValuesRuff(finalRetoorn, retoorn2, retoorn1.length);
        }
        Arrays.sort(finalRetoorn);
        return finalRetoorn;
    }

    /*Método donde asignamos los valores obtenidos con ruffini y con ecuación de segundo grado a finalRetoorn de manera correcta
    (A partir de la posición inicial, de esta forma evitamos sobreescribir valores que hayamos introducido anteriormente y obtener
    un error por intentar acceder a una posición inexistente del array retoorn (ya que finalRetoorn es de mayor longitud que retoorn)*/
    private void assignValuesRuff(float[] finalRetoorn, float[] retoorn, int posInicial) {
        for (int j = 0; j < retoorn.length; j++, posInicial++) {
            finalRetoorn[posInicial] = retoorn[j];
        }
    }

    /*Asignamos valores a la matriz que contendrá dos veces los valores resultantes de calcular la ecuación de segundo grado de un
    polinomio bicuadrático. A los valores duplicados les cambiaremos el signo (Ya que la raíz de un polinomio bicuadrático retorna
    el valor positivo y negativo.*/
    private float[] createNegative(float[] re2, float[] re1) {
        for (int i = 0, count = 0; i < re2.length; i++) {
            re2[i++] = re1[count++];
            re2[i] = re2[i - 1] * -1;
        }
        Arrays.sort(re2);
        return re2;
    }

    /*Método donde aplicaremos ruffini al polinomio que nos pasan(ruffProv2) a partir del divisor que también nos pasan (divPro).
    El polinomio resultado lo meteremos en ruffProv3*/
    private int Ruffini(Polynomial ruffProv2, float[] ruffProv3, int divPro) {
        float provisional;
        //Asignamos el primer valor del polinomio en el array resultante (Ya que el primer valor de ruffini baja directamente)
        ruffProv3[0] = ruffProv2.mon[0];
        //Calculamos sobre todos los monomios del polinomio
        for (int i = 1; i < ruffProv2.mon.length; i++) {
            /*Aplicamos ruffini (multiplicamos el divisor por el valor anterior del array resultante y lo sumamos al valor
            correspondiente del polinomio)*/
            provisional = divPro * ruffProv3[i - 1];
            ruffProv3[i] = ruffProv2.mon[i] + provisional;
            if (i == ruffProv3.length - 1) {
                /*En este punto estamos en la última posición del array resultante. Sólo nos queda comprobar si (el coeficiente
                del monomio con menor exponente del polinomio del array resultante por el divisor) sumado al término independiente
                del polinomio da como resultado 0. Si es así, retornamos el divisor, si no, convertimos el divisor en si mismo con
                signo negativo y reiniciamos el proceso. En caso de que el divisor ya fuera negativo, retornamos 0*/
                provisional = divPro * ruffProv3[i];
                if (ruffProv2.mon[i + 1] + provisional == 0) {
                    return divPro;
                } else {
                    if (divPro > 0) {
                        ruffProv3[0] = ruffProv2.mon[0];
                        i = 0;
                        divPro *= -1;
                    } else {
                        break;
                    }
                }
            }
        }
        return 0;
    }

    /* Override del método equals de la clase Object. Comprobamos que los objetos que vamos a comparar sean de la misma clase y si
    es así, comprobamos que contengan los mismos valores.*/
    @Override
    public boolean equals(Object o) {
        if (o instanceof Polynomial) {
            Polynomial content = (Polynomial) o;
            if (this.monStr.toString().equals(content.monStr.toString())) {
                return true;
            }
        }
        return false;
    }

    // Override del método toString de la clase Object. Retornamos el stringBuilder miembro del objeto.
    @Override
    public String toString() {
        return monStr.toString();
    }

    /*Contamos qué cantidad de 0 contiene un array de coeficientes de un polinomio. (Me parece necesario este método ya que podemos
    calcular la cantidad real de monomios de un polinomio, ya que puede que tengamos posiciones vacías por no tener valores con ese
    grado en el polinomio.*/
    private int Zvalue(float[] cfs) {
        int count = 0;
        for (float prov : cfs) {
            if (prov == 0) {
                count++;
            }
        }
        return count;
    }

    //Creamos el stringBuilder miembro a partir del array miembro.
    private void passToString() {
        for (int i = 0; i < mon.length; i++) {
            if (mon[i] == 0) {
                continue;
            }
            //Si estamos tratando el primer monomio del polinomio y es un valor negativo, añadimos el signo "-" (sin espacios)
            if (monStr.length() == 0 && mon[i] < 0) {
                monStr.append("-");
            }
            /*Si estamos, por lo menos, con el segundo monomio del polinomio y en el stringBuilder miembro tiene algún carácter ya
            introducido, introduciremos en el stringBuilder las operaciones de suma o resta dependiendo del signo del monomio
            que vayamos a introducir.*/
            else if (i > 0) {
                if (monStr.length() > 0) {
                    monStr.append((mon[i] > 0) ? " + " : " - ");
                }
            }
            /*Si el valor que estamos tratando es distinto a 1 o (es igual a 1 y es el término independiente (último valor del array miembro))
            introducimos el valor absoluto del valor que queramos introducir (ya que antes hemos introducido su signo)).*/
            if (!(Math.abs(mon[i]) == 1) || ((i == mon.length - 1) && (Math.abs(mon[i]) == 1))) {
                monStr.append(Math.abs((int) mon[i]));
            }
            //Dependiendo de su posición en el array, introduciremos la incógnita y su exponente.
            if ((mon.length - i) - 1 > 0) {
                monStr.append("x");
                if ((mon.length - i) - 1 > 1) {
                    monStr.append("^" + ((mon.length - i) - 1));
                }
            }
        }
        /*Ya recorrido el array de floats miembro, si no hemos introducido ningún valor al stringBuilder miembro, significa que el array
        de floats solo contenía 0, así que introducimos en el stringBuilder el número 0.*/
        if (monStr.length() == 0) {
            mon = new float[]{0};
            monStr.append("0");
        }
    }
}