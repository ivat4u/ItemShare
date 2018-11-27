package example.com.itemshare;

/**
 * Created by hasee on 2017/8/15.
 */

public class Item {
    private String name;
    private int imageId;
    private double price;
    private int number;
    private String place;
    public Item(String Name,int ImageId,double price,int number,String place){
        this.name=Name;
        this.imageId=ImageId;
        this.place=place;
        this.number=number;
        this.price=price;
    }
    public String getName(){
        return  name;
    }
    public int getImageId(){
        return  imageId;
    }

    public double getPrice() {
        return price;
    }

    public int getNumber() {
        return number;
    }

    public String getPlace() {
        return place;
    }

}
