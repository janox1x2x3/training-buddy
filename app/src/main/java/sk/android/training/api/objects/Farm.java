package sk.android.training.api.objects;

import android.content.ContentValues;
import android.content.res.Resources;
import android.text.TextUtils;

import com.creatix.cratefree.R;
import com.creatix.cratefree.db.tables.FarmTable;
import com.creatix.cratefree.utils.Utils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jano on 26.11.2015.
 */
public class Farm implements Serializable {

    private Long id;
    private String farmId;
    private String name;
    private String brand;
    private String v;
    private String URL;
    private String phone;
    private String email;
    private String person;
    private String longitude;
    private String latitude;
    private String county;
    private String zip;
    private String town;
    private String street;
    private Float distance;
    private List<String> products;
    private List<String> types;

    public Long getId() {
        return id;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Float getDistance() {
        return distance;
    }

    public String getAddressList() {

        String delimiter = ", ";

        StringBuilder stringBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(street)) {
            stringBuilder.append(street);
        }

        if (!TextUtils.isEmpty(county)) {
            if (stringBuilder.toString().length() != 0) {
                stringBuilder.append(delimiter);
            }
            stringBuilder.append(county);
        }

        if (!TextUtils.isEmpty(zip)) {
            if (stringBuilder.toString().length() != 0) {
                stringBuilder.append(delimiter);
            }
            stringBuilder.append(zip);
        }

        if (!TextUtils.isEmpty(town)) {
            if (stringBuilder.toString().length() != 0) {
                stringBuilder.append(delimiter);
            }
            stringBuilder.append(town);
        }

        return stringBuilder.toString();
    }

    public String getAddress() {

        String delimiter = ", ";

        StringBuilder stringBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(town)) {
            stringBuilder.append(town);
        }

        if (!TextUtils.isEmpty(zip)) {
            if (stringBuilder.toString().length() != 0) {
                stringBuilder.append(delimiter);
            }
            stringBuilder.append(zip);
        }

        if (!TextUtils.isEmpty(county)) {
            if (stringBuilder.toString().length() != 0) {
                stringBuilder.append(delimiter);
            }
            stringBuilder.append(county);
        }

        return stringBuilder.toString();
    }

    public String getTypesString() {

        String delimiter = " & ";

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < types.size(); i++) {
            String type = types.get(i);
            stringBuilder.append(type.substring(0, 1).toUpperCase() + type.substring(1));
            if (i < types.size() - 1) {
                stringBuilder.append(delimiter);
            }
        }

        return stringBuilder.toString();
    }

    public String getProductsString(Resources res) {

        String delimiter = res.getString(R.string.char_bullet);

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < products.size(); i++) {
            stringBuilder.append(products.get(i).toUpperCase());
            if (i < products.size() - 1) {
                stringBuilder.append(delimiter);
            }
        }

        return stringBuilder.toString();
    }

    public String getDistanceInMiles() {

        String miles = "";
        if (this.distance != null) {
            double raw = this.distance / 1609.344d;
            miles = Utils.round(raw, raw > 10d ? 0 : 1);
        }

        return miles;
    }

    public static int getProductRes(String  productString) {

        if(TextUtils.isEmpty(productString)) {
            return 0;
        }

        Product product = Product.findByProduct(productString);

        switch (product) {
            case PORK:
                return R.drawable.filters_pig;
            case CHICKEN:
                return R.drawable.filters_chicken;
            case BEEF:
                return R.drawable.filters_beef;
            case VEAL:
                return R.drawable.filters_veal;
            case LAMB:
                return R.drawable.filters_lamb;
            case GOAT:
                return R.drawable.filters_goat;
            case TURKEY:
                return R.drawable.filters_turkey;
            case DUCK:
                return R.drawable.filters_duck;
            case BISON:
                return R.drawable.filters_bison;
            case LLAMAS:
                return R.drawable.filters_llamas;
            case GOOSE:
                return R.drawable.filters_goose;
            case DAIRY:
                return R.drawable.filters_dairy;
            case EGGS:
                return R.drawable.filters_egg;
            default:
                return R.drawable.beef;
        }
    }

    public boolean hashChanged(ContentValues values) {

        String name = values.getAsString(FarmTable.NAME);
        String brand = values.getAsString(FarmTable.BRAND);
        String v = values.getAsString(FarmTable.V);
        String URL = values.getAsString(FarmTable.URL);
        String phone = values.getAsString(FarmTable.PHONE);
        String email = values.getAsString(FarmTable.EMAIL);
        String person = values.getAsString(FarmTable.PERSON);
        String longitude = values.getAsString(FarmTable.LONGITUDE);
        String latitude = values.getAsString(FarmTable.LATITUDE);
        String county = values.getAsString(FarmTable.COUNTY);
        String zip = values.getAsString(FarmTable.ZIP);
        String town = values.getAsString(FarmTable.TOWN);
        String street = values.getAsString(FarmTable.STREET);
        String products = values.getAsString(FarmTable.PRODUCTS);
        String types = values.getAsString(FarmTable.TYPES);

        if (name != null && !name.equals(this.name)) {
            return true;
        } else if (brand != null && !brand.equals(this.brand)) {
            return true;
        } else if (v != null && !v.equals(this.v)) {
            return true;
        } else if (URL != null && !URL.equals(this.URL)) {
            return true;
        } else if (phone != null && !phone.equals(this.phone)) {
            return true;
        } else if (email != null && !email.equals(this.email)) {
            return true;
        } else if (person != null && !person.equals(this.person)) {
            return true;
        } else if (longitude != null && !longitude.equals(this.longitude)) {
            return true;
        } else if (latitude != null && !latitude.equals(this.latitude)) {
            return true;
        } else if (county != null && !county.equals(this.county)) {
            return true;
        } else if (zip != null && !zip.equals(this.zip)) {
            return true;
        } else if (town != null && !town.equals(this.town)) {
            return true;
        } else if (street != null && !street.equals(this.street)) {
            return true;
        } else if (products != null && !products.equals(this.products)) {
            return true;
        } else if (types != null && !types.equals(this.types)) {
            return true;
        }

        return false;
    }
}
