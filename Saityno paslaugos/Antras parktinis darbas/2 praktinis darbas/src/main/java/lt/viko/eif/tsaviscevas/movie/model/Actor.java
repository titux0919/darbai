package lt.viko.eif.tsaviscevas.movie.model;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "actor")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"name", "age"})
public class Actor {

    private String name;
    private int age;

    public Actor() {}

    public Actor(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // ✔ konsolės + SOAP + PDF friendly formatas
    @Override
    public String toString() {
        return String.format("%s (%d)", name, age);
    }
}