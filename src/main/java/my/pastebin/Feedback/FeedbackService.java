package my.pastebin.Feedback;

import lombok.RequiredArgsConstructor;
import my.pastebin.Event.Event;
import my.pastebin.Event.EventService;
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
    private final EventService eventService;

    public FeedbackService(FeedbackRepo feedbackRepo, @Lazy UserService userService, FeedbackInfoDTOMapper feedbackInfoDTOMapper, EventService eventService) {
        this.feedbackRepo = feedbackRepo;
        this.userService = userService;
        this.feedbackInfoDTOMapper = feedbackInfoDTOMapper;
        this.eventService = eventService;
    }

    public Feedback createFeedback(NewFeedbackDTO newFeedbackDTO){
        Feedback feedback = Feedback.builder()
                .text(newFeedbackDTO.text())
                .rating(newFeedbackDTO.rating())
                .event(eventService.getById(newFeedbackDTO.eventId()))
                .creator(userService.getCurrentUser())
                .createdAt(LocalDateTime.now())
                .build();
        return feedbackRepo.save(feedback);
    }

    public void deleteFeedback(Long id)throws AccessDeniedException {
        Feedback feedback = feedbackRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Feedback not found"));
        if(!feedback.getEvent().getCreator().getId().equals(userService.getCurrentUser().getId()) && !userService.getCurrentUser().getRole().equals(Role.ADMIN))
            throw new AccessDeniedException("You are not allowed to delete this feedback");
        feedbackRepo.deleteById(id);
    }

    public void changeFeedback(Long feedback_id, NewFeedbackDTO newFeedbackDTO){
        Feedback feedback = feedbackRepo.findById(feedback_id).orElseThrow(() -> new ResourceNotFoundException("Feedback not found"));
        if(!feedback.getEvent().getCreator().getId().equals(userService.getCurrentUser().getId()) && !userService.getCurrentUser().getRole().equals(Role.ADMIN))
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

    public List<FeedbackInfoDTO> getInfoByCurrentUserAndEventId(Long eventId) {
        return feedbackRepo.findAllByCreatorIdAndEventId(userService.getCurrentUser().getId(), eventId)
                .stream()
                .map(feedbackInfoDTOMapper)
                .collect(Collectors.toList());
    }

    public List<FeedbackInfoDTO> getAll() {
        return feedbackRepo.findAll()
                .stream()
                .map(feedbackInfoDTOMapper)
                .collect(Collectors.toList());
    }

    public List<FeedbackInfoDTO> getAllByEventId(Long eventId) {
        return feedbackRepo.findAllByEventId(eventId)
                .stream()
                .map(feedbackInfoDTOMapper)
                .collect(Collectors.toList());
    }


    public List<FeedbackInfoDTO> getAllByCurrentUserWrite() {
        return feedbackRepo.findAllByCreatorId(userService.getCurrentUser().getId())
                .stream()
                .map(feedbackInfoDTOMapper)
                .collect(Collectors.toList());
    }

    public List<FeedbackInfoDTO> getAllByCurrentUserReceive() {
        List<Event> events = eventService.getAllByCreatorId(userService.getCurrentUser().getId());
        return feedbackRepo.findAllByEventIn(events)
                .stream()
                .map(feedbackInfoDTOMapper)
                .collect(Collectors.toList());
    }

    public Integer getQuantityOfFeedbacks(User user) {
        return feedbackRepo.findAllByCreatorId(user.getId()).size();
    }
}
