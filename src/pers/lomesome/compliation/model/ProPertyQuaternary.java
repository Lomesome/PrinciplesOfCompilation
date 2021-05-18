package pers.lomesome.compliation.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProPertyQuaternary {
    private final SimpleIntegerProperty No;
    private final SimpleStringProperty First;
    private final SimpleStringProperty Second;
    private final SimpleStringProperty Third;
    private final SimpleStringProperty Fourth;

    public ProPertyQuaternary(int no, String First, String Second, String Third, String Fourth) {
        this.No = new SimpleIntegerProperty(no);
        this.First = new SimpleStringProperty(First);
        this.Second = new SimpleStringProperty(Second);
        this.Third = new SimpleStringProperty(Third);
        this.Fourth = new SimpleStringProperty(Fourth);
    }

    public int getNo() {
        return No.get();
    }

    public void setNo(int no) {
        this.No.set(no);
    }

    public String getFirst() {
        return First.get();
    }

    public void setFirst(String first) {
        this.First.set(first);
    }

    public String getSecond() {
        return Second.get();
    }

    public void setSecond(String second) {
        this.Second.set(second);
    }

    public String getThird() {
        return Third.get();
    }

    public void setThird(String third) {
        this.Third.set(third);
    }

    public String getFourth() {
        return Fourth.get();
    }

    public void setFourth(String fourth) {
        this.Fourth.set(fourth);
    }

}
