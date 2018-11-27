package example.com.itemshare;

/**
 * Created by hasee on 2017/8/15.
 */

public class Item {
    private String name;
    private String imageId;
    private String price;
    private String number;
    private int good_id;
    private int score;
    private int user_id;
    private String var;

    public Item(String Name,int good_id,String price,int score,String number,String imageId,int user_id,String var){
        this.name=Name;
        this.imageId=imageId;
        this.good_id=good_id;
        this.user_id=user_id;;
        this.var=var;
        this.score=score;
        this.number=number;
        this.price=price;
    }
    public String getName(){
        return  name;
    }
    public String getImageId(){
        return  imageId;
    }

    public String getPrice() {
        return price;
    }

    public String getNumber() {
        return number;
    }

    public int getGood_id() {
        return good_id;
    }

    public int getScore() {
        return score;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getVar() {
        return var;
    }
}
