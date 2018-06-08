package casati.guido.mybakingapp.Utils;

import java.util.List;

import casati.guido.mybakingapp.Data.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;


/**
 * Created by guido on 26/05/2018.
 */

public interface RecipeApiInterface {

    @GET("baking.json")
    Call<List<Recipe>> doGetListRecipes();
}

