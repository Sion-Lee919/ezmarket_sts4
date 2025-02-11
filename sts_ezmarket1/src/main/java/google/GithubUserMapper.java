package google;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GithubUserMapper {
	 int countByGithubId(String githubId);
	    int saveUser(@Param("githubId") String githubId, @Param("email") String email,
	                  @Param("name") String name, @Param("picture") String picture);
	    int updateUser(@Param("githubId") String githubId, @Param("email") String email,
	                    @Param("name") String name, @Param("picture") String picture);
	
}
