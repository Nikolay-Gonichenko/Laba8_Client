package Client;

import Connection.*;
import data.FuelType;
import data.Vehicle;


import reader.MyReader;

import java.io.*;
import java.net.ConnectException;
import java.net.InetSocketAddress;


import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * @author Gonichenko Nikolay R3136
 * This is main class
 */
public class ClientMain {
    private static final Scanner scanner = new Scanner(System.in);
    private static SocketChannel socket;

    //private  static MessageForClient message = new MessageForClient();
    public static String init(User user){
        MessageForClient answer = null;
        try {
            socket = SocketChannel.open();
            socket.connect(new InetSocketAddress("localhost", 3346));

            DataToOutput<User> newUser = new DataToOutput<>("newUser", user);
            answer = send(newUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer.getMessage();
    }
    public static String help(){
        DataToOutput<String> help = new DataToOutput<>("help", "nothing");
        MessageForClient message = send(help);
        return message.getMessage();
    }
    public static String info(){
        DataToOutput<String> info = new DataToOutput<>("info", "nothing");
        MessageForClient message = send(info);
        return  message.getMessage();
    }
    public static String show(){
        DataToOutput<String> show = new DataToOutput<>("show", "nothing");
        MessageForClient message = send(show);
        return  message.getMessage();
    }
    public static String groupCount(){
        DataToOutput<String> groupByCreationDate = new DataToOutput<>("group_counting_by_creation_date", "nothing");
        MessageForClient message = send(groupByCreationDate);
        return message.getMessage();
    }
    public static String maxName(){
        DataToOutput<String> maxByName = new DataToOutput<>("max_by_name", "nothing");
        MessageForClient message = send(maxByName);
        return message.getMessage();
    }
    public static String removeFirst(){
        DataToOutput<String> removeFirst = new DataToOutput<>("remove_first", "nothing");
        MessageForClient message = send(removeFirst);
        return message.getMessage();
    }
    public static String add(Vehicle vehicle){
        DataToOutput<Vehicle> add = new DataToOutput<>("add", vehicle);
        MessageForClient message = send(add);
        return message.getMessage();
    }
    public static String update(Vehicle vehicle){
        DataToOutput<Vehicle> update = new DataToOutput<>("update", vehicle);
        MessageForClient message = send(update);
        return message.getMessage();
    }
    public static String removeId(int id){
        DataToOutput<Integer> removeById = new DataToOutput<>("remove_by_id", id);
        MessageForClient message = send(removeById);
        return message.getMessage();
    }
    public static String clear(){
        DataToOutput<String> clear = new DataToOutput<>("clear", "nothing");
        MessageForClient message = send(clear);
        return message.getMessage();
    }
    public static String removeFuelType(FuelType fuelType){
        DataToOutput<FuelType> removeAnyFuelType = new DataToOutput<>("remove_any_by_fuel_type", fuelType);
        MessageForClient message = send(removeAnyFuelType);
        return message.getMessage();
    }
    public static String addMax(Vehicle vehicle){
        DataToOutput<Vehicle> addIfMax = new DataToOutput<>("add_if_max", vehicle);
        MessageForClient message = send(addIfMax);
        return message.getMessage();
    }
    public static String getCollection(){
        DataToOutput<String> getCollection = new DataToOutput<>("getCollection","nothing");
        MessageForClient message = send(getCollection);
        return message.getMessage();
    }
    public static void start() {
        System.out.println("Enter an username: ");
        String username = scanner.nextLine();
        System.out.println("Enter a password: ");
        String password = scanner.nextLine();
        User user = new User(username,password);

        try {
            socket = SocketChannel.open();
            socket.connect(new InetSocketAddress("localhost", 3346));

            DataToOutput<User> newUser = new DataToOutput<>("newUser", user);
            MessageForClient answer = send(newUser);
            System.out.println(answer.getMessage());
            if (!answer.isCommandDone()){
                System.out.println("Exit....");
                System.exit(-1);
            }



            System.out.println("Enter a command");
            String command = scanner.nextLine();
            String[] commands = checkTask(command);

            while (true) {
                MessageForClient message = new MessageForClient();
                switch (commands[0]) {
                    case "execute_script":
                        File scriptName = MyReader.getFileName(scanner, commands);
                        DataToOutput<File> executeScript = new DataToOutput<>("execute_script", scriptName);
                        message = send(executeScript);
                        if (message.getMessage().contains("Data is saved")){
                            commands[0] = "exit";
                        }
                        break;
                    case "add_if_max":
                        Vehicle vehicleAddIfMax = MyReader.getElementFromConsole(scanner);
                        DataToOutput<Vehicle> addIfMax = new DataToOutput<>("add_if_max", vehicleAddIfMax);
                        message = send(addIfMax);
                        break;
                    case "exit":
                        DataToOutput<String> exit = new DataToOutput<>("exit", "nothing");
                        message = send(exit);
                        break;
                    default:
                        System.out.println("There is no this command. Try again");
                        break;
                }
                if (message != null) {
                    if (message.getMessage()!=null){
                        System.out.println("Command was done " + message.isCommandDone());
                        System.out.println(message.getMessage());
                        if (commands[0].equals("exit")){
                            System.exit(0);
                        }
                    }
                }else throw new ConnectException();
                System.out.println("Enter a command");
                command = scanner.nextLine();
                commands = checkTask(command);
            }
        } catch (ConnectException e){
            System.out.println("Server isn't working now. Try to connect later");
        } catch (IOException e) {
            System.out.println("There is no such port or port isn't available");

        } catch (NoSuchElementException e){
            MessageForClient messageCtrlD;
            DataToOutput<String> exit = new DataToOutput<>("exit", "nothing");
            messageCtrlD = send(exit);
            System.out.println("It was exit combination");
            System.out.println("Command was done " + messageCtrlD.isCommandDone());
            System.out.println(messageCtrlD.getMessage());
            System.exit(0);
        }
    }

    private static MessageForClient send(DataToOutput<?> command) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(65336);
            buffer.put(serialize(command));
            buffer.flip();
            socket.write(buffer);
            buffer.clear();
            socket.read(buffer);
            MessageForClient message = deserialize(buffer.array());
            buffer.clear();
            return message;
        } catch (IOException e) {
            return null;
        }
    }

    private static MessageForClient deserialize(byte[] array) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array)) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
                Object ob = objectInputStream.readObject();
                return (MessageForClient) ob;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Deserialize error");
        }
        return null;
    }

    private static <T> byte[] serialize(T command) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(command);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            System.out.println("Serialize problem");
        }
        return null;
    }


    private static String[] checkTask(String task) {
        String[] commands = task.split(" ");
        String[] parameters = new String[2];
        parameters[0] = "";
        parameters[1] = "";
        try {
            for (int i = 0; i < commands.length; i++) {
                if ((commands[i].equals("help")) || (commands[i].equals("info")) || (commands[i].equals("show")) || (commands[i].equals("add")) ||
                        (commands[i].equals("update")) || (commands[i].equals("remove_by_id")) || (commands[i].equals("clear")) ||
                        (commands[i].equals("save")) || (commands[i].equals("execute_script")) || (commands[i].equals("remove_first")) ||
                        (commands[i].equals("remove_head")) || (commands[i].equals("add_if_max")) || (commands[i].equals("remove_any_by_fuel_type")) ||
                        (commands[i].equals("max_by_name")) || (commands[i].equals("group_counting_by_creation_date")) || (commands[i].equals("exit"))) {
                    parameters[0] = commands[i];
                    parameters[1] = commands[i + 1];
                    break;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            parameters[1] = "";
        }
        return parameters;
    }
}
