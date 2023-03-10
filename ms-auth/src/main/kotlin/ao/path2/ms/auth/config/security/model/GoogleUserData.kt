package ao.path2.ms.auth.config.security.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls

@JsonInclude(JsonInclude.Include.NON_NULL)
class GoogleUserData {
  @JsonSetter("given_name", nulls = Nulls.SKIP)
  var firstName: String? = ""

  @JsonSetter("family_name", nulls = Nulls.SKIP)
  var lastName: String? = ""

  @JsonSetter("name", nulls = Nulls.SKIP)
  var name: String? = ""

  @JsonSetter("picture", nulls = Nulls.SKIP)
  var profilePicture: String? = ""

  @JsonSetter("email", nulls = Nulls.SKIP)
  var email: String? = ""

  @JsonSetter("id", nulls = Nulls.SKIP)
  var id: String? = ""
  override fun toString(): String =
    "SocialUserData(firstName=$firstName, lastName=$lastName, name=$name, profilePicture=$profilePicture, email=$email, id=$id)"

}