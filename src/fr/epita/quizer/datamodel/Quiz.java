package fr.epita.quizer.datamodel;

public class Quiz {

    private String title;
    private Integer id;

    private Quiz(){

    }

    public Quiz(String title){
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
