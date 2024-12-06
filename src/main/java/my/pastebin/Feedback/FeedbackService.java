package my.pastebin.Feedback;

import lombok.RequiredArgsConstructor;
import my.pastebin.Exceptions.NotAuthorizedException;
import my.pastebin.Exceptions.ResourceNotFoundException;
import my.pastebin.Feedback.dto.FeedbackInfoDTO;
import my.pastebin.Feedback.dto.FeedbackInfoDTOMapper;
import my.pastebin.Feedback.dto.NewFeedbackDTO;
import my.pastebin.User.Role;
import my.pastebin.User.User;
import my.pastebin.User.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    private final FeedbackRepo feedbackRepo;
    private final UserService userService;
    private final FeedbackInfoDTOMapper feedbackInfoDTOMapper;

    public FeedbackService(FeedbackRepo feedbackRepo, @Lazy UserService userService, FeedbackInfoDTOMapper feedbackInfoDTOMapper) {
        this.feedbackRepo = feedbackRepo;
        this.userService = userService;
        this.feedbackInfoDTOMapper = feedbackInfoDTOMapper;
    }

    public Feedback createFeedback(NewFeedbackDTO newFeedbackDTO){
        Feedback feedback = Feedback.builder()
                .text(newFeedbackDTO.text())
                .rating(newFeedbackDTO.rating())
                .creator(userService.getCurrentUser())
                .user(userService.getUserById(newFeedbackDTO.userToRateId()))
                .createdAt(LocalDateTime.now())
                .build();
        return feedbackRepo.save(feedback);
    }

    public void deleteFeedback(Long id)throws AccessDeniedException {
        Feedback feedback = feedbackRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Feedback not found"));
        if(!feedback.getCreator().getId().equals(userService.getCurrentUser().getId()) && !userService.getCurrentUser().getRole().equals(Role.ADMIN))
            throw new AccessDeniedException("You are not allowed to delete this feedback");
        feedbackRepo.deleteById(id);
    }

    public void changeFeedback(Long feedback_id, NewFeedbackDTO newFeedbackDTO){
        Feedback feedback = feedbackRepo.findById(feedback_id).orElseThrow(() -> new ResourceNotFoundException("Feedback not found"));
        if(!feedback.getCreator().getId().equals(userService.getCurrentUser().getId()) && !userService.getCurrentUser().getRole().equals(Role.ADMIN))
            throw new AccessDeniedException("You are not allowed to change this feedback");

        feedback.setText(newFeedbackDTO.text());
        feedback.setRating(newFeedbackDTO.rating());
        userService.calculatePointsAsCreator(feedback);
        feedbackRepo.save(feedback);
    }

    public Feedback getById(Long id) {
        return feedbackRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Feedback not found"));
    }

    public FeedbackInfoDTO getInfoById(Long id) {
        return feedbackInfoDTOMapper.apply(feedbackRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Feedback not found")));
    }

    public List<FeedbackInfoDTO> getAll() {
        return feedbackRepo.findAll()
                .stream()
                .map(feedbackInfoDTOMapper)
                .collect(Collectors.toList());
    }

    public List<FeedbackInfoDTO> getAllByCurrentUserWrite() {
        return feedbackRepo.findAllByCreator(userService.getCurrentUser())
                .stream()
                .map(feedbackInfoDTOMapper)
                .collect(Collectors.toList());
    }

    public List<FeedbackInfoDTO> getAllByCurrentUserReceive() {
        return feedbackRepo.findAllByUserId(userService.getCurrentUser().getId())
                .stream()
                .map(feedbackInfoDTOMapper)
                .collect(Collectors.toList());
    }

    public Integer getQuantityOfFeedbacks(User user) {
        return feedbackRepo.findAllByUserId(user.getId()).size();
    }
}
