package model.movie;


import javax.lang.model.element.Name;

public enum Genres {

    All("All genres"),
    Action("Action"),
    Adventure("Adventure"),
    Comedy("Comedy"),
    Drama("Drama"),
    Fantasy("Fantasy"),
    Horror("Horror"),
    Mystery("Mystery"),
    Romance("Romance"),
    ScienceFiction("Science Fiction"),
    Thriller("Thriller"),
    Western("Western");
    private final String displayName;

    Genres(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() { return displayName;}
    @Override public String toString() { return displayName; }
}
