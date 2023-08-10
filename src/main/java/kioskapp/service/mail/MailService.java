package kioskapp.service.mail;

import kioskapp.domain.history.mail.MailSendHistory;
import kioskapp.respository.history.mail.MailSendHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final MailSendHistoryRepository mailSendHistoryRepository;

    public void sendMail(String from, String to, String subject, String content) {
        log.info("Mail send");
        mailSendHistoryRepository.save(MailSendHistory.builder()
            .fromEmail(from)
            .toEmail(to)
            .subject(subject)
            .content(content)
            .build()
        );
    }
}
