package com.ssafy.api.service;
import com.ssafy.api.response.ConferenceMapping;
import com.ssafy.db.entity.ConferenceHistory;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserConference;
import com.ssafy.db.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ssafy.db.entity.Conference;
import com.ssafy.db.repository.ConferenceRepository;
import com.ssafy.api.request.ConferenceRegisterPostReq;
import com.ssafy.db.entity.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Service;
import javax.persistence.EntityListeners;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 방 관련 비즈니스 로직 처리를 위한 서비스 구현 정의
 */

@Service("conferenceService")
@RequiredArgsConstructor
public class ConferenceServiceImpl implements ConferenceService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRepositorySupport userRepositorySupport;

    @Autowired
    ConferenceRepository conferenceRepository;

    @Autowired
    ConferenceHistoryRepository conferenceHistoryRepository;

    @Autowired
    UserConferenceRepository userConferenceRepository;

    @Autowired
    GameCategoryRepository gameCategoryRepository;

    @Override
    public Optional<Conference> getConferenceById(Long id) {
        return conferenceRepository.findById(id);
    }

    @Override
    public Optional<List<ConferenceMapping>> getConferenceByActiveTrue() { return conferenceRepository.findByActiveTrue(); }

    @Override
    public ConferenceHistory exitConference(User user, Long conferenceId){
        // 방 정보(user_conference) 삭제 (userId, RoomId를 통한 삭제)
        Optional<UserConference> conference = userConferenceRepository.findByUserId(user.getId());
        userConferenceRepository.delete(conference.get());
        //컨퍼런스 테이블에 남겨두기 , create(0), join(1), exit(2)
        ConferenceHistory conferenceHistory = ConferenceHistory.builder()
                                                .conference(conference.get().getConference())
                                                .action(2)
                                                .user(user)
                                                .build();
        return conferenceHistoryRepository.save(conferenceHistory);
    }

    @Override
    public int register(ConferenceRegisterPostReq dto) {
        User user = userRepository.findByUserId(dto.getUserid()).get();
        GameCategory gameCategory = gameCategoryRepository.findById(dto.getGamecategory()).get();
        Conference conference = Conference.builder()
                .owner(user)
                .gameCategory(gameCategory)
                .title(dto.getTitle())
                .password(dto.getPassword())
                .maxUser(dto.getMaxUser())
                .build();
        Conference result = conferenceRepository.save(conference);

        UserConference userConference = UserConference.builder()
                .conference(result)
                .user(user)
                .build();
        userConferenceRepository.save(userConference);

        ConferenceHistory conferenceHistory =ConferenceHistory.builder()
                .conference(result)
                .user(user)
                .action(0)
                .build();
        conferenceHistoryRepository.save(conferenceHistory);

        return result.getMaxUser();


    }
}


  
