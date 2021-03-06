package com.ssafy.api.response;

import com.ssafy.db.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.bridge.Message;

import java.util.List;

/**
 * 회원 본인 정보 조회 API ([GET] /users/{id}) 요청에 대한 응답값 정의.
 */
@Getter
@Setter
@ApiModel("UserResponse")
public class UserRes{
//	@ApiModelProperty(name="User ID")
//	String userId;
	@ApiModelProperty(name="User Nickname")
	String nickname;
	@ApiModelProperty(name="User Exp")
	int exp;
	@ApiModelProperty(name="WinRate List")
	List<WinRateMapping> WinRateList;


//	public static UserRes of(User user) {
//		UserRes res = new UserRes();
//		res.setNickname(user.getNickname());
//		res.setExp(user.getExp());
//		return res;
//	}

	
	
}
