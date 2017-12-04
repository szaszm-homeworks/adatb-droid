package tk.szaszm.adatb.model;

import moe.banana.jsonapi2.HasMany;
import moe.banana.jsonapi2.JsonApi;
import moe.banana.jsonapi2.Resource;

/**
 * Created by marci on 12/3/17.
 */

@JsonApi(type="Users")
public class Users extends Resource {
    public String displayName;
    public String loginName;
    public String email;
    public String neptun;
    public HasMany<StudentRegistrations> StudentRegistrations;
}
