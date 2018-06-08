package casati.guido.mybakingapp.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guido on 25/05/2018.
 */

public class Recipe implements Parcelable, Serializable {

    @SerializedName("id")
    private String sId;
    @SerializedName("name")
    private String sRecipeName;
    @SerializedName("ingredients")
    private List<Ingredient> lIngredients;
    @SerializedName("steps")
    private List<Step> lSteps;
    @SerializedName("servings")
    private String sServings;
    @SerializedName("image")
    private String sImage;

    //getters
    public String getsServings() {
        return sServings;
    }

    public String getsId() {
        return sId;
    }

    public String getsRecipeName() {
        return sRecipeName;
    }

    public List<Ingredient> getlIngredients() {
        return lIngredients;
    }

    public List<Step> getlSteps() {
        return lSteps;
    }

    //constructor
    public Recipe() {
    }

    //parcelable
    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sId);
        dest.writeString(this.sRecipeName);
    }

    private Recipe(Parcel in) {
        this.sId = in.readString();
        this.sRecipeName = in.readString();
    }
}
