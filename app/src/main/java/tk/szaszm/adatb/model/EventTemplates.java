package tk.szaszm.adatb.model;

import moe.banana.jsonapi2.HasOne;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

/**
 * Created by marci on 12/4/17.
 */

@JsonApi(type="EventTemplates")
public class EventTemplates extends Resource {
    public int seqNumber;
    public String type;

    public HasOne<ExerciseCategories> ExerciseCategory;

    public ExerciseCategories getExerciseCategory()
    {
        return ExerciseCategory.get(getContext());
    }
}
