package DashBoard;

import Dto.OpcEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpcRepository extends JpaRepository<OpcEntity,Long> {


}
