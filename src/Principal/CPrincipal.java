
package Principal;

import Utilidades.ES;

/**
 *
 * @author Antonio Ramos
 */
public class CPrincipal {

    private static final String AMARILLO = "\u001B[33m";
    private static final String BLANCO = "\u001b[37m";
    private static final String ROJO = "\u001B[31m";
    private static final String RESET = "\u001B[0m";
    private static final int FILAS = 6;
    private static final int COLUMNAS = 7;
    
    private static int[][] tablero;  
    //private static int turno = 0;       // Comprobar si se pone local
    private static boolean unJugador = true;
   
    /**
     * Método principal del juego.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        char opcion;
        
        tablero = new int[FILAS][COLUMNAS];
        
        // Compruebo que las dimensiones del tablero son mayores que 0
        if(tablero.length > 0 && tablero[0].length > 0){        
            do{
                opcion = ES.leerCaracter("\nCONECTA 4\n"
                    + "---------\n"
                    + "a. Un jugador\n"
                    + "b. Dos jugadores\n"
                    + "c. Salir del juego\n"
                    + "Elija una opción: ");

                switch(opcion){                    
                    case 'a':
                        resetJuego(1);
                        jugar();
                        break;
                    case 'b':
                        resetJuego(2);
                        jugar();
                        break;
                    case 'c':
                        System.out.println("Gracias por jugar. ¡Nos vemos pronto!");
                        break;
                    default:
                        System.err.println("Error. Opción no válida.");
                }

            }while(opcion != 'c');
        }
        else{
            System.err.println("Error. Las filas o columnas del tablero no pueden ser 0.");
        }
    }
    
    /**
     * Método que reinicia todas las variables generales del juego y limpia el tablero.
     * @param numeroJugadores indica el número de jugadores que van a participar. Ha de ser 1 ó 2
     */
    private static void resetJuego(int numeroJugadores){
        if (numeroJugadores == 1)
            unJugador = true;
        else
            unJugador = false;
        
        for(int i = 0; i < tablero.length; i++)
            for(int j = 0; j < tablero[i].length; j++)
                tablero[i][j] = 0;
    }
    
    /**
     * Método que dibuja por pantalla el tablero.
     */
    private static void dibujarTablero(){
        System.out.print("  ");
        for(int i = 0; i < tablero[0].length; i++){
            System.out.print(i + "   ");
        }
        System.out.println("");
        for (int i = 0; i < tablero.length; i++){
            System.out.print("| ");
            for (int j = 0; j < tablero[i].length; j++){
                if(tablero[i][j] == 1)
                    System.out.print(ROJO + "●" + RESET + " | ");
                else if(tablero[i][j] == 2 || tablero[i][j] == 3)
                    System.out.print(AMARILLO + "●" + RESET + " | ");
                else
                    System.out.print(BLANCO + "●" + RESET + " | ");
            }
            System.out.println("");
        }
    }
    
    /**
     * Método que se encarga de la gestión del juego una vez que comienza.
     */
    private static void jugar(){        
        int ganador = -1;
        int turno = 1;
        // Opción 1
        //int celdasLibres = FILAS * COLUMNAS;
        // Opción 2
        int celdasLibres = tablero.length * tablero[0].length;
        
        dibujarTablero();
        do{
            soltarFicha(turno);
            ganador = comprobarGanador(turno);
            if(turno == 1)
                turno = 2;
            else
                turno = 1;
            
            celdasLibres--;
            dibujarTablero();
        }while(ganador == -1 && celdasLibres > 0);
        
        if(ganador == 1)
            System.out.println("Gana el jugador 1");
        else if( ganador == 2) 
            System.out.println("Gana el jugador 2");
        else
            System.out.println("Ha habido un empate");
    }
    
    /**
     * Método que permite soltar una ficha en una de las columnas del tablero.
     * @param jugador indica el jugador al que pertenece la ficha.
     */
    private static void soltarFicha(int jugador){         
        int columna;
        
        if(jugador == 2 && unJugador == true){
            do{
                columna = (int)(Math.random()*tablero[0].length);
            }while(colocarFicha(jugador, columna) == false);
            System.out.println("Ficha de la CPU: " + columna);
        }
        else{
            boolean fichaColocada;
            
            do{
                columna = ES.leerEntero(0, COLUMNAS - 1, "Jugador " + jugador + " Introduzca la columna donde quiere soltar la ficha: ");
                fichaColocada = colocarFicha(jugador, columna);
                if(fichaColocada == false)
                    System.err.println("No se puede colocar en esta columna porque está llena.");
            }while(!fichaColocada);
        }
    }
    
