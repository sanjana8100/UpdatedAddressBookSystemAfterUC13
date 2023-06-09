package com.bridgelabz.AddressBookSystem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class AddressBookMain {
    public static Map<String,AddressBook> addressBookMap= new HashMap<>();

    public static void addAddressBook() {
        Scanner in = new Scanner(System.in);
        AddressBook addressBook = new AddressBook();

        System.out.println("Enter the name of the new Address Book: ");
        addressBook.setAddressBookName(in.next());

        if (addressBookMap.containsKey(addressBook.getAddressBookName())) {
            System.out.println("Address Book already exists!!!!");
            return;
        }

        addressBookMap.put(addressBook.getAddressBookName(),addressBook);
        System.out.println("Address Book Added!!!");
        System.out.println();

        boolean status= true;
        while(status){
            System.out.println("=> To ADD a Contact to this Address Book: PRESS 1");
            System.out.println("=> To Close this Address Book: PRESS 2");
            int choice = in.nextInt();
            switch (choice){
                case 1:
                    addressBook.addContact();
                    System.out.println(addressBook);
                    System.out.println("Contact Added!!!");
                    System.out.println();
                    break;
                case 2:
                    status=false;
                    break;
                default:
                    System.out.println("Enter a valid choice!!!");
            }
        }
    }

    public static void addContacts() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the name of the address book you want to add contact:");
        String name = in.next();

        if(addressBookMap.containsKey(name)) {
            AddressBook Temp= addressBookMap.get(name);
            Temp.addContact();
            System.out.println(Temp);
            System.out.println("Contact Added!!!");
            System.out.println();
        }
        else
            System.out.println("Given Address Book not Found!!!\n");
    }

    public static void editContact(){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the name of the address book, the contact you want to edit exists:");
        String name= in.next();

        if(addressBookMap.containsKey(name)) {
            AddressBook Temp= addressBookMap.get(name);
            Temp.editDetails();
        }
        else
            System.out.println("Given Address Book not Found!!!\n");
    }

    public static void deleteContact(){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the name of the address book, the contact you want to Delete exists:");
        String name= in.next();

        if(addressBookMap.containsKey(name)) {
            AddressBook Temp= addressBookMap.get(name);
            Temp.deleteDetails();
        }
        else
            System.out.println("Given Address Book not Found!!!\n");
    }

    public static void searchContact(){
        Scanner in = new Scanner(System.in);
        System.out.println("=> To search all Contacts from a specific City: PRESS 1");
        System.out.println("=> To search all Contacts from a specific State: PRESS 2");
        int choice = in.nextInt();

        switch (choice){
            case 1:
                System.out.print("Enter the name of the City: ");
                String cityName = in.next();
                List<Contact> cityList = new ArrayList<>();
                addressBookMap.values().stream().forEach(addressBook -> cityList.addAll(addressBook.getContacts().stream().filter(
                        contact -> contact.getCity().equalsIgnoreCase(cityName)).toList()));
                int count1 = cityList.size();
                System.out.println(count1+" Contacts Found, which belongs to " + cityName +" city");
                System.out.println(cityList);
                System.out.println();
                break;
            case 2:
                System.out.print("Enter the name of the State: ");
                String stateName = in.next();
                List<Contact> stateList = new ArrayList<>();
                addressBookMap.values().stream().forEach(addressBook -> stateList.addAll(addressBook.getContacts().stream().filter(
                        contact -> contact.getState().equalsIgnoreCase(stateName)).toList()));
                int count2 = stateList.size();
                System.out.println(count2+" Contacts Found, which belongs to " + stateName +" city");
                System.out.println(stateList);
                System.out.println();
                break;
            default:
                System.out.println("Please Choose valid option!!!");
                searchContact();
                break;
        }
    }

    public static void displayAddressBook(){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the name of the address book you want to Display:");
        String name = in.next();
        if(addressBookMap.containsKey(name)) {
            AddressBook Temp = addressBookMap.get(name);
            System.out.println(Temp);
        }
        else
            System.out.println("Given Address Book not Found!!!\n");
    }

    public static void displaySortedAddressBook(){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the name of the address book you want to Display:");
        String name = in.next();
        if(addressBookMap.containsKey(name)) {
            AddressBook Temp = addressBookMap.get(name);
            System.out.println("Choose the option to sort the contacts in the Address Book based on:");
            System.out.println("1.First Name\t 2.City \t 3.State\t 4.ZIP Code");
            int choice = in.nextInt();

            List<Contact> sortedList = new ArrayList<>();
            switch (choice){
                case 1:
                    sortedList = Temp.getContacts().stream().sorted(Comparator.comparing(Contact::getFirstName)).collect(Collectors.toList());
                    break;
                case 2:
                    sortedList = Temp.getContacts().stream().sorted(Comparator.comparing(Contact::getCity)).collect(Collectors.toList());
                    break;
                case 3:
                    sortedList = Temp.getContacts().stream().sorted(Comparator.comparing(Contact::getState)).collect(Collectors.toList());
                    break;
                case 4:
                    sortedList = Temp.getContacts().stream().sorted(Comparator.comparing(Contact::getZip)).collect(Collectors.toList());
                    break;
                default:
                    System.out.println("Choose Valid Option!!!");
                    break;
            }
            System.out.println("The Sorted Contacts: ");
            System.out.println(sortedList);
            System.out.println();
        }
        else
            System.out.println("Given Address Book not Found!!!\n");
    }

    private static void writeToFile() {
        String path = "C:\\Users\\INS 5570\\IdeaProjects\\UpdatedAddressBookUsingCSVandJSON\\src\\main\\java\\com\\bridgelabz\\AddressBookSystem\\AddressBooks.txt";
        StringBuffer addressBookBuffer = new StringBuffer();
        addressBookMap.values().stream().forEach(contact -> {
            String contactDataString = contact.toString().concat("\n");
            addressBookBuffer.append(contactDataString);
        });

        try {
            Files.write(Paths.get(path), addressBookBuffer.toString().getBytes());
        }
        catch (IOException e) {
            System.out.println("Catch block");
        }
    }

    private static void readFromFile() {
        String path = "C:\\Users\\INS 5570\\IdeaProjects\\UpdatedAddressBookUsingCSVandJSON\\src\\main\\java\\com\\bridgelabz\\AddressBookSystem\\AddressBooks.txt";
        System.out.println("Reading from : " + path + "\n");
        try {
            Files.lines(new File(path).toPath()).forEach(contactDetails -> System.out.println(contactDetails));
        }
        catch(IOException e){
            System.out.println("Catch block");
        }
    }

    private static void writeToCSVFile() {
        FileWriter fileWriter = null;
        String csvPath = "C:\\Users\\INS 5570\\Desktop\\AddressBooks.csv";

        try {
            fileWriter = new FileWriter(csvPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        CSVWriter writer = new CSVWriter(fileWriter);
        List<String[]> csvLines = new ArrayList<>();

        addressBookMap.keySet().stream().forEach(bookName -> addressBookMap.get(bookName).getContacts()
                .stream().forEach(contact -> csvLines.add(new String[]{contact.toString()})));


        writer.writeAll(csvLines);

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readFromCSVFile(){
        String csvPath = "C:\\Users\\INS 5570\\Desktop\\AddressBooks.csv";
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(csvPath);
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        CSVReader reader = new CSVReaderBuilder(fileReader).build();

        List<String[]> linesOfData = null;

        try {
            linesOfData = reader.readAll();
        } catch (IOException | CsvException e) {

            e.printStackTrace();
        }

        System.out.println("\nReading from : " + csvPath + "\n");
        linesOfData.stream().forEach(lines -> {
            for (String value : lines)
                System.out.print(value + "\t");
            System.out.println();
        });

        try {
            reader.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private static void writeToJSONFile(){
        String jsonPath = "C:\\Users\\INS 5570\\IdeaProjects\\UpdatedAddressBookUsingCSVandJSON\\src\\main\\java\\com\\bridgelabz\\AddressBookSystem\\AddressBooks.json";

        try {
            FileWriter fileWriter = new FileWriter(jsonPath);
            Gson gson = new GsonBuilder().registerTypeAdapter(Contact.class,
                    new TypeAdapter<String>() {
                        @Override
                        public void write(JsonWriter jsonWriter, String contact) throws IOException{
                            jsonWriter.value(contact.toString());
                        }
                        @Override
                        public String read(JsonReader jsonReader) throws IOException{
                            return jsonReader.nextString();
                        }
                    }).create();
            addressBookMap.values().stream().forEach(contact -> {
                String contactDataString = contact.toString().concat("\n");
                gson.toJson(contactDataString,fileWriter);
            });
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readFromJSONFile(){
        String jsonPath = "C:\\Users\\INS 5570\\IdeaProjects\\UpdatedAddressBookUsingCSVandJSON\\src\\main\\java\\com\\bridgelabz\\AddressBookSystem\\AddressBooks.json";

        try {
            FileReader fileReader = new FileReader(jsonPath);
            Type type = new TypeToken<HashMap<String,AddressBook>>(){}.getType();
            Gson gson = new GsonBuilder().registerTypeAdapter(Contact.class,
                    new TypeAdapter<String>() {
                        @Override
                        public void write(JsonWriter jsonWriter, String contact) throws IOException{
                             jsonWriter.value(contact.toString());
                        }
                        @Override
                        public String read(JsonReader jsonReader) throws IOException{
                            return jsonReader.nextString();
                        }
                    }).create();
            System.out.println("Reading from : " + jsonPath + "\n");
            List<String> theList = gson.fromJson(fileReader,type);
            fileReader.close();
            for (String contact : theList){
                System.out.println(contact.toString());
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Address Book Program in Address Book Main class on Main Branch");

        Scanner in = new Scanner(System.in);
        boolean status= true;
        while(status) {
            System.out.println("******************MENU:******************");
            System.out.println("=> To Add Address Book: PRESS 1");
            System.out.println("=> To Add Contact: PRESS 2");
            System.out.println("=> To Edit an Existing Contact: PRESS 3");
            System.out.println("=> To Delete a Contact: PRESS 4");
            System.out.println("=> To Search all the Contacts from a specific City or specific State: PRESS 5");
            System.out.println("=> To Display Dictionary of Address Books: PRESS 6");
            System.out.println("=> To Display Address Books Of Contacts: PRESS 7");
            System.out.println("=> To Display Contacts in an Address Book in Sorted Order based on a specific detail: PRESS 8");
            System.out.println("=> To Read and Display all the Contacts from the Address Book File: PRESS 9");
            System.out.println("=> To Read and Display all the Contacts from the Address Book CSV File: PRESS 10");
            System.out.println("=> To Read and Display all the Contacts from the Address Book JSON File: PRESS 11");
            System.out.println("=> To EXIT: PRESS 12");
            int choice = in.nextInt();

            switch (choice) {
                case 1:
                    addAddressBook();
                    writeToFile();
                    writeToCSVFile();
                    writeToJSONFile();
                    System.out.println();
                    break;
                case 2:
                    addContacts();
                    writeToFile();
                    writeToCSVFile();
                    writeToJSONFile();
                    break;
                case 3:
                    editContact();
                    break;
                case 4:
                    deleteContact();
                    break;
                case 5:
                    searchContact();
                    break;
                case 6:
                    System.out.println(addressBookMap);
                    break;
                case 7:
                    displayAddressBook();
                    break;
                case 8:
                    displaySortedAddressBook();
                    break;
                case 9:
                    readFromFile();
                    break;
                case 10:
                    readFromCSVFile();
                    break;
                case 11:
                    readFromJSONFile();
                    break;
                default:
                    status=false;
                    break;
            }
        }
    }
}
