package com.eqdushu.server.service.book;

import com.eqdushu.server.domain.book.Book;
import com.eqdushu.server.domain.book.BookBorrow;
import com.eqdushu.server.domain.book.BookInLibJrl;
import com.eqdushu.server.domain.book.PageQuery;
import com.eqdushu.server.vo.Response;

import java.util.List;

/**
 * Created by nobita on 17/9/15.
 */
public interface IBookService {

    /**
     * 通过公司id查询公司的书库信息
     * @param companyId
     * @return
     */
    public List<Book> getLibraryByUsr(long companyId);

    /**
     * 通过isbn号查询书籍详情
     * @param isbn
     * @return
     */
    public Book getBookInfoByIsbn(String isbn);

    /**
     * 通过isbn号借阅书籍
     * @param
     * @return
     */
    public Response borrowBookByIsb(BookBorrow bookBorrow)throws Exception;

    /**
     * 通过企业ID查询企业借阅信息
     * @param page
     * @return
     */
    public List<BookBorrow> queryBorrowInfoByCompanyId(PageQuery page);

    /**
     * 通过用户id查询用户借阅信息
     * @param page
     * @return
     */
    public List<BookBorrow> queryBorrowInfoByUsrId(PageQuery page);

    /**
     * 还书
     * @return
     */
    public Response backBook(BookBorrow bookBorrow) throws Exception;

    /**
     * 书籍入库
     * @param isbn
     * @param usrId
     * @return
     */
    public Response addBookToLibrary(String isbn,long usrId) throws Exception;

    /**
     *
     * @param isbn
     * @param usrId
     * @return
     * @throws Exception
     */
    public Response addBookToLibraryReq(String isbn, long usrId) throws Exception;

    /**
     * 管理员查询所有入库申请
     * @param usrId
     * @return
     * @throws Exception
     */
    public Response getAllBookInLibReq(long usrId)throws Exception;

    /**
     * 审批入库申请
     * @param isbn
     * @return
     * @throws Exception
     */
    public Response approveBookInlib(long usrId,String isbn)throws Exception;
}
