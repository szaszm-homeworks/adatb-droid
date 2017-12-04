package tk.szaszm.adatb.model;

import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

/**
 * Created by marci on 12/4/17.
 */

@JsonApi(type="Roles")
public class Roles extends Resource {
    public String name;
}
