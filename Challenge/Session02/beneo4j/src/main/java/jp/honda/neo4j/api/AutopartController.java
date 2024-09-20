package jp.honda.neo4j.api;

import jp.honda.neo4j.autoparts.AutopartResponseDto;
import jp.honda.neo4j.autoparts.AutopartService;
import jp.honda.neo4j.mail.MailData;
import jp.honda.neo4j.mail.SendMailService;
import jp.honda.neo4j.persons.Person;
import jp.honda.neo4j.excel.ReadExcelService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
class AutopartController {

    private final AutopartService autopartService;
    private final ReadExcelService readExcelService;
    private final SendMailService sendMailService;

    AutopartController(AutopartService autopartService, ReadExcelService readExcelService, SendMailService sendMailService) {
        this.autopartService = autopartService;
        this.readExcelService = readExcelService;
        this.sendMailService = sendMailService;
    }

    @GetMapping("/search")
    AutopartResponseDto search(@RequestParam("name") String name, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        return autopartService.searchPartsByName(stripWildcards(name), page, pageSize);
    }

    @GetMapping("/search-related-part")
    AutopartResponseDto searchRelatedPart(@RequestParam("name") String name, @RequestParam("relation") String relation) {
        return autopartService.searchRelatedPartsByName(stripWildcards(name), stripWildcards(relation));
    }

    @GetMapping("/read-excel")
    void searchRelatedPart() throws IOException {
        readExcelService.handleExcelFile();
    }

    @PostMapping("/send-mail")
    void sendMail(List<MailData> mailDataList) {
        mailDataList.stream().forEach(mailData -> {
            sendMailService.handleSendMail(mailData.getEmail(), mailData.getPartName());
        });
    }

    @GetMapping("/graph")
    public Map<String, List<Object>> getGraph() {
        return autopartService.fetchPartGraph();
    }

    //** helper **/
    private static String stripWildcards(String title) {
        String result = title;
        if (result.startsWith("*")) {
            result = result.substring(1);
        }
        if (result.endsWith("*")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
