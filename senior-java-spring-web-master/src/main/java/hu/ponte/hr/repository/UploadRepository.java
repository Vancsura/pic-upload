package hu.ponte.hr.repository;

import hu.ponte.hr.domain.FileRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadRepository extends JpaRepository<FileRegistry, Long> {
}
