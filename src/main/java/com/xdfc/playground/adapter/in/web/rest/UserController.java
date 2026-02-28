package com.xdfc.playground.adapter.in.web.rest;

import com.xdfc.playground.adapter.in.web.rest.exception.NotFoundHttpException;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import com.xdfc.playground.adapter.out.web.rest.dto.ListingUserDTO;
import com.xdfc.playground.domain.delegate.UserRequirementsDelegate;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/users")
final public class UserController {
    @Autowired
    private UserRequirementsDelegate users;

    @GetMapping()
    public Page<ListingUserDTO> index(Pageable pageable) {
        return this.users.getService().all(pageable).map(
            (UserEntity user) -> this.users.getListingMapper().toDto(user)
        );
    }

    @GetMapping("/{id}")
    public ListingUserDTO find(@PathVariable @UUID String id) {
        UserEntity user = this.users.getService().find(id).orElseThrow(
                NotFoundHttpException::new
        );

        return this.users.getListingMapper().toDto(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable @UUID String id) {
        this.users.getService().delete(id);
    }
}
