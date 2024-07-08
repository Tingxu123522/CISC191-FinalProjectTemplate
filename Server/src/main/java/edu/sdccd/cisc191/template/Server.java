package edu.sdccd.cisc191.template;

import java.io.*;
import java.net.*;
import java.util.*;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    // Module 1: 2D Array Operations
    int[][] array;

    // Module 5: Generics and Collections
    private List<int[]> arrayList;

    public Server() {
        array = new int[5][5];
        arrayList = new ArrayList<>();
        for (int[] row : array) {
            arrayList.add(row);
        }
    }

    public void start(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            CustomerRequest request = CustomerRequest.fromJSON(inputLine);
            CustomerResponse response = new CustomerResponse(request.getId(), "Jane", "Doe");
            out.println(CustomerResponse.toJSON(response));
        }
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    // Module 1: Interactive Console Menu
    public void consoleMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Get at Index");
            System.out.println("2. Set at Index");
            System.out.println("3. Find Index Of");
            System.out.println("4. Print All");
            System.out.println("5. Delete at Index");
            System.out.println("6. Expand");
            System.out.println("7. Shrink");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    getAtIndex();
                    break;
                case 2:
                    setAtIndex();
                    break;
                case 3:
                    findIndexOf();
                    break;
                case 4:
                    printAll();
                    break;
                case 5:
                    deleteAtIndex();
                    break;
                case 6:
                    expand();
                    break;
                case 7:
                    shrink();
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void getAtIndex() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter row index: ");
        int row = scanner.nextInt();
        System.out.print("Enter column index: ");
        int col = scanner.nextInt();
        try {
            System.out.println("Value at [" + row + "][" + col + "] is: " + array[row][col]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Index out of bounds");
        }
    }

    private void setAtIndex() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter row index: ");
        int row = scanner.nextInt();
        System.out.print("Enter column index: ");
        int col = scanner.nextInt();
        System.out.print("Enter value: ");
        int value = scanner.nextInt();
        try {
            array[row][col] = value;
            System.out.println("Value set at [" + row + "][" + col + "] is: " + value);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Index out of bounds");
        }
    }

    private void findIndexOf() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter value to find: ");
        int value = scanner.nextInt();
        boolean found = false;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] == value) {
                    System.out.println("Value found at [" + i + "][" + j + "]");
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            System.out.println("Value not found");
        }
    }

    private void printAll() {
        for (int[] row : array) {
            System.out.println(Arrays.toString(row));
        }
    }

    private void deleteAtIndex() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter row index to delete: ");
        int row = scanner.nextInt();
        System.out.print("Enter column index to delete: ");
        int col = scanner.nextInt();
        try {
            array[row][col] = 0;
            System.out.println("Value at [" + row + "][" + col + "] deleted");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Index out of bounds");
        }
    }

    private void expand() {
        array = Arrays.copyOf(array, array.length + 1);
        array[array.length - 1] = new int[array[0].length];
        arrayList.add(array[array.length - 1]);
        System.out.println("Array expanded");
    }

    private void shrink() {
        if (array.length > 1) {
            arrayList.remove(array.length - 1);
            array = arrayList.toArray(new int[arrayList.size()][]);
            System.out.println("Array shrunk");
        } else {
            System.out.println("Array cannot be shrunk further");
        }
    }

    // Module 2: JavaFX GUI
    public static class GUI extends Application {
        private Server server = new Server();

        @Override
        public void start(Stage primaryStage) {
            primaryStage.setTitle("2D Array Operations");

            VBox vbox = new VBox();
            vbox.setPadding(new Insets(10));
            vbox.setSpacing(10);

            TextField rowField = new TextField();
            rowField.setPromptText("Row index");
            TextField colField = new TextField();
            colField.setPromptText("Column index");
            TextField valueField = new TextField();
            valueField.setPromptText("Value");

            Button getButton = new Button("Get at Index");
            getButton.setOnAction(e -> {
                int row = Integer.parseInt(rowField.getText());
                int col = Integer.parseInt(colField.getText());
                try {
                    String result = "Value at [" + row + "][" + col + "] is: " + server.array[row][col];
                    showResult(result);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    showResult("Index out of bounds");
                }
            });

            Button setButton = new Button("Set at Index");
            setButton.setOnAction(e -> {
                int row = Integer.parseInt(rowField.getText());
                int col = Integer.parseInt(colField.getText());
                int value = Integer.parseInt(valueField.getText());
                try {
                    server.array[row][col] = value;
                    showResult("Value set at [" + row + "][" + col + "] is: " + value);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    showResult("Index out of bounds");
                }
            });

            Button findButton = new Button("Find Index Of");
            findButton.setOnAction(e -> {
                int value = Integer.parseInt(valueField.getText());
                boolean found = false;
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < server.array.length; i++) {
                    for (int j = 0; j < server.array[i].length; j++) {
                        if (server.array[i][j] == value) {
                            result.append("Value found at [").append(i).append("][").append(j).append("]\n");
                            found = true;
                        }
                    }
                }
                if (!found) {
                    result.append("Value not found");
                }
                showResult(result.toString());
            });

            Button printButton = new Button("Print All");
            printButton.setOnAction(e -> {
                StringBuilder result = new StringBuilder();
                for (int[] row : server.array) {
                    result.append(Arrays.toString(row)).append("\n");
                }
                showResult(result.toString());
            });

            Button deleteButton = new Button("Delete at Index");
            deleteButton.setOnAction(e -> {
                int row = Integer.parseInt(rowField.getText());
                int col = Integer.parseInt(colField.getText());
                try {
                    server.array[row][col] = 0;
                    showResult("Value at [" + row + "][" + col + "] deleted");
                } catch (ArrayIndexOutOfBoundsException ex) {
                    showResult("Index out of bounds");
                }
            });

            Button expandButton = new Button("Expand");
            expandButton.setOnAction(e -> {
                server.expand();
                showResult("Array expanded");
            });

            Button shrinkButton = new Button("Shrink");
            shrinkButton.setOnAction(e -> {
                server.shrink();
                showResult("Array shrunk");
            });

            vbox.getChildren().addAll(rowField, colField, valueField, getButton, setButton, findButton, printButton, deleteButton, expandButton, shrinkButton);


            Scene scene = new Scene(vbox, 300, 400);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        private void showResult(String result) {
            Stage resultStage = new Stage();
            resultStage.setTitle("Result");
            VBox vbox = new VBox();
            vbox.setPadding(new Insets(10));
            vbox.setSpacing(10);

            TextArea resultArea = new TextArea(result);
            resultArea.setEditable(false);

            vbox.getChildren().add(resultArea);

            Scene scene = new Scene(vbox, 300, 200);
            resultStage.setScene(scene);
            resultStage.show();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start(4444);
            server.consoleMenu();  // Show interactive console menu

            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Launch JavaFX GUI
        Application.launch(GUI.class, args);
    }
}
