package com.wesleyfuchter.peopleservice;

import java.util.List;
import java.util.Optional;

public interface PeopleRepository {

    People save(People people);

    People update(String id, People people);

    List<People> findAll();

    Optional<People> findById(String id);

    void delete(String id);

}
