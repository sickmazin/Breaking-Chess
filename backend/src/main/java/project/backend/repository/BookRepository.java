package project.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.backend.data.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

}
