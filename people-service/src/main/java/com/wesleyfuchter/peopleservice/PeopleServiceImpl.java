package com.wesleyfuchter.peopleservice;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@RequestScoped
public class PeopleServiceImpl implements PeopleService {

    private final PeopleRepository peopleRepository;

    @Inject
    public PeopleServiceImpl(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public People save(People people) {
        return peopleRepository.save(people);
    }

    @Override
    public People update(String id, People people) {
        return peopleRepository.update(id, people);
    }

    @Override
    public List<People> findAll() {
        return peopleRepository.findAll();
    }

    @Override
    public Optional<People> findById(String id) {
        return peopleRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        peopleRepository.delete(id);
    }
}
