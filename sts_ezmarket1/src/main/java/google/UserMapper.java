package google;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    int countByGoogleId(@Param("googleId") String googleId);
    int saveUser(@Param("googleId") String googleId, @Param("email") String email,
                  @Param("name") String name, @Param("picture") String picture);
    int updateUser(@Param("googleId") String googleId, @Param("email") String email,
                    @Param("name") String name, @Param("picture") String picture);

    
    int countByKakaoId(@Param("kakaoId") String kakaoId);
    int saveKakaoUser(@Param("kakaoId") String kakaoId, @Param("email") String email,
                      @Param("name") String name, @Param("picture") String picture);
    int updateKakaoUser(@Param("kakaoId") String kakaoId, @Param("email") String email,
                        @Param("name") String name, @Param("picture") String picture);
}
