package com.ssafy.api.service;

import com.ssafy.api.request.UserUpdateNicknamePutReq;
import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.api.request.UserUpdatePasswordPostReq;
import com.ssafy.db.entity.Game;
import com.ssafy.db.entity.GameCategory;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.WinRate;
import org.springframework.http.ResponseEntity;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface UserService {
	User createUser(UserRegisterPostReq userRegisterInfo);
	User getUserByUserId(String userId);
	User getUserById(Long id);
	void removeUser(String userId); //user 삭제

	void modifyUser(UserUpdateNicknamePutReq userDTO);

	default User dtoToEntity(UserUpdateNicknamePutReq userDTO) {
		User user = User.builder()
				.userId(userDTO.getUserId())
				.nickname(userDTO.getNickname())
				.exp(userDTO.getExp())
				.build();
		return user;
	}

	default UserUpdateNicknamePutReq EntityToUser(User user) {

		UserUpdateNicknamePutReq userDTO = UserUpdateNicknamePutReq.builder()
				.userId(user.getUserId())
				.id(user.getId())
				.nickname(user.getNickname())
				.exp(user.getExp())
				.build();

		return userDTO;
	}

	ResponseEntity updateUserPassword(String id, UserUpdatePasswordPostReq userReq);
	WinRate getWinRateByUserAndGameCategory(User user, GameCategory gameCategory);
	WinRate saveWinRate(WinRate winRate);
}
