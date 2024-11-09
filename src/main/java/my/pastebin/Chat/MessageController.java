package my.pastebin.Chat;


import lombok.RequiredArgsConstructor;
import my.pastebin.Chat.MessageService;
import my.pastebin.Chat.dto.NewMessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/message")
public class MessageController {
    private final MessageService messageService;
    @PostMapping
    public ResponseEntity<?> createMessage(@RequestBody NewMessageDTO newMessageDTO){
        messageService.createMessage(newMessageDTO);
        return ResponseEntity.ok("Message was send successfully");
    }
    @GetMapping("/{messageId}")
    public ResponseEntity<?> getById(@PathVariable Long messageId){
        return ResponseEntity.ok(messageService.getById(messageId));
    }
    @GetMapping("/{senderId}")
    public ResponseEntity<?> getBySenderId(@PathVariable Long senderId){
        return ResponseEntity.ok(messageService.getAllBySenderId(senderId));
    }

    @GetMapping("/{receiverId}")
    public ResponseEntity<?> getByReceiverId(@PathVariable Long receiverId){
        return ResponseEntity.ok(messageService.getAllByReceiverId(receiverId));
    }
}
