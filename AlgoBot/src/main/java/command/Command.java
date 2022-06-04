package command;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "commands")
@NamedQueries({
        @NamedQuery(name = "Command.findByName",
                query = "select e from Command e where e.name = :name"),
        @NamedQuery(name = "Command.findAllNames",
                query = "select e.name from Command e")
})
public class Command implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "id")
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "information")
    private String information;

    @Column(name = "link")
    private String link;

    public Command() {
    }

    public Command(Integer id, String name, String info, String link) {
        this.id = id;
        this.name = name;
        this.information = info;
        this.link = link;
    }

    public String getInformation() {
        return information;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "Command{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", information='" + information + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
