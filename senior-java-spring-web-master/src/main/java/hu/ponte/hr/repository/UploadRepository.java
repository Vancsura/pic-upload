package hu.ponte.hr.repository;

import hu.ponte.hr.domain.FileRegistry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadRepository extends JpaRepository<FileRegistry, Long> {
}
