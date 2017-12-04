package tk.szaszm.adatb.model;

import moe.banana.jsonapi2.HasMany;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

/**
 * Created by marci on 12/4/17.
 */

@JsonApi(type="StudentRegistrations")
public class StudentRegistrations extends Resource {
    public String neptunCourseCode;
    public String neptunSubjectCode;

    public HasMany<Events> Events;
}
