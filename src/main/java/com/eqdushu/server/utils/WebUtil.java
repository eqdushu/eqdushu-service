package com.eqdushu.server.utils;

import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebUtil {


    private static final Logger LOG = LoggerFactory
            .getLogger(WebUtil.class);


    public static void outputJsonString(String json, HttpServletRequest request,
                                        HttpServletResponse response) {
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setHeader("Pragma", "no-cache");
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            IOUtils.write(json, out, "UTF-8");
            out.flush();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 获取客户端IP
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        LOG.info("getIpAddr::x-forwarded-for::" + ip);
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            LOG.info("getIpAddr::Proxy-Client-IP::" + ip);
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            LOG.info("getIpAddr::WL-Proxy-Client-IP::" + ip);
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            LOG.info("getIpAddr::HTTP_CLIENT_IP::" + ip);
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            LOG.info("getIpAddr::HTTP_X_FORWARDED_FOR::" + ip);
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            LOG.info("getIpAddr::remoteAddr::" + ip);
        }
        if (StringUtils.isNotBlank(ip)) {
            String[] arr = ip.split("\\,");
            ip = arr.length > 0 ? arr[0] : "";
        }
        return ip;
    }

}
