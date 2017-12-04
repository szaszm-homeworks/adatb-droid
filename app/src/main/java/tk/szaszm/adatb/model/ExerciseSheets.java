package tk.szaszm.adatb.model;

import moe.banana.jsonapi2.HasOne;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

/**
 * Created by marci on 12/4/17.
 */

@JsonApi(type="ExerciseSheets")
public class ExerciseSheets extends Resource {
    public HasOne<ExerciseCategories> ExerciseCategory;
    public HasOne<ExerciseTypes> ExerciseType;
}