    /**
     * Comprueba si hay un hueco en la columna indicada. De ser así busca la casilla más baja en el tablero (en el array sería la fila de mayor tamaño)
     * @param jugador indica el jugador al que pertenece la ficha.
     * @param columna indica la columna donde se debe colocar la ficha.
     * @return devuelve true si se ha conseguido colocar y false en el caso contrario.
     */
    private static boolean colocarFicha(int jugador, int columna){
        if(tablero[0][columna] == 0){
            for(int i = tablero.length - 1; i >= 0 ; i--){
                if(tablero[i][columna] == 0){
                    tablero[i][columna] = jugador;
                    return true;                    
                }
            }
        }   
        return false;
    }
    
    
    /**
     * Comprueba si hay un ganador después de colocar una ficha.
     * @param jugador indica el jugador que acaba de colocar la ficha y para el que hay que realizar la comprobación.
     * @return devuelve el valor del jugador pasado por parámetro en caso de que gane y -1 si no hay ninguna combinación ganadora.
     */
    private static int comprobarGanador(int jugador){
        int fichasConsecutivas;
        
        // Vamos a utilizar en los bucles siempre la letra "i" para las filas y la "j" para las columnas
        
        // Comprobación de las filas (4 en raya horizontal)
        for(int fila = 0; fila < tablero.length; fila++){
            fichasConsecutivas = 0;
            for (int colum = 0; colum < tablero[fila].length; colum++){
                if(tablero[fila][colum] == jugador){
                    fichasConsecutivas++;
                    
                    if(fichasConsecutivas == 4){
                        return jugador;
                    }
                }
                else{
                    fichasConsecutivas = 0;
                                        
                    if(colum >= tablero[fila].length - 4)
                        break;
                }
            }            
        }
        
        // Comprobación de las columnas (4 en raya vertical)
        for(int colum = 0; colum < tablero[0].length; colum++){
            fichasConsecutivas = 0;
            for (int fila = tablero.length - 1; fila >= 0 ; fila--){
                 if(tablero[fila][colum] == 0){
                     break;
                 }
                 else  if(tablero[fila][colum] == jugador){
                    fichasConsecutivas++;
                    
                    if(fichasConsecutivas == 4){
                        return jugador;
                    }
                 }
                else{
                    fichasConsecutivas = 0;

                    // Comprobamos si está por la fila 2 de ser así reseteamos
                    if(fila <= 2)
                        break;
                }
            }
        }     
        
        // Comprobación de las diagonales descendentes
        for(int fila = 0; fila <= tablero.length - 4; fila++){
            for (int columna = 0; columna <= tablero[fila].length - 4; columna++){
                if(comprobarDiagonalDescendente(jugador, fila, columna) == true)
                    return jugador;
            }
        }
        
        // Comprobación de las diagonales descendentes
        for(int fila = 3; fila < tablero.length; fila++){
            for (int columna = 0; columna <= tablero[fila].length - 4; columna++){
                if(comprobarDiagonalAscendente(jugador, fila, columna) == true)
                    return jugador;
            }
        }       
        
        // Una vez comprobado todo, si no hay hay un ganador devuelve -1
        return -1;
    }
    
    /**
     * Comprueba si hay 4 fichas iguales de un jugador en la diagonal descendente de izquierda a derecha a partir de la celda indicada por parámetro
     * @param jugador indica el jugador que acaba de colocar la ficha y para el que hay que realizar la comprobación.
     * @param inicioFila indica la fila donde debe comenzar la comprobación.
     * @param inicioColumna indica la columna donde debe comenzar la comprobación.
     * @return devuelve true si hay 4 fichas iguales del jugador y false en caso contrario.
     */
    private static boolean comprobarDiagonalDescendente(int jugador, int inicioFila, int inicioColumna){
        
        // OPCION 1
        /*
        int filaMax;
        int fichasConsecutivas = 0;
        
        boolean salir = false;
        
        filaMax = fila + 3;
        
        do{
            if(tablero[fila][columna] == jugador){
                fichasConsecutivas++;
                fila++;
                columna++;
            }
            else
                salir = true;
        }while(salir == false && fila <= filaMax);
        
        if(fichasConsecutivas == 4)
            return true;
        else
            return false;
        */
        
        // OPCION 2
        // En cuanto se encuentre en la diagonal una ficha distinta (o ninguna) se acaba la comprobación.
        // Si es capaz de realizar las 4 comprobaciones es que hay ganador.
        for(int fila = inicioFila, col = inicioColumna; fila < inicioFila + 4; fila++, col++){
            if(tablero[fila][col] != jugador)
                return false;            
        }
        return true;
    }
    
    /**
     * Comprueba si hay 4 fichas iguales de un jugador en la diagonal ascendente de izquierda a derecha a partir de la celda indicada por parámetro
     * @param jugador indica el jugador que acaba de colocar la ficha y para el que hay que realizar la comprobación.
     * @param inicioFila indica la fila donde debe comenzar la comprobación.
     * @param inicioColumna indica la columna donde debe comenzar la comprobación.
     * @return devuelve true si hay 4 fichas iguales del jugador y false en caso contrario.
     */
    private static boolean comprobarDiagonalAscendente(int jugador, int inicioFila, int inicioColumna){
        for(int fila = inicioFila, col = inicioColumna; fila > inicioFila - 4; fila--, col++){
            if(tablero[fila][col] != jugador)
                return false;            
        }
        return true;
    }
}
