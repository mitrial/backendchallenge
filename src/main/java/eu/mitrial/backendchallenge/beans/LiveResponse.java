package eu.mitrial.backendchallenge.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class LiveResponse {

    @JsonProperty("items")
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
