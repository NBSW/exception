package com.tuniu.common.exception.resolver;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.tuniu.common.exception.UnifiedException;
import com.tuniu.operation.platform.tsg.base.core.utils.JsonUtil;

/**
 * 
 * <Description>通用异常返回值处理类<br> 
 *  
 * @author wanglei<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2015年4月20日 <br>
 */
public class UnifiedExceptionResolver extends SimpleMappingExceptionResolver {

    @Override
    public ModelAndView doResolveException(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex) {
        ResponseVo responseVo = new ResponseVo();
        responseVo.setSuccess(false);
        UnifiedException unifiedException = null;
        if (ex instanceof UnifiedException) {
            unifiedException = (UnifiedException) ex;
            responseVo.setErrorCode(Integer.parseInt(unifiedException.getErrorCode()));
            responseVo.setMsg(unifiedException.getDescription());
        } else {
            responseVo.setErrorCode(500);
            responseVo.setMsg("unknow exception!");
        }
        write(response, responseVo);
        return null;
    }

    /**
     * 
     * Description:输出数据<br> 
     *  
     * @author wanglei<br>
     * @taskId <br>
     * @param response
     * @param result <br>
     */
    protected void write(HttpServletResponse response, ResponseVo result) {
        PrintWriter out = null;
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("success", result.isSuccess());
            map.put("msg", result.getMsg());
            map.put("errorCode", result.getErrorCode());

            String resStr = Base64.encodeBase64String(JsonUtil.toString(map).getBytes("utf-8"));         
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Content-Type","application/json;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Methods", "*");
            out = response.getWriter();
            out.write(resStr);
        } catch (UnsupportedEncodingException e) {
            
        } catch (IOException e) {
            
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }
}
