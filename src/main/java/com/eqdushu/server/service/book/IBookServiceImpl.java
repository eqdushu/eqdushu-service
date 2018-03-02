package com.eqdushu.server.service.book;

import com.alibaba.fastjson.JSONObject;
import com.eqdushu.server.domain.book.*;
import com.eqdushu.server.domain.user.User;
import com.eqdushu.server.exception.BookDealException;
import com.eqdushu.server.mapper.book.BookMapper;
import com.eqdushu.server.mapper.user.UserMapper;
import com.eqdushu.server.utils.ResponseCode;
import com.eqdushu.server.utils.date.DateTimeUtil;
import com.eqdushu.server.utils.http.HttpUtil;
import com.eqdushu.server.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by nobita on 17/9/15.
 */
@Service
public class IBookServiceImpl implements IBookService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Book> getLibraryByUsr(long companyId) {
        return bookMapper.getLibrary(companyId);
    }

    @Override
    public Book getBookInfoByIsbn(String isbn) {
        return bookMapper.getBookByIsbn(isbn);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = BookDealException.class)
    public Response borrowBookByIsb(BookBorrow bookBorrow) throws Exception {

        //减少公司对应书籍信息
        int deleteBook = bookMapper.deleteNumByCompanyId(bookBorrow);
        if (deleteBook != 1) {
            throw new BookDealException(ResponseCode.book_not_exit);

        }
        //登记借阅流水
        int borrowJrl = bookMapper.createBorrow(bookBorrow);
        if (borrowJrl != 1) {
            throw new BookDealException(ResponseCode.borrow_jrl_error);

        }
        return new Response(ResponseCode.success);
    }

    @Override
    public List<BookBorrow> queryBorrowInfoByCompanyId(PageQuery page) {
        return bookMapper.getBorrowInfoByCompanyId(page);
    }

    @Override
    public List<BookBorrow> queryBorrowInfoByUsrId(PageQuery page) {
        return bookMapper.getBorrowInfoByUsrId(page);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = BookDealException.class)
    public Response backBook(BookBorrow bookBorrow) throws Exception {
        //增加企业对应书籍的数量
        int addNum = bookMapper.addNumByCompanyId(bookBorrow);
        if (addNum != 1) {
            throw new BookDealException(ResponseCode.add_booknum_error);
        }
        //修改对应借阅的状态
        int backState = bookMapper.updateBorrowSts(bookBorrow);
        if (backState != 1) {
            throw new BookDealException(ResponseCode.upd_borrowsts_error);
        }
        return new Response(ResponseCode.success);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = BookDealException.class)
    public Response addBookToLibrary(String isbn, long usrId) throws Exception {

        Date date = new Date();
        //查询书籍信息是否存在
        Book book = bookMapper.getBookByIsbn(isbn);
        //如果书籍信息中此本书籍不存在 则生成此书籍信息入库登记
        if (null == book) {
            //通过接口获取书籍信息
            String url = "https://api.douban.com/v2/book/isbn/" + isbn;
            String bookRsp = HttpUtil.doGet(url, null, null, null);
            //解析返回报文
            JSONObject jsonObject = JSONObject.parseObject(bookRsp);
            String author = jsonObject.getString("author");
            String image = jsonObject.getString("image");
            String publisher = jsonObject.getString("publisher");
            String title = jsonObject.getString("title");
            String summary = jsonObject.getString("summary");
            Book newBook = new Book();
            newBook.setIsbn(isbn);
            newBook.setBookAuthor(author);
            newBook.setBookImg(image);
            newBook.setBookPublisher(publisher);
            newBook.setBookTitle(title);
            newBook.setBookSummary(summary);
            newBook.setCreateTime(date);
            newBook.setUpdateTime(date);
            int addResult = bookMapper.createBook(newBook);
            if (addResult != 1) {
                throw new BookDealException(ResponseCode.create_book_error);
            }
        }
        //查询用户信息
        User user = userMapper.getById(usrId);
        if(!"admin".equals(user.getUserType().name())){
            return new Response(ResponseCode.need_admin);//非管理员用户
        }
        //查询书籍在企业是否存在
        BookRelation bookSource = new BookRelation();
        bookSource.setCompanyId(user.getCompanyId());
        bookSource.setIsbn(isbn);
        BookRelation bookRelation = bookMapper.getBookRelationByCompanyIdIsbn(bookSource);
        //如果企业不存在该书籍
        if (null == bookRelation) {
            bookSource.setRemainNum(1);
            bookSource.setTotalNum(1);
            bookSource.setCreateTime(date);
            bookSource.setUpdateTime(date);
            int addRelation = bookMapper.createBookRelation(bookSource);
            if (addRelation != 1) {
                //增加关系失败
                throw new BookDealException(ResponseCode.book_error);
            }
        } else {
            //该企业该书籍已存在
            throw new BookDealException(ResponseCode.inbook_error);
        }

        return new Response(ResponseCode.success);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = BookDealException.class)
    public Response addBookToLibraryReq(String isbn, long usrId) throws Exception {
        Date date = new Date();
        //查询书籍信息是否存在
        Book book = bookMapper.getBookByIsbn(isbn);
        //如果书籍信息中此本书籍不存在 则生成此书籍信息入库登记
        if (null == book) {
            //通过接口获取书籍信息
            String url = "https://api.douban.com/v2/book/isbn/" + isbn;
            String bookRsp = HttpUtil.doGet(url, null, null, null);
            //解析返回报文
            JSONObject jsonObject = JSONObject.parseObject(bookRsp);
            String author = jsonObject.getString("author");
            String image = jsonObject.getString("image");
            String publisher = jsonObject.getString("publisher");
            String title = jsonObject.getString("title");
            String summary = jsonObject.getString("summary");
            Book newBook = new Book();
            newBook.setIsbn(isbn);
            newBook.setBookAuthor(author);
            newBook.setBookImg(image);
            newBook.setBookPublisher(publisher);
            newBook.setBookTitle(title);
            newBook.setBookSummary(summary);
            newBook.setCreateTime(date);
            newBook.setUpdateTime(date);
            //登记书籍信息
            int addResult = bookMapper.createBook(newBook);
            if (addResult != 1) {
                throw new BookDealException(ResponseCode.create_book_error);
            }
            book = bookMapper.getBookByIsbn(isbn);
        }
        //查询用户信息
        User user = userMapper.getById(usrId);
        //查询书籍在企业是否存在
        BookRelation bookSource = new BookRelation();
        bookSource.setCompanyId(user.getCompanyId());
        bookSource.setIsbn(isbn);
        BookRelation bookRelation = bookMapper.getBookRelationByCompanyIdIsbn(bookSource);
        //如果企业不存在该书籍
        if (null == bookRelation) {
            //登记申请流水
            BookInLibJrl jrl = new BookInLibJrl();
            jrl.setBookId(book.getId());
            jrl.setCompanyId(user.getCompanyId());
            jrl.setUserId(user.getId());
            jrl.setIsbn(isbn);
            jrl.setCreateTime(date);
            jrl.setUpdateTime(date);
            jrl.setSts("U");
            int addJrl = bookMapper.createInLibReq(jrl);
            if (addJrl != 1) {
                //登记流水失败
                throw new BookDealException(ResponseCode.book_error);
            }
            BookBorrow bookBorrow = new BookBorrow();
            bookBorrow.setCompanyId(user.getCompanyId());
            bookBorrow.setIsbn(isbn);
            bookBorrow.setUserId(usrId);
            bookBorrow.setBookId(book.getId());
            bookBorrow.setBorrowSts("B");
            String nowDt = new SimpleDateFormat("yyyyMMdd").format(date);
            String nowTm = new SimpleDateFormat("HHmmss").format(date);
            bookBorrow.setBorrowBegDt(nowDt);
            bookBorrow.setBorrowBegTm(nowTm);
            String endDt = new SimpleDateFormat("yyyyMMdd").format(DateTimeUtil.addDay(date,30));
            bookBorrow.setBorrowEndDt(endDt);
            bookBorrow.setBorrowEndTm(nowTm);
            bookBorrow.setCreateTime(date);
            bookBorrow.setUpdateTime(date);
            //登记借阅流水
            int borrowJrl = bookMapper.createBorrow(bookBorrow);
            if (borrowJrl != 1) {
                throw new BookDealException(ResponseCode.borrow_jrl_error);

            }
        } else {
            //企业该书籍已存在
            throw new BookDealException(ResponseCode.inbook_error);
        }

        return new Response(ResponseCode.success);
    }

    @Override
    public Response getAllBookInLibReq(long usrId) throws Exception {

        //查询用户信息
        User user = userMapper.getById(usrId);
        if(!"admin".equals(user.getUserType().name())){
            return new Response(ResponseCode.need_admin);//非管理员用户
        }
        List<BookInLibJrl> list = bookMapper.getAllInLibJrl(user.getCompanyId());
        return new Response(ResponseCode.success, list);
    }

    @Override
    public Response approveBookInlib(long usrId, String isbn) throws Exception {
        //查询用户信息
        User user = userMapper.getById(usrId);
        if(!"admin".equals(user.getUserType().name())){
            return new Response(ResponseCode.need_admin);//非管理员用户
        }
        Date date = new Date();
        //审批入库申请，插入企业与该书籍对应的关系信息 其中remainnum为0
        BookRelation bookSource = new BookRelation();
        bookSource.setCompanyId(user.getCompanyId());
        bookSource.setIsbn(isbn);
        BookRelation bookRelation = bookMapper.getBookRelationByCompanyIdIsbn(bookSource);
        //如果企业不存在该书籍
        if (null == bookRelation) {
            bookSource.setRemainNum(0);
            bookSource.setTotalNum(1);
            bookSource.setCreateTime(date);
            bookSource.setUpdateTime(date);
            int addRelation = bookMapper.createBookRelation(bookSource);
            if (addRelation != 1) {
                //增加关系失败
                throw new BookDealException(ResponseCode.book_error);
            }
        } else {
            //该企业该书籍已存在
            throw new BookDealException(ResponseCode.inbook_error);
        }
        BookInLibJrl bookInLibJrl = new BookInLibJrl();
        bookInLibJrl.setSts("S");
        bookInLibJrl.setCompanyId(user.getCompanyId());
        bookInLibJrl.setIsbn(isbn);
        //修改流水状态为成功
        int updRes = bookMapper.updReqSts(bookInLibJrl);
        if(1 != updRes){
            throw new BookDealException(ResponseCode.updjrl_error);
        }
        return new Response(ResponseCode.success);
    }

}
