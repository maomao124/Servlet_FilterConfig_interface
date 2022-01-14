package mao.servlet_filterconfig_interface;

import javax.servlet.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 返回值类型	方法	描述
 * String	getInitParameter(String name)	根据初始化参数名 name，返回对应的初始化参数值。
 * Enumeration	getInitParameterNames()	返回过滤器的所有初始化参数名的枚举集合。
 * ServletContext	getServletContext()	返回 Servlet 上下文对象的引用。
 * String	getFilterName() 	返回过滤器的名称。
 */
@WebFilter(filterName = "BlackListFilter", value = "/login")
public class BlackListFilter implements Filter
{
    private FilterConfig fConfig;

    public void init(FilterConfig config) throws ServletException
    {
        this.fConfig = config;
    }

    public void destroy()
    {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        Boolean successde = true;
        //获取前台登录的账号信息
        String name = request.getParameter("username");
        //获取过滤器中的初始化参数
        Enumeration<String> blackListNames = fConfig.getInitParameterNames();
        //判断前台登录账号是否为空
        if (name == null || "".equals(name))
        {
            response.getWriter().write("用户名不能为空");
        }
        else
        {
            //登录账号不为空，循环遍历黑名单
            while (blackListNames.hasMoreElements())
            {
                //若登录账号是黑名单账号则不允许登录
                if (fConfig.getInitParameter(blackListNames.nextElement()).equals(name))
                {
                    successde = false;
                }
            }
            if (successde)
            {
                chain.doFilter(request, response);
            }
            else
            {
                response.getWriter().write("<h1 align=\"center\" style=\"font-family:arial;color:red;\">温馨提示：您的存在风险，暂时不能为您提供服务 </h1>\n");
            }
        }
    }
}
