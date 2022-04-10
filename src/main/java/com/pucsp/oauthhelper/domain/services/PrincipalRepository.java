package com.pucsp.oauthhelper.domain.services;

import com.pucsp.oauthhelper.domain.entities.Principal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrincipalRepository extends CrudRepository<Principal, String> {

}
