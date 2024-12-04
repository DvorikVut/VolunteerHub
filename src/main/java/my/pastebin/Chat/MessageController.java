package my.pastebin.Chat;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import my.pastebin.Chat.MessageService;
import my.pastebin.Chat.dto.NewMessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/message")
@Tag(name = "MessageController", description = "Operations related to messages")
public class MessageController {
    private final MessageService messageService;

    @Operation(summary = "Create a new message")
    @PostMapping
    public ResponseEntity<?> createMessage(@RequestBody NewMessageDTO newMessageDTO){
        messageService.createMessage(newMessageDTO);
        return ResponseEntity.ok("Message was send successfully");
    }

    @Operation(summary = "Get a message by its ID")
    @GetMapping("/{messageId}")
    public ResponseEntity<?> getById(@PathVariable Long messageId){
        return ResponseEntity.ok(messageService.getById(messageId));
    }

    @Operation(summary = "Get all messages sent by a user")
    @GetMapping("/{senderId}")
    public ResponseEntity<?> getBySenderId(@PathVariable Long senderId){
        return ResponseEntity.ok(messageService.getAllBySenderId(senderId));
    }

    @Operation(summary = "Get all messages received by a user")
    @GetMapping("/{receiverId}")
    public ResponseEntity<?> getByReceiverId(@PathVariable Long receiverId){
        return ResponseEntity.ok(messageService.getAllByReceiverId(receiverId));
    }

    @Operation(summary = "Delete a message")
    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long messageId){
        messageService.delete(messageService.getById(messageId));
        return ResponseEntity.ok("Message was deleted successfully");
    }

    @Operation(summary = "Get all messages between two users")
    @GetMapping("/{senderId}/{receiverId}")
    public ResponseEntity<?> getBySenderIdAndReceiverId(@PathVariable Long senderId, @PathVariable Long receiverId){
        return ResponseEntity.ok(messageService.getAllBySenderIdAndReceiverId(senderId, receiverId));
    }
}
