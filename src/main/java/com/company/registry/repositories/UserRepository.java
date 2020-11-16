package com.company.registry.repositories;

import com.company.registry.domain.Occupation;
import com.company.registry.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    //запрос будет делать fullscan, индексов нет на таблице

    @Query("select u from User u where "
            + " (:userId is null or u.id = :userId)"
            + " and (:name is null or u.name = :name)"
            + " and (:surname is null or u.surname = :surname)"
            + " and (:patronymic is null or u.patronymic = :patronymic)"
            + " and (:occupation is null or u.occupation = :occupation)"
            + " and (:age is null or u.age = :age)"
    )
    List<User> findByFilter(
            @Param("userId") UUID userId
            , @Param("name") String name
            , @Param("surname") String surname
            , @Param("patronymic") String patronymic
            , @Param("occupation") Occupation occupation
            , @Param("age") int age
    );
}
