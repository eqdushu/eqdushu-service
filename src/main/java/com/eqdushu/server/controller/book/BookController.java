package com.eqdushu.server.controller.book;

import com.eqdushu.server.annotation.NonAuthorization;
import com.eqdushu.server.domain.book.Book;
import com.eqdushu.server.domain.book.BookBorrow;
import com.eqdushu.server.domain.book.PageQuery;
import com.eqdushu.server.domain.user.User;
import com.eqdushu.server.exception.BookDealException;
import com.eqdushu.server.service.book.IBookService;
import com.eqdushu.server.service.user.IUserService;
import com.eqdushu.server.utils.AuthUtil;
import com.eqdushu.server.utils.ResponseCode;
import com.eqdushu.server.utils.date.DateTimeUtil;
import com.eqdushu.server.vo.Response;
import com.github.nickvl.xspring.core.log.aop.annotation.LogException;
import com.github.nickvl.xspring.core.log.aop.annotation.LogInfo;
import com.google.common.base.Strings;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by nobita on 17/9/13.
 */
@Controller
@RequestMapping("/book")
@LogInfo
@LogException(value = { @LogException.Exc(value = Exception.class, stacktrace = false) }, warn = { @LogException.Exc({ IllegalArgumentException.class }) })
public class BookController {

    private final static Logger LOG = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private IBookService bookService;

    @Autowired
    private IUserService userService;

    /**
     * 获取用户所在公司的书库信息
     *
     * @param params
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getLibrary", method = RequestMethod.POST)
    public @ResponseBody
    Response getLibrary(
            @RequestBody Map<String, Object> params, HttpServletRequest request) throws Exception {
        Long usrId = AuthUtil.getUserId();
        LOG.info("userid is: " + usrId);
        //获取用户信息
        User user = userService.getById(usrId);
        //查询书库信息
        List<Book> list = bookService.getLibraryByUsr(user.getCompanyId());

        return new Response(ResponseCode.success, list);
    }

    /**
     * 获取书籍对应的详情信息
     *
     * @param params
     * @return
     * @throws Exception
     */
    @NonAuthorization
    @RequestMapping(value = "/getBookDetails", method = RequestMethod.POST)
    public @ResponseBody
    Response getBookDetails (@RequestBody Map<String, Object> params) throws RuntimeException, Exception {
        String isbn = MapUtils.getString(params, "isbn");
        if (Strings.isNullOrEmpty(isbn)) {
            return new Response(ResponseCode.invalid_attribute);
        }
        Book book = bookService.getBookInfoByIsbn(isbn);
        return new Response(ResponseCode.success, book);
    }

    /**
     * 扫码获取其他借阅书籍
     *
     * @param params
     * @return
     * @throws RuntimeException
     * @throws Exception
     */
    @RequestMapping(value = "/borrowBook", method = RequestMethod.POST)
    public @ResponseBody
    Response borrowBook (@RequestBody Map<String, Object> params, HttpServletRequest request) throws RuntimeException, Exception {
        //获取ISBN号
        String isbn = MapUtils.getString(params, "isbn");
        if (Strings.isNullOrEmpty(isbn)) {
            return new Response(ResponseCode.invalid_attribute);
        }
        Book book = bookService.getBookInfoByIsbn(isbn);
        if(null == book){
            return new Response(ResponseCode.book_not_exit);//书籍不存在
        }
        //获取用户信息
        Long userId = AuthUtil.getUserId();

        //获取用户信息
        User user = userService.getById(userId);
        BookBorrow bookBorrow = new BookBorrow();
        bookBorrow.setCompanyId(user.getCompanyId());
        bookBorrow.setIsbn(isbn);
        bookBorrow.setUserId(userId);
        bookBorrow.setBookId(book.getId());
        bookBorrow.setBorrowSts("B");
        Date date =new Date();
        String nowDt = new SimpleDateFormat("yyyyMMdd").format(date);
        String nowTm = new SimpleDateFormat("HHmmss").format(date);
        bookBorrow.setBorrowBegDt(nowDt);
        bookBorrow.setBorrowBegTm(nowTm);
        String endDt = new SimpleDateFormat("yyyyMMdd").format(DateTimeUtil.addDay(date,30));
        bookBorrow.setBorrowEndDt(endDt);
        bookBorrow.setBorrowEndTm(nowTm);
        bookBorrow.setCreateTime(date);
        bookBorrow.setUpdateTime(date);

        Response resp = null;
        try {
            resp = bookService.borrowBookByIsb(bookBorrow);
        } catch (BookDealException e) {
            resp = new Response(e.getCode());
        }

        return resp;
    }

