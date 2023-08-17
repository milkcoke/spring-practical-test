package kioskapp.service.mail;

import kioskapp.domain.history.mail.MailSendHistory;
import kioskapp.respository.history.mail.MailSendHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {
  @Spy
  private MailSendHistoryRepository mailSendHistoryRepository;

  @InjectMocks
  private MailService mailService;

  @DisplayName("메일 전송시 전송 내역을 저장한다.")
  @Test
  void sendMail() {
    // given
    // when
    mailService.sendMail("", "", "", "");

    // then
    // Mock 객체를 감싸며 1회 불림, save 는 인자로 'any'일 때
    verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
  }
}