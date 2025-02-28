package Teahwa.Server.auth.repository;

import Teahwa.Server.auth.entity.BlackList;
import org.springframework.data.repository.CrudRepository;

public interface BlackListRepository extends CrudRepository<BlackList, Long> {
}
