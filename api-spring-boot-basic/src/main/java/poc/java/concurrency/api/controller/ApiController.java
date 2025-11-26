package poc.java.concurrency.api.controller;

import org.owasp.encoder.Encode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poc.java.concurrency.api.service.ProcessService;

@RestController
@RequestMapping("/")
public class ApiController {

  private final ProcessService processService;

  public ApiController(ProcessService processService) {
    this.processService = processService;
  }

  @GetMapping("lookup/{value}")
  public String lookupValue(@PathVariable String value) {
    var inputEncoded = Encode.forUriComponent(value);
    return processService.processValue(inputEncoded);
  }
}
