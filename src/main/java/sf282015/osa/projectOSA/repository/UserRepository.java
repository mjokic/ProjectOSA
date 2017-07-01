package sf282015.osa.projectOSA.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import sf282015.osa.projectOSA.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	public User findByEmail(String email);

	@Transactional
	@Modifying
	@Query("update User set device_token=?1 where email=?2")
	public void updateUserDeviceToken(String token, String email);
	
}
