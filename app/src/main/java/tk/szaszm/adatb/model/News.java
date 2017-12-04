package tk.szaszm.adatb.model;

import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

/**
 * Created by marci on 12/3/17.
 */

@JsonApi(type = "News")
public class News extends Resource {
    public String title;
    public String published;
    public String until;
    public String text;
    public boolean students;
}
