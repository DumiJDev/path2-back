package ao.path2.app.adapters.inbound.controller

import ao.path2.app.adapters.inbound.dto.request.UserDTO
import ao.path2.app.core.domain.PageQuery
import ao.path2.app.core.domain.User
import ao.path2.app.core.service.UserService
import ao.path2.app.utils.mapping.Mapper
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.validation.Valid

@RequestMapping("/api/v1/users")
@RestController
@CrossOrigin("*")
class UserController(private val service: UserService, private val mapper: Mapper) {
  @GetMapping
  fun getAll(@PageableDefault(size = 15, page = 0) page: Pageable): ResponseEntity<PageImpl<User>> {
    val pageQuery: PageQuery = PageQuery(page.pageSize, page.pageNumber)

    val pageResponse = PageImpl(service.listAll(pageQuery), page, page.pageSize.toLong())

    return ResponseEntity.ok(pageResponse)
  }

  @PostMapping
  fun save(@RequestBody @Valid user: UserDTO): ResponseEntity<Any> {

    if (user.image == null)
      user.image = ""

    val userSaved = service.save(mapper.map(user, User()) as User)

    val res = mapper.map(userSaved, ao.path2.app.adapters.inbound.dto.response.UserDTO())

    return ResponseEntity.created(URI.create("/api/v1/users")).body(res)
  }

  @PatchMapping("/{username}")
  fun update(@RequestBody @Valid user: User, @PathVariable("username") username: String): ResponseEntity<out Any> {
    if (username != user.username)
      return ResponseEntity.badRequest().body("Path username $username and user username ${user.username} is not equal")
    return ResponseEntity.ok(service.update(user))
  }

  @GetMapping("/{username}")
  fun getUser(@PathVariable username: String) = ResponseEntity.ok(
    mapper.map(
      service.findByUsername(username),
      ao.path2.app.adapters.inbound.dto.response.UserDTO()
    )
  )
}