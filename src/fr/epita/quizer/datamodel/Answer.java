package fr.epita.quizer.datamodel;

public class Answer {
    private Integer id;
    private String text;

    private Answer(){

    }

    public Answer(String text){
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