    /**
     * 此处为分页查询 查询企业书籍借阅详情
     *
     * @param params
     * @return
     * @throws RuntimeException
     * @throws Exception
     */
    @RequestMapping(value = "/getCompanyBrowDetail", method = RequestMethod.POST)
    public @ResponseBody
    Response getCompanyBrowDetail
    (@RequestBody Map<String, Object> params, HttpServletRequest request) throws RuntimeException, Exception {

        //获取用户信息
    	Long userId = AuthUtil.getUserId();
        //获取用户信息
        User user = userService.getById(userId);

        int pageNo = (null==MapUtils.getInteger(params, "pageNo"))?1:MapUtils.getInteger(params, "pageNo"); //获取页数
        int pageNum = (null==MapUtils.getInteger(params, "pageNum"))?10:MapUtils.getInteger(params, "pageNum");//每页条数
        //如果传递为空则默认pageno为1  pageNum为10
        if (pageNo == 0) {
            pageNo = 1;
        }
        if (pageNum == 0) {
            pageNum = 10;
        }
        int startNum = (pageNo - 1) * pageNum;
        PageQuery page = new PageQuery();
        page.setId(user.getCompanyId());
        page.setStartNum(startNum);
        page.setPageNum(pageNum);
        List<BookBorrow> list = bookService.queryBorrowInfoByCompanyId(page);
        return new Response(ResponseCode.success, list);
    }

    /**
     * 此处为分页查询 查询个人借阅详情
     *
     * @param params
     * @return
     * @throws RuntimeException
     * @throws Exception
     */
    @RequestMapping(value = "/getReaderBrowDetail", method = RequestMethod.POST)
    public @ResponseBody
    Response getReaderBrowDetail
    (@RequestBody Map<String, Object> params, HttpServletRequest request) throws RuntimeException, Exception {

        //获取用户信息
        //String authentication = request.getHeader(Constants.HEADER_TOKEN);
        //TokenModel model = tokenService.getToken(authentication);
        //long usrId = model.getUserId();
    	Long userId = AuthUtil.getUserId();
        int pageNo = (null==MapUtils.getInteger(params, "pageNo"))?1:MapUtils.getInteger(params, "pageNo"); //获取页数
        int pageNum = (null==MapUtils.getInteger(params, "pageNum"))?10:MapUtils.getInteger(params, "pageNum");//每页条数
        //如果传递为空则默认pageno为1  pageNum为10
        if (pageNo == 0) {
            pageNo = 1;
        }
        if (pageNum == 0) {
            pageNum = 10;
        }
        int startNum = (pageNo - 1) * pageNum;
        PageQuery page = new PageQuery();
        page.setId(userId);
        page.setStartNum(startNum);
        page.setPageNum(pageNum);
        List<BookBorrow> list = bookService.queryBorrowInfoByUsrId(page);
        return new Response(ResponseCode.success, list);
    }
    /**
     * 用户还书
     *
     * @param params
     * @param request
     * @return
     * @throws RuntimeException
     * @throws Exception
     */
    @RequestMapping(value = "/backBook", method = RequestMethod.POST)
    public @ResponseBody
    Response backBook
    (@RequestBody Map<String, Object> params, HttpServletRequest request) throws RuntimeException, Exception {

        //获取ISBN号
        String isbn = MapUtils.getString(params, "isbn");
        if (Strings.isNullOrEmpty(isbn)) {
            return new Response(ResponseCode.invalid_attribute);
        }
        Book book = bookService.getBookInfoByIsbn(isbn);
        //获取用户信息
        Long userId = AuthUtil.getUserId();
        //获取用户信息
        User user = userService.getById(userId);

        BookBorrow bookBorrow = new BookBorrow();
        bookBorrow.setBorrowSts("R");
        bookBorrow.setBookId(book.getId());
        bookBorrow.setUserId(userId);
        bookBorrow.setIsbn(isbn);
        bookBorrow.setCompanyId(user.getCompanyId());
        bookBorrow.setUpdateTime(new Date());
        Response resp = null;
        try {
            resp = bookService.backBook(bookBorrow);
        } catch (BookDealException e) {
            resp = new Response(e.getCode());
        }
        return resp;
    }

