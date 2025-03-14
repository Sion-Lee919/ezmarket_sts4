package ezmarket;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class OrderProductTypeHandler extends BaseTypeHandler<List<OrderProductDTO>> {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Gson gson = new Gson();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<OrderProductDTO> parameter, JdbcType jdbcType)
            throws SQLException {
        try {
            String json = gson.toJson(parameter);
            System.out.println("주문 상품 정보 저장: " + json);
            ps.setString(i, json);
        } catch (Exception e) {
            System.err.println("주문 상품 정보 변환 실패: " + e.getMessage());
            e.printStackTrace();
            ps.setString(i, "[]");
        }
    }

    @Override
    public List<OrderProductDTO> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return convertToList(rs.getString(columnName));
    }

    @Override
    public List<OrderProductDTO> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convertToList(rs.getString(columnIndex));
    }

    @Override
    public List<OrderProductDTO> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convertToList(cs.getString(columnIndex));
    }
    
    private List<OrderProductDTO> convertToList(String s) {
        if (!StringUtils.hasLength(s)) {
            System.out.println("주문 상품 정보가 비어있습니다.");
            return Collections.emptyList();
        }
        
        try {
            System.out.println("주문 상품 정보 파싱 시도: " + s);
            try {
                List<OrderProductDTO> result = gson.fromJson(s, new TypeReference<List<OrderProductDTO>>(){}.getType());
                System.out.println("Gson 파싱 성공: " + result.size() + "개 상품");
                return result;
            } catch (Exception e) {
                System.out.println("Gson 파싱 실패, ObjectMapper 시도: " + e.getMessage());
                List<OrderProductDTO> result = objectMapper.readValue(s, new TypeReference<List<OrderProductDTO>>(){});
                System.out.println("ObjectMapper 파싱 성공: " + result.size() + "개 상품");
                return result;
            }
        } catch (Exception e) {
            System.err.println("주문 상품 정보 파싱 실패: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}