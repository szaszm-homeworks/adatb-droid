package tk.szaszm.adatb.model;

import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

/**
 * Created by marci on 12/4/17.
 */

@JsonApi(type="ExerciseCategories")
public class ExerciseCategories extends Resource {
    public String type;
}
