package ao.path2.ms.email.service

import ao.path2.ms.email.models.EmailModel
import freemarker.template.Configuration
import freemarker.template.TemplateException
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.io.IOException
import java.io.StringWriter
import java.nio.charset.StandardCharsets
import javax.mail.internet.MimeMessage

@Service
class EmailServiceImpl(private val configuration: Configuration, private val sender: JavaMailSender) : EmailService {

  override fun sendSimpleEmail() {

  }

  @Throws(IOException::class, TemplateException::class, MailException::class)
  override fun sendAttachedWithAssetsEmail(emailModel: EmailModel) {
    val mimeMessage: MimeMessage = sender.createMimeMessage()
    val helper = MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name())
    helper.setFrom("path2app@gmail.com", "Path2 App")
    helper.setSubject(emailModel.subject)
    helper.setTo(emailModel.to.toTypedArray())
    helper.setText(getEmailContent(emailModel), true)
    sender.send(mimeMessage)
  }

  @Throws(IOException::class, TemplateException::class)
  fun getEmailContent(emailModel: EmailModel): String {
    val stringWriter = StringWriter()
    configuration.getTemplate("${emailModel.template}.ftlh").process(emailModel, stringWriter)
    return stringWriter.buffer.toString()
  }
}