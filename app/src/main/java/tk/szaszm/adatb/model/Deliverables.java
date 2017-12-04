package tk.szaszm.adatb.model;

import moe.banana.jsonapi2.HasOne;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

/**
 * Created by marci on 12/4/17.
 */

@JsonApi(type="Deliverables")
public class Deliverables extends Resource {
    public boolean finalized;
    public int grade;
    public boolean grading;
    public int imsc;
    public String comment;

    public HasOne<DeliverableTemplates> DeliverableTemplate;
}
