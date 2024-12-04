package my.pastebin.S3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    public String upload(String Content){
        String key = UUID.randomUUID().toString();
        s3Client.putObject(request -> request.bucket(bucketName).key(key), RequestBody.fromString(Content));
        return key;
    }

    public String download(String key) throws IOException {
        byte[] bytes = s3Client.getObject(request -> request.bucket(bucketName).key(key)).readAllBytes();
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public void delete(String key){
        s3Client.deleteObject(request -> request.bucket(bucketName).key(key));
    }

}