    /**
     * 书籍入库
     *
     * @param params
     * @param request
     * @return
     * @throws RuntimeException
     * @throws Exception
     */
    @RequestMapping(value = "/bookInLibrary", method = RequestMethod.POST)
    public @ResponseBody
    Response bookInLibrary
    (@RequestBody Map<String, Object> params, HttpServletRequest request) throws RuntimeException, Exception {

        //获取ISBN号
        String isbn = MapUtils.getString(params, "isbn");
        if (Strings.isNullOrEmpty(isbn)) {
            return new Response(ResponseCode.invalid_attribute);
        }

        //获取用户信息
        Long userId = AuthUtil.getUserId();
        Response resp = null;
        try {
            resp = bookService.addBookToLibrary(isbn, userId);
        } catch (BookDealException e) {
            resp = new Response(e.getCode());
        }
        return resp;
    }

    /**
     * 非管理员申请书籍入库
     * @param params
     * @param request
     * @return
     * @throws RuntimeException
     * @throws Exception
     */
    @RequestMapping(value = "/readerBookInReq", method = RequestMethod.POST)
    public @ResponseBody
    Response bookInLibReq(@RequestBody Map<String, Object> params, HttpServletRequest request) throws RuntimeException, Exception {
    	Long userId = AuthUtil.getUserId();
        //获取ISBN号
        String isbn = MapUtils.getString(params, "isbn");
        if (Strings.isNullOrEmpty(isbn)) {
            return new Response(ResponseCode.invalid_attribute);
        }

        Response resp = bookService.addBookToLibraryReq(isbn,userId);
        return resp;
    }



    /**
     * 查询所有入库申请
     * @param params
     * @param request
     * @return
     * @throws RuntimeException
     * @throws Exception
     */
    @RequestMapping(value="/getAllBookInLibReq",method = RequestMethod.POST)
    public @ResponseBody
    Response getAllBookInLibReq(@RequestBody Map<String, Object> params, HttpServletRequest request) throws RuntimeException, Exception {
    	Long userId = AuthUtil.getUserId();
        Response resp = bookService.getAllBookInLibReq(userId);
        return resp;
    }

    /**
     * 管理员审批入库
     * @param params
     * @param request
     * @return
     * @throws RuntimeException
     * @throws Exception
     */
    @RequestMapping(value="/approveBookInLib",method = RequestMethod.POST)
    public @ResponseBody
    Response approveBookInLib(@RequestBody Map<String, Object> params, HttpServletRequest request) throws RuntimeException, Exception {
    	Long userId = AuthUtil.getUserId();
        //获取ISBN号
        String isbn = MapUtils.getString(params, "isbn");
        if (Strings.isNullOrEmpty(isbn)) {
            return new Response(ResponseCode.invalid_attribute);
        }

        Response resp = bookService.approveBookInlib(userId,isbn);
        return resp;
    }

}
