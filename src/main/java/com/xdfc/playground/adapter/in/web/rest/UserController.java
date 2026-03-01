package com.xdfc.playground.adapter.in.web.rest;

import com.xdfc.playground.adapter.in.web.rest.annotation.IsAdministrator;
import com.xdfc.playground.adapter.in.web.rest.annotation.IsOwnerOrAdministrator;
import com.xdfc.playground.adapter.in.web.rest.exception.NotFoundHttpException;
import com.xdfc.playground.adapter.in.web.rest.route.UserControllerRoutes;
import com.xdfc.playground.adapter.out.persistence.jpa.entity.UserEntity;
import com.xdfc.playground.adapter.in.web.rest.dto.user.ListingUserDTO;
import com.xdfc.playground.domain.delegate.UserRequirementsDelegate;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(UserControllerRoutes.ROOT_ENDPOINT)
public class UserController {
    @Autowired
    private UserRequirementsDelegate users;

    @IsAdministrator
    @GetMapping()
    public Page<ListingUserDTO> index(final Pageable pageable) {
        return this.users.getService().all(pageable).map(
            (UserEntity user) -> this.users.getMapper().toDto(user)
        );
    }

    @IsOwnerOrAdministrator
    @GetMapping(UserControllerRoutes.USER_ID_SEGMENT)
    public ListingUserDTO find(@PathVariable @UUID final String id) {
        final UserEntity user = this.users.getService().find(id)
            .orElseThrow(NotFoundHttpException::new);

        return this.users.getMapper().toDto(user);
    }

    @IsOwnerOrAdministrator
    @DeleteMapping(UserControllerRoutes.USER_ID_SEGMENT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable @UUID final String id) {
        this.users.getService().delete(id);
    }
}
