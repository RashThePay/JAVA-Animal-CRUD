
enum AnimalKind {
    MAMMAL, BIRD, FISH, REPTILE, OTHER
}

public final class Animal {

    private String name;
    private float weight;
    private boolean isMale;
    private int birthYear;
    private AnimalKind kind;

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    float getWeight() {
        return weight;
    }

    void setWeight(float weight) {
        this.weight = weight;
    }

    boolean getIsMale() {
        return isMale;
    }

    void setIsMale(boolean isMale) {
        this.isMale = isMale;
    }

    int getBirthYear() {
        return birthYear;
    }

    void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    AnimalKind getKind() {
        return kind;
    }

    void setKind(AnimalKind kind) {
        this.kind = kind;
    }

    //////------------------------- constructor all properties
    Animal(String name, float weight, boolean isMale, int birthYear, AnimalKind kind) {
        setName(name);
        setWeight(weight);
        setIsMale(isMale);
        setBirthYear(birthYear);
        setKind(kind);
    }

    //////------------------------- constructor default
     public Animal() {
        this.name = "Unknown";
        this.weight = 10;
        this.isMale = true;
        this.birthYear = 2022;
        this.kind = AnimalKind.OTHER;

    }

    //// ------------------------ constructor copy
     public Animal(Animal animal) {
        this.name = animal.name;
        this.weight = animal.weight;
        this.birthYear = animal.birthYear;
        this.isMale = animal.isMale;
        this.kind = animal.kind;
    }

    public String showInfo() {
        return String.format("Name: %s\tWeight: %.2f\tGender: %s\tBirth year: %d\tKind: %s",
         getName(), getWeight(), getIsMale() ? "male" : "female", getBirthYear(), getKind());
    }
}
