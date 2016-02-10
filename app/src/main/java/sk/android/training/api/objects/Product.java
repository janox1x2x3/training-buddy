package sk.android.training.api.objects;

import java.util.HashMap;
import java.util.Map;

public enum Product {

    PORK("PORK", "pork"),
    CHICKEN("CHICKEN", "chicken"),
    BEEF("BEEF", "beef"),
    VEAL("VEAL", "veal"),
    LAMB("LAMB / SHEEP", "lamb / sheep"),
    GOAT("GOAT", "goat"),
    TURKEY("TURKEY", "turkey"),
    DUCK("DUCK", "duck"),
    BISON("BISON", "bison"),
    LLAMAS("LLAMAS", "llamas"),
    GOOSE("GOOSE", "goose"),
    DAIRY("DAIRY", "dairy"),
    EGGS("EGGS", "eggs");

    private String label;
    private String product;

    Product(String label, String product) {
        this.label = label;
        this.product = product;
    }

    public String getLabel() {
        return label;
    }

    public String getProduct() {
        return product;
    }

    private static final Map<String, Product> channels = new HashMap<>();

    static {
        for (Product channel : values()) {
            channels.put(channel.getProduct(), channel);
        }
    }

    public static Product findByProduct(String product) {
        return channels.get(product);
    }
}