package kioskapp.service.mail;

import kioskapp.domain.history.mail.MailSendHistory;
import kioskapp.respository.history.mail.MailSendHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MailService {

    private final MailSendHistoryRepository mailSendHistoryRepository;

    @Transactional
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
