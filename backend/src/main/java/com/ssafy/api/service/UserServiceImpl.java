package com.ssafy.api.service;

import com.ssafy.api.request.UserUpdateNicknamePutReq;
import com.ssafy.api.request.UserUpdatePasswordPostReq;
import com.ssafy.api.response.UserLoginPostRes;
import com.ssafy.common.util.JwtTokenUtil;
import com.ssafy.db.entity.Game;
import com.ssafy.db.entity.GameCategory;
import com.ssafy.db.entity.WinRate;
import com.ssafy.db.repository.WinRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.db.entity.User;
import com.ssafy.db.repository.UserRepository;
import com.ssafy.db.repository.UserRepositorySupport;

import java.util.Optional;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */
@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserRepositorySupport userRepositorySupport;

	@Autowired
	WinRateRepository winRateRepository;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public User createUser(UserRegisterPostReq userRegisterInfo) {
		User user = new User();
		user.setUserId(userRegisterInfo.getId());
		// 보안을 위해서 유저 패스워드 암호화 하여 디비에 저장.
		user.setPassword(passwordEncoder.encode(userRegisterInfo.getPassword()));
		user.setNickname(userRegisterInfo.getNickname());
		return userRepository.save(user);
	}

	@Override
	public User getUserByUserId(String userId) {
		// 디비에 유저 정보 조회 (userId 를 통한 조회).
		User user = userRepositorySupport.findUserByUserId(userId).get();
		return user;
	}

	@Override
	public User getUserById(Long id) {
		return userRepository.findById(id).get();
	}

	@Override
	public void removeUser(String userId) {
		// 유저 정보 삭제 (userId를 통한 삭제)
		Optional<User> user = userRepository.findByUserId(userId);
		userRepository.delete(user.get());

	}
//
	@Override
	public void modifyUser(UserUpdateNicknamePutReq userDTO) {
		/* 유저 정보 수정
		 * 파라미터로 들어온 userDTO에서 userId를 뽑음
		 * userId를 기준으로 유저찾음
		 * 유저 닉네임 변경 후 저장 */
		String userId = userDTO.getUserId();

		Optional<User> result = userRepository.findByUserId(userId);

		if (result.isPresent()) {
			User user = result.get();
			user.setNickname(userDTO.getNickname());
			userRepository.save(user);
		}
	}
	//비밀번호 수정
	@Override
	public ResponseEntity updateUserPassword(String id, UserUpdatePasswordPostReq userReq){
		User user = userRepositorySupport.findUserByUserId(id).get();
		// 로그인 요청한 유저로부터 입력된 패스워드 와 디비에 저장된 유저의 암호화된 패스워드가 같은지 확인.(유효한 패스워드인지 여부 확인)
		// matches - 암호화되지 않은 비밀번호(raw-)와 암호화된 비밀번호(encoded-)가 일치하는지 비교
		if(passwordEncoder.matches(userReq.getPassword(), user.getPassword())) {
			//보안을 위해서 수정한 유저 패스워드 암호화 하여 디비에 저장.
			user.setPassword(passwordEncoder.encode(userReq.getChangePassword()));
			userRepository.save(user);
			return ResponseEntity.ok(UserLoginPostRes.of(200, "Success", JwtTokenUtil.getToken(id)));
		}
		// 유효하지 않는 패스워드인 경우, 인증 실패로 응답.
		return ResponseEntity.status(401).body(UserLoginPostRes.of(401, "Invalid Password", JwtTokenUtil.getToken(id)));
	}

	@Override
	public WinRate getWinRateByUserAndGameCategory(User user, GameCategory gameCategory) {
		return winRateRepository.findWinRateByUserAndGameCategory(user, gameCategory);
	}

	@Override
	public WinRate saveWinRate(WinRate winRate) {
		return winRateRepository.save(winRate);
	}
}
