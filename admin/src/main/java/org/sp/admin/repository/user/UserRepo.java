package org.sp.admin.repository.user;

import org.sp.admin.enums.StatusEnum;
import org.sp.admin.model.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * (MUser)
 *
 * @author Tobin
 * @since 2024-11-10 10:05:25
 */
@Repository
public interface UserRepo extends JpaRepository<UserModel, Long>, JpaSpecificationExecutor<UserModel> {



    UserModel findByUsername(String username);

}
