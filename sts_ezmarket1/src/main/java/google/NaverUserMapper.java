package google;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface NaverUserMapper {
	int getMemberId(String naverId);
    int countByNaverId(@Param("naverId") String naverId);
    int saveUser(@Param("naverId") String naverId, @Param("email") String email,
                  @Param("name") String name, @Param("picture") String profile_image);
    int updateUser(@Param("naverId") String naverId, @Param("email") String email,
                    @Param("name") String name, @Param("picture") String profile_image);
}