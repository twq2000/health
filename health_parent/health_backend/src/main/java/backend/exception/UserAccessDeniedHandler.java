package backend.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/** 权限不足的异常处理器 */
@Component
public class UserAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      AccessDeniedException e)
      throws IOException {
    httpServletResponse.setCharacterEncoding("utf-8");
    httpServletResponse.setContentType("application/json");
    PrintWriter writer = httpServletResponse.getWriter();
    writer.println("{\"statusCode\": 403, \"message\": \"无操作权限\"}");
    writer.flush();
  }
}
