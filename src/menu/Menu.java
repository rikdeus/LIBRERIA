/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package menu;

import java.util.Scanner;

public class Menu {
    public static void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("================================");
            System.out.println("        LIBRERÍA NOVA           ");
            System.out.println("================================");
            System.out.println("1. Consultar productos");
            System.out.println("2. Registrar cliente");
            System.out.println("3. Registrar pedido");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    System.out.println("\n[Consultando productos...]\n");
                    break;
                case 2:
                    System.out.println("\n[Registrando cliente...]\n");
                    break;
                case 3:
                    System.out.println("\n[Registrando pedido...]\n");
                    break;
                case 4:
                    System.out.println("\nSaliendo del sistema...\n");
                    break;
                default:
                    System.out.println("\nOpción inválida. Intente de nuevo.\n");
            }
        } while (opcion != 4);
    }
}
