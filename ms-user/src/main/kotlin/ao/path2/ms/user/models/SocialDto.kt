package ao.path2.ms.user.models

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

data class SocialDto(
  @NotEmpty
  @NotBlank
  val token: String,
  val type: UserSource
)