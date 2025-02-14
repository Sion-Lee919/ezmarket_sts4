package google;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface KakaoUserMapper {
    int countByKakaoId(@Param("kakaoId") String kakaoId);
    int saveKakaoUser(@Param("kakaoId") String kakaoId, @Param("email") String email,
                  @Param("name") String name, @Param("picture") String profile_image);
    int updateKakaoUser(@Param("kakaoId") String kakaoId, @Param("email") String email,
                    @Param("name") String name, @Param("picture") String profile_image);
}