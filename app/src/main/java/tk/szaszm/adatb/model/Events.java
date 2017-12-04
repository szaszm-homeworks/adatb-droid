package tk.szaszm.adatb.model;

import moe.banana.jsonapi2.HasMany;
import moe.banana.jsonapi2.HasOne;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

/**
 * Created by marci on 12/4/17.
 */

@JsonApi(type="Events")
public class Events extends Resource {
    public boolean finalized;
    public int grade;
    public int imsc;
    public String location;

    public HasOne<Users> Demonstrator;
    public HasOne<EventTemplates> EventTemplate;
    public HasMany<Deliverables> Deliverables;

    public String getLabel()
    {
        EventTemplates eventTemplate = this.EventTemplate.get(this.getContext());
        int seq = eventTemplate.seqNumber;
        String eventTemplateType = eventTemplate.type;

        ExerciseCategories exerciseCategory = eventTemplate.ExerciseCategory.get(getContext());
        String exerciseCategoryType = exerciseCategory.type;

        return Integer.toString(seq) + ". " + exerciseCategoryType.toUpperCase() + " " + eventTemplateType.toUpperCase() + (grade > 0 ? " (" + Integer.toString(grade) + ")" : "");
    }
}
