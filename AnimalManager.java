
import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

class AnimalManager {

    public static void main(String[] args) {
        Scanner mainScanner = new Scanner(System.in);
        try {
            addAnimal(new Animal("Lion", 120.20f, true, 2015, AnimalKind.MAMMAL));
            addAnimal(new Animal("Eagle", 75.25f, false, 2018, AnimalKind.BIRD));

            showMenu(mainScanner);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            mainScanner.close();
        }
    }
    private static final String FILE_PATH = "./animals.xml";

    private static void createFileIfNotExists() throws Exception {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("Animals");
            doc.appendChild(rootElement);

            saveDocument(doc);
        }
    }

    private static void saveDocument(Document doc) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(FILE_PATH));
        transformer.transform(source, result);
    }

    public static Animal getNewAnimal(Scanner scanner) {
        Animal inputAnimal = new Animal();
        System.out.println("Enter Name:");
        inputAnimal.setName(scanner.nextLine());

        System.out.println("Enter Weight:");
        inputAnimal.setWeight(scanner.nextFloat());
        scanner.nextLine(); // Consume newline

        System.out.println("Enter 1 if male, 2 if female:");
        inputAnimal.setIsMale(scanner.nextInt() == 1);
        scanner.nextLine(); // Consume newline

        System.out.println("Enter Birth Year:");
        inputAnimal.setBirthYear(scanner.nextInt());
        scanner.nextLine(); // Consume newline

        System.out.println("Choose Animal Kind:");
        for (AnimalKind ak : AnimalKind.values()) {
            System.out.printf("\n%d) %s", ak.ordinal(), ak);
        }
        inputAnimal.setKind(AnimalKind.values()[scanner.nextInt()]);
        scanner.nextLine(); // Consume newline

        return inputAnimal;
    }

    public static void addAnimal(Animal animal) throws Exception {
        createFileIfNotExists();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new File(FILE_PATH));

        Element root = doc.getDocumentElement();

        Element animalElement = doc.createElement("Animal");

        appendElement(doc, animalElement, "Name", animal.getName());
        appendElement(doc, animalElement, "Weight", String.valueOf(animal.getWeight()));
        appendElement(doc, animalElement, "IsMale", String.valueOf(animal.getIsMale()));
        appendElement(doc, animalElement, "BirthYear", String.valueOf(animal.getBirthYear()));
        appendElement(doc, animalElement, "Kind", animal.getKind().name());

        root.appendChild(animalElement);
        saveDocument(doc);
        System.out.println("Animal added");
    }

    public static List<Animal> getAllAnimals() throws Exception {
        List<Animal> animals = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            throw new Exception("File not found");
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);

        NodeList nodeList = doc.getElementsByTagName("Animal");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            String name = element.getElementsByTagName("Name").item(0).getTextContent();
            float weight = Float.parseFloat(element.getElementsByTagName("Weight").item(0).getTextContent());
            boolean isMale = Boolean.parseBoolean(element.getElementsByTagName("IsMale").item(0).getTextContent());
            int birthYear = Integer.parseInt(element.getElementsByTagName("BirthYear").item(0).getTextContent());
            AnimalKind kind = AnimalKind.valueOf(element.getElementsByTagName("Kind").item(0).getTextContent());

            animals.add(new Animal(name, weight, isMale, birthYear, kind));
        }
        return animals;
    }

    public static Animal getAnimalByName(String name) throws Exception {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            throw new Exception("File not found");
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);

        NodeList nodeList = doc.getElementsByTagName("Animal");

        Element element;
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (doc.getElementsByTagName("Name").item(i).getTextContent().contains(name)) {
                element = (Element) nodeList.item(i);
                String fullname = element.getElementsByTagName("Name").item(0).getTextContent();
                float weight = Float.parseFloat(element.getElementsByTagName("Weight").item(0).getTextContent());
                boolean isMale = Boolean.parseBoolean(element.getElementsByTagName("IsMale").item(0).getTextContent());
                int birthYear = Integer.parseInt(element.getElementsByTagName("BirthYear").item(0).getTextContent());
                AnimalKind kind = AnimalKind.valueOf(element.getElementsByTagName("Kind").item(0).getTextContent());
                return new Animal(fullname, weight, isMale, birthYear, kind);
            }
        }
        return null;
    }

    public static void updateAnimal(String oldName, Animal newAnimal) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new File(FILE_PATH));

        NodeList nodeList = doc.getElementsByTagName("Animal");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            String currentName = element.getElementsByTagName("Name").item(0).getTextContent();
            if (currentName.equals(oldName)) {
                element.getElementsByTagName("Name").item(0).setTextContent(newAnimal.getName());
                element.getElementsByTagName("Weight").item(0).setTextContent(String.valueOf(newAnimal.getWeight()));
                element.getElementsByTagName("IsMale").item(0).setTextContent(String.valueOf(newAnimal.getIsMale()));
                element.getElementsByTagName("BirthYear").item(0).setTextContent(String.valueOf(newAnimal.getBirthYear()));
                element.getElementsByTagName("Kind").item(0).setTextContent(newAnimal.getKind().name());
                break;
            }
        }
        saveDocument(doc);
    }

    public static void deleteAnimal(String name) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new File(FILE_PATH));

        NodeList nodeList = doc.getElementsByTagName("Animal");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            String currentName = element.getElementsByTagName("Name").item(0).getTextContent();
            if (currentName.equals(name)) {
                element.getParentNode().removeChild(element);
                break;
            }
        }
        saveDocument(doc);
    }

    private static void appendElement(Document doc, Element parent, String tagName, String value) {
        Element element = doc.createElement(tagName);
        element.appendChild(doc.createTextNode(value));
        parent.appendChild(element);
    }

    static void showMenu(Scanner scanner) {
        boolean exit = false;
        int answer;
        while (!exit) {
            System.out.println("What do you want to do?");
            System.out.println("1) Create");
            System.out.println("2) Read All");
            System.out.println("3) Read One");
            System.out.println("4) Update");
            System.out.println("5) Delete");
            System.out.println("6) Exit");
            try {
                answer = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (answer) {
                    case 1 ->
                        addAnimal(getNewAnimal(scanner));
                    case 2 -> {
                        for (Animal a : getAllAnimals()) {
                            System.out.println(a.showInfo());
                        }
                    }
                    case 3 -> {
                        System.out.println("Enter name to read:");
                        String name = scanner.nextLine();
                        Animal animal = getAnimalByName(name);
                        if (animal != null) {
                            System.out.println(animal.showInfo());
                        } else {
                            System.out.println("Animal not found.");
                        }
                    }
                    case 4 -> {
                        System.out.println("Enter name to update:");
                        String oldName = scanner.nextLine();
                        System.out.println("Enter new info:");
                        updateAnimal(oldName, getNewAnimal(scanner));
                    }
                    case 5 -> {
                        System.out.println("Enter name to delete:");
                        String nameToDelete = scanner.nextLine();
                        deleteAnimal(nameToDelete);
                    }
                    case 6 ->
                        exit = true;
                    default ->
                        System.err.println("Wrong choice!");
                }
            } catch (InputMismatchException e) {
                System.err.println("Please enter a valid number.");
                scanner.nextLine(); // Clear invalid input
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
    }

}
